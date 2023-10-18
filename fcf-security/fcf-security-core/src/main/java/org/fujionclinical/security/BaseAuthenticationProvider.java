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
package org.fujionclinical.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fujionclinical.api.security.ISecurityDomain;
import org.fujionclinical.api.security.SecurityDomains;
import org.fujionclinical.api.user.User;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Provides authentication support for the framework. Takes provided authentication credentials and
 * authenticates them against the database.
 */
public class BaseAuthenticationProvider implements AuthenticationProvider {
    
    private static final Log log = LogFactory.getLog(BaseAuthenticationProvider.class);
    
    private final List<String> systemGrantedAuthorities = new ArrayList<>();
    
    public BaseAuthenticationProvider() {
        this(false);
    }
    
    public BaseAuthenticationProvider(boolean debugRole) {
        systemGrantedAuthorities.add(Constants.ROLE_USER);
        
        if (debugRole) {
            systemGrantedAuthorities.add(Constants.PRIV_DEBUG);
        }
    }
    
    public BaseAuthenticationProvider(List<String> grantedAuthorities) {
        this(false);
        this.systemGrantedAuthorities.addAll(grantedAuthorities);
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
    
    /**
     * Produces a trusted <code>UsernamePasswordAuthenticationToken</code> if authentication was
     * successful.
     *
     * @param authentication The authentication context.
     * @return authentication Authentication object if authentication succeeded.
     * @throws AuthenticationException Exception on authentication failure.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        FCFAuthenticationDetails details = (FCFAuthenticationDetails) authentication.getDetails();
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        String domain = null;
        
        if (log.isDebugEnabled()) {
            log.debug("User: " + username);
            log.debug("Details, RA: " + (details == null ? "null" : details.getRemoteAddress()));
        }
        
        if (username != null && username.contains("\\")) {
            String[] pcs = username.split("\\\\", 2);
            domain = pcs[0];
            username = pcs.length > 1 ? pcs[1] : null;
        }
        
        ISecurityDomain securityDomain = SecurityDomains.getSecurityDomain(domain);
        
        if (username == null || password == null || securityDomain == null) {
            throw new BadCredentialsException("Missing security credentials.");
        }

        User user = securityDomain.authenticate(username, password);
        details.setDetail("user", user);
        Set<String> mergedAuthorities = mergeAuthorities(securityDomain.getGrantedAuthorities(user),
                systemGrantedAuthorities);
        List<GrantedAuthority> userAuthorities = new ArrayList<>();

        for (String authority : mergedAuthorities) {
            if (!authority.isEmpty()) {
                userAuthorities.add(new SimpleGrantedAuthority(authority));
            }
        }

        org.springframework.security.core.userdetails.User principal = new org.springframework.security.core.userdetails.User(username, password, true, true, true, true, userAuthorities);
        authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(),
                principal.getAuthorities());
        ((UsernamePasswordAuthenticationToken) authentication).setDetails(details);
        return authentication;
    }
    
    /**
     * Merges authorities granted (or revoked) by the system with those granted by the security
     * domain.
     *
     * @param userGrants Authorities granted by the security domain.
     * @param systemGrants Authorities granted by the system.
     * @return A merged set of granted authorities.
     */
    protected Set<String> mergeAuthorities(List<String> userGrants, List<String> systemGrants) {
        Set<String> authorities = userGrants == null ? new HashSet<>() : new HashSet<>(userGrants);
        
        for (String grantedAuthority : systemGrantedAuthorities) {
            if (grantedAuthority.startsWith("-")) {
                authorities.remove(grantedAuthority.substring(1));
            } else {
                authorities.add(grantedAuthority);
            }
        }
        
        return authorities;
    }
    
}
