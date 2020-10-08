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

import edu.utah.kmm.model.cool.core.datatype.IdentifierUse;
import edu.utah.kmm.model.cool.terminology.ConceptReferenceImpl;
import edu.utah.kmm.model.cool.terminology.ConceptReferenceSet;
import edu.utah.kmm.model.cool.terminology.ConceptReferenceSetImpl;
import org.fujionclinical.api.model.impl.IdentifierImpl;
import org.fujionclinical.api.model.person.PersonQueryCriteria;

import java.net.URI;

/**
 * Search criteria for patient lookup.
 */
public class PatientQueryCriteria extends PersonQueryCriteria<IPatient> {

    private static final ConceptReferenceSet MRN_TYPE = new ConceptReferenceSetImpl(
            new ConceptReferenceImpl("http://hl7.org/fhir/v2/0203", "MR", "Medical Record Number"));

    public PatientQueryCriteria() {
        super(IPatient.class, null);
    }

    /**
     * Parses a criterion and sets the result in the query context.
     *
     * @param criterion The search criterion to parse.
     * @param position  The position of the criterion in the search string.
     * @return True if the criterion was successfully parsed.
     */
    @Override
    protected boolean parseCriterion(
            String criterion,
            int position) {

        if (!super.parseCriterion(criterion, position)) {
            setMRN(criterion);
        }

        return true;
    }

    /**
     * Sets the MRN criterion.
     *
     * @param mrn MRN.
     */
    public void setMRN(String mrn) {
        queryContext.setParam("identifiers", mrn == null ? null : new IdentifierImpl((URI) null, mrn, IdentifierUse.OFFICIAL, MRN_TYPE));
    }

}
