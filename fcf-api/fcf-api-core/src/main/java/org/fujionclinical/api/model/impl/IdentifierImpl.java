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

import edu.utah.kmm.model.cool.core.datatype.IdentifierUse;
import edu.utah.kmm.model.cool.terminology.ConceptReferenceSet;

import java.net.URI;

public class IdentifierImpl implements edu.utah.kmm.model.cool.core.datatype.Identifier {

    private URI system;

    private String id;

    private ConceptReferenceSet type;

    private IdentifierUse use;

    public IdentifierImpl(
            String system,
            String id) {
        this(system, id, null, null);
    }

    public IdentifierImpl(
            URI system,
            String id) {
        this(system, id, null, null);
    }

    public IdentifierImpl(
            String system,
            String id,
            IdentifierUse use,
            ConceptReferenceSet type) {
        this(system == null ? null : URI.create(system), id, use, type);
    }

    public IdentifierImpl(
            URI system,
            String id,
            IdentifierUse use,
            ConceptReferenceSet type) {
        this.system = system;
        this.id = id;
        this.use = use;
        this.type = type;
    }

    @Override
    public URI getSystem() {
        return system;
    }

    @Override
    public void setSystem(URI system) {
        this.system = system;
    }

    public void setSystem(String system) {
        setSystem(URI.create(system));
    }

    @Override
    public boolean hasSystem() {
        return getSystem() != null;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean hasId() {
        return getId() != null;
    }

    public ConceptReferenceSet getType() {
        return type;
    }

    @Override
    public void setType(ConceptReferenceSet type) {
        this.type = type;
    }

    @Override
    public boolean hasType() {
        return getType() != null;
    }

    public IdentifierUse getUse() {
        return use;
    }

    @Override
    public void setUse(IdentifierUse use) {
        this.use = use;
    }

    @Override
    public boolean hasUse() {
        return getUse() != null;
    }

}
