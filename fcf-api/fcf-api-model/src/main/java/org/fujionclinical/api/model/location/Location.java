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
package org.fujionclinical.api.model.location;

import edu.utah.kmm.model.cool.terminology.ConceptReferenceSet;
import org.fujionclinical.api.model.core.IContactPoint;
import org.fujionclinical.api.model.core.IPostalAddress;
import org.fujionclinical.api.model.impl.BaseDomainType;

import java.util.ArrayList;
import java.util.List;

public class Location extends BaseDomainType implements ILocation {

    private final List<String> aliases = new ArrayList<>();

    private final List<ConceptReferenceSet> types = new ArrayList<>();

    private final List<IContactPoint> contactPoints = new ArrayList<>();

    private final List<IPostalAddress> addresses = new ArrayList<>();

    private LocationStatus status;

    private String name;

    private String description;

    @Override
    public LocationStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(LocationStatus status) {
        this.status = status;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public List<ConceptReferenceSet> getTypes() {
        return types;
    }

    @Override
    public List<IContactPoint> getContactPoints() {
        return contactPoints;
    }

    @Override
    public List<IPostalAddress> getAddresses() {
        return addresses;
    }

}
