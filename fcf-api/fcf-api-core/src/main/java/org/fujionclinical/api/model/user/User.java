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
package org.fujionclinical.api.model.user;

import org.fujionclinical.api.model.person.IPersonName;
import org.fujionclinical.api.model.person.PersonNameParser;
import org.fujionclinical.api.security.ISecurityDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple implementation of a user.
 */
public class User implements IUser {
    
    private static final long serialVersionUID = 1L;
    
    private String id;
    
    private final List<IPersonName> names = new ArrayList<>();
    
    private String loginName;
    
    private String password;
    
    private ISecurityDomain securityDomain;
    
    public User() {
        
    }
    
    public User(String id, String fullName, String loginName, String password, ISecurityDomain securityDomain) {
        this.id = id;
        addNames(PersonNameParser.instance.fromString(fullName));
        this.loginName = loginName;
        this.password = password;
        this.securityDomain = securityDomain;
    }
    
    /**
     * Returns the logical identifier for the user.
     * 
     * @return User logical identifier.
     */
    @Override
    public String getId() {
        return id;
    }
    
    /**
     * Sets the logical identifier for the user.
     * 
     * @param id User logical identifier.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the users' known names.
     * 
     * @return Users' known names.
     */
    @Override
    public List<IPersonName> getNames() {
        return names;
    }

    /**
     * Return the user's login name.
     * 
     * @return User's login name.
     */
    @Override
    public String getLoginName() {
        return loginName;
    }
    
    /**
     * Sets the user's login name.
     * 
     * @param loginName User's login name.
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    
    /**
     * Return the user's password.
     * 
     * @return User's password.
     */
    @Override
    public String getPassword() {
        return password;
    }
    
    /**
     * Sets the user's password.
     * 
     * @param password User's password.
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Returns the user's security domain.
     * 
     * @return User's security domain.
     */
    @Override
    public ISecurityDomain getSecurityDomain() {
        return securityDomain;
    }
    
    /**
     * Sets the user's security domain.
     * 
     * @param securityDomain User's security domain.
     */
    public void setSecurityDomain(ISecurityDomain securityDomain) {
        this.securityDomain = securityDomain;
    }
    
}
