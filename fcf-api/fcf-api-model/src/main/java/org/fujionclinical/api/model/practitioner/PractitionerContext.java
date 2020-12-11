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

import edu.utah.kmm.model.cool.foundation.entity.Person;
import org.fujionclinical.api.context.ContextManager;
import org.fujionclinical.api.context.IContextSubscriber;
import org.fujionclinical.api.model.common.AbstractIdentifiableContext;

/**
 * Wrapper for shared practitioner context.
 */
public class PractitionerContext extends AbstractIdentifiableContext<Person> {

    public interface IPractitionerContextEvent extends IContextSubscriber {

    }

    /**
     * Returns the managed practitioner context.
     *
     * @return Practitioner context.
     */
    public static PractitionerContext getPractitionerContext() {
        return ContextManager.getSharedContext(PractitionerContext.class);
    }

    /**
     * Returns the practitioner in the current context.
     *
     * @return Practitioner object (may be null).
     */
    public static Person getActivePractitioner() {
        return ContextManager.getCurrentValue(PractitionerContext.class);
    }

    /**
     * Request a practitioner context change.
     *
     * @param practitioner New practitioner.
     */
    public static void changePractitioner(Person practitioner) {
        ContextManager.changeContext(PractitionerContext.class, practitioner);
    }

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
    public PractitionerContext(Person practitioner) {
        super("Practitioner", IPractitionerContextEvent.class, practitioner);
    }

}
