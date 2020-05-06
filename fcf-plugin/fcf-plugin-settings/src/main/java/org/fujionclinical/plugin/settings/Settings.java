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
package org.fujionclinical.plugin.settings;

import org.fujionclinical.shell.elements.ElementBase;
import org.fujionclinical.shell.property.IPropertyAccessor;
import org.fujionclinical.shell.property.PropertyInfo;

/**
 * This is a simple wrapper class that allows the property editor to access settings through the
 * settings provider.
 */
public class Settings extends ElementBase implements IPropertyAccessor {
    
    private final ISettingsProvider provider;
    
    public Settings(String group, ISettingsProvider provider) {
        super();
        this.provider = provider;
        setDefinition(provider.fetch(group));
    }
    
    @Override
    public Object getPropertyValue(PropertyInfo propInfo) throws Exception {
        return provider.getPropertyValue(propInfo);
    }
    
    @Override
    public void setPropertyValue(PropertyInfo propInfo, Object value) throws Exception {
        provider.setPropertyValue(propInfo, value);
    }
    
}
