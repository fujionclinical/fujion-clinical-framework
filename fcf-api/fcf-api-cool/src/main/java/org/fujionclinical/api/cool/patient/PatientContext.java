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
package org.fujionclinical.api.cool.patient;

import edu.utah.kmm.model.cool.foundation.entity.Person;
import edu.utah.kmm.model.cool.foundation.entity.PersonImpl;
import edu.utah.kmm.model.cool.util.PersonNameParsers;
import edu.utah.kmm.model.cool.util.PersonUtils;
import org.fujionclinical.api.context.ContextItems;
import org.fujionclinical.api.context.ContextManager;
import org.fujionclinical.api.context.IContextSubscriber;
import org.fujionclinical.api.cool.common.AbstractIdentifiableContext;

/**
 * Wrapper for shared patient context.
 */
public class PatientContext extends AbstractIdentifiableContext<Person> {

    public interface IPatientContextSubscriber extends IContextSubscriber {

    }

    private static final String CCOW_ID = "Patient.Id";

    private static final String CCOW_MRN = CCOW_ID + ".MRN";

    // protected static final String CCOW_MPI = CCOW_ID + ".MPI";

    private static final String CCOW_GENDER = "Co.Sex";

    private static final String CCOW_DOB = "Co.DateTimeOfBirth";

    private static final String CCOW_NAM = "Co.PatientName";

    /**
     * Returns the managed patient context.
     *
     * @return Patient context.
     */
    public static PatientContext getPatientContext() {
        return ContextManager.getSharedContext(PatientContext.class);
    }

    /**
     * Returns the patient in the current context.
     *
     * @return Patient object (may be null).
     */
    public static Person getActivePatient() {
        return ContextManager.getCurrentValue(PatientContext.class);
    }

    /**
     * Request a patient context change.
     *
     * @param patient New patient.
     */
    public static void changePatient(Person patient) {
        ContextManager.changeContext(PatientContext.class, patient);
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
        super("Patient", IPatientContextSubscriber.class, patient);
    }

    /**
     * Creates a CCOW context from the specified person object.
     */
    @Override
    public ContextItems toCCOWContext(Person person) {
        contextItems.setItem(CCOW_MRN, PersonUtils.hasMRN(person) ? PersonUtils.getMRN(person).getId() : null, "MRN");
        contextItems.setItem(CCOW_NAM, person.hasName() ? PersonNameParsers.get().toString(person.getName().get(0)) : null);
        contextItems.setItem(CCOW_GENDER, person.hasGender() ? person.getGender().getFirstConcept().getCode() : null);
        contextItems.setItem(CCOW_DOB, person.getBirthDate());
        return contextItems;
    }

    /**
     * Returns a person object based on the specified CCOW context.
     */
    @Override
    public Person fromCCOWContext(ContextItems contextItems) {
        Person person = new PersonImpl();
        String id = contextItems.getItem(CCOW_ID);

        if (id != null) {
            person.setDefaultId(id);
        }

        person.setBirthDate(contextItems.getDate(CCOW_DOB));
        person.setGender(PersonUtils.genderAsConceptReferenceSet(contextItems.getItem(CCOW_GENDER)));
        person.addName(PersonNameParsers.get().fromString(contextItems.getItem(CCOW_NAM)));
        String mrn = contextItems.getItem(CCOW_MRN, "MRN");
        PersonUtils.setMRN(person, mrn == null ? null : PersonUtils.createMRN(null, mrn));
        return person;
    }

}
