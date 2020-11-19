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
package org.fujionclinical.api.query.filter;

import edu.utah.kmm.model.cool.core.datatype.Period;
import edu.utah.kmm.model.cool.mediator.query.QueryContext;
import edu.utah.kmm.model.cool.util.IntervalUtils;
import org.fujion.common.DateRange;
import org.fujionclinical.api.query.core.QueryUtil;

import java.time.LocalDateTime;

public class DateQueryFilter<T> extends AbstractQueryFilter<T> {

    public interface IDateTypeExtractor<T> {

        LocalDateTime getDateByType(
                T result,
                DateType dateType);

    }

    /**
     * Type of date to be considered in a query.
     */
    public enum DateType {
        MEASURED, // When the entity was measured
        UPDATED, // When the entity was last updated
        CREATED // When the entity was first created
    }

    private DateType dateType = DateType.MEASURED;

    private Period dateRange;

    private final IDateTypeExtractor<T> dateTypeExtractor;

    public DateQueryFilter(IDateTypeExtractor<T> dateTypeExtractor) {
        this.dateTypeExtractor = dateTypeExtractor;
    }

    /**
     * Filter result based on selected date range.
     */
    @Override
    public boolean include(T result) {
        LocalDateTime dateTime = dateTypeExtractor.getDateByType(result, dateType);
        return dateTime == null || IntervalUtils.inRange(getDateRange(), dateTime);
    }

    @Override
    public boolean updateContext(QueryContext context) {
        context.setParam("dateType", dateType);
        Period oldDateRange = (Period) context.getParam("dateRange");

        if (dateRange == null || oldDateRange == null || !IntervalUtils.inRange(oldDateRange, dateRange)) {
            context.setParam("dateRange", dateRange);
        }

        return context.hasChanged();
    }

    public DateType getDateType() {
        return dateType;
    }

    public void setDateType(DateType dateType) {
        if (this.dateType != dateType) {
            this.dateType = dateType;
            notifyListeners();
        }
    }

    public Period getDateRange() {
        return dateRange;
    }

    public void setDateRange(Period dateRange) {
        if (this.dateRange != dateRange) {
            this.dateRange = dateRange;
            notifyListeners();
        }
    }

    public void setDateRange(DateRange dateRange) {
        this.dateRange = QueryUtil.dateRangeToPeriod(dateRange);
        notifyListeners();
    }

}
