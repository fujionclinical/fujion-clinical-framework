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
package org.fujionclinical.api.patient;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fujionclinical.api.context.ContextItems;
import org.fujionclinical.api.context.ContextManager;
import org.fujionclinical.api.context.IContextSubscriber;
import org.fujionclinical.api.context.ManagedContext;
import org.fujionclinical.api.model.IIdentifier;
import org.fujionclinical.api.model.person.IPersonName;
import org.fujionclinical.api.model.Identifier;
import org.fujionclinical.api.model.person.PersonNameParser;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Wrapper for shared patient context.
 */
public class PatientContext extends ManagedContext<IPatient> {

    public interface IPatientContextSubscriber extends IContextSubscriber {
    }

    protected static final String SUBJECT_NAME = "Patient";

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
     * Create a shared patient context with a specified initial state.
     *
     * @param patient Patient that will be the initial state.
     */
    public PatientContext(IPatient patient) {
        super(SUBJECT_NAME, IPatientContextSubscriber.class, patient);
    }

    /**
     * Returns the managed patient context.
     *
     * @return Patient context.
     */
    public static PatientContext getPatientContext() {
        return (PatientContext) ContextManager.getInstance().getSharedContext(PatientContext.class.getName());
    }

    /**
     * Request a patient context change.
     *
     * @param patient New patient.
     */
    public static void changePatient(IPatient patient) {
        try {
            getPatientContext().requestContextChange(patient);
        } catch (Exception e) {
            log.error("Error during patient context change.", e);
        }
    }

    /**
     * Returns the patient in the current context.
     *
     * @return Patient object (may be null).
     */
    public static IPatient getActivePatient() {
        return getPatientContext().getContextObject(false);
    }

    /**
     * Creates a CCOW context from the specified patient object.
     */
    @Override
    public ContextItems toCCOWContext(IPatient patient) {
        contextItems.setItem(CCOW_MRN, patient.getMRN() == null ? null : patient.getMRN().getValue(), "MRN");
        contextItems.setItem(CCOW_NAM, patient.getName().toString());
        contextItems.setItem(CCOW_GENDER, patient.getGender());
        contextItems.setItem(CCOW_DOB, patient.getBirthDate());
        return contextItems;
    }

    /**
     * Returns a patient object based on the specified CCOW context.
     */
    @Override
    public IPatient fromCCOWContext(ContextItems contextItems) {
        return new IPatient() {
            final IIdentifier mrn = new Identifier(null, contextItems.getItem(CCOW_MRN, "MRN"));
            final IPersonName name = PersonNameParser.instance.fromString(contextItems.getItem(CCOW_NAM));

            @Override
            public IIdentifier getMRN() {
                return mrn;
            }

            @Override
            public Gender getGender() {
                return EnumUtils.getEnum(Gender.class, contextItems.getItem(CCOW_GENDER));
            }

            @Override
            public Date getBirthDate() {
                return contextItems.getDate(CCOW_DOB);
            }

            @Override
            public List<IPersonName> getNames() {
                return name == null ? null : Collections.singletonList(name);
            }

            @Override
            public String getId() {
                return contextItems.getItem((CCOW_ID));
            }
        };
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
