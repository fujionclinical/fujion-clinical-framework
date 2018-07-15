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
package org.fujionclinical.ui.manifest;

import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.commons.lang.StringUtils;
import org.fujion.common.StrUtil;

/**
 * Model object for a single manifest.
 */
public class ManifestItem implements IMatchable<ManifestItem> {
    
    public final Manifest manifest;
    
    public final String implVersion;
    
    public final String implVendor;
    
    public final String implModule;
    
    /**
     * Wrapper for a single manifest.
     * 
     * @param manifest A manifest.
     */
    public ManifestItem(Manifest manifest) {
        this.manifest = manifest;
        implVersion = get("Implementation-Version", "Bundle-Version");
        implVendor = get("Implementation-Vendor", "Bundle-Vendor");
        implModule = get("Bundle-Name", "Implementation-Title", "Implementation-URL");
    }
    
    private String get(String... names) {
        String result = null;
        Attributes attributes = manifest.getMainAttributes();
        
        for (String name : names) {
            result = attributes.getValue(name);
            result = result == null ? "" : StrUtil.stripQuotes(result.trim());
            
            if (!result.isEmpty()) {
                break;
            }
        }
        
        return result;
    }
    
    public boolean isEmpty() {
        return implModule == null || implModule.isEmpty();
    }
    
    @Override
    public int compareTo(ManifestItem o) {
        int result = compare(implModule, o.implModule);
        result = result == 0 ? compare(implVendor, o.implVendor) : result;
        result = result == 0 ? compare(implVersion, o.implVersion) : result;
        return result;
    }
    
    @Override
    public boolean equals(Object o) {
        return o instanceof ManifestItem && compareTo((ManifestItem) o) == 0;
    }
    
    private int compare(String s1, String s2) {
        return s1 == s2 ? 0 : s1 == null ? -1 : s2 == null ? 1 : s1.compareToIgnoreCase(s2);
    }
    
    @Override
    public boolean matches(String filter) {
        return StringUtils.containsIgnoreCase(implModule, filter) || StringUtils.containsIgnoreCase(implVendor, filter)
                || StringUtils.containsIgnoreCase(implVersion, filter);
    }
}
