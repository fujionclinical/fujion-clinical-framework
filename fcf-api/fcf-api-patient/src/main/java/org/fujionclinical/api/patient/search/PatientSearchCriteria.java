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
package org.fujionclinical.api.patient.search;

import org.apache.commons.lang.StringUtils;
import org.fujion.common.DateUtil;
import org.fujion.common.LocalizedMessage;
import org.fujionclinical.api.model.ConceptCode;
import org.fujionclinical.api.model.IConceptCode;
import org.fujionclinical.api.model.IIdentifier;
import org.fujionclinical.api.model.Identifier;
import org.fujionclinical.api.model.person.IPersonName;
import org.fujionclinical.api.model.person.PersonNameParser;
import org.fujionclinical.api.patient.IPatient;
import org.fujionclinical.api.query.SearchCriteria;

import java.util.Date;

/**
 * Search criteria for patient lookup.
 */
public class PatientSearchCriteria extends SearchCriteria<IPatient> {

    public static final LocalizedMessage MSG_ERROR_MISSING_REQUIRED = new LocalizedMessage("patientsearch.error.missing.required");

    private static final IConceptCode SSN_TYPE = new ConceptCode("http://hl7.org/fhir/identifier-type", "SB");

    private static final IConceptCode MRN_TYPE = new ConceptCode("http://hl7.org/fhir/v2/0203", "MR");

    public PatientSearchCriteria() {
        super(IPatient.class, MSG_ERROR_MISSING_REQUIRED.toString());
    }

    /**
     * Creates a criteria instance with settings parsed from search text.
     *
     * @param searchText Search text to parse. Uses pattern matching to determine which criterion is
     *                   associated with a given input component. Separate multiple input components with
     *                   semicolons.
     */
    public PatientSearchCriteria(String searchText) {
        this();
        searchText = searchText == null ? null : searchText.trim();

        if (!StringUtils.isEmpty(searchText)) {
            String[] pcs = searchText.split(";");

            for (String pc : pcs) {
                pc = pc.trim();
                Date tempDate;

                if (pc.isEmpty()) {
                    continue;
                }

                if (isValid() && (pc.equalsIgnoreCase("M") || pc.equalsIgnoreCase("F"))) {
                    setGender(pc.toUpperCase());
                } else if (!pc.matches(".*\\d.*")) {
                    setName(pc);
                } else if (pc.matches("^=.+$")) {
                    setId(pc.substring(1));
                } else if (pc.matches("^\\d{3}-\\d{2}-\\d{4}$")) {
                    setSSN(pc);
                } else if ((tempDate = parseDate(pc)) != null) {
                    setBirth(tempDate);
                } else {
                    setMRN(pc);
                }
            }
        }
    }

    @Override
    protected void buildQueryString(StringBuilder sb) {
        addFragment(sb, "name", "~");
        addFragment(sb, "identifier", "=");
        addFragment(sb, "gender", "=");
        addFragment(sb, "birthDate", "=");
    }

    /**
     * Returns a date value if the input is a valid date. Otherwise, returns null. Explicitly
     * excludes some patterns that may successfully parse as a date.
     *
     * @param value Input to parse.
     * @return Result of parsed input, or null if parsing unsuccessful.
     */
    private Date parseDate(String value) {
        if (StringUtils.isNumeric(value)) {
            return null;
        }

        if (value.matches("^\\d+-\\d+$")) {
            return null;
        }

        return DateUtil.parseDate(value);
    }

    /**
     * Returns true if the current criteria settings meet the minimum requirements for a search.
     *
     * @return True if minimum search requirements have been met.
     */
    @Override
    public boolean isValid() {
        return super.isValid() || queryContext.hasParam("identifier") || queryContext.hasParam("name");
    }

    /**
     * Sets the patient name criterion.
     *
     * @param name Patient name.
     */
    public void setName(String name) {
        setName(name == null ? null : PersonNameParser.instance.fromString(name));
    }

    /**
     * Sets the patient name criterion.
     *
     * @param name Patient name.
     */
    public void setName(IPersonName name) {
        queryContext.setParam("name", name == null ? null : name.getFamilyName());
    }

    /**
     * Sets the MRN criterion.
     *
     * @param mrn MRN.
     */
    public void setMRN(String mrn) {
        queryContext.setParam("identifier", mrn == null ? null : new Identifier(null, mrn, IIdentifier.IdentifierUse.OFFICIAL, MRN_TYPE));
    }

    /**
     * Sets the SSN criterion.
     *
     * @param ssn SSN.
     */
    public void setSSN(String ssn) {
        queryContext.setParam("identifier", ssn == null ? null : new Identifier("http://hl7.org/fhir/sid/us-ssn", ssn, IIdentifier.IdentifierUse.OFFICIAL, SSN_TYPE));
    }

    /**
     * Sets the gender criterion.
     *
     * @param gender Gender.
     */
    public void setGender(String gender) {
        queryContext.setParam("gender", StringUtils.trimToNull(gender));
    }

    /**
     * Sets the date of birth criterion.
     *
     * @param birth Date of birth.
     */
    public void setBirth(Date birth) {
        queryContext.setParam("birthDate", birth);
    }

}
