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
package org.fujionclinical.api.security.mock;

import org.coolmodel.util.PersonNameParsers;
import org.fujionclinical.api.security.ISecurityDomain;
import org.fujionclinical.api.security.SecurityDomains;
import org.fujionclinical.api.user.UserImpl;

/**
 * Mock user for testing.
 */
public class MockUser extends UserImpl {

    public MockUser() {
        this("mockId", "User, Mock", "username", "password", new MockSecurityDomain());
    }

    public MockUser(
            String logicalId,
            String fullName,
            String username,
            String password,
            String securityDomain) {
        this(logicalId, fullName, username, password, SecurityDomains.getSecurityDomain(securityDomain));
    }

    public MockUser(
            String logicalId,
            String fullName,
            String username,
            String password,
            ISecurityDomain securityDomain) {
        super(username, password, securityDomain);
        setDefaultId(logicalId);
        addName(PersonNameParsers.get().fromString(fullName));
    }

    @Override
    public String toString() {
        return getFullName();
    }

}
