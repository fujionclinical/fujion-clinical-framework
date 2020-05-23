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
package org.fujionclinical.api.model.person;

import org.fujionclinical.api.context.ISerializer;
import org.fujionclinical.api.model.person.IPersonName;

import java.util.Arrays;
import java.util.List;

/**
 * Serializer / deserializer for IPersonName class.
 */
public class PersonNameSerializer implements ISerializer<IPersonName> {

    private static final String COMPONENT_DELIM = "^";

    private static final String REPEAT_DELIM = "|";

    @Override
    public String serialize(IPersonName value) {
        return (value.hasFamilyName() ? value.getFamilyName() : "") + COMPONENT_DELIM
                + getComponent(value.getGivenNames()) + COMPONENT_DELIM
                + getComponent(value.getPrefixes()) + COMPONENT_DELIM
                + getComponent(value.getSuffixes()) + COMPONENT_DELIM
                + (value.hasUse() ? value.getUse() : "");
    }

    @Override
    public IPersonName deserialize(
            String value,
            IPersonName name) {
        String[] components = value.split("\\" + COMPONENT_DELIM);
        int i = 0;
        name.setFamilyName(getComponent(components, i++));
        name.setGivenNames(getRepeats(components, i++));
        name.setPrefixes(getRepeats(components, i++));
        name.setSuffixes(getRepeats(components, i++));
        name.setUse(getUse(getComponent(components, i++)));
        return name;
    }

    @Override
    public Class<IPersonName> getType() {
        return IPersonName.class;
    }

    private IPersonName.PersonNameUse getUse(String use) {
        try {
            return IPersonName.PersonNameUse.valueOf(use);
        } catch (Exception e) {
            return null;
        }
    }

    private List<String> getRepeats(
            String[] components,
            int index) {
        String component = getComponent(components, index);
        List<String> result = Arrays.asList(component.split("\\" + REPEAT_DELIM));
        return result.isEmpty() ? null : result;
    }

    private String getComponent(
            String[] components,
            int index) {
        return index >= components.length ? "" : components[index];
    }

    private String getComponent(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (String component : list) {
            component = getComponent(component);

            if (!component.isEmpty()) {
                sb.append(sb.length() == 0 ? "" : REPEAT_DELIM).append(component);
            }
        }

        return sb.toString();
    }

    private String getComponent(String value) {
        return value == null ? "" : value;
    }

}
