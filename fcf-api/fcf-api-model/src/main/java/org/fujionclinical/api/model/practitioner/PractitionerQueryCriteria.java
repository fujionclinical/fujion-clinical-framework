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
package org.fujionclinical.api.model.practitioner;

import edu.utah.kmm.model.cool.core.datatype.Identifier;
import edu.utah.kmm.model.cool.core.datatype.IdentifierImpl;
import edu.utah.kmm.model.cool.foundation.entity.Person;
import edu.utah.kmm.model.cool.terminology.ConceptReferenceSet;
import edu.utah.kmm.model.cool.terminology.ConceptReferenceSetImpl;
import org.fujionclinical.api.model.person.PersonQueryCriteria;

/**
 * Criteria for practitioner searches.
 */
public class PractitionerQueryCriteria extends PersonQueryCriteria<Person> {

    public static final ConceptReferenceSet DEA_CONCEPT_CODE = new ConceptReferenceSetImpl((String) null, "DEA", null);

    public PractitionerQueryCriteria() {
        super(Person.class, null);
    }

    @Override
    protected void buildQueryString(StringBuilder sb) {
        super.buildQueryString(sb);
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
            setDEA(criterion);
        }

        return true;
    }

    /**
     * Sets the DEA criterion.
     *
     * @param dea DEA.
     */
    public void setDEA(String dea) {
        Identifier identifier = null;

        if (dea != null) {
            identifier = new IdentifierImpl(null, dea);
            identifier.setType(DEA_CONCEPT_CODE);
        }

        queryContext.setParam("identifier", identifier);
    }

}
