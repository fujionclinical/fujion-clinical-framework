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

import jakarta.annotation.PostConstruct;
import org.fujionclinical.api.security.SecurityDomains;
import org.fujionclinical.security.AbstractSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Hibernate-based security service.
 */
@Component
@Profile("root")
public class SecurityService extends AbstractSecurityService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private SecurityDomainDAO securityDomainDAO;

    public SecurityService() {
    }

    @PostConstruct
    public void init() {
        List<SecurityDomain> domains = securityDomainDAO.getSecurityDomains();

        for (SecurityDomain domain : domains) {
            if (!"*".equals(domain.getLogicalId())) {
                SecurityDomains.registerSecurityDomain(domain);
            }
        }

    }

    @Override
    public boolean validatePassword(String password) {
        return password.equals(getAuthenticatedUser().getPassword());
    }

    @Override
    public String changePassword(String oldPassword, String newPassword) {
        if (!validatePassword(oldPassword)) {
            return "Invalid username or password.";
        }

        User user = (User) getAuthenticatedUser();
        user.setPassword(newPassword);
        try {
            userDAO.persist(user);
            return null;
        } catch (Exception e) {
            user.setPassword(oldPassword);
            return e.getMessage();
        }
    }

}
