/*
 * #%L
 * Fujion Clinical Framework
 * %%
 * Copyright (C) 2019 fujionclinical.org
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

import org.fujionclinical.api.domain.IUser;
import org.fujionclinical.hibernate.core.AbstractDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 * DAO for User class.
 */
public class UserDAO extends AbstractDAO<User> {
    
    
    private static final String HQL_AUTHENTICATE = "FROM org.fujionclinical.hibernate.security.User "
            + "WHERE LOWER(username)=:username AND password=:password AND (domain=:domain OR domain='*')";
    
    public UserDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    public IUser authenticate(String username, String password, SecurityDomain domain) {
        Session session = getSession();
        Transaction tx = session.beginTransaction();
        User user = null;
        
        try {
            @SuppressWarnings("unchecked")
            Query<User> query = getSession().createQuery(HQL_AUTHENTICATE);
            query.setParameter("password", password);
            query.setParameter("username", username.toLowerCase());
            query.setParameter("domain", domain.getLogicalId());
            user = query.uniqueResult();
            
            if (user != null) {
                user.setLoginDomain(domain);
            }
            
            tx.commit();
            return user;
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }
    
}
