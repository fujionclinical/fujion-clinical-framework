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

import org.fujionclinical.api.spring.BeanRegistry;
import org.springframework.util.Assert;

/**
 * Tracks all domain DAO implementations.
 */
public class DomainDAORegistry<T extends IDomainObject> extends BeanRegistry<Class<T>, IDomainDAO<T>> {

    private static final DomainDAORegistry<?> instance = new DomainDAORegistry<>();

    public static <T extends IDomainObject> DomainDAORegistry<T> getInstance() {
        return (DomainDAORegistry<T>) instance;
    }

    /**
     * Returns a domain DAO for the specified class.
     *
     * @param <T>   Class of domain object.
     * @param clazz Class of object managed by DAO.
     * @return A domain object DAO.
     * @throws IllegalArgumentException If no DAO found.
     */
    public static <T extends IDomainObject> IDomainDAO<T> getDAO(Class<T> clazz) {
        IDomainDAO<T> dao = instance.get((Class) clazz);
        Assert.notNull(dao, () -> "Class has no registered DAO: " + clazz.getName());
        return dao;
    }

    private DomainDAORegistry() {
        super((Class) IDomainDAO.class);
    }

    @Override
    protected Class<T> getKey(IDomainDAO item) {
        return item.getDomainClass();
    }

}
