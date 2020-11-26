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
package org.fujionclinical.api.model.patient;

import edu.utah.kmm.model.cool.foundation.entity.Person;
import org.fujionclinical.api.context.IContextSubscriber;
import org.fujionclinical.api.model.person.AbstractPersonContext;

/**
 * Wrapper for shared patient context.
 */
public class PatientContext extends AbstractPersonContext {

    public interface IPatientContextSubscriber extends IContextSubscriber {

    }

    /**
     * Returns the managed patient context.
     *
     * @return Person context.
     */
    public static PatientContext getPatientContext() {
        return getPersonContext(PatientContext.class);
    }

    /**
     * Request a patient context change.
     *
     * @param patient New patient.
     */
    public static void changePatient(Person patient) {
        changePerson(patient, PatientContext.class);
    }

    /**
     * Returns the patient in the current context.
     *
     * @return Person object (may be null).
     */
    public static Person getActivePatient() {
        return getActivePerson(PatientContext.class);
    }

    /**
     * Create a shared patient context with an initial null state.
     */
    public PatientContext() {
        this(null);
    }

    /**
     * Create a shared patient context with a specified initial state.
     *
     * @param patient Person that will be the initial state.
     */
    public PatientContext(Person patient) {
        super("Person", IPatientContextSubscriber.class, patient);
    }

}
