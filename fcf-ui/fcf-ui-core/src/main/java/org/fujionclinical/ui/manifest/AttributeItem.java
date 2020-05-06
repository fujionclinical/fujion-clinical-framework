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
package org.fujionclinical.ui.manifest;

import org.apache.commons.lang.StringUtils;

import java.util.Map.Entry;

/**
 * Model object for a single manifest attribute.
 */
public class AttributeItem implements IMatchable<AttributeItem> {
    
    public final String name;
    
    public final String value;
    
    public AttributeItem(Entry<Object, Object> entry) {
        name = entry.getKey().toString();
        value = entry.getValue().toString();
    }
    
    @Override
    public int compareTo(AttributeItem o) {
        return name.compareToIgnoreCase(o.name);
    }
    
    @Override
    public boolean matches(String filter) {
        return StringUtils.containsIgnoreCase(name, filter) || StringUtils.containsIgnoreCase(value, filter);
    }
    
}
