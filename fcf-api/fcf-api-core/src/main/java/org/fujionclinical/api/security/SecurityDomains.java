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
package org.fujionclinical.api.security;

import org.apache.commons.lang3.StringUtils;
import org.fujion.common.AbstractRegistry;

/**
 * Tracks all security domains.
 */
public class SecurityDomains extends AbstractRegistry<String, ISecurityDomain> {
    
    private static final SecurityDomains instance = new SecurityDomains();
    
    public static SecurityDomains getInstance() {
        return instance;
    }
    
    public static ISecurityDomain getSecurityDomain(String name) {
        return StringUtils.isBlank(name) ? null : instance.get(name);
    }
    
    public static void registerSecurityDomain(ISecurityDomain securityDomain) {
        instance.register(securityDomain);
    }
    
    private SecurityDomains() {
        super();
    }
    
    @Override
    protected String getKey(ISecurityDomain item) {
        return item.getLogicalId();
    }
    
}
