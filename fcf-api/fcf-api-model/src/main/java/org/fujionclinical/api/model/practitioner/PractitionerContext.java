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
package org.fujionclinical.api.model.practitioner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fujionclinical.api.context.ContextItems;
import org.fujionclinical.api.context.ContextManager;
import org.fujionclinical.api.context.IContextSubscriber;
import org.fujionclinical.api.context.ManagedContext;

/**
 * Wrapper for shared practitioner context.
 */
public class PractitionerContext extends ManagedContext<IPractitioner> {

    private static final String SUBJECT_NAME = "Practitioner";

    private static final Log log = LogFactory.getLog(PractitionerContext.class);

    /**
     * Create a shared practitioner context with an initial null state.
     */
    public PractitionerContext() {
        this(null);
    }

    /**
     * Create a shared practitioner context with a specified initial state.
     *
     * @param practitioner Practitioner that will be the initial state.
     */
    public PractitionerContext(IPractitioner practitioner) {
        super(SUBJECT_NAME, IPractitionerContextEvent.class, practitioner);
    }

    /**
     * Returns the managed practitioner context.
     *
     * @return Practitioner context.
     */
    public static PractitionerContext getPractitionerContext() {
        return (PractitionerContext) ContextManager.getInstance().getSharedContext(PractitionerContext.class.getName());
    }

    /**
     * Request a practitioner context change.
     *
     * @param practitioner New practitioner.
     */
    public static void changePractitioner(IPractitioner practitioner) {
        try {
            getPractitionerContext().requestContextChange(practitioner);
        } catch (Exception e) {
            log.error("Error during practitioner context change.", e);
        }
    }

    /**
     * Returns the practitioner in the current context.
     *
     * @return Practitioner object (may be null).
     */
    public static IPractitioner getActivePractitioner() {
        return getPractitionerContext().getContextObject(false);
    }

    /**
     * Creates a CCOW context from the specified practitioner object.
     */
    @Override
    protected ContextItems toCCOWContext(IPractitioner practitioner) {
        //TODO: contextItems.setItem(...);
        return contextItems;
    }

    /**
     * Returns a practitioner object based on the specified CCOW context.
     */
    @Override
    protected IPractitioner fromCCOWContext(ContextItems contextItems) {
        return null;
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

    public interface IPractitionerContextEvent extends IContextSubscriber {
    }

}
