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
package org.fujionclinical.hibernate.property;

import org.fujionclinical.api.user.User;
import org.fujionclinical.hibernate.core.AbstractDAO;
import org.fujionclinical.hibernate.property.Property.PropertyId;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import java.util.Collections;
import java.util.List;

public class PropertyDAO extends AbstractDAO<Property> {


    private static final String GET_INSTANCES = "SELECT DISTINCT INSTANCE FROM FCF_PROPERTY WHERE NAME=:name AND USER=:user AND INSTANCE<>''";

    public PropertyDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Property get(
            String propertyName,
            String instanceName,
            User user) {
        PropertyId id = new PropertyId(propertyName, instanceName, user == null ? null : user.getId());
        return get(Property.class, id);
    }

    public List<String> getInstances(
            String propertyName,
            User user) {
        Session session = getSession();
        Transaction tx = session.beginTransaction();

        try {
            @SuppressWarnings("unchecked")
            NativeQuery<String> query = session.createNativeQuery(GET_INSTANCES);
            query.setParameter("name", propertyName).setParameter("user", user == null ? "" : user.getId());
            List<String> result = query.list();
            Collections.sort(result, String.CASE_INSENSITIVE_ORDER);
            tx.commit();
            return result;
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }

}
