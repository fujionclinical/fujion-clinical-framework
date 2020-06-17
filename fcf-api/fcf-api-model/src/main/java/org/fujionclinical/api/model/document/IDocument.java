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

import org.fujion.common.CollectionUtil;
import org.fujion.common.DateTimeWrapper;
import org.fujionclinical.api.core.CoreUtil;
import org.fujionclinical.api.model.core.*;
import org.fujionclinical.api.model.encounter.IEncounter;
import org.fujionclinical.api.model.person.IPerson;

import java.util.Collections;
import java.util.List;

public interface IDocument extends IDomainType, IAttachmentType, ICategoryType {

    enum DocumentStatus {
        CURRENT, SUPERSEDED, ENTERED_IN_ERROR;

        @Override
        public String toString() {
            return CoreUtil.enumToString(this);
        }

    }

    enum CompositionStatus {
        PRELIMINARY, FINAL, AMENDED, ENTERED_IN_ERROR;

        @Override
        public String toString() {
            return CoreUtil.enumToString(this);
        }

    }

    enum DocumentRelationship {
        REPLACES, TRANSFORMS, SIGNS, APPENDS;

        @Override
        public String toString() {
            return CoreUtil.enumToString(this);
        }

    }

    interface IRelatedDocument extends IBaseType {

        DocumentRelationship getRelationship();

        default void setRelationship(DocumentRelationship relationship) {
            notSupported();
        }

        default boolean hasRelationship() {
            return getRelationship() != null;
        }

        IReference<IDocument> getDocument();

        default void setDocument(IReference<IDocument> value) {
            notSupported();
        }

        default boolean hasDocument() {
            return getDocument() != null;
        }

    }

    DateTimeWrapper getCreationDate();

    default void setCreationDate(DateTimeWrapper creationDate) {
        notSupported();
    }

    default boolean hasCreationDate() {
        return getCreationDate() != null;
    }

    DocumentStatus getDocumentStatus();

    default void setDocumentStatus(DocumentStatus status) {
        notSupported();
    }

    default boolean hasDocumentStatus() {
        return getDocumentStatus() != null;
    }

    CompositionStatus getCompositionStatus();

    default void setCompositionStatus(CompositionStatus status) {
        notSupported();
    }

    default boolean hasCompositionStatus() {
        return getCompositionStatus() != null;
    }

    IConcept getType();

    default void setType(IConcept type) {
        notSupported();
    }

    default boolean hasType() {
        return getType() != null;
    }

    default boolean hasType(IConceptCode code) {
        return hasType() && getType().getCodes().stream().filter(cde -> code.isSame(cde)).findFirst().isPresent();
    }

    default boolean hasType(String type) {
        return hasType() && getType().getCodes().stream().filter(code -> type.equals(code.getCode())).findFirst().isPresent();
    }

    default List<IReference<IPerson>> getAuthors() {
        return Collections.emptyList();
    }

    default void setAuthors(List<IReference<IPerson>> authors) {
        CollectionUtil.replaceElements(getAuthors(), authors);
    }

    default void addAuthors(IReference<IPerson>... authors) {
        Collections.addAll(getAuthors(), authors);
    }

    default boolean hasAuthor() {
        return CollectionUtil.notEmpty(getAuthors());
    }

    String getDescription();

    default void setDescription(String description) {
        notSupported();
    }

    default boolean hasDescription() {
        return getDescription() != null;
    }

    IReference<IEncounter> getEncounter();

    default void setEncounter(IReference<IEncounter> encounter) {
        notSupported();
    }

    default boolean hasEncounter() {
        return getEncounter() != null;
    }

    default List<IRelatedDocument> getRelatedDocuments() {
        return Collections.emptyList();
    }

    default void setRelatedDocuments(List<IRelatedDocument> relatedDocuments) {
        CollectionUtil.replaceElements(getRelatedDocuments(), relatedDocuments);
    }

    default void addRelatedDocuments(IRelatedDocument... relatedDocuments) {
        Collections.addAll(getRelatedDocuments(), relatedDocuments);
    }

    default boolean hasRelatedDocument() {
        return CollectionUtil.notEmpty(getRelatedDocuments());
    }

}
