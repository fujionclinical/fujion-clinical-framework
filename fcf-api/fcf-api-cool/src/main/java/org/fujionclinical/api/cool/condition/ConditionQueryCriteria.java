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

import org.clinicalontology.terminology.api.model.ConceptSet;
import org.coolmodel.clinical.encounter.Encounter;
import org.coolmodel.clinical.finding.AssertionOfPresenceClinicalStatus;
import org.coolmodel.clinical.finding.AssertionalFindingEntry;
import org.coolmodel.foundation.entity.Person;
import org.fujionclinical.api.query.AbstractSearchCriteria;

import java.util.Date;

/**
 * Search criteria for encounter lookup.
 */
public class ConditionQueryCriteria extends AbstractSearchCriteria<AssertionalFindingEntry> {

    public ConditionQueryCriteria() {
        super(AssertionalFindingEntry.class, ';', null);
    }

    /**
     * Sets the patient criterion.
     *
     * @param patient Patient.
     */
    public void setPatient(Person patient) {
        setContextParam("patient", patient);
    }

    public void setEncounter(Encounter encounter) {
        setContextParam("encounter", encounter);
    }

    public void setStatus(ConceptSet status) {
        setContextParam("verificationStatus", status);
    }

    public void setStatus(AssertionOfPresenceClinicalStatus status) {
        setContextParam("clinicalStatus", status);
    }

    public void setCreationDate(Date creationDate) {
        setContextParam("creationDate", creationDate);
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
        return super.isValid() || hasContextParam("patient");
    }

}
