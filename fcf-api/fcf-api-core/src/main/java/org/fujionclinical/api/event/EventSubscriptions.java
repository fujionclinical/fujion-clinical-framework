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
package org.fujionclinical.api.event;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * Manages event subscriptions in a thread-safe way.
 * 
 * @param <T> Event data type.
 */
/*package*/class EventSubscriptions<T> {
    
    private static final Log log = LogFactory.getLog(EventSubscriptions.class);
    
    private final Map<String, List<IEventSubscriber<T>>> subscriptions = new HashMap<>();
    
    /**
     * Adds a subscriber to the specified event.
     * 
     * @param eventName Name of the event.
     * @param subscriber Subscriber to add.
     * @return Count of subscribers after the operation.
     */
    public synchronized int addSubscriber(String eventName, IEventSubscriber<T> subscriber) {
        List<IEventSubscriber<T>> subscribers = getSubscribers(eventName, true);
        subscribers.add(subscriber);
        return subscribers.size();
    }
    
    /**
     * Removes a subscriber from the specified event.
     * 
     * @param eventName Name of the event.
     * @param subscriber Subscriber to remove.
     * @return Count of subscribers after the operation, or -1 no subscriber list existed.
     */
    public synchronized int removeSubscriber(String eventName, IEventSubscriber<T> subscriber) {
        List<IEventSubscriber<T>> subscribers = getSubscribers(eventName, false);
        
        if (subscribers != null) {
            subscribers.remove(subscriber);
            
            if (subscribers.isEmpty()) {
                subscriptions.remove(eventName);
            }
            
            return subscribers.size();
        }
        
        return -1;
    }
    
    /**
     * Returns true if the event has any subscribers.
     * 
     * @param eventName Name of the event.
     * @return True if subscribers exist.
     */
    public synchronized boolean hasSubscribers(String eventName) {
        return getSubscribers(eventName, false) != null;
    }
    
    /**
     * Returns true If the event has subscribers.
     * 
     * @param eventName Name of the event.
     * @param exact If false, will iterate through parent events until a subscriber is found. If
     *            true, only the exact event is considered.
     * @return True if a subscriber was found.
     */
    public boolean hasSubscribers(String eventName, boolean exact) {
        while (!StringUtils.isEmpty(eventName)) {
            if (hasSubscribers(eventName)) {
                return true;
            } else if (exact) {
                return false;
            } else {
                eventName = stripLevel(eventName);
            }
        }
        
        return false;
    }
    
    /**
     * Returns a thread-safe iterable for the subscriber list.
     * 
     * @param eventName Name of the event.
     * @return Iterable for the subscriber list, or null if no list exists.
     */
    public synchronized Iterable<IEventSubscriber<T>> getSubscribers(String eventName) {
        List<IEventSubscriber<T>> subscribers = getSubscribers(eventName, false);
        return subscribers == null ? null : new ArrayList<>(subscribers);
    }
    
    /**
     * Returns a thread-safe iterable for all events with subscribers.
     * 
     * @return List of events.
     */
    public synchronized Iterable<String> getEvents() {
        return new ArrayList<>(subscriptions.keySet());
    }
    
    /**
     * Removes all subscriptions.
     */
    public synchronized void clear() {
        subscriptions.clear();
    }
    
    /**
     * Invokes callbacks on all subscribers of this and parent events.
     * 
     * @param eventName Name of the event.
     * @param eventData The associated event data.
     */
    public void invokeCallbacks(String eventName, T eventData) {
        String name = eventName;
        
        while (!StringUtils.isEmpty(name)) {
            Iterable<IEventSubscriber<T>> subscribers = getSubscribers(name);
            
            if (subscribers != null) {
                for (IEventSubscriber<T> subscriber : subscribers) {
                    try {
                        if (log.isDebugEnabled()) {
                            log.debug(String.format("Firing local Event[name=%s,data=%s]", eventName, eventData));
                        }
                        subscriber.eventCallback(eventName, eventData);
                    } catch (Throwable e) {
                        log.error("Error during local event callback.", e);
                    }
                }
            }
            
            name = stripLevel(name);
        }
    }
    
    /**
     * Gets the list of subscribers associated with an event.
     * 
     * @param eventName Name of the event.
     * @param canCreate If true and the list does not exist, create it.
     * @return The requested list; may be null.
     */
    private List<IEventSubscriber<T>> getSubscribers(String eventName, boolean canCreate) {
        List<IEventSubscriber<T>> subscribers = subscriptions.get(eventName);
        
        if (subscribers == null && canCreate) {
            subscribers = new LinkedList<>();
            subscriptions.put(eventName, subscribers);
        }
        
        return subscribers;
    }
    
    /**
     * Strips the lowest hierarchical level from the event type.
     * 
     * @param eventName Event type.
     * @return Event type with the lowest level removed.
     */
    private String stripLevel(String eventName) {
        int i = eventName.lastIndexOf('.');
        return i > 1 ? eventName.substring(0, i) : "";
    }
    
}
