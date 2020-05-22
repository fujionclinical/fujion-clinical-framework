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
package org.fujionclinical.api.practitioner.search;

import org.apache.commons.lang.StringUtils;
import org.fujionclinical.api.model.*;
import org.fujionclinical.api.query.SearchCriteria;

/**
 * Criteria for practitioner searches.
 */
public class PractitionerSearchCriteria extends SearchCriteria {

    public static final IConceptCode DEA_CONCEPT_CODE = new ConceptCode(null, "DEA", null);

    //private static final String ERROR_MISSING_REQUIRED = "@practitionersearch.error.missing.required";

    private IPersonName name;

    private IIdentifier dea;

    private IIdentifier ssn;

    private String gender;

    public PractitionerSearchCriteria() {
        super("Insufficient search parameters.");
    }

    /**
     * Creates a criteria instance with settings parsed from search text.
     *
     * @param searchText Search text to parse. Uses pattern matching to determine which criterion is
     *                   associated with a given input component. Separate multiple input components with a
     *                   semicolons.
     */
    public PractitionerSearchCriteria(String searchText) {
        this();
        searchText = searchText == null ? null : searchText.trim();

        if (!StringUtils.isEmpty(searchText)) {
            String[] pcs = searchText.split(";");

            for (String pc : pcs) {
                pc = pc.trim();

                if (pc.isEmpty()) {
                    continue;
                } else if (isValid() && (pc.equalsIgnoreCase("M") || pc.equalsIgnoreCase("F"))) {
                    setGender(pc.toUpperCase());
                } else if (!pc.matches(".*\\d.*")) {
                    setName(pc);
                } else if (pc.matches("^=.+$")) {
                    setId(pc.substring(1));
                } else if (pc.matches("^\\d{3}-\\d{2}-\\d{4}$")) {
                    setSSN(pc);
                } else {
                    setDEA(pc);
                }
            }
        }
    }

    /**
     * Returns true if the current criteria settings meet the minimum requirements for a search.
     *
     * @return True if minimum search requirements have been met.
     */
    @Override
    protected boolean isValid() {
        return super.isValid() || ssn != null || dea != null || name != null;
    }

    /**
     * Returns the patient name criterion.
     *
     * @return Patient name criterion.
     */
    public IPersonName getName() {
        return name;
    }

    /**
     * Sets the patient name criterion.
     *
     * @param name Patient name.
     */
    public void setName(String name) {
        this.name = PersonNameParser.instance.fromString(name);
    }

    /**
     * Sets the patient name criterion.
     *
     * @param name Patient name.
     */
    public void setName(IPersonName name) {
        this.name = name;
    }

    /**
     * Returns the DEA criterion.
     *
     * @return DEA criterion.
     */
    public IIdentifier getDEA() {
        return dea;
    }

    /**
     * Sets the DEA criterion.
     *
     * @param dea DEA.
     */
    public void setDEA(String dea) {
        this.dea = new Identifier(null, dea, null, new ConceptCode(null, "DEA"));
    }

    /**
     * Returns the SSN criterion.
     *
     * @return SSN criterion.
     */
    public IIdentifier getSSN() {
        return ssn;
    }

    /**
     * Sets the SSN criterion.
     *
     * @param ssn SSN.
     */
    public void setSSN(String ssn) {
        this.ssn = new Identifier("http://hl7.org/fhir/sid/us-ssn", ssn);
    }

    /**
     * Returns the gender criterion.
     *
     * @return Gender criterion.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender criterion.
     *
     * @param gender Gender.
     */
    public void setGender(String gender) {
        this.gender = StringUtils.trimToNull(gender);
    }

    /**
     * Returns true if no criteria have been set.
     *
     * @return True if no criteria have been set.
     */
    @Override
    public boolean isEmpty() {
        return super.isEmpty() && name == null && dea == null && ssn == null && gender == null;
    }
}
