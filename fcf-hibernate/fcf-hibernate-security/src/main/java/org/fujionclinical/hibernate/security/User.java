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

import org.fujion.common.StrUtil;
import org.fujionclinical.api.model.PersonName;
import org.fujionclinical.api.model.PersonNameParser;
import org.fujionclinical.api.model.user.IUser;
import org.fujionclinical.api.security.ISecurityDomain;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "FCF_USER")
public class User implements IUser {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "id")
    private String id;
    
    @JoinColumn(name = "domain", nullable = true)
    @ManyToOne
    @PrimaryKeyJoinColumn
    private SecurityDomain assignedDomain;
    
    @Column(name = "name")
    private String fullName;
    
    @Column(name = "username")
    private String loginName;
    
    @Column(name = "password")
    private String password;
    
    @Column(name = "authorities")
    @Lob
    private String authorities;
    
    @Transient
    private SecurityDomain loginDomain;

    @Transient
    private PersonName name;

    protected User() {
    }
    
    public User(String id, String fullName, String loginName, String password, SecurityDomain securityDomain,
        String authorities) {
        this.id = id;
        this.fullName = fullName;
        this.loginName = loginName;
        this.password = password;
        this.assignedDomain = securityDomain;
        this.authorities = authorities;
        this.name = fullName == null ? null : PersonNameParser.instance.fromString(fullName);
    }
    
    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public List<PersonName> getNames() {
        return name == null ? null : Collections.singletonList(name);
    }

    @Override
    public String getFullName() {
        return fullName;
    }
    
    @Override
    public String getLoginName() {
        return loginName;
    }
    
    @Override
    public String getPassword() {
        return password;
    }
    
    @Override
    public ISecurityDomain getSecurityDomain() {
        return loginDomain != null ? loginDomain : assignedDomain;
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    protected void setPassword(String password) {
        this.password = password;
    }
    
    protected void setLoginDomain(SecurityDomain loginDomain) {
        this.loginDomain = loginDomain;
    }
    
    public List<String> getGrantedAuthorities() {
        return StrUtil.toList(authorities, ",");
    }
}
