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
package org.fujionclinical.api.user;

import edu.utah.kmm.model.cool.foundation.entity.PersonImpl;
import org.fujionclinical.api.security.ISecurityDomain;

public class UserImpl extends PersonImpl implements User {

    private ISecurityDomain securityDomain;

    private String username;

    private String password;

    public UserImpl() {
    }

    public UserImpl(String username, String password, ISecurityDomain securityDomain) {
        this.username = username;
        this.password = password;
        this.securityDomain = securityDomain;
    }

    @Override
    public String getUsername() {
        return username;
    }

    protected void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    protected void setPassword(String password) {
        this.password = password;
    }

    @Override
    public ISecurityDomain getSecurityDomain() {
        return securityDomain;
    }

    protected void setSecurityDomain(ISecurityDomain securityDomain) {
        this.securityDomain = securityDomain;
    }

}
