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
package org.fujionclinical.shell.elements;

import org.fujionclinical.shell.plugins.PluginDefinition;
import org.fujionclinical.shell.property.IPropertyAccessor;
import org.fujionclinical.shell.property.PropertyInfo;

import java.util.HashMap;

/**
 * Proxy for an arbitrary UI element that can store and return property values. This is used by the
 * designer to create placeholders for actual UI elements without creating the element itself and
 * for deferring property changes to existing UI elements.
 */
public class ElementProxy extends ElementBase implements IPropertyAccessor {
    
    private final HashMap<String, Object> properties = new HashMap<>();
    
    private ElementBase real;
    
    private boolean deleted;
    
    public ElementProxy(PluginDefinition def) {
        super();
        setDefinition(def);
        revert();
    }
    
    public ElementProxy(ElementBase real) {
        super();
        this.real = real;
        
        if (real != null) {
            setDefinition(real.getDefinition());
        }
        
        revert();
    }
    
    /**
     * Override to get property value from proxy's property cache.
     *
     * @see org.fujionclinical.shell.property.IPropertyAccessor#getPropertyValue
     */
    @Override
    public Object getPropertyValue(PropertyInfo propInfo) {
        return getPropertyValue(propInfo.getId());
    }
    
    public Object getPropertyValue(String propName) {
        return properties.get(propName);
    }
    
    /**
     * Overridden to set property value in proxy's property cache.
     *
     * @see org.fujionclinical.shell.property.IPropertyAccessor#setPropertyValue
     */
    @Override
    public void setPropertyValue(PropertyInfo propInfo, Object value) {
        setPropertyValue(propInfo.getId(), value);
    }
    
    public Object setPropertyValue(String propName, Object value) {
        return properties.put(propName, value);
    }
    
    public ElementBase getReal() {
        return real;
    }
    
    protected void revert() {
        properties.clear();
        syncProperties(true);
    }
    
    public void commit() {
        syncProperties(false);
    }
    
    /**
     * Realizes the creation or destruction of the proxied object. In other words, if this is a
     * deletion operation and the proxied object exists, the proxied object is removed from its
     * parent. If this is not a deletion and the proxied object does not exist, a new object is
     * instantiated as a child to the specified parent.
     *
     * @param parent The parent UI element.
     * @return Returns the updated proxied object.
     */
    public ElementBase realize(ElementBase parent) {
        if (!deleted && real == null) {
            real = getDefinition().createElement(parent, null, false);
        } else if (deleted && real != null) {
            real.remove(true);
            real = null;
        }

        return real;
    }
    
    /**
     * Synchronizes property values between the proxy and its object.
     *
     * @param fromReal If true, property values are copied from the target to the proxy. If false,
     *            property values are copied from the proxy to the target.
     */
    private void syncProperties(boolean fromReal) {
        PluginDefinition def = getDefinition();
        
        for (PropertyInfo propInfo : def.getProperties()) {
            if (fromReal) {
                syncProperty(propInfo, real, this);
            } else {
                syncProperty(propInfo, this, real);
            }
        }
    }
    
    private void syncProperty(PropertyInfo propInfo, Object from, Object to) {
        if (to != null) {
            propInfo.setPropertyValue(to, propInfo.getPropertyValue(from));
        }
    }
    
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
    public boolean isDeleted() {
        return deleted;
    }
}
