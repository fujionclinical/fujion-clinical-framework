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
package org.fujionclinical.hibernate.core;

import jakarta.persistence.LockModeType;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

/**
 * Abstract base implementation for a DAO object.
 *
 * @param <T> The data class.
 */
public abstract class AbstractDAO<T> {

    @Autowired
    @Qualifier("fcfHibernateSessionFactory")
    private SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Transactional
    public void persist(T entity) {
        getSession().persist(entity);
    }

    @Transactional
    public T merge(T entity) {
        return getSession().merge(entity);
    }

    @Transactional
    public void remove(T entity) {
        getSession().remove(entity);
    }

    @Transactional(readOnly = true)
    public T get(Class<T> clazz, Object id) {
        return getSession().get(clazz, id);
    }

}
