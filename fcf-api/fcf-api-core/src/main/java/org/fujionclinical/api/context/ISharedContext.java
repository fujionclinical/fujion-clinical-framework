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

import org.fujionclinical.api.event.IEventSubscriber;

/**
 * Every managed context must implement this interface to permit access to the wrapped
 * context object.
 *
 * @param <T> The class of the context object that is wrapped by this managed context.
 */
public interface ISharedContext<T> {
    
    /**
     * Returns the underlying context object associated with the current or pending context.
     *
     * @param pending If true, the context object associated with the pending context is returned. If
     *            false, the context object associated with the current context is returned.
     * @return Domain object in the specified context.
     */
    T getContextObject(boolean pending);
    
    /**
     * Sets the specified context object into the pending context and invokes the context change
     * sequence.
     *
     * @param newContextObject New context object
     * @throws ContextException Context exception.
     */
    void requestContextChange(T newContextObject) throws ContextException;
    
    /**
     * Adds a context change subscriber to the subscription list for this context.
     *
     * @param subscriber Object that is to subscribe to context changes.
     * @return True if the subscription request was successful.
     */
    boolean addSubscriber(IContextSubscriber subscriber);
    
    /**
     * Adds multiple context change subscribers.
     *
     * @param subscribers List of subscribers to add.
     * @return True if at least one subscription request succeeded.
     */
    boolean addSubscribers(Iterable<IContextSubscriber> subscribers);
    
    /**
     * Removes a subscriber from the subscription list.
     *
     * @param subscriber Object to be removed.
     */
    void removeSubscriber(IContextSubscriber subscriber);
    
    /**
     * Removes multiple context change subscribers.
     *
     * @param subscribers List of subscribers to remove.
     */
    void removeSubscribers(Iterable<IContextSubscriber> subscribers);
    
    /**
     * Adds a listener for context change events.
     *
     * @param listener The listener.
     */
    void addListener(IEventSubscriber<T> listener);
    
    /**
     * Removes a listener for context change events.
     *
     * @param listener The listener.
     */
    void removeListener(IEventSubscriber<T> listener);

}
