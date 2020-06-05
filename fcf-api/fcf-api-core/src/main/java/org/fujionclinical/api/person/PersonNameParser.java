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
package org.fujionclinical.api.person;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Default person name parser. Assumes family name(s) are first and separated from given name(s) by a
 * comma. For example, <em>Smith Jones, Jonathan Xavier</em>. Parser currently doesn't handle name
 * prefixes and suffixes.
 */
public class PersonNameParser implements IPersonNameParser {

    public static final IPersonNameParser instance = new PersonNameParser();

    @Override
    public String toString(IPersonName name) {
        StringBuilder sb = new StringBuilder();
        append(sb, name.getPrefixes());
        append(sb, name.getFamilyName());
        sb.append(",");
        append(sb, name.getGivenNames());
        append(sb, name.getSuffixes());
        return sb.toString();
    }

    @Override
    public IPersonName fromString(
            String value,
            IPersonName name) {
        String[] pcs = value.split(",", 2);
        String familyName = pcs[0].trim();
        String[] givenNames = pcs.length == 1 ? null : pcs[1].split(" ");

        if (name == null) {
            name = new PersonName();
        }

        name.setFamilyName(familyName);

        if (givenNames != null) {
            name.setGivenNames(Arrays.stream(givenNames).map(nm -> nm.trim()).filter(nm -> !nm.isEmpty()).collect(Collectors.toList()));
        }

        return name;
    }

    private void append(
            StringBuilder sb,
            List<String> components) {
        for (String component : components) {
            append(sb, component);
        }
    }

    private void append(
            StringBuilder sb,
            String component) {
        if (component != null && !component.isEmpty()) {
            if (sb.length() > 0) {
                sb.append(" ");
            }

            sb.append(component);
        }
    }

}
