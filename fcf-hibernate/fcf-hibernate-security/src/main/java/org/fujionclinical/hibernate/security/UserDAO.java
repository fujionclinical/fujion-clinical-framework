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
package org.fujionclinical.hibernate.security;

import org.fujionclinical.hibernate.core.AbstractDAO;
import org.hibernate.query.Query;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static org.fujionclinical.api.spring.Constants.PROFILE_ROOT;

/**
 * DAO for User class.
 */
@Component
@Profile(PROFILE_ROOT)
public class UserDAO extends AbstractDAO<User> {

    private static final String HQL_AUTHENTICATE = "FROM User "
            + "WHERE LOWER(username)=:username AND password=:password AND (assignedDomain.id=:domain OR assignedDomain.id='*')";

    public UserDAO() {
    }

    @Transactional
    public User authenticate(String username, String password, SecurityDomain domain) {
        User user;
        Query<User> query = getSession().createQuery(HQL_AUTHENTICATE, User.class);
        query.setParameter("password", password);
        query.setParameter("username", username.toLowerCase());
        query.setParameter("domain", domain.getLogicalId());
        user = query.uniqueResult();

        if (user != null) {
            user.setLoginDomain(domain);
        }
        return user;
    }
}
