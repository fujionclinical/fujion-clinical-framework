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

import edu.utah.kmm.model.cool.core.datatype.Identifier;
import edu.utah.kmm.model.cool.core.datatype.IdentifierImpl;
import edu.utah.kmm.model.cool.foundation.datatype.PersonName;
import edu.utah.kmm.model.cool.foundation.entity.Person;
import edu.utah.kmm.model.cool.foundation.entity.PersonImpl;
import edu.utah.kmm.model.cool.util.PersonNameParsers;
import edu.utah.kmm.model.cool.util.PersonUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fujionclinical.api.context.ContextItems;
import org.fujionclinical.api.context.ContextManager;
import org.fujionclinical.api.context.IContextSubscriber;
import org.fujionclinical.api.context.ManagedContext;

/**
 * Wrapper for shared patient context.
 */
public class PatientContext extends ManagedContext<Person> {

    public interface IPatientContextSubscriber extends IContextSubscriber {

    }

    protected static final String SUBJECT_NAME = "Person";

    protected static final String CCOW_ID = SUBJECT_NAME + ".Id";

    protected static final String CCOW_MRN = CCOW_ID + ".MRN";

    // protected static final String CCOW_MPI = CCOW_ID + ".MPI";

    protected static final String CCOW_CO = SUBJECT_NAME + ".Co";

    protected static final String CCOW_GENDER = CCOW_CO + ".Sex";

    protected static final String CCOW_DOB = CCOW_CO + ".DateTimeOfBirth";

    protected static final String CCOW_NAM = CCOW_CO + ".PatientName";

    private static final Log log = LogFactory.getLog(PatientContext.class);

    /**
     * Create a shared patient context with an initial null state.
     */
    public PatientContext() {
        this(null);
    }

    /**
     * Returns the managed patient context.
     *
     * @return Person context.
     */
    public static PatientContext getPatientContext() {
        return (PatientContext) ContextManager.getInstance().getSharedContext(PatientContext.class.getName());
    }

    /**
     * Request a patient context change.
     *
     * @param patient New patient.
     */
    public static void changePatient(Person patient) {
        try {
            getPatientContext().requestContextChange(patient);
        } catch (Exception e) {
            log.error("Error during patient context change.", e);
        }
    }

    /**
     * Returns the patient in the current context.
     *
     * @return Person object (may be null).
     */
    public static Person getActivePatient() {
        return getPatientContext().getContextObject(false);
    }

    /**
     * Create a shared patient context with a specified initial state.
     *
     * @param patient Person that will be the initial state.
     */
    public PatientContext(Person patient) {
        super(SUBJECT_NAME, IPatientContextSubscriber.class, patient);
    }

    /**
     * Creates a CCOW context from the specified patient object.
     */
    @Override
    public ContextItems toCCOWContext(Person patient) {
        contextItems.setItem(CCOW_MRN, PersonUtils.hasMRN(patient) ? PersonUtils.getMRN(patient).getId() : null, "MRN");
        contextItems.setItem(CCOW_NAM, patient.getName().toString());
        contextItems.setItem(CCOW_GENDER, patient.getGender());
        contextItems.setItem(CCOW_DOB, patient.getBirthDate());
        return contextItems;
    }

    /**
     * Returns a patient object based on the specified CCOW context.
     */
    @Override
    public Person fromCCOWContext(ContextItems contextItems) {
        Person patient = new PersonImpl();
        patient.setDefaultId(contextItems.getItem((CCOW_ID)));
        patient.addIdentifiers(new IdentifierImpl(null, contextItems.getItem(CCOW_MRN, "MRN")));
        patient.setBirthDate(contextItems.getDate(CCOW_DOB));
        // TODO: patient.setGender(contextItems.getItem(CCOW_GENDER));
        Identifier mrn = new IdentifierImpl(null, contextItems.getItem(CCOW_MRN, "MRN"));
        PersonUtils.setMRN(patient, mrn);
        PersonName name = PersonNameParsers.get().fromString(contextItems.getItem(CCOW_NAM));
        patient.addName(name);
        return patient;
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

}
