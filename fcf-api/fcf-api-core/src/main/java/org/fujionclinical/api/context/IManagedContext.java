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

import org.fujionclinical.api.context.ISurveyResponse.ISurveyCallback;

/**
 * Every managed context must implement this interface. The context manager uses this interface to
 * manage context changes.
 *
 * @param <T> The class of the context object wrapped by this managed context.
 */
public interface IManagedContext<T> extends ISharedContext<T>, Comparable<IManagedContext<?>> {

    /**
     * Commits or rejects the pending context change.
     *
     * @param accept If true, the pending change is committed. If false, the pending change is
     *               canceled.
     */
    void commit(boolean accept);

    /**
     * Returns the CCOW context that corresponds to the current or pending context.
     *
     * @param pending If true, use the pending context. If false, use the current context.
     * @return The requested context.
     */
    ContextItems getContextItems(boolean pending);
    
    /**
     * Returns the name associated with this context. If the context supports CCOW, this should
     * match the CCOW subject name that corresponds to this context.
     *
     * @return The associated context name.
     */
    String getContextName();
    
    /**
     * Returns the priority that governs the sequence of context change commits when multiple
     * context changes are committed in the same transaction.
     *
     * @return The context priority.
     */
    int getPriority();
    
    /**
     * Sets the context to an initial state.
     */
    void init();
    
    /**
     * Returns true if a context change is pending.
     *
     * @return True if a context change is pending.
     */
    boolean isPending();
    
    /**
     * Sets the pending context to null.
     */
    void reset();
    
    /**
     * Set the pending context to match the specified CCOW context.
     *
     * @param contextItems Map representing the CCOW context.
     * @return True if the pending context was successfully set.
     */
    boolean setContextItems(ContextItems contextItems);
    
    /**
     * Notify all subscribers of the context change outcome.
     *
     * @param accept If true, context change was committed. If false, it was canceled.
     * @param all If false, notify only those subscribers who were polled. Otherwise, notify all
     *            subscribers.
     */
    void notifySubscribers(boolean accept, boolean all);

    /**
     * Survey all subscribers for context change response.
     *
     * @param silent   If true, subscribers should not request user interaction and all subscribers
     *                 will be surveyed. If false, a subscriber may request user interaction and the
     *                 first rejection response will terminate the survey.
     * @param callback Callback to report subscriber responses to survey.
     */
    void surveySubscribers(
            boolean silent,
            ISurveyCallback callback);

    /**
     * Sort managed contexts by priority.
     *
     * @param o Managed context to compare.
     * @return The sort order.
     */
    default int compareTo(IManagedContext<?> o) {
        return this == o ? 0 : o.getPriority() < getPriority() ? -1 : 1;
    }

}
