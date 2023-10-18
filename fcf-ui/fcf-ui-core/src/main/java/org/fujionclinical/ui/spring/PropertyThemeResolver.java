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
package org.fujionclinical.ui.spring;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.fujionclinical.api.property.PropertyUtil;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.ThemeResolver;

/**
 * Resolves theme name from property named <code>FCF.THEME</code>.
 */
public class PropertyThemeResolver implements ThemeResolver, Ordered {

    public static final String THEME_PROPERTY = "FCF.THEME";
    
    @Override
    public String resolveThemeName(HttpServletRequest request) {
        return PropertyUtil.isAvailable() ? PropertyUtil.getValue(THEME_PROPERTY, null) : null;
    }

    @Override
    public void setThemeName(HttpServletRequest request, HttpServletResponse response, String themeName) {
        // NOP
    }
    
    @Override
    public int getOrder() {
        return 0;
    }

}
