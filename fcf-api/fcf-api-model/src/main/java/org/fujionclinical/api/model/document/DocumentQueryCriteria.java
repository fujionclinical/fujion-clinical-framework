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
package org.fujionclinical.api.model.document;

import edu.utah.kmm.model.cool.clinical.encounter.Encounter;
import edu.utah.kmm.model.cool.clinical.finding.Document;
import edu.utah.kmm.model.cool.clinical.finding.DocumentStatus;
import edu.utah.kmm.model.cool.foundation.entity.Person;
import org.fujionclinical.api.query.expression.AbstractCriteria;

public class DocumentQueryCriteria extends AbstractCriteria<Document> {

    protected DocumentQueryCriteria() {
        super(Document.class, ';', null);
    }

    /**
     * Sets the patient criterion.
     *
     * @param patient Patient.
     */
    public void setPatient(Person patient) {
        queryContext.setParam("patient", patient);
    }

    public void setEncounter(Encounter encounter) {
        queryContext.setParam("encounter", encounter);
    }

    public void setStatus(DocumentStatus status) {
        queryContext.setParam("documentStatus", status);
    }

    /* TODO: what to do?
    public void setStatus(Document.CompositionStatus status) {
        queryContext.setParam("compositionStatus", status);
    }
    */

    @Override
    protected void buildQueryString(StringBuilder sb) {
        addFragment(sb, "patient.id");
        addFragment(sb, "encounter.id");
        addFragment(sb, "documentStatus");
        addFragment(sb, "compositionStatus");
    }

    @Override
    protected boolean parseCriterion(
            String criterion,
            int position) {
        return false;
    }

}
