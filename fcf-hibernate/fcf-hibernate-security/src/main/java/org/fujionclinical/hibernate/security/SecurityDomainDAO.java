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

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.fujionclinical.hibernate.core.AbstractDAO;
import org.hibernate.Session;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.fujionclinical.api.spring.Constants.PROFILE_ROOT;

/**
 * Loader for Hibernate-based security domains.
 */
@Component
@Profile(PROFILE_ROOT)
public class SecurityDomainDAO extends AbstractDAO<SecurityDomain> {

    public SecurityDomainDAO() {
    }

    @Transactional
    public List<SecurityDomain> getSecurityDomains() {
        Session session = getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<SecurityDomain> criteria = builder.createQuery(SecurityDomain.class);
        criteria.from(SecurityDomain.class);
        return session.createQuery(criteria).getResultList();
    }

}
