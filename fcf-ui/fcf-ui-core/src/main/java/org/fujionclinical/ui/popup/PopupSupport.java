/*
 * #%L
 * Fujion Clinical Framework
 * %%
 * Copyright (C) 2023 fujionclinical.org
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
package org.fujionclinical.ui.popup;

import org.fujion.client.ExecutionContext;
import org.fujion.component.Label;
import org.fujion.component.Page;
import org.fujion.component.Window;
import org.fujion.component.Window.Mode;
import org.fujion.core.CoreUtil;
import org.fujion.event.Event;
import org.fujion.event.IEventListener;
import org.fujion.page.PageDefinition;
import org.fujion.page.PageParser;
import org.fujionclinical.api.event.EventManager;
import org.fujionclinical.api.event.IEventSubscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Displays a popup message when a POPUP generic event is received.
 */
public class PopupSupport implements IEventSubscriber<Object>, IEventListener {

    private static final String RESOURCE_PREFIX = CoreUtil.getResourceClassPath(PopupSupport.class);
    
    private static final String EVENT_POPUP = "POPUP";
    
    private static final int INITIAL_POSITION = 45;
    
    private PageDefinition popupDefinition = null;
    
    private int position = INITIAL_POSITION;
    
    private final List<Window> windows = Collections.synchronizedList(new ArrayList<>());
    
    private EventManager eventManager;
    
    /**
     * Subscribe to the popup event upon creation.
     */
    public PopupSupport() {
        super();
    }
    
    /**
     * Called after all properties have been injected by IOC container.
     */
    public void init() {
        eventManager.subscribe(EVENT_POPUP, this);
    }
    
    /**
     * Unsubscribe from popup event upon destruction.
     */
    public void destroy() {
        closeAll();
        eventManager.unsubscribe(EVENT_POPUP, this);
    }
    
    /**
     * Close all open popups.
     */
    public synchronized void closeAll() {
        for (Window window : windows) {
            try {
                window.removeEventListener("close", this);
                window.destroy();
            } catch (Throwable e) {
                // NOP
            }
        }
        
        windows.clear();
        resetPosition();
    }
    
    /**
     * Popup event handler - display popup dialog upon receipt.
     *
     * @param eventName Name of popupEvent
     * @param eventData May either be an encoded string (for backward compatibility) or a PopupData
     *            instance.
     */
    @SuppressWarnings("deprecation")
    @Override
    public void eventCallback(String eventName, Object eventData) {
        try {
            PopupData popupData;
            
            if (eventData instanceof PopupData) {
                popupData = (PopupData) eventData;
            } else {
                popupData = new PopupData(eventData.toString());
            }
            
            if (popupData.isEmpty()) {
                return;
            }
            
            Page currentPage = ExecutionContext.getPage();
            Window window = getPopupWindow();
            window.setTitle(popupData.getTitle());
            window.setParent(currentPage);
            String pos = getPosition();
            window.addStyle("left", pos);
            window.addStyle("top", pos);
            window.addEventListener("close", this);
            Label label = window.findByName("messagetext", Label.class);
            label.setLabel(popupData.getMessage());
            window.setMode(Mode.POPUP);
        } catch (Exception e) {
            // NOP
        }
    }
    
    /**
     * Reset window placement to initial position if no more popups are open.
     */
    private synchronized void resetPosition() {
        if (windows.isEmpty()) {
            position = INITIAL_POSITION;
        }
    }
    
    /**
     * Return the screen position for window placement
     *
     * @return The screen position.
     */
    private synchronized String getPosition() {
        position = position < 80 ? position + 5 : 10;
        return position + "%";
    }
    
    /**
     * Return a popup window instance.
     *
     * @return A popup window instance.
     */
    private synchronized Window getPopupWindow() {
        if (popupDefinition == null) {
            popupDefinition = PageParser.getInstance().parse(RESOURCE_PREFIX + "popupWindow.fsp");
        }
        
        Window window = (Window) popupDefinition.materialize(null);
        windows.add(window);
        return window;
    }
    
    @Override
    public void onEvent(Event event) {
        windows.remove(event.getTarget());
        resetPosition();
    }
    
    /**
     * Set the event manager instance.
     *
     * @param eventManager The event manager.
     */
    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }
    
    /**
     * Get the event manager instance.
     *
     * @return The event manager.
     */
    public EventManager getEventManager() {
        return eventManager;
    }
}
