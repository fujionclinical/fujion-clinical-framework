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
package org.fujionclinical.api.query.core;

import edu.utah.kmm.model.cool.core.datatype.Period;
import edu.utah.kmm.model.cool.core.datatype.PeriodImpl;
import org.fujion.common.DateRange;
import org.fujion.common.DateUtil;
import org.fujionclinical.api.query.service.IQueryResult;
import org.fujionclinical.api.query.service.IQueryResult.CompletionStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Static utility methods.
 */
public class QueryUtil {

    private static class QueryResult<T> implements IQueryResult<T> {

        private final List<T> results;
        
        private final CompletionStatus status;
        
        private final Map<String, Object> metadata;
        
        private QueryResult(List<T> results, CompletionStatus status, Map<String, Object> metadata) {
            this.results = results;
            this.status = status == null ? CompletionStatus.COMPLETED : status;
            this.metadata = metadata;
        }
        
        @Override
        public CompletionStatus getStatus() {
            return status;
        }
        
        @Override
        public List<T> getResults() {
            return results == null ? Collections.emptyList() : results;
        }
        
        @Override
        public Object getMetadata(String key) {
            return metadata.get(key);
        }
        
    }
    
    /**
     * Returns a query result for an aborted operation.
     * 
     * @param <T> Class of query result.
     * @param reason Optional reason for the aborted operation.
     * @return Query result.
     */
    public static <T> IQueryResult<T> abortResult(String reason) {
        return packageResult(null, CompletionStatus.ABORTED,
            reason == null ? null : Collections.singletonMap("reason", reason));
    }
    
    /**
     * Returns a query result for an error.
     * 
     * @param <T> Class of query result.
     * @param exception The exception being reported.
     * @return Query result.
     */
    public static <T> IQueryResult<T> errorResult(Throwable exception) {
        return packageResult(null, CompletionStatus.ERROR,
            exception == null ? null : Collections.singletonMap("exception", exception));
    }
    
    /**
     * Convenience method for packaging query results.
     *
     * @param <T> Class of query result.
     * @param results Results to package.
     * @return Packaged results.
     */
    public static <T> IQueryResult<T> packageResult(List<T> results) {
        return packageResult(results, null);
    }
    
    /**
     * Convenience method for packaging query results.
     *
     * @param <T> Class of query result.
     * @param results Results to package.
     * @param status The completion status.
     * @return Packaged results.
     */
    public static <T> IQueryResult<T> packageResult(List<T> results, CompletionStatus status) {
        return packageResult(results, status, null);
    }
    
    /**
     * Convenience method for packaging query results.
     *
     * @param <T> Class of query result.
     * @param results Results to package.
     * @param status The completion status.
     * @param metadata Additional metadata.
     * @return Packaged results and metadata.
     */
    public static <T> IQueryResult<T> packageResult(List<T> results, CompletionStatus status, Map<String, Object> metadata) {
        return new QueryResult<>(results, status, metadata);
    }

    /**
     * Returns an enum member if the value matches the start of one and only one member's name.
     * Matching is case-insensitive.
     *
     * @param clazz The enum class.
     * @param value The value to test.
     * @param <T> The enum type.
     * @return The matching member, or null if no match.
     */
    public static <T extends Enum<T>> T findMatchingMember(Class<T> clazz, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        String uc = value.toUpperCase();
        List<T> result = Arrays.stream(clazz.getEnumConstants())
                .filter(element -> element.name().toUpperCase().startsWith(uc))
                .collect(Collectors.toList());
        return result.size() == 1 ? result.get(0) : null;
    }

    public static DateRange periodToDateRange(Period period) {
        return new DateRange(DateUtil.toDate(period.getStart()), DateUtil.toDate(period.getEnd()));
    }

    public static Period dateRangeToPeriod(DateRange dateRange) {
        return new PeriodImpl(DateUtil.toLocalDateTime(dateRange.getStartDate()), DateUtil.toLocalDateTime(dateRange.getEndDate()));
    }

    /**
     * Force static class.
     */
    private QueryUtil() {
    }
}
