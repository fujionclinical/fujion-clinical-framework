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
package org.fujionclinical.api.query;

import org.apache.commons.lang.StringUtils;
import org.fujion.common.LocalizedMessage;
import org.fujionclinical.api.model.core.IDomainType;
import org.fujionclinical.api.model.dao.DomainDAORegistry;

import java.util.List;

/**
 * Base class for search criteria.
 */
public abstract class AbstractQueryCriteria<T extends IDomainType> {

    private static final LocalizedMessage MSG_ERROR_MISSING_REQUIRED = new LocalizedMessage("patientsearch.error.missing.required");

    protected final IQueryContext queryContext = new QueryContext();

    private final Class<T> domainClass;

    private final String validationFailureMessage;

    private final Character criterionSeparator;

    protected AbstractQueryCriteria(
            Class<T> domainClass,
            Character criterionSeparator,
            String validationFailureMessage) {
        this.domainClass = domainClass;
        this.criterionSeparator = criterionSeparator;
        this.validationFailureMessage = validationFailureMessage == null ? MSG_ERROR_MISSING_REQUIRED.toString() : validationFailureMessage;
    }

    /**
     * Implement to build a query string from the current search criteria.
     *
     * @param sb A string builder to use to build the query string.
     */
    abstract protected void buildQueryString(StringBuilder sb);

    /**
     * Parses the search string criterion.
     *
     * @param criterion The search criterion.
     * @param position  The position of the criterion in the search string.
     * @return True if the criterion was parsed.
     */
    abstract protected boolean parseCriterion(
            String criterion,
            int position);

    /**
     * Compile the current search criteria.
     *
     * @return The compiled search criteria (as a list of query expression tuples).
     */
    public List<QueryExpressionTuple> compile() {
        validate();
        StringBuilder sb = new StringBuilder();
        addFragment(sb, "id", "=");
        buildQueryString(sb);
        return QueryExpressionParser.getInstance().parse(domainClass, sb.toString()).resolve(queryContext);
    }

    /**
     * Perform a search based on given criteria.
     *
     * @return Resources matching the search criteria.
     */
    public List<T> search() {
        return DomainDAORegistry.getDAO(domainClass).search(compile());
    }

    /**
     * Returns true if the current criteria settings meet the minimum requirements for a search.
     *
     * @return True if minimum search requirements have been met.
     */
    protected boolean isValid() {
        return queryContext.hasParam("id");
    }

    /**
     * Validates that the current criteria settings meet the minimum requirements for a search. If
     * not, throws a run-time exception describing the deficiency.
     */
    public void validate() {
        if (!isValid()) {
            throw new QueryException(validationFailureMessage);
        }
    }

    /**
     * Sets the maximum hits criterion.
     *
     * @param maximum Maximum.
     */
    public void setMaximum(Integer maximum) {
        queryContext.setParam("_count", maximum);
    }

    /**
     * Sets the domain identifier
     *
     * @param id Domain identifier.
     */
    public void setId(String id) {
        queryContext.setParam("id", StringUtils.trimToNull(id));
    }

    /**
     * Returns true if no criteria have been set.
     *
     * @return True if no criteria have been set.
     */
    public boolean isEmpty() {
        return queryContext.isEmpty();
    }

    public void reset() {
        queryContext.reset();
    }

    public void reset(String searchString) {
        reset();
        String[] criteria = criterionSeparator == null ? new String[]{searchString} : StringUtils.split(searchString, criterionSeparator);
        int position = 0;

        for (String criterion : criteria) {
            criterion = StringUtils.trimToNull(criterion);

            if (criterion != null) {
                boolean processed = parseCriterion(criterion.trim(), position++);

                if (!processed) {
                    throw new QueryException("Unrecognized search criterion '" + criterion + "'.");
                }
            }
        }
    }

    protected void addFragment(
            StringBuilder sb,
            String parameter) {
        addFragment(sb, parameter, "=");
    }

    protected void addFragment(
            StringBuilder sb,
            String parameter,
            String operator) {
        if (queryContext.hasParam(parameter)) {
            sb.append(sb.length() == 0 ? "" : "&");
            sb.append(parameter).append(" ").append(operator).append(" {{").append(parameter).append("}}");
        }
    }

}
