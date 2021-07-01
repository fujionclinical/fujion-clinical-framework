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

import org.coolmodel.foundation.datatype.PersonName;
import org.coolmodel.util.PersonNameParsers;
import org.fujion.common.StrUtil;
import org.fujionclinical.api.security.ISecurityDomain;
import org.fujionclinical.api.user.UserImpl;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "FCF_USER")
public class User extends UserImpl {

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
    private String username;

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

    public User(
            String id,
            String fullName,
            String username,
            String password,
            SecurityDomain securityDomain,
            String authorities) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.assignedDomain = securityDomain;
        this.authorities = authorities;
        postLoad();
    }

    @Override
    public ISecurityDomain getSecurityDomain() {
        return loginDomain != null ? loginDomain : assignedDomain;
    }

    protected void setLoginDomain(SecurityDomain loginDomain) {
        this.loginDomain = loginDomain;
    }

    public List<String> getGrantedAuthorities() {
        return StrUtil.toList(authorities, ",");
    }

    @Override
    protected void setUsername(String username) {
        super.setUsername(username);
    }

    @Override
    protected void setPassword(String password) {
        super.setPassword(password);
    }

    @PostLoad
    protected void postLoad() {
        setId(id);
        setSecurityDomain(assignedDomain);
        setUsername(username);
        setPassword(password);

        if (fullName != null) {
            setName(Collections.singletonList(PersonNameParsers.get().fromString(fullName)));
        }
    }
}
