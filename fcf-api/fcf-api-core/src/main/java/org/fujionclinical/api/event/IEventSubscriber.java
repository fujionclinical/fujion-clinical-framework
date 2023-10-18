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

/**
 * This is the event callback interface for handling generic events. Any object that wishes to
 * subscribe to a generic event must implement this interface.
 * 
 * @param <T> Return type of event data.
 */
public interface IEventSubscriber<T> {
    
    /**
     * The callback interface used to notify a subscriber of a subscribed event.
     * 
     * @param eventName Name of the event.
     * @param eventData Associated data.
     */
    void eventCallback(String eventName, T eventData);
}
