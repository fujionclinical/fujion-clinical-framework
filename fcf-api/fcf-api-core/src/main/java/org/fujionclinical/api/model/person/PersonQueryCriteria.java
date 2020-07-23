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
package org.fujionclinical.api.model.person;

import org.apache.commons.lang3.StringUtils;
import org.fujion.common.DateTimeWrapper;
import org.fujionclinical.api.model.core.IConceptCode;
import org.fujionclinical.api.model.core.IIdentifier;
import org.fujionclinical.api.model.impl.ConceptCode;
import org.fujionclinical.api.model.impl.Identifier;
import org.fujionclinical.api.query.core.QueryUtil;
import org.fujionclinical.api.query.expression.AbstractCriteria;

/**
 * Base search criteria for person lookups.
 */
public abstract class PersonQueryCriteria<T extends IPerson> extends AbstractCriteria<T> {

    private static final IConceptCode SSN_TYPE = new ConceptCode("http://hl7.org/fhir/identifier-type", "SB");

    protected PersonQueryCriteria(
            Class<T> domainClass,
            String validationFailureMessage) {
        super(domainClass, ';', validationFailureMessage);
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
        DateTimeWrapper tempDate;
        IPerson.Gender tempGender;

        if (position > 0 && (tempGender = asGender(criterion)) != null) {
            setGender(tempGender);
        } else if (!criterion.matches(".*\\d.*")) {
            setName(criterion);
        } else if (criterion.matches("^=.+$")) {
            setId(criterion.substring(1));
        } else if (criterion.matches("^\\d{3}-\\d{2}-\\d{4}$")) {
            setSSN(criterion);
        } else if ((tempDate = parseDate(criterion)) != null) {
            setBirth(tempDate);
        } else {
            return false;
        }

        return true;
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
    private DateTimeWrapper parseDate(String value) {
        if (StringUtils.isNumeric(value)) {
            return null;
        }

        if (value.matches("^\\d+-\\d+$")) {
            return null;
        }

        return DateTimeWrapper.parse(value);
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
     * Sets the person name criterion.
     *
     * @param name Person name.
     */
    public void setName(String name) {
        setName(name == null ? null : PersonNameParser.instance.fromString(name));
    }

    /**
     * Sets the person name criterion.
     *
     * @param name Person name.
     */
    public void setName(IPersonName name) {
        queryContext.setParam("name", name == null ? null : name.getFamilyName());
    }

    /**
     * Sets the SSN criterion.
     *
     * @param ssn SSN.
     */
    public void setSSN(String ssn) {
        queryContext.setParam("identifiers", ssn == null ? null : new Identifier("http://hl7.org/fhir/sid/us-ssn", ssn, IIdentifier.IdentifierUse.OFFICIAL, SSN_TYPE));
    }

    /**
     * Sets the gender criterion.
     *
     * @param gender Gender.
     */
    public void setGender(IPerson.Gender gender) {
        queryContext.setParam("gender", gender);
    }

    /**
     * Returns a gender enum member if the value matches the start of a member's name.
     *
     * @param value The value to test.
     * @return The matching gender, or null if no match.
     */
    private IPerson.Gender asGender(String value) {
        return QueryUtil.findMatchingMember(IPerson.Gender.class, value);
    }

    /**
     * Sets the date of birth criterion.
     *
     * @param birth Date of birth.
     */
    public void setBirth(DateTimeWrapper birth) {
        queryContext.setParam("birthDate", birth);
    }

}
