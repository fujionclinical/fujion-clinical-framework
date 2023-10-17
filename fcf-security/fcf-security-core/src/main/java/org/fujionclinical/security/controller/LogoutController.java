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
package org.fujionclinical.security.controller;

import org.fujionclinical.security.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Controller for logout and logout success pages.
 */
@Controller
public class LogoutController {
    
    @GetMapping("security/logout")
    public String logout(ModelMap model, HttpServletRequest request) {
        model.addAttribute("action", request.getRequestURI() + "?" + request.getQueryString());
        return "classpath:/web/org/fujionclinical/security/logout.htm";
    }

    @GetMapping("security/logout_success")
    public String logoutSuccess(ModelMap model, @RequestParam(name = "message", required = false) String message,
                                @RequestParam(name = "target", required = false) String target, HttpSession session) {
        model.addAttribute("message",
            StringUtils.isEmpty(message) ? Constants.MSG_LOGOUT_MESSAGE_DEFAULT.toString() : message);
        model.addAttribute("target", StringUtils.isEmpty(target) ? "/" : target);
        return "classpath:/web/org/fujionclinical/security/logoutSuccess.htm";
    }
    
}
