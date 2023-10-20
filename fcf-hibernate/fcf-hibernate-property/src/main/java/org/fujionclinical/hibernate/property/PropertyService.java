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
package org.fujionclinical.hibernate.property;

import org.apache.commons.lang3.StringUtils;
import org.fujion.common.StrUtil;
import org.fujionclinical.api.property.IPropertyService;
import org.fujionclinical.api.security.ISecurityService;
import org.fujionclinical.api.security.SecurityUtil;
import org.fujionclinical.api.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Hibernate implementation of a property service.
 */
@Component
@Profile("root")
public class PropertyService implements IPropertyService {

    @Autowired
    private PropertyDAO propertyDAO;

    public PropertyService() {
    }

    private User getUser(boolean asGlobal) {
        ISecurityService securityService = SecurityUtil.getSecurityService();
        return asGlobal || securityService == null ? null : securityService.getAuthenticatedUser();
    }

    private String getUserId(boolean asGlobal) {
        User user = getUser(asGlobal);
        return user == null ? null : user.getId();
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public String getValue(String propertyName, String instanceName) {
        Property property = propertyDAO.get(propertyName, instanceName, getUserId(false));
        property = property != null ? property : propertyDAO.get(propertyName, instanceName, null);
        return property == null ? null : property.getValue();
    }

    @Override
    public List<String> getValues(String propertyName, String instanceName) {
        List<String> results = null;
        String result = getValue(propertyName, instanceName);

        if (StringUtils.isNotEmpty(result)) {
            results = StrUtil.toList(result);
        }

        return results;
    }

    @Override
    public void saveValue(String propertyName, String instanceName, boolean asGlobal, String value) {
        String userId = getUserId(asGlobal);
        Property property = propertyDAO.get(propertyName, instanceName, userId);

        if (property == null) {
            if (value != null) {
                property = new Property(propertyName, value, instanceName, userId);
                propertyDAO.persist(property);
            }
        } else if (value == null) {
            int rowcount = propertyDAO.remove(property.getId());
            System.out.println(rowcount);
        } else {
            property.setValue(value);
            propertyDAO.merge(property);
        }
    }

    @Override
    public void saveValues(String propertyName, String instanceName, boolean asGlobal, List<String> values) {
        saveValue(propertyName, instanceName, asGlobal, values == null ? null : StrUtil.fromList(values));
    }

    @Override
    public List<String> getInstances(String propertyName, boolean asGlobal) {
        return propertyDAO.getInstances(propertyName, getUserId(asGlobal));
    }

    public List<Property> getAllProperties() {
        return propertyDAO.getAll();
    }
}
