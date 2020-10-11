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
package org.fujionclinical.api.query.expression;

import edu.utah.kmm.model.cool.dao.core.EntityDAORegistry;
import edu.utah.kmm.model.cool.dao.query.ExpressionParser;
import edu.utah.kmm.model.cool.dao.query.ExpressionTuple;
import edu.utah.kmm.model.cool.dao.query.QueryContext;
import edu.utah.kmm.model.cool.dao.query.QueryContextImpl;
import org.apache.commons.lang3.StringUtils;
import org.fujionclinical.api.model.core.IDomainType;
import org.fujionclinical.api.query.core.QueryException;

import java.util.List;

/**
 * Base class for specifying search criteria for a given domain type.  Converts user input to a set of search criteria
 * that are then validated and compiled to a query expression.
 */
public abstract class AbstractCriteria<T extends IDomainType> {

    private static final String MSG_ERROR_MISSING_REQUIRED = "A required search criterion is missing.";

    protected final QueryContext queryContext = new QueryContextImpl();

    private final Class<T> entityType;

    private final String validationFailureMessage;

    private final Character criterionSeparator;

    protected AbstractCriteria(
            Class<T> entityType,
            Character criterionSeparator,
            String validationFailureMessage) {
        this.entityType = entityType;
        this.criterionSeparator = criterionSeparator;
        this.validationFailureMessage = validationFailureMessage == null ? MSG_ERROR_MISSING_REQUIRED : validationFailureMessage;
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
    public List<ExpressionTuple> compile() {
        validate();
        StringBuilder sb = new StringBuilder();
        addFragment(sb, "id", "=");
        buildQueryString(sb);
        return ExpressionParser.getInstance().parse(entityType, sb.toString()).resolve(queryContext);
    }

    /**
     * Perform a search based on given criteria.
     *
     * @return Resources matching the search criteria.
     */
    public List<T> search() {
        return EntityDAORegistry.get(entityType).search(compile());
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
