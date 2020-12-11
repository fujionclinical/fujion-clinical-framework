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
package org.fujionclinical.api.model.common;

import edu.utah.kmm.model.cool.foundation.core.Identifiable;
import edu.utah.kmm.model.cool.util.CoolUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fujionclinical.api.context.IContextSubscriber;
import org.fujionclinical.api.context.ManagedContext;

/**
 * Base context for shared contexts of identifiable types.
 */
public class AbstractIdentifiableContext<T extends Identifiable> extends ManagedContext<T> {

    private static final Log log = LogFactory.getLog(AbstractIdentifiableContext.class);

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
        this(contextName, subscriberType, null);
    }

    /**
     * Get a context item.
     *
     * @param itemName The item name.
     * @return The item value.
     */
    protected String getItem(String itemName) {
        return contextItems.getItem(qualifyItemName(itemName));
    }

    /**
     * Get a context item.
     *
     * @param itemName  The item name.
     * @param qualifier The item qualifier.
     * @return The item value.
     */
    protected String getItem(
            String itemName,
            String qualifier) {
        return contextItems.getItem(qualifyItemName(itemName), qualifier);
    }

    /**
     * Set a context item.
     *
     * @param itemName The item name.
     * @param value    The item value.
     */
    protected void setItem(
            String itemName,
            Object value) {
        contextItems.setItem(qualifyItemName(itemName), value);
    }

    /**
     * Set a context item.
     *
     * @param itemName  The item name.
     * @param value     The item value.
     * @param qualifier The item qualifier.
     */
    protected void setItem(
            String itemName,
            String value,
            String qualifier) {
        contextItems.setItem(qualifyItemName(itemName), value, qualifier);
    }

    /**
     * Prepends the subject name (i.e., the context name) to the item name.
     *
     * @param itemName The item name.
     * @return The qualified item name.
     */
    private String qualifyItemName(String itemName) {
        return getContextName() + "." + itemName;
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
