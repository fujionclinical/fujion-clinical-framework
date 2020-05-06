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
package org.fujionclinical.api.spring;

import org.apache.commons.lang.StringUtils;

/**
 * This is a simple implementation of a property-aware configurator that includes a property
 * prefix and an optional qualifier.  Each declared property will be prefixed with the
 * property prefix and, if a qualifier is specified, with the qualifier as well:
 * <p><code>&lt;propertyPrefix&gt;,[&lt;qualifier&gt;.]&lt;internal property name&gt;</code>.<p>
 * For example, if the property prefix is "fhir.client" and the qualifier is "terminology_service",
 * the property declared as "server.base" internally, externally would be specified as:
 * <p><code>fhir.client.terminology_service.server.base</code>.<p>
 * If no qualifier was specified, that same property becomes:
 * <p><code>fhir.client.server.base</code>.
 */
public class SimplePropertyAwareConfigurator extends PropertyAwareConfigurator {

    private final String qualifier;

    private final String propertyPrefix;

    private final String fullPrefix;

    public SimplePropertyAwareConfigurator(String propertyPrefix, String qualifier) {
        this.qualifier = qualifier;
        this.propertyPrefix = propertyPrefix;
        fullPrefix = prepString(propertyPrefix) + prepString(qualifier);
    }

    public String getPropertyPrefix() {
        return propertyPrefix;
    }

    public String getQualifier() {
        return qualifier;
    }

    @Override
    public String expandPropertyName(String name) {
        return fullPrefix + name;
    }

    private String prepString(String value) {
        value = StringUtils.removeEnd(StringUtils.trimToEmpty(value), ".");
        return value.isEmpty() ? value : (value + ".");
    }
}
