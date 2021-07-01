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
package org.fujionclinical.api.cool.encounter;

import org.coolmodel.clinical.encounter.Encounter;
import org.coolmodel.foundation.entity.Person;
import org.fujion.common.MiscUtil;
import org.fujionclinical.api.context.ContextItems;
import org.fujionclinical.api.context.ContextManager;
import org.fujionclinical.api.context.IContextSubscriber;
import org.fujionclinical.api.context.ISurveyResponse;
import org.fujionclinical.api.cool.common.AbstractIdentifiableContext;
import org.fujionclinical.api.cool.patient.PatientContext;

/**
 * Wrapper for shared encounter context.
 */
public class EncounterContext extends AbstractIdentifiableContext<Encounter> {

    public interface EncounterContextSubscriber extends IContextSubscriber {

    }

    private static final String SUBJECT_NAME = "Encounter";

    private boolean fromEncounter;

    private final EncounterContextSubscriber encounterContextSubscriber = new EncounterContextSubscriber() {
        @Override
        public void pending(ISurveyResponse response) {
            Encounter encounter = getContextObject(true);
            Person patient = encounter == null || !encounter.hasSubject() ? null : MiscUtil.castTo(encounter.getSubject(), Person.class);

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

    private final PatientContext.IPatientContextSubscriber patientContextSubscriber = new PatientContext.IPatientContextSubscriber() {

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
        return ContextManager.getSharedContext(EncounterContext.class);
    }

    /**
     * Returns the encounter in the current context.
     *
     * @return Encounter object (may be null).
     */
    public static Encounter getActiveEncounter() {
        return ContextManager.getCurrentValue(EncounterContext.class);
    }

    /**
     * Request a encounter context change.
     *
     * @param encounter New encounter.
     */
    public static void changeEncounter(Encounter encounter) {
        ContextManager.changeContext(EncounterContext.class, encounter);
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
    public EncounterContext(Encounter encounter) {
        super(SUBJECT_NAME, EncounterContextSubscriber.class, encounter);
        PatientContext.getPatientContext().addSubscriber(patientContextSubscriber);
        addSubscriber(encounterContextSubscriber);
    }

    /**
     * Creates a CCOW context from the specified encounter object.
     */
    @Override
    protected ContextItems toCCOWContext(Encounter encounter) {
        //TODO: contextItems.setItem(...);
        return contextItems;
    }

    /**
     * Returns a encounter object based on the specified CCOW context.
     */
    @Override
    protected Encounter fromCCOWContext(ContextItems contextItems) {
        return null;
    }

}
