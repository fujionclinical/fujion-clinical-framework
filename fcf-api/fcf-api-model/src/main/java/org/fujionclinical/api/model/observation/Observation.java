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
package org.fujionclinical.api.model.observation;

import edu.utah.kmm.model.cool.core.datatype.Identifier;
import edu.utah.kmm.model.cool.core.datatype.Metadata;
import edu.utah.kmm.model.cool.core.datatype.MetadataImpl;
import edu.utah.kmm.model.cool.terminology.ConceptReferenceSet;
import org.fujionclinical.api.model.encounter.IEncounter;
import org.fujionclinical.api.model.patient.IPatient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Observation extends ObservationComponent implements IObservation {

    private final List<IObservationComponent> components = new ArrayList<>();

    private final List<ConceptReferenceSet> categories = new ArrayList<>();

    private final List<Identifier> identifiers = new ArrayList<>();

    private Metadata metadata;

    private LocalDateTime effectiveDate;

    private ObservationStatus status;

    private IPatient patient;

    private IEncounter encounter;

    private String id;

    @Override
    public LocalDateTime getEffectiveDate() {
        return effectiveDate;
    }

    @Override
    public void setEffectiveDate(LocalDateTime effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    @Override
    public ObservationStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(ObservationStatus status) {
        this.status = status;
    }

    @Override
    public IPatient getPatient() {
        return patient;
    }

    @Override
    public void setPatient(IPatient patient) {
        this.patient = patient;
    }

    @Override
    public IEncounter getEncounter() {
        return encounter;
    }

    @Override
    public void setEncounter(IEncounter encounter) {
        this.encounter = encounter;
    }

    @Override
    public List<IObservationComponent> getComponents() {
        return components;
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
    public List<Identifier> getIdentifiers() {
        return identifiers;
    }

    @Override
    public List<ConceptReferenceSet> getCategories() {
        return categories;
    }

    @Override
    public Metadata getMetadata() {
        return metadata == null ? metadata = new MetadataImpl() : metadata;
    }

    @Override
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public boolean hasMetadata() {
        return metadata != null;
    }

}
