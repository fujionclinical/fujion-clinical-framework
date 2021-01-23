/*
 * #%L
 * Fujion Clinical Framework
 * %%
 * Copyright (C) 2020 fujionclinical.org
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * This Source Code Form is also subject to the terms of the Health-Related
 * Additional Disclaimer of Warranty and Limitation of Liability available at
 *
 *      http://www.fujionclinical.org/licensing/disclaimer
 *
 * #L%
 */
package org.fujionclinical.ui.controller;

import org.fujion.ancillary.IAutoWired;
import org.fujion.component.BaseComponent;
import org.fujion.component.BaseUIComponent;
import org.fujion.event.Event;
import org.fujion.event.IEventListener;
import org.fujion.thread.ThreadUtil;
import org.fujion.thread.ThreadedTask;
import org.fujionclinical.api.core.AppFramework;
import org.fujionclinical.api.core.FrameworkUtil;
import org.fujionclinical.api.event.EventManager;
import org.fujionclinical.api.event.IEventManager;
import org.fujionclinical.api.event.IEventSubscriber;
import org.fujionclinical.api.spring.SpringUtil;
import org.fujionclinical.ui.core.Constants;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Can be subclassed to be used as a controller with convenience methods for accessing the
 * application context and framework services. The controller is automatically registered with the
 * framework and subclasses can implement special interfaces recognized by the framework, such as
 * context change interfaces.
 */
public class FrameworkController implements IAutoWired {

    private ApplicationContext appContext;

    private AppFramework appFramework;

    private IEventManager eventManager;

    protected BaseUIComponent root;

    private BaseUIComponent comp;

    private final List<Future<?>> threads = new ArrayList<>();

    private final IEventListener threadCompletionListener = new IEventListener() {

        /**
         * Background thread completion will be notified via this event listener. The listener will
         * in turn invoke either the threadFinished or threadAborted methods, as appropriate.
         *
         * @param event The completion event.
         */
        @Override
        public void onEvent(Event event) {
            ThreadedTask thread = (ThreadedTask) event.getData();
            
            if (thread != null) {
                removeThread(thread);
                
                if (thread.isCancelled()) {
                    threadAborted(thread);
                } else {
                    threadFinished(thread);
                }
            }
        }
        
    };
    
    private final IEventSubscriber<Object> refreshListener = (eventName, eventData) -> {
        refresh();
    };
    
    /**
     * Returns the controller associated with the specified component, if any.
     *
     * @param comp The component whose controller is sought.
     * @return The associated controller, or null if none found.
     */
    public static Object getController(BaseComponent comp) {
        return getController(comp, false);
    }
    
    /**
     * Returns the controller associated with the specified component, if any.
     *
     * @param comp The component whose controller is sought.
     * @param recurse If true, search up the parent chain until a controller is found.
     * @return The associated controller, or null if none found.
     */
    public static Object getController(BaseComponent comp, boolean recurse) {
        return recurse ? comp.findAttribute(Constants.ATTR_COMPOSER) : comp.getAttribute(Constants.ATTR_COMPOSER);
    }
    
    /**
     * Locates and returns a controller of the given type by searching up the component tree
     * starting at the specified component.
     *
     * @param <T> The type of controller sought.
     * @param comp Component for start of search.
     * @param type The type of controller sought.
     * @return The controller instance, or null if not found.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getController(BaseComponent comp, Class<T> type) {
        while (comp != null) {
            Object controller = getController(comp);
            
            if (type.isInstance(controller)) {
                return (T) controller;
            }
            
            comp = comp.getParent();
        }
        
        return null;
    }
    
    /**
     * Returns the application context associated with the active desktop.
     *
     * @return An application context instance.
     */
    public ApplicationContext getAppContext() {
        return appContext;
    }
    
    /**
     * Returns the application framework associated with the active session.
     *
     * @return An application framework instance.
     */
    public AppFramework getAppFramework() {
        return appFramework;
    }
    
    /**
     * Returns the event manager associated with the active page.
     *
     * @return The event manager.
     */
    public IEventManager getEventManager() {
        return eventManager;
    }
    
    /**
     * Override the doAfterCompose method to set references to the application context and the
     * framework and register the controller with the framework.
     *
     * @param comp BaseComponent associated with this controller.
     */
    @Override
    public void afterInitialized(BaseComponent comp) {
        root = (BaseUIComponent) comp;
        this.comp = root;
        comp.setAttribute(Constants.ATTR_COMPOSER, this);
        comp.addEventListener(ThreadedTask.DEFAULT_EVENT_NAME, threadCompletionListener);
        appContext = SpringUtil.getAppContext();
        appFramework = FrameworkUtil.getAppFramework();
        eventManager = EventManager.getInstance();
        initialize();
    }
    
    /**
     * Process a refresh event.
     */
    public void onRefresh() {
        refresh();
    }
    
    /**
     * Override to respond to a refresh request.
     */
    public void refresh() {
        
    }
    
    /**
     * Override to perform additional cleanup.
     */
    protected void cleanup() {
        eventManager.unsubscribe(Constants.REFRESH_EVENT, refreshListener);
        appFramework.unregisterObject(FrameworkController.this);
    }
    
    /**
     * Override to perform additional initializations.
     */
    protected void initialize() {
        eventManager.subscribe(Constants.REFRESH_EVENT, refreshListener);
        appFramework.registerObject(FrameworkController.this);
        comp.addEventListener("destroy", event -> cleanup());
    }
    
    /**
     * Abort background thread if it is running.
     */
    protected void abortBackgroundThreads() {
        while (!threads.isEmpty()) {
            abortBackgroundThread(threads.get(0));
        }
    }

    /**
     * Abort background thread if it is running.
     *
     * @param thread Thread to abort.
     */
    protected void abortBackgroundThread(Future<?> thread) {
        removeThread(thread).cancel(true);
    }

    /**
     * Add a thread to the active list.
     *
     * @param thread Thread to add.
     * @return The thread that was added.
     */
    protected Future<?> addThread(Future<?> thread) {
        threads.add(thread);
        return thread;
    }

    /**
     * Remove a thread from the active list.
     *
     * @param thread Thread to remove.
     * @return The thread that was removed.
     */
    protected Future<?> removeThread(Future<?> thread) {
        threads.remove(thread);
        return thread;
    }
    
    /**
     * Returns true if any active threads are present.
     *
     * @return True if any active threads are present.
     */
    protected boolean hasActiveThreads() {
        return !threads.isEmpty();
    }
    
    /**
     * Starts a background thread.
     *
     * @param task The task to be executed in the background thread.
     * @return The new thread.
     */
    protected ThreadedTask startBackgroundThread(ThreadedTask.TaskExecutor task) {
        ThreadedTask thread = new ThreadedTask(task, comp);
        addThread(thread);
        ThreadUtil.execute(thread);
        return thread;
    }

    /**
     * Called when a background thread has completed.
     *
     * @param thread The background thread.
     */
    protected void threadFinished(Future<?> thread) {
        removeThread(thread);
    }

    /**
     * Called when a background thread has aborted.
     *
     * @param thread The background thread.
     */
    protected void threadAborted(Future<?> thread) {
        removeThread(thread);
    }
    
}
