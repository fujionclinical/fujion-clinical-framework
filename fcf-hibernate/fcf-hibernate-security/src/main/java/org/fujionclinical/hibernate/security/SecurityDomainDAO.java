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
package org.fujionclinical.hibernate.security;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.fujionclinical.api.security.SecurityDomains;
import org.fujionclinical.hibernate.core.AbstractDAO;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Loader for Hibernate-based security domains.
 */
public class SecurityDomainDAO extends AbstractDAO<SecurityDomain> {


    public SecurityDomainDAO() {
    }

    public void init() {
        Session session = getSession();
        Transaction tx = session.beginTransaction();

        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<SecurityDomain> criteria = builder.createQuery(SecurityDomain.class);
            criteria.from(SecurityDomain.class);
            List<SecurityDomain> domains = session.createQuery(criteria).getResultList();

            for (SecurityDomain domain : domains) {
                if (!"*".equals(domain.getLogicalId())) {
                    SecurityDomains.registerSecurityDomain(domain);
                }
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }
}
