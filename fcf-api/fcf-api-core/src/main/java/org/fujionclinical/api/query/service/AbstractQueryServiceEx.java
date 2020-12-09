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

import edu.utah.kmm.model.cool.mediator.datasource.DataSource;

/**
 * A convenience extension to the AbstractQueryService for query services that use an underlying
 * data source.
 *
 * @param <T> Class of query result.
 */
public abstract class AbstractQueryServiceEx<T> extends AbstractQueryService<T> {
    
    private final DataSource dataSource;
    
    public AbstractQueryServiceEx(DataSource dataSource) {
        super();
        this.dataSource = dataSource;
    }
    
    public AbstractQueryServiceEx(DataSource dataSource, IAsyncQueryStrategy<T> strategy) {
        super(strategy);
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
    
}
