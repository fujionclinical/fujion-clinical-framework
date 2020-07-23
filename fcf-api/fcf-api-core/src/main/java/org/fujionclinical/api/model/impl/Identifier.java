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
package org.fujionclinical.api.model.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.fujionclinical.api.model.core.IConcept;
import org.fujionclinical.api.model.core.IConceptCode;
import org.fujionclinical.api.model.core.IIdentifier;

import java.util.Collections;

public class Identifier implements IIdentifier {

    private String system;

    private String value;

    private IConcept type;

    private IdentifierUse use;

    public Identifier(
            String system,
            String value) {
        this(system, value, null);
    }

    public Identifier(
            String system,
            String value,
            IdentifierUse use,
            IConceptCode... types) {
        this.system = system;
        this.value = value;
        this.use = use;

        if (ArrayUtils.isNotEmpty(types)) {
            type = new Concept(types);
            Collections.addAll(type.getCodes(), types);
        }
    }

    @Override
    public String getSystem() {
        return system;
    }

    @Override
    public void setSystem(String system) {
        this.system = system;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    public IConcept getType() {
        return type;
    }

    @Override
    public void setType(IConcept type) {
        this.type = type;
    }

    public IdentifierUse getUse() {
        return use;
    }

    @Override
    public void setUse(IdentifierUse use) {
        this.use = use;
    }

}