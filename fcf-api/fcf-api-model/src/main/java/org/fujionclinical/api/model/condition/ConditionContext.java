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
package org.fujionclinical.api.model.condition;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fujionclinical.api.context.*;
import org.fujionclinical.api.model.patient.IPatient;
import org.fujionclinical.api.model.patient.PatientContext;

/**
 * Wrapper for shared condition context.
 */
public class ConditionContext extends ManagedContext<ICondition> {

    public interface IConditionContextSubscriber extends IContextSubscriber {

    }

    private static final String SUBJECT_NAME = "Condition";

    private static final Log log = LogFactory.getLog(ConditionContext.class);

    private boolean fromCondition;

    private final IConditionContextSubscriber conditionContextSubscriber = new IConditionContextSubscriber() {
        @Override
        public void pending(ISurveyResponse response) {
            ICondition condition = getContextObject(true);
            IPatient patient = condition == null || !condition.hasPatient() ? null : condition.getPatient().getReferenced();

            if (patient != null) {
                fromCondition = true;
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
            if (!fromCondition) {
                changeCondition(null);
            } else {
                fromCondition = false;
            }
        }

        @Override
        public void canceled() {
            fromCondition = false;
        }
    };

    /**
     * Returns the managed condition context.
     *
     * @return Condition context.
     */
    public static ConditionContext getConditionContext() {
        return (ConditionContext) ContextManager.getInstance().getSharedContext(ConditionContext.class.getName());
    }

    /**
     * Request a condition context change.
     *
     * @param condition New condition.
     */
    public static void changeCondition(ICondition condition) {
        try {
            getConditionContext().requestContextChange(condition);
        } catch (Exception e) {
            log.error("Error during condition context change.", e);
        }
    }

    /**
     * Returns the condition in the current context.
     *
     * @return Condition object (may be null).
     */
    public static ICondition getActiveCondition() {
        return getConditionContext().getContextObject(false);
    }

    /**
     * Create a shared condition context with an initial null state.
     */
    public ConditionContext() {
        this(null);
    }

    /**
     * Create a shared condition context with a specified initial state.
     *
     * @param condition Condition that will be the initial state.
     */
    public ConditionContext(ICondition condition) {
        super(SUBJECT_NAME, IConditionContextSubscriber.class, condition);
        PatientContext.getPatientContext().addSubscriber(patientContextSubscriber);
        addSubscriber(conditionContextSubscriber);
    }

    /**
     * Creates a CCOW context from the specified condition object.
     */
    @Override
    protected ContextItems toCCOWContext(ICondition condition) {
        //TODO: contextItems.setItem(...);
        return contextItems;
    }

    /**
     * Returns a condition object based on the specified CCOW context.
     */
    @Override
    protected ICondition fromCCOWContext(ContextItems contextItems) {
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
