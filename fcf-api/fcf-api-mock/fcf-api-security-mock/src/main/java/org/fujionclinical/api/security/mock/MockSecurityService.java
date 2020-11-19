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

import org.fujionclinical.api.user.User;
import org.fujionclinical.api.security.ISecurityService;

/**
 * Mock security service for testing.
 */
public class MockSecurityService implements ISecurityService {
    
    private final MockUser mockUser;
    
    public MockSecurityService() {
        this(new MockUser());
    }
    
    public MockSecurityService(MockUser mockUser) {
        this.mockUser = mockUser;
    }
    
    @Override
    public void logout(boolean force, String target, String message) {
        // NOP
    }
    
    @Override
    public boolean validatePassword(String password) {
        return password.equals(mockUser.getPassword());
    }
    
    @Override
    public String changePassword(String oldPassword, String newPassword) {
        return "Operation not supported";
    }
    
    @Override
    public void changePassword() {
    }
    
    @Override
    public boolean canChangePassword() {
        return false;
    }
    
    @Override
    public String generateRandomPassword() {
        return null;
    }
    
    @Override
    public void setAuthorityAlias(String authority, String alias) {
    }
    
    @Override
    public boolean isAuthenticated() {
        return true;
    }
    
    @Override
    public User getAuthenticatedUser() {
        return mockUser;
    }
    
    @Override
    public boolean hasDebugRole() {
        return true;
    }
    
    @Override
    public boolean isGranted(String grantedAuthority) {
        return true;
    }
    
    @Override
    public boolean isGranted(String grantedAuthorities, boolean checkAllRoles) {
        return true;
    }
    
    @Override
    public String loginDisabled() {
        return null;
    }
    
}
