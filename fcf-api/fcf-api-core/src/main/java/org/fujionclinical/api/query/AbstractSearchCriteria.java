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

import edu.utah.kmm.model.cool.foundation.core.Identifiable;
import edu.utah.kmm.model.cool.mediator.datasource.DataSource;
import edu.utah.kmm.model.cool.mediator.expression.ExpressionParser;
import edu.utah.kmm.model.cool.mediator.expression.ExpressionTuple;
import edu.utah.kmm.model.cool.mediator.query.QueryContext;
import edu.utah.kmm.model.cool.mediator.query.QueryContextImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Base class for specifying search criteria for a given domain type.  Converts user input to a set of search criteria
 * that are then validated and compiled to a query expression.
 */
public abstract class AbstractSearchCriteria<L extends Identifiable> {

    private static final String MSG_ERROR_MISSING_REQUIRED = "A required search criterion is missing.";

    private final QueryContext queryContext = new QueryContextImpl();

    private final Class<L> logicalType;

    private final String validationFailureMessage;

    private final Character criterionSeparator;

    protected AbstractSearchCriteria(
            Class<L> logicalType,
            Character criterionSeparator,
            String validationFailureMessage) {
        this.logicalType = logicalType;
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
        return ExpressionParser.getInstance().parse(logicalType, sb.toString()).resolve(queryContext);
    }

    /**
     * Perform a search based on given criteria.
     *
     * @param dataSource The data source
     * @return Resources matching the search criteria.
     */
    public List<L> search(DataSource dataSource) {
        return dataSource.getModelDAO(logicalType).search(compile());
    }

    /**
     * Returns true if the current criteria settings meet the minimum requirements for a search.
     *
     * @return True if minimum search requirements have been met.
     */
    protected boolean isValid() {
        return hasContextParam("id");
    }

    /**
     * Validates that the current criteria settings meet the minimum requirements for a search. If
     * not, throws a run-time exception describing the deficiency.
     */
    public void validate() {
        Assert.state(isValid(), validationFailureMessage);
    }

    public boolean hasContextParam(String parameter) {
        return queryContext.hasParam(toContextParam(parameter));
    }

    public void setContextParam(String parameter, Object value) {
        queryContext.setParam(toContextParam(parameter), value);
    }

    private String toContextParam(String parameter) {
        return parameter.replace(".", "_");
    }

    /**
     * Sets the maximum hits criterion.
     *
     * @param maximum Maximum.
     */
    public void setMaximum(Integer maximum) {
        setContextParam("_count", maximum);
    }

    /**
     * Sets the domain identifier
     *
     * @param id Domain identifier.
     */
    public void setId(String id) {
        setContextParam("id", StringUtils.trimToNull(id));
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
                boolean processed = parseCriterion(criterion, position++);
                Assert.isTrue(processed, "Unrecognized search criterion '" + criterion + "'.");
            }
        }
    }

    protected void addFragment(
            StringBuilder sb,
            String searchParam) {
        addFragment(sb, searchParam, "=");
    }

    protected void addFragment(
            StringBuilder sb,
            String searchParam,
            String operator) {
        String contextParam = toContextParam(searchParam);

        if (hasContextParam(contextParam)) {
            sb.append(sb.length() == 0 ? "" : "&");
            sb.append(searchParam).append(" ").append(operator).append(" {{").append(contextParam).append("}}");
        }
    }

}
