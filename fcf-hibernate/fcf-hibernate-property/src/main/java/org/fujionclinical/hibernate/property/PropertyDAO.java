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
package org.fujionclinical.hibernate.property;

import jakarta.persistence.LockModeType;
import org.fujionclinical.hibernate.core.AbstractDAO;
import org.hibernate.Session;
import org.hibernate.query.SelectionQuery;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Profile("root")
public class PropertyDAO extends AbstractDAO<Property> {

    private static final String GET_INSTANCES = "SELECT distinct p.id.instance FROM Property p WHERE p.id.name=:name AND p.id.user=:user AND p.id.instance<>''";

    public PropertyDAO() {
    }

    @Transactional
    public Property get(
            String propertyName,
            String instanceName,
            String userId) {
        PropertyId id = new PropertyId(propertyName, instanceName, userId);
        return get(Property.class, id);
    }

    @Transactional(readOnly = true)
    public List<String> getInstances(
            String propertyName,
            String userId) {
        Session session = getSession();
        SelectionQuery<String> query = session.createSelectionQuery(GET_INSTANCES, String.class);
        query.setParameter("name", propertyName).setParameter("user", userId);
        query.setLockMode(LockModeType.READ);
        List<String> result = query.list();
        result.sort(String.CASE_INSENSITIVE_ORDER);
        return result;
    }

}
