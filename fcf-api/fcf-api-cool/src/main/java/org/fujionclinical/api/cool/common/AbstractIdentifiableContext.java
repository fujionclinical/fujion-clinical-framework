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
package org.fujionclinical.api.cool.common;

import org.coolmodel.foundation.core.Identifiable;
import org.coolmodel.util.CoolUtils;
import org.fujionclinical.api.context.IContextSubscriber;
import org.fujionclinical.api.context.ManagedContext;

/**
 * Base context for shared contexts of identifiable types.
 */
public abstract class AbstractIdentifiableContext<T extends Identifiable> extends ManagedContext<T> {

    /**
     * Create a shared context with a specified initial state.
     *
     * @param contextName    The unique context name.
     * @param subscriberType The interface for context subscriptions.
     * @param identifiable   The initial state.
     */
    protected AbstractIdentifiableContext(
            String contextName,
            Class<? extends IContextSubscriber> subscriberType,
            T identifiable) {
        super(contextName, subscriberType, identifiable);
    }

    /**
     * Create a shared context with an initial null state.
     *
     * @param contextName    The unique context name.
     * @param subscriberType The interface for context subscriptions.
     */
    protected AbstractIdentifiableContext(
            String contextName,
            Class<? extends IContextSubscriber> subscriberType) {
        super(contextName, subscriberType);
    }

    /**
     * Returns a priority value of 10.
     *
     * @return Priority value for context manager.
     */
    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    protected boolean isSameContext(
            Identifiable identifiable1,
            Identifiable identifiable2) {
        return CoolUtils.areSame(identifiable1, identifiable2);
    }

}
