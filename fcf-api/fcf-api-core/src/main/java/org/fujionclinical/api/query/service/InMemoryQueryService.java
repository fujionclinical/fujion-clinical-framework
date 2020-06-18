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
package org.fujionclinical.api.query.service;

import org.fujionclinical.api.query.core.IQueryContext;
import org.fujionclinical.api.query.core.QueryUtil;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * A query service implementation that returns an externally provided result.  Useful for unit testing, but also in instances where
 * a query service is required, but the result data is local.
 *
 * @param <T> Class of query result.
 */
public class InMemoryQueryService<T> extends AbstractQueryService<T> {

    private Supplier<List<T>> queryResult;

    public InMemoryQueryService() {
    }

    public InMemoryQueryService(List<T> queryResult) {
        setQueryResult(queryResult);
    }

    public InMemoryQueryService(Supplier<List<T>> queryResult) {
        setQueryResult(queryResult);
    }

    public void setQueryResult(List<T> queryResult) {
        this.queryResult = () -> queryResult;
    }

    public void setQueryResult(Supplier<List<T>> queryResult) {
        this.queryResult = queryResult;
    }

    @Override
    public boolean hasRequired(IQueryContext context) {
        return true;
    }

    @Override
    public IQueryResult<T> fetch(IQueryContext context) {
        List<T> result = queryResult == null ? null : queryResult.get();
        return QueryUtil.packageResult(result == null ? Collections.emptyList() : result, IQueryResult.CompletionStatus.COMPLETED);
    }

}
