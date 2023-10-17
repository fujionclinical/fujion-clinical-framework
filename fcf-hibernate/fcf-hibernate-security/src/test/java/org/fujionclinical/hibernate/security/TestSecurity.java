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

import org.fujionclinical.api.security.SecurityDomains;
import org.fujionclinical.api.spring.SpringUtil;
import org.fujionclinical.ui.test.MockUITest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestSecurity extends MockUITest {

    @Test
    public void testService() {
        SecurityDomainDAO sdao = SpringUtil.getBean(SecurityDomainDAO.class);
        setupDomains(sdao);
        SecurityService sc = SpringUtil.getBean(SecurityService.class);
        sc.init();
        assertEquals(3, SecurityDomains.getInstance().getAll().size());
        UserDAO udao = SpringUtil.getBean(UserDAO.class);
        setupUsers(udao);
        SecurityDomain domain = getSecurityDomain("1");
        authenticate(domain, "DOCTOR123", "DOCTOR321$", "1");
        authenticate(domain, "DOCTOR123", "DOCTOR321$XXX", null);
        authenticate(domain, "USER123", "USER321$", "2");
    }

    private void setupDomains(SecurityDomainDAO sc) {
        SecurityDomain domain = new SecurityDomain("1", "General Medicine Clinic", null);
        sc.merge(domain);
        domain = new SecurityDomain("2", "Emergency Room", null);
        sc.merge(domain);
        domain = new SecurityDomain("3", "Test Hospital", "default=true");
        sc.merge(domain);
        domain = new SecurityDomain("*", "All Domains", null);
        sc.merge(domain);
    }

    private void setupUsers(UserDAO udao) {
        SecurityDomain domain = getSecurityDomain("1");
        User user = new User("1", "Doctor, Test", "DOCTOR123", "DOCTOR321$", domain, "PRIV_PATIENT_SELECT");
        udao.merge(user);
        user = new User("2", "User, Test", "USER123", "USER321$", domain,
                "PRIV_DEBUG,PRIV_FCF_DESIGNER,PRIV_PATIENT_SELECT");
        udao.merge(user);
    }

    private void authenticate(SecurityDomain domain, String username, String password, String expectedId) {
        try {
            org.fujionclinical.api.user.User user = domain.authenticate(username, password);
            assertEquals(expectedId, user.getId());
        } catch (Exception e) {
            assertNull(expectedId);
        }
    }

    private SecurityDomain getSecurityDomain(String id) {
        return (SecurityDomain) SecurityDomains.getInstance().get(id);
    }
}
