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
package org.fujionclinical.security.mock.controller;

import org.fujionclinical.api.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for mock login page.
 */
@Controller
public class MockLoginController {

    @Value("#{securityMockUser}")
    private User mockUser;

    @GetMapping("security/login")
    public String login(ModelMap model) {
        model.addAttribute("action", "./login");
        model.addAttribute("username", getUsername());
        model.addAttribute("password", mockUser.getPassword());
        return "classpath:/web/org/fujionclinical/security/mock/login.htm";
    }
    
    private String getUsername() {
        String domain = mockUser.getSecurityDomain().getLogicalId();
        return (domain.isEmpty() ? "" : domain + "\\") + mockUser.getUsername();
    }

}
