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

import org.apache.commons.lang.NotImplementedException;

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
     * @return The new domain object instance.
     */
    default T create() {
        return create(null);
    }

    /**
     * Creates a new instance of an object of this domain.
     *
     * @param id The id of the domain object.
     * @return The new domain object instance.
     */
    T create(String id);

    default List<T> createMultiple(String... ids) {
        List<T> result = new ArrayList<>();

        for (String id: ids) {
            result.add(create(id));
        }

        return result;
    }

    /**
     * Fetches an object, identified by its unique id, from the underlying data store.
     *
     * @param id Unique id of the object.
     * @return The requested object.
     */
    default T fetchObject(String id) {
        throw new NotImplementedException();
    }

    /**
     * Fetches multiple domain objects as specified by an array of identifier values.
     *
     * @param ids An array of unique identifiers.
     * @return A list of domain objects in the same order as requested in the ids parameter.
     */
    default List<T> fetchObjects(String[] ids) {
        List<T> objects = new ArrayList<>();

        for (String id: ids) {
            objects.add(fetchObject(id));
        }

        return objects;
    }

    /**
     * Returns the type of domain object created by this factory.
     *
     * @return The type of domain object created by this factory.
     */
    Class<T> getDomainClass();
}
