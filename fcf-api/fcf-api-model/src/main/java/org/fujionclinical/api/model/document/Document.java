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
package org.fujionclinical.api.model.document;

import edu.utah.kmm.model.cool.terminology.ConceptReferenceSet;
import org.fujionclinical.api.model.core.IAttachment;
import org.fujionclinical.api.model.core.IReference;
import org.fujionclinical.api.model.encounter.IEncounter;
import org.fujionclinical.api.model.impl.BaseDomainType;
import org.fujionclinical.api.model.person.IPerson;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Document extends BaseDomainType implements IDocument {

    private final List<IReference<IPerson>> authors = new ArrayList<>();

    private final List<IRelatedDocument> relatedDocuments = new ArrayList<>();

    private final List<IAttachment> attachments = new ArrayList<>();

    private final List<ConceptReferenceSet> categories = new ArrayList<>();

    private LocalDateTime creationDate;

    private DocumentStatus documentStatus;

    private CompositionStatus compositionStatus;

    private ConceptReferenceSet type;

    private String description;

    private IReference<IEncounter> encounter;

    @Override
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    @Override
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public DocumentStatus getDocumentStatus() {
        return documentStatus;
    }

    @Override
    public void setDocumentStatus(DocumentStatus documentStatus) {
        this.documentStatus = documentStatus;
    }

    @Override
    public CompositionStatus getCompositionStatus() {
        return compositionStatus;
    }

    @Override
    public void setCompositionStatus(CompositionStatus compositionStatus) {
        this.compositionStatus = compositionStatus;
    }

    @Override
    public ConceptReferenceSet getType() {
        return type;
    }

    @Override
    public void setType(ConceptReferenceSet type) {
        this.type = type;
    }

    @Override
    public List<IReference<IPerson>> getAuthors() {
        return authors;
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
    public IReference<IEncounter> getEncounter() {
        return encounter;
    }

    @Override
    public void setEncounter(IReference<IEncounter> encounter) {
        this.encounter = encounter;
    }

    @Override
    public List<IRelatedDocument> getRelatedDocuments() {
        return relatedDocuments;
    }

    @Override
    public List<IAttachment> getAttachments() {
        return attachments;
    }

    @Override
    public List<ConceptReferenceSet> getCategories() {
        return categories;
    }

}
