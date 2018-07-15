/*
 * #%L
 * Fujion Clinical Framework
 * %%
 * Copyright (C) 2018 fujionclinical.org
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

import org.fujionclinical.api.domain.IUser;
import org.fujionclinical.api.security.SecurityUtil;
import org.fujion.common.StrUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for access denied page.
 */
@Controller
public class AccessDeniedController {
    
    @RequestMapping("security/accessDenied")
    public String accessDenied(ModelMap model) {
        IUser user = SecurityUtil.getAuthenticatedUser();
        model.addAttribute("title", "Access Denied");
        model.addAttribute("authenticated", user != null);
        
        if (user == null) {
            model.addAttribute("message", StrUtil.getLabel("security.denied.message.anonymous"));
        } else {
            model.addAttribute("message", StrUtil.getLabel("security.denied.message.authenticated", user.getLoginName()));
        }

        return "classpath:/web/org/fujionclinical/security/accessDenied.htm";
    }

}
