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
package org.fujionclinical.api.model;

import org.fujionclinical.api.query.IQueryContext;
import org.fujionclinical.api.query.QueryExpression;
import org.fujionclinical.api.query.QueryExpressionTuple;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface for a domain object DAO.
 *
 * @param <T> Class created by DAO.
 */
public interface IDomainDAO<T extends IDomainObject> {

    /**
     * Creates a new instance of an object of this domain.
     *
     * @param template The domain object to use as a template.
     * @return The new domain object instance.
     */
    T create(T template);

    /**
     * Creates multiple domain object instances from a list of templates..
     *
     * @param templates The domain objects to use as templates.
     * @return A list of domain object instances.
     */
    default List<T> create(T... templates) {
        List<T> result = new ArrayList<>();

        for (T template : templates) {
            result.add(create(template));
        }

        return result;
    }

    /**
     * Fetches an object, identified by its unique id, from the underlying data store.
     *
     * @param id Unique id of the object.
     * @return The requested object.
     */
    abstract T read(String id);

    /**
     * Fetches multiple domain objects given a list of ids.
     *
     * @param ids A list of unique identifiers.
     * @return A list of domain objects in the same order as requested in the ids parameter.
     */
    default List<T> read(String... ids) {
        List<T> objects = new ArrayList<>();

        for (String id : ids) {
            objects.add(read(id));
        }

        return objects;
    }

    /**
     * Performs a query, returning a list of matching domain objects.
     *
     * @param query The query expression.
     * @return A list of matching domain objects.
     */
    default List<T> search(QueryExpression query) {
        return search(query, null);
    }

    /**
     * Performs a query, returning a list of matching domain objects.
     *
     * @param query The query expression.
     * @param queryContext The query context (may be null).
     * @return A list of matching domain objects.
     */
    default List<T> search(QueryExpression query, IQueryContext queryContext) {
        return search(query.resolve(queryContext));
    }

    /**
     * Performs a query, returning a list of matching domain objects.
     *
     * @param tuples A list of query tuples.
     * @return A list of matching domain objects.
     */
    List<T> search(List<QueryExpressionTuple> tuples);

    /**
     * Returns the type of domain object created by this factory.
     *
     * @return The type of domain object created by this factory.
     */
    Class<T> getDomainClass();

}
