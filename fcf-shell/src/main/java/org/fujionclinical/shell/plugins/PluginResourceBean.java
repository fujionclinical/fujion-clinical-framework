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
package org.fujionclinical.shell.plugins;

import org.fujionclinical.shell.Shell;
import org.fujionclinical.shell.elements.ElementBase;
import org.fujionclinical.shell.elements.ElementPlugin;

/**
 * Resource for declaring helper beans associated with the plugin.
 */
public class PluginResourceBean implements IPluginResource {
    
    private String bean;
    
    private boolean required = true;
    
    /**
     * Sets the referenced bean by its id.
     * 
     * @param bean The bean id.
     */
    public void setBean(String bean) {
        this.bean = bean;
    }
    
    /**
     * Gets the id of the referenced bean.
     * 
     * @return The bean id.
     */
    public String getBean() {
        return bean;
    }
    
    /**
     * Sets whether or not the reference bean is required.
     * 
     * @param required If true and the bean is not found, an exception is raised.
     */
    public void setRequired(boolean required) {
        this.required = required;
    }
    
    /**
     * Returns whether or not the reference bean is required.
     * 
     * @return If true and the bean is not found, an exception is raised.
     */
    public boolean isRequired() {
        return required;
    }
    
    /**
     * Registers/unregisters a bean resource.
     * 
     * @param shell The running shell.
     * @param owner Owner of the resource.
     * @param register If true, register the resource. If false, unregister it.
     */
    @Override
    public void register(Shell shell, ElementBase owner, boolean register) {
        if (register) {
            ElementPlugin plugin = (ElementPlugin) owner;
            plugin.registerBean(bean, required);
        }
    }
    
}
