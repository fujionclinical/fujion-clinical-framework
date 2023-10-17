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
package org.fujionclinical.security.basic.controller;

import org.fujion.common.StrUtil;
import org.fujion.core.RequestUtil;
import org.fujion.webjar.WebJarLocator;
import org.fujionclinical.api.security.ISecurityDomain;
import org.fujionclinical.api.security.ISecurityService;
import org.fujionclinical.api.security.SecurityDomains;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * Controller for login page.
 */
@Controller
public class LoginController {
    
    @Autowired
    private ISecurityService securityService;
    
    @GetMapping(path = "security/login", produces = "text/html")
    public String login(ModelMap model, HttpServletRequest request) {
        Collection<ISecurityDomain> domains = SecurityDomains.getInstance().getAll();
        model.addAttribute("baseUrl", RequestUtil.getBaseURL(request));
        model.addAttribute("importMap", WebJarLocator.getInstance().getImportMap());
        model.addAttribute("timeout", request.getSession().getMaxInactiveInterval() * 1000);
        model.addAttribute("domainCount", domains.size());
        model.addAttribute("domains", domains);
        model.addAttribute("disabled", securityService.loginDisabled());
        model.addAttribute("action", "security/login");

        String error = request.getParameter("error");
        model.addAttribute("error",
            error == null ? null : error.isEmpty() ? StrUtil.getLabel("security.login.error") : error);
        return "classpath:/web/org/fujionclinical/security/basic/login.htm";
    }
    
}
