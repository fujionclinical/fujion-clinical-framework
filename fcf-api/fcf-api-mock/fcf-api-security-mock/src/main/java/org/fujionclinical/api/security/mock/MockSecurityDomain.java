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

import org.fujion.common.StrUtil;
import org.fujionclinical.api.security.ISecurityDomain;
import org.fujionclinical.api.spring.SpringUtil;
import org.fujionclinical.api.user.User;

import java.io.Serial;
import java.util.List;
import java.util.Map;

/**
 * Mock user for testing.
 */
public class MockSecurityDomain implements ISecurityDomain {

    @Serial
    private static final long serialVersionUID = 1L;
    
    private final String logicalId;
    
    private final String name;
    
    private String mockAuthorities;
    
    private final Map<String, String> attributes;
    
    public MockSecurityDomain() {
        this("mockId", "mockDomain");
    }
    
    public MockSecurityDomain(String logicalId, String name) {
        this(logicalId, name, null);
    }
    
    public MockSecurityDomain(String logicalId, String name, Map<String, String> attributes) {
        this.logicalId = logicalId;
        this.name = name;
        this.attributes = attributes;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getLogicalId() {
        return logicalId;
    }
    
    @Override
    public String getAttribute(String name) {
        return attributes == null ? null : attributes.get(name);
    }
    
    @Override
    public User authenticate(String username, String password) {
        if (!check("org.fujionclinical.security.mock.username", username)
                || !check("org.fujionclinical.security.mock.password", password)) {
            throw new RuntimeException("Authentication failed.");
        }
        
        return new MockUser(SpringUtil.getProperty("org.fujionclinical.security.mock.userid"),
                SpringUtil.getProperty("org.fujionclinical.security.mock.fullname"), username, password, this);
    }
    
    @Override
    public MockSecurityDomain getNativeSecurityDomain() {
        return this;
    }
    
    @Override
    public List<String> getGrantedAuthorities(User user) {
        return StrUtil.toList(mockAuthorities, ",");
    }
    
    public void setMockAuthorities(String mockAuthorities) {
        this.mockAuthorities = mockAuthorities;
    }
    
    protected boolean check(String property, String value) {
        return value.equals(SpringUtil.getProperty(property));
    }
    
}
