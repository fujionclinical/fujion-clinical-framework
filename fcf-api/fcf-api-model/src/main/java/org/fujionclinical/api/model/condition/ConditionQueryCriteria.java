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

import org.fujion.common.DateRange;
import org.fujionclinical.api.model.patient.IPatient;
import org.fujionclinical.api.query.AbstractQueryCriteria;
import org.fujionclinical.api.query.QueryOperator;

/**
 * Search criteria for encounter lookup.
 */
public class ConditionQueryCriteria extends AbstractQueryCriteria<ICondition> {

    public ConditionQueryCriteria() {
        super(ICondition.class, ';', null);
    }

    /**
     * Sets the patient criterion.
     *
     * @param patient Patient.
     */
    public void setPatient(IPatient patient) {
        queryContext.setParam("patient", patient);
    }

    public void setStatus(ICondition.VerificationStatus status) {
        queryContext.setParam("verificationStatus", status);
    }

    public void setStatus(ICondition.ClinicalStatus status) {
        queryContext.setParam("clinicalStatus", status);
    }

    @Override
    protected void buildQueryString(StringBuilder sb) {
        addFragment(sb, "patient.id", "=");
        addFragment(sb, "clinicalStatus", "=");
        addFragment(sb, "verificationStatus", "=");
    }

    @Override
    protected boolean parseCriterion(
            String criterion,
            int position) {
        return false;
    }

    /**
     * Returns true if the current criteria settings meet the minimum requirements for a search.
     *
     * @return True if minimum search requirements have been met.
     */
    @Override
    public boolean isValid() {
        return super.isValid() || queryContext.hasParam("patient");
    }

}