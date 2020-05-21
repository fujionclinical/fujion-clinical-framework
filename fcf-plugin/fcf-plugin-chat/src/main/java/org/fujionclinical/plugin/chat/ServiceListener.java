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
package org.fujionclinical.plugin.chat;

import org.fujionclinical.api.event.IEventManager;
import org.fujionclinical.api.event.IEventSubscriber;

/**
 * Simple listener implementation that tracks its own subscription.
 * 
 * @param <T> Class of event object.
 */
public abstract class ServiceListener<T> implements IEventSubscriber<T> {
    
    private final String eventName;
    
    private final IEventManager eventManager;
    
    ServiceListener(String eventName, IEventManager eventManager) {
        this.eventName = eventName;
        this.eventManager = eventManager;
    }
    
    void setActive(boolean active) {
        if (active) {
            eventManager.subscribe(eventName, this);
        } else {
            eventManager.unsubscribe(eventName, this);
        }
    }
}
