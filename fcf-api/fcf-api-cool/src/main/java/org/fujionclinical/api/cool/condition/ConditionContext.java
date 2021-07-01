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
package org.fujionclinical.api.cool.condition;

import edu.utah.kmm.common.utils.CommonUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.coolmodel.clinical.finding.Condition;
import org.coolmodel.foundation.entity.Person;
import org.fujionclinical.api.context.ContextManager;
import org.fujionclinical.api.context.IContextSubscriber;
import org.fujionclinical.api.context.ISurveyResponse;
import org.fujionclinical.api.cool.common.AbstractIdentifiableContext;
import org.fujionclinical.api.cool.patient.PatientContext;

/**
 * Wrapper for shared condition context.
 */
public class ConditionContext extends AbstractIdentifiableContext<Condition> {

    public interface IConditionContextSubscriber extends IContextSubscriber {

    }

    private static final String SUBJECT_NAME = "Condition";

    private static final Log log = LogFactory.getLog(ConditionContext.class);

    private boolean fromCondition;

    private final IConditionContextSubscriber conditionContextSubscriber = new IConditionContextSubscriber() {
        @Override
        public void pending(ISurveyResponse response) {
            Condition condition = getContextObject(true);
            Person patient = CommonUtils.cast(condition.getSubject(), Person.class);

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
        return ContextManager.getSharedContext(ConditionContext.class);
    }

    /**
     * Returns the condition in the current context.
     *
     * @return Condition object (may be null).
     */
    public static Condition getActiveCondition() {
        return ContextManager.getCurrentValue(ConditionContext.class);
    }

    /**
     * Request a condition context change.
     *
     * @param condition New condition.
     */
    public static void changeCondition(Condition condition) {
        ContextManager.changeContext(ConditionContext.class, condition);
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
    public ConditionContext(Condition condition) {
        super(SUBJECT_NAME, IConditionContextSubscriber.class, condition);
        PatientContext.getPatientContext().addSubscriber(patientContextSubscriber);
        addSubscriber(conditionContextSubscriber);
    }

}
