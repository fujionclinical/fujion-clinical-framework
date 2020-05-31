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
package org.fujionclinical.api.encounter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fujionclinical.api.context.*;
import org.fujionclinical.api.patient.IPatient;
import org.fujionclinical.api.patient.PatientContext;

/**
 * Wrapper for shared encounter context.
 */
public class EncounterContext extends ManagedContext<IEncounter> {

    public interface IEncounterContextSubscriber extends IContextSubscriber {

    }

    private static final String SUBJECT_NAME = "Encounter";

    private static final Log log = LogFactory.getLog(EncounterContext.class);

    private boolean fromEncounter;

    private IEncounterContextSubscriber encounterContextSubscriber = new IEncounterContextSubscriber() {
        @Override
        public void pending(ISurveyResponse response) {
            IEncounter encounter = getContextObject(true);
            IPatient patient = encounter == null ? null : encounter.getPatient();

            if (patient != null) {
                fromEncounter = true;
                PatientContext.changePatient(patient);
            }
        }

        @Override
        public void committed() {
        }

        @Override
        public void canceled() {
        }
    };

    private PatientContext.IPatientContextSubscriber patientContextSubscriber = new PatientContext.IPatientContextSubscriber() {

        @Override
        public void pending(ISurveyResponse response) {
        }

        @Override
        public void committed() {
            if (!fromEncounter) {
                changeEncounter(null);
            } else {
                fromEncounter = false;
            }
        }

        @Override
        public void canceled() {
            fromEncounter = false;
        }
    };

    /**
     * Returns the managed encounter context.
     *
     * @return Encounter context.
     */
    public static EncounterContext getEncounterContext() {
        return (EncounterContext) ContextManager.getInstance().getSharedContext(EncounterContext.class.getName());
    }

    /**
     * Request a encounter context change.
     *
     * @param encounter New encounter.
     */
    public static void changeEncounter(IEncounter encounter) {
        try {
            getEncounterContext().requestContextChange(encounter);
        } catch (Exception e) {
            log.error("Error during encounter context change.", e);
        }
    }

    /**
     * Returns the encounter in the current context.
     *
     * @return Encounter object (may be null).
     */
    public static IEncounter getActiveEncounter() {
        return getEncounterContext().getContextObject(false);
    }

    /**
     * Create a shared encounter context with an initial null state.
     */
    public EncounterContext() {
        this(null);
    }

    /**
     * Create a shared encounter context with a specified initial state.
     *
     * @param encounter Encounter that will be the initial state.
     */
    public EncounterContext(IEncounter encounter) {
        super(SUBJECT_NAME, IEncounterContextSubscriber.class, encounter);
        PatientContext.getPatientContext().addSubscriber(patientContextSubscriber);
        addSubscriber(encounterContextSubscriber);
    }

    /**
     * Creates a CCOW context from the specified encounter object.
     */
    @Override
    protected ContextItems toCCOWContext(IEncounter encounter) {
        //TODO: contextItems.setItem(...);
        return contextItems;
    }

    /**
     * Returns a encounter object based on the specified CCOW context.
     */
    @Override
    protected IEncounter fromCCOWContext(ContextItems contextItems) {
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

}
