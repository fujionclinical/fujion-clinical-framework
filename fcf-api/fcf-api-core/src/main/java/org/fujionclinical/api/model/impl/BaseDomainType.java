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

import edu.utah.kmm.model.cool.core.datatype.Identifier;
import edu.utah.kmm.model.cool.core.datatype.Metadata;
import edu.utah.kmm.model.cool.terminology.ConceptReferenceSet;
import org.fujionclinical.api.model.core.IDomainType;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseDomainType implements IDomainType {

    private final List<Identifier> identifiers = new ArrayList<>();

    private final Metadata metadata = new MetadataImpl();

    private String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public List<Identifier> getIdentifiers() {
        return identifiers;
    }

    @Override
    public List<ConceptReferenceSet> getTags() {
        return metadata.getGroup();
    }

    @Override
    public Metadata getMetadata() {
        return null;
    }

    @Override
    public void setMetadata(Metadata metadata) {

    }

    @Override
    public boolean hasMetadata() {
        return false;
    }

}
