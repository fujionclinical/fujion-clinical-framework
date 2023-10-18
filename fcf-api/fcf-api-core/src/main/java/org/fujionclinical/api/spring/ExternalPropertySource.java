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
package org.fujionclinical.api.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fujionclinical.api.property.IPropertyService;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.PropertySource;

/**
 * Allows properties obtained from the IPropertyService API to be referenced in Spring configuration
 * files.
 */
public class ExternalPropertySource extends PropertySource<Object> {
    
    private static final Log log = LogFactory.getLog(ExternalPropertySource.class);
    
    private static final String PREFIX = "external.";
    
    private static final int PREFIX_LEN = PREFIX.length();
    
    private IPropertyService propertyService;
    
    private final ApplicationContext appContext;
    
    public ExternalPropertySource(ApplicationContext appContext) {
        super("External Properties");
        this.appContext = appContext;
    }
    
    /**
     * Returns a property value from the underlying data store.
     * 
     * @param name Property name prefixed with "external.".
     */
    @Override
    public String getProperty(String name) {
        try {
            if (name.startsWith(PREFIX) && initPropertyService()) {
                return propertyService.getValue(name.substring(PREFIX_LEN), null);
            }
        } catch (Exception e) {
            log.error("Exception getting property.", e);
        }
        
        return null;
    }
    
    /**
     * Initializes the property service.
     * 
     * @return True if initialized.
     */
    private boolean initPropertyService() {
        if (propertyService == null && appContext.containsBean("propertyService")) {
            propertyService = appContext.getBean("propertyService", IPropertyService.class);
        }
        
        return propertyService != null;
    }
}
