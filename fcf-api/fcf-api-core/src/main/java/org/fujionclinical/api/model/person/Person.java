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

import edu.utah.kmm.model.cool.core.datatype.IdentifierExImpl;
import edu.utah.kmm.model.cool.terminology.ConceptReference;
import edu.utah.kmm.model.cool.terminology.ConceptReferenceSet;
import org.fujionclinical.api.model.core.Address;
import org.fujionclinical.api.model.core.ContactPoint;
import org.fujionclinical.api.model.core.IAttachment;
import org.fujionclinical.api.model.impl.BaseDomainType;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Person extends BaseDomainType implements IPerson {

    private final List<IPersonName> names = new ArrayList<>();

    private final List<IAttachment> photos = new ArrayList<>();

    private final List<ConceptReferenceSet> languages = new ArrayList<>();

    private final List<ContactPoint> contactPoints = new ArrayList<>();

    private final List<Address> addresses = new ArrayList<>();

    private Gender gender;

    private ConceptReference birthSex;

    private ConceptReference ethnicity;

    private MaritalStatus maritalStatus;

    private ConceptReference race;

    private LocalDateTime birthDate;

    private LocalDateTime deceasedDate;

    @Override
    public List<IPersonName> getNames() {
        return names;
    }

    @Override
    public Gender getGender() {
        return gender;
    }

    @Override
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public ConceptReference getBirthSex() {
        return birthSex;
    }

    @Override
    public void setBirthSex(ConceptReference birthSex) {
        this.birthSex = birthSex;
    }

    @Override
    public ConceptReference getEthnicity() {
        return ethnicity;
    }

    @Override
    public void setEthnicity(ConceptReference ethnicity) {
        this.ethnicity = ethnicity;
    }

    @Override
    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    @Override
    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    @Override
    public ConceptReference getRace() {
        return race;
    }

    @Override
    public void setRace(ConceptReference race) {
        this.race = race;
    }

    @Override
    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    @Override
    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public LocalDateTime getDeceasedDate() {
        return deceasedDate;
    }

    @Override
    public void setDeceasedDate(LocalDateTime deceasedDate) {
        this.deceasedDate = deceasedDate;
    }

    @Override
    public List<Address> getAddresses() {
        return addresses;
    }

    @Override
    public List<ConceptReferenceSet> getLanguages() {
        return languages;
    }

    @Override
    public List<IAttachment> getPhotos() {
        return photos;
    }

    @Override
    public List<ContactPoint> getContactPoints() {
        return contactPoints;
    }

    @Override
    public edu.utah.kmm.model.cool.core.datatype.Identifier createIdentifier(
            URI system,
            String id) {
        return new IdentifierExImpl(system, id);
    }

}
