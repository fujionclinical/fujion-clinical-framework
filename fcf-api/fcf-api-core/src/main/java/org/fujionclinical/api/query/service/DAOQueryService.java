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

import org.fujionclinical.api.model.core.IDomainType;
import org.fujionclinical.api.model.dao.DomainDAOs;
import org.fujionclinical.api.model.dao.IDomainDAO;
import org.fujionclinical.api.query.core.IQueryContext;
import org.fujionclinical.api.query.core.QueryUtil;
import org.fujionclinical.api.query.expression.Expression;
import org.fujionclinical.api.query.expression.ExpressionParser;

import java.util.List;

/**
 * Query service that uses a DAO.
 *
 * @param <T> The type of domain object.
 */
public class DAOQueryService<T extends IDomainType> extends AbstractQueryServiceEx<IDomainDAO<T>, T> {

    private final Expression<T> queryExpression;

    public DAOQueryService(
            Class<T> domainClass,
            String queryString) {
        super(DomainDAOs.getDAO(domainClass));
        this.queryExpression = ExpressionParser.getInstance().parse(domainClass, queryString);
    }

    @Override
    public boolean hasRequired(IQueryContext context) {
        return true;
    }

    @Override
    public IQueryResult<T> fetch(IQueryContext queryContext) {
        List<T> results = service.search(queryExpression.resolve(queryContext));
        return QueryUtil.packageResult(results, IQueryResult.CompletionStatus.COMPLETED);
    }

}
