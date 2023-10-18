/*
 * #%L
 * Fujion Clinical Framework
 * %%
 * Copyright (C) 2023 fujionclinical.org
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
package org.fujionclinical.sharedforms.controller;

import org.coolmodel.foundation.core.Identifiable;
import org.coolmodel.foundation.entity.Person;
import org.coolmodel.mediator.dao.DAO;
import org.coolmodel.mediator.datasource.DataSource;
import org.coolmodel.mediator.expression.parser.Expression;
import org.coolmodel.mediator.expression.parser.ExpressionParser;
import org.coolmodel.mediator.query.QueryContext;
import org.coolmodel.mediator.query.QueryContextImpl;
import org.fujion.common.Assert;

/**
 * Controller for displaying logical model resources in a columnar format.
 *
 * @param <L> Type of resource object.
 * @param <M> Type of model object.
 * @param <S> Type of data source.
 */
public abstract class ResourceListView<L extends Identifiable, M, S extends DataSource> extends AbstractResourceListView<L, M, S> {

    private final QueryContext queryContext = new QueryContextImpl();

    private Expression<L> queryExpression;

    private DAO<L> dao;

    protected void setup(
            Class<L> logicalType,
            String title,
            String detailTitle,
            String queryString,
            int sortBy,
            String... headers) {
        super.setup(logicalType, title, detailTitle, queryString, sortBy, headers);
        this.dao = getDataSource().getDAO(logicalType);
        Assert.notNull(dao, () -> "Cannot find DAO for " + getResourceClass());
        this.queryExpression = ExpressionParser.getInstance().parse(logicalType, queryString);
    }

    @Override
    protected void requestData() {
        startBackgroundThread(map -> map.put("results", dao.search(queryExpression, queryContext).getElements()));
    }

    @Override
    protected void afterPatientChange(Person patient) {
        queryContext.setParam("patient", patient == null ? null : patient.getDefaultId().getId());
    }

}
