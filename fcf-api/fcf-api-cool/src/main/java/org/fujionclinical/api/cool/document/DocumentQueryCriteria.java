/*
 * #%L
 * Fujion Clinical Framework
 * %%
 * Copyright (C) 2023 fujionclinical.org
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
package org.fujionclinical.api.cool.document;

import org.coolmodel.clinical.encounter.Encounter;
import org.coolmodel.clinical.finding.Document;
import org.coolmodel.clinical.finding.DocumentStatus;
import org.coolmodel.foundation.entity.Person;
import org.fujionclinical.api.query.AbstractSearchCriteria;

public class DocumentQueryCriteria extends AbstractSearchCriteria<Document> {

    protected DocumentQueryCriteria() {
        super(Document.class, ';', null);
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

    public void setStatus(DocumentStatus status) {
        setContextParam("documentStatus", status);
    }

    /* TODO: what to do?
    public void setStatus(Document.CompositionStatus status) {
        setContextParam("compositionStatus", status);
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
