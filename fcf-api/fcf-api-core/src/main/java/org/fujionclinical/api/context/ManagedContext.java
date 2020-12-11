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
package org.fujionclinical.api.context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fujionclinical.api.context.ISurveyResponse.ISurveyCallback;
import org.fujionclinical.api.context.SurveyResponse.ResponseState;
import org.fujionclinical.api.core.AppFramework;
import org.fujionclinical.api.core.IRegisterEvent;
import org.fujionclinical.api.core.StopWatchFactory;
import org.fujionclinical.api.core.StopWatchFactory.IStopWatch;
import org.fujionclinical.api.event.IEventManager;
import org.fujionclinical.api.event.IEventSubscriber;

import java.util.*;

/**
 * Base class for creating context objects. Descendant classes wrap domain objects such as patient
 * or user that represent a shared context for other objects to reference. By implementing the
 * IManagedContext interface, the management of this shared context is delegated to the
 * ContextManager class which orchestrates polling and notifying subscribers when changes in the
 * shared context occur.
 *
 * @param <DomainClass> Class of underlying domain object.
 */
public class ManagedContext<DomainClass> implements Comparable<IManagedContext<DomainClass>>, IRegisterEvent, IManagedContext<DomainClass> {
    
    private static final Log log = LogFactory.getLog(ManagedContext.class);
    
    private static final int CONTEXT_CURRENT = 0;
    
    private static final int CONTEXT_PENDING = 1;

    @SuppressWarnings("unchecked")
    private final DomainClass[] domainObject = (DomainClass[]) new Object[2];

    private final Class<? extends IContextSubscriber> subscriberType;
    
    private final String contextName;
    
    private boolean isPending;
    
    private final List<IContextSubscriber> subscribers = new ArrayList<>();
    
    private final List<IContextSubscriber> surveyed = new ArrayList<>();
    
    protected IContextManager contextManager;
    
    protected IEventManager eventManager;
    
    protected AppFramework appFramework;
    
    protected final ContextItems contextItems = new ContextItems();
    
    /**
     * Every managed context must specify a unique context name and the context change event
     * interface it supports.
     *
     * @param contextName Unique name for this context.
     * @param subscriberType The context change interface supported by this managed context.
     */
    protected ManagedContext(String contextName, Class<? extends IContextSubscriber> subscriberType) {
        this(contextName, subscriberType, null);
    }
    
    /**
     * Every managed context must specify a unique context name and the context change event
     * interface it supports.
     *
     * @param contextName Unique name for this context.
     * @param subscriberType The type of context subscriber supported by this managed context.
     * @param initialContext The initial context state. May be null.
     */
    protected ManagedContext(String contextName, Class<? extends IContextSubscriber> subscriberType, DomainClass initialContext) {
        this.contextName = contextName;
        this.subscriberType = subscriberType;
        setPending(initialContext);
        commit(true);
    }
    
    /**
     * Extracts and returns the CCOW context from the specified domain object. Each subclass should
     * override this and supply their own implementation.
     *
     * @param domainObject The domain object.
     * @return Context items extracted from the domain object.
     */
    protected ContextItems toCCOWContext(DomainClass domainObject) {
        return contextItems;
    }
    
    /**
     * Creates a local context based on the specified CCOW context. Each subclass should override
     * this and supply their own implementation.
     *
     * @param contextItems Map containing CCOW-compliant context settings.
     * @return Instance of the domain object that matches the CCOW context. Return null if this is
     *         not supported or the CCOW context is not valid for this context object.
     */
    protected DomainClass fromCCOWContext(ContextItems contextItems) {
        return null;
    }
    
    /**
     * Sets the pending state to the specified domain object.
     *
     * @param domainObject The domain object.
     */
    protected void setPending(DomainClass domainObject) {
        this.domainObject[CONTEXT_PENDING] = domainObject;
        isPending = true;
    }
    
    /**
     * Compares whether two domain objects are the same. This is used to determine whether a context
     * change request really represents a different context. It may be overridden if the default
     * implementation is inadequate.
     *
     * @param domainObject1 First domain object for comparison.
     * @param domainObject2 Second domain object for comparison.
     * @return True if the two objects represent the same context.
     */
    protected boolean isSameContext(DomainClass domainObject1, DomainClass domainObject2) {
        return Objects.equals(domainObject1, domainObject2);
    }
    
    /**
     * Sets the context manager instance.
     *
     * @param contextManager The context manager.
     */
    public void setContextManager(IContextManager contextManager) {
        this.contextManager = contextManager;
    }
    
    /**
     * Sets the event manager instance.
     *
     * @param eventManager The event manager.
     */
    public void setEventManager(IEventManager eventManager) {
        this.eventManager = eventManager;
    }
    
    /**
     * Sets the application framework instance.
     *
     * @param appFramework The application framework.
     */
    public void setAppFramework(AppFramework appFramework) {
        this.appFramework = appFramework;
    }
    
    // ************************************************************************************************
    // * IManagedContext implementation
    // ***********************************************************************************************/
    
    /**
     * @see org.fujionclinical.api.context.IManagedContext#commit(boolean)
     */
    @Override
    public void commit(boolean accept) {
        if (accept) {
            domainObject[CONTEXT_CURRENT] = domainObject[CONTEXT_PENDING];
        }
        
        domainObject[CONTEXT_PENDING] = null;
        isPending = false;
    }
    
    /**
     * @see org.fujionclinical.api.context.IManagedContext#getContextItems(boolean)
     */
    @Override
    public ContextItems getContextItems(boolean pending) {
        contextItems.clear();
        DomainClass domainObject = getContextObject(pending);
        return domainObject == null ? contextItems : toCCOWContext(domainObject);
    }
    
    /**
     * @see org.fujionclinical.api.context.IManagedContext#getContextName()
     */
    @Override
    public String getContextName() {
        return contextName;
    }
    
    /**
     * @see org.fujionclinical.api.context.IManagedContext#isPending()
     */
    @Override
    public boolean isPending() {
        return isPending;
    }
    
    /**
     * @see org.fujionclinical.api.context.IManagedContext#getPriority()
     */
    @Override
    public int getPriority() {
        return 0;
    }
    
    /**
     * @see org.fujionclinical.api.context.IManagedContext#init()
     */
    @Override
    public void init() {
        reset();
    }
    
    /**
     * @see org.fujionclinical.api.context.IManagedContext#reset()
     */
    @Override
    public void reset() {
        setPending(null);
    }
    
    /**
     * @see org.fujionclinical.api.context.IManagedContext#setContextItems(org.fujionclinical.api.context.ContextItems)
     */
    @Override
    public boolean setContextItems(ContextItems contextItems) {
        DomainClass domainObject = fromCCOWContext(contextItems);
        
        if (domainObject == null) {
            return false;
        }
        
        setPending(domainObject);
        return true;
    }
    
    /**
     * @see org.fujionclinical.api.context.IManagedContext#addSubscriber(IContextSubscriber)
     */
    @Override
    public boolean addSubscriber(IContextSubscriber subscriber) {
        if (!this.subscriberType.isInstance(subscriber)) {
            return false;
        }
        
        if (subscribers.contains(subscriber)) {
            return false;
        }
        
        subscribers.add(subscriber);
        return true;
    }
    
    /**
     * @see org.fujionclinical.api.context.IManagedContext#addSubscribers(java.lang.Iterable)
     */
    @Override
    public boolean addSubscribers(Iterable<IContextSubscriber> subscribers) {
        boolean result = false;
        
        for (IContextSubscriber subscriber : subscribers) {
            result |= addSubscriber(subscriber);
        }
        
        return result;
    }
    
    /**
     * @see org.fujionclinical.api.context.IManagedContext#removeSubscriber(IContextSubscriber)
     */
    @Override
    public void removeSubscriber(IContextSubscriber subscriber) {
        if (this.subscriberType.isInstance(subscriber)) {
            subscribers.remove(subscriber);
            surveyed.remove(subscriber);
        }
    }
    
    /**
     * @see org.fujionclinical.api.context.IManagedContext#removeSubscribers(java.lang.Iterable)
     */
    @Override
    public void removeSubscribers(Iterable<IContextSubscriber> subscribers) {
        for (IContextSubscriber subscriber : subscribers) {
            removeSubscriber(subscriber);
        }
    }
    
    /**
     * @see org.fujionclinical.api.context.IManagedContext#notifySubscribers(boolean, boolean)
     */
    @Override
    public void notifySubscribers(boolean accept, boolean all) {
        Map<String, Object> map = null;
        
        if (log.isDebugEnabled() && StopWatchFactory.hasFactory()) {
            map = new HashMap<>();
            map.put("action", accept ? "committed" : "canceled");
            map.put("context", getContextName());
        }
        
        for (IContextSubscriber event : getIterable(all)) {
            IStopWatch sw = null;
            
            try {
                if (map != null) {
                    map.remove("exception");
                    map.put("subscriber", event.getClass().getName());
                    sw = StopWatchFactory.create("org.fujionclinical.context.notifySubscribers", map);
                    sw.start();
                }
                
                if (accept) {
                    event.committed();
                } else {
                    event.canceled();
                }
                
            } catch (Throwable e) {
                log.error("Error during notifySubscribers.", e);
                
                if (map != null) {
                    map.put("exception", e.toString());
                }
            }
            
            if (sw != null) {
                sw.stop();
            }
        }
        
        surveyed.clear();
        
        if (accept) {
            eventManager.fireLocalEvent(getEventName(), getContextObject(false));
        }
    }
    
    /**
     * Returns a callback list that is safe for iteration.
     *
     * @param all If true, all callbacks are returned. If false, only callbacks for surveyed
     *            subscribers are returned.
     * @return Callback list.
     */
    private Iterable<IContextSubscriber> getIterable(boolean all) {
        return new ArrayList<>(all ? subscribers : surveyed);
    }
    
    /**
     * @see org.fujionclinical.api.context.IManagedContext#surveySubscribers
     */
    @Override
    public void surveySubscribers(boolean silent, ISurveyCallback callback) {
        SurveyResponse response = new SurveyResponse(silent);
        Iterator<IContextSubscriber> iter = getIterable(true).iterator();
        surveySubscribers(iter, response, callback);
    }
    
    private void surveySubscribers(Iterator<IContextSubscriber> iter, SurveyResponse response, ISurveyCallback callback) {
        if ((response.isSilent() || !response.rejected()) && iter.hasNext()) {
            IContextSubscriber subscriber = iter.next();
            
            response.reset(__ -> surveySubscribers(iter, response, callback));
            
            try {
                subscriber.pending(response);
            } catch (Throwable e) {
                log.error("Error during surveysubscribers.", e);
                response.reject(e.toString());
            }
            
            ResponseState state = response.getState();
            
            if (state != ResponseState.DEFERRED) {
                surveySubscribers(iter, response, callback);
            }
        } else if (callback != null) {
            callback.response(response);
        }
    }
    
    /**
     * Returns the name of the event fired after a successful context change.
     *
     * @return The name of the event fired after a successful context change.
     */
    private String getEventName() {
        return "CONTEXT.CHANGED." + getContextName();
    }

    @Override
    public void addListener(IEventSubscriber<DomainClass> listener) {
        eventManager.subscribe(getEventName(), listener);
    }
    
    @Override
    public void removeListener(IEventSubscriber<DomainClass> listener) {
        eventManager.unsubscribe(getEventName(), listener);
    }
    
    // ************************************************************************************************
    // * ISharedContext implementation
    // ***********************************************************************************************/
    
    /**
     * @see org.fujionclinical.api.context.ISharedContext#requestContextChange(Object)
     */
    @Override
    public void requestContextChange(DomainClass newContextObject) throws ContextException {
        if (isSameContext(newContextObject, getContextObject(false))) {
            return;
        }
        
        if (isPending) {
            throw new ContextException("A context change is already pending.");
        }
        
        contextManager.localChangeBegin(this);
        domainObject[CONTEXT_PENDING] = newContextObject;
        isPending = true;
        contextManager.localChangeEnd(this, null);
    }
    
    /**
     * @see org.fujionclinical.api.context.ISharedContext#getContextObject(boolean)
     */
    @Override
    public DomainClass getContextObject(boolean pending) {
        return domainObject[pending ? CONTEXT_PENDING : CONTEXT_CURRENT];
    }
    
    // ************************************************************************************************
    // * Comparable implementation
    // ***********************************************************************************************/
    
    /**
     * Compares by priority, with higher priorities collating first.
     */
    @Override
    public int compareTo(IManagedContext<DomainClass> o) {
        int pri1 = o.getPriority();
        int pri2 = getPriority();
        return this == o ? 0 : pri1 < pri2 ? -1 : 1;
    }
    
    // ************************************************************************************************
    // * IRegisterEvent implementation
    // ***********************************************************************************************/
    
    /**
     * Register an object as a subscriber if it implements the callback interface.
     *
     * @see IRegisterEvent#registerObject(Object)
     * @param object Object to register.
     */
    @Override
    public void registerObject(Object object) {
        if (object instanceof IContextSubscriber) {
            addSubscriber((IContextSubscriber) object);
        }
    }
    
    /**
     * Remove an object as a subscriber if it implements the callback interface.
     *
     * @see IRegisterEvent#unregisterObject(Object)
     * @param object Object to unregister.
     */
    @Override
    public void unregisterObject(Object object) {
        if (object instanceof IContextSubscriber) {
            removeSubscriber((IContextSubscriber) object);
        }
    }
    
}
