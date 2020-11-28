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
package org.fujionclinical.sharedforms.controller;

import edu.utah.kmm.model.cool.foundation.core.Identifiable;
import edu.utah.kmm.model.cool.foundation.entity.Person;
import edu.utah.kmm.model.cool.mediator.dao.ModelDAO;
import edu.utah.kmm.model.cool.mediator.datasource.DataSource;
import edu.utah.kmm.model.cool.mediator.expression.Expression;
import edu.utah.kmm.model.cool.mediator.expression.ExpressionParser;
import edu.utah.kmm.model.cool.mediator.query.QueryContext;
import edu.utah.kmm.model.cool.mediator.query.QueryContextImpl;
import org.springframework.util.Assert;

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

    private ModelDAO<L> dao;

    protected void setup(
            Class<L> resourceClass,
            String title,
            String detailTitle,
            String queryString,
            int sortBy,
            String... headers) {
        super.setup(resourceClass, title, detailTitle, queryString, sortBy, headers);
        this.dao = getDataSource().getModelDAO(resourceClass);
        Assert.notNull(dao, () -> "Cannot find DAO for " + getResourceClass());
        this.queryExpression = ExpressionParser.getInstance().parse(resourceClass, queryString);
    }

    @Override
    protected void requestData() {
        startBackgroundThread(map -> map.put("results", dao.search(queryExpression, queryContext)));
    }

    @Override
    protected void afterPatientChange(Person patient) {
        queryContext.setParam("patient", patient == null ? null : patient.getDefaultId().getId());
    }

}
