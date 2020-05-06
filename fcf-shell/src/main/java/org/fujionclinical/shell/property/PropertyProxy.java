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
package org.fujionclinical.shell.property;

/**
 * Used to hold property values prior to plugin initialization. When a plugin is subsequently
 * initialized and registers a property, the value in the corresponding proxy is used to initialize
 * the property. This allows the deserializer to initialize property values even though the plug-in
 * has not yet been instantiated.
 */
public class PropertyProxy {
    
    private Object value;
    
    private final PropertyInfo propInfo;
    
    public Object getValue() {
        return value;
    }
    
    public void setValue(Object value) {
        this.value = value;
    }
    
    public PropertyInfo getPropertyInfo() {
        return propInfo;
    }
    
    public PropertyProxy(PropertyInfo propInfo, Object value) {
        this.propInfo = propInfo;
        this.value = value;
    }
}
