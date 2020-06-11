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
import org.fujionclinical.api.model.core.IAttachmentType;
import org.fujionclinical.api.model.core.IConcept;
import org.fujionclinical.api.model.core.IConceptCode;
import org.fujionclinical.api.model.core.IDomainObject;
import org.fujionclinical.api.model.encounter.IEncounter;
import org.fujionclinical.api.model.person.IPerson;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public interface IDocument extends IDomainObject, IAttachmentType {

    enum DocumentStatus {
        CURRENT, SUPERSEDED, ENTERED_IN_ERROR
    }

    enum CompositionStatus {
        PRELIMINARY, FINAL, AMENDED, ENTERED_IN_ERROR
    }

    enum DocumentRelationship {
        REPLACES, TRANSFORMS, SIGNS, APPENDS
    }

    interface IRelatedDocument {

        DocumentRelationship getRelationship();

        default void setRelationship(DocumentRelationship relationship) {
            throw new UnsupportedOperationException();
        }

        default boolean hasRelationship() {
            return getRelationship() != null;
        }

    }

    Date getCreationDate();

    default void setCreationDate(Date creationDate) {
        throw new UnsupportedOperationException();
    }

    default boolean hasCreationDate() {
        return getCreationDate() != null;
    }

    DocumentStatus getDocumentStatus();

    default void setDocumentStatus(DocumentStatus status) {
        throw new UnsupportedOperationException();
    }

    default boolean hasDocumentStatus() {
        return getDocumentStatus() != null;
    }

    CompositionStatus getCompositionStatus();

    default void setCompositionStatus(CompositionStatus status) {
        throw new UnsupportedOperationException();
    }

    default boolean hasCompositionStatus() {
        return getCompositionStatus() != null;
    }

    IConcept getType();

    default void setType(IConcept type) {
        throw new UnsupportedOperationException();
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

    default List<IConcept> getCategories() {
        return Collections.emptyList();
    }

    default void setCategories(List<IConcept> categories) {
        CollectionUtil.replaceList(getCategories(), categories);
    }

    default void addCategories(IConcept... categories) {
        Collections.addAll(getCategories(), categories);
    }

    default boolean hasCategory() {
        return CollectionUtil.notEmpty(getCategories());
    }

    default List<IPerson> getAuthors() {
        return Collections.emptyList();
    }

    default void setAuthors(List<IPerson> authors) {
        CollectionUtil.replaceList(getAuthors(), authors);
    }

    default void addAuthors(IPerson... authors) {
        Collections.addAll(getAuthors(), authors);
    }

    default boolean hasAuthor() {
        return CollectionUtil.notEmpty(getAuthors());
    }

    String getDescription();

    default void setDescription(String description) {
        throw new UnsupportedOperationException();
    }

    default boolean hasDescription() {
        return getDescription() != null;
    }

    IEncounter getEncounter();

    default void setEncounter(IEncounter encounter) {
        throw new UnsupportedOperationException();
    }

    default boolean hasEncounter() {
        return getEncounter() != null;
    }

    default List<IRelatedDocument> getRelatedDocuments() {
        return Collections.emptyList();
    }

    default void setRelatedDocuments(List<IRelatedDocument> relatedDocuments) {
        CollectionUtil.replaceList(getRelatedDocuments(), relatedDocuments);
    }

    default void addRelatedDocuments(IRelatedDocument... relatedDocuments) {
        Collections.addAll(getRelatedDocuments(), relatedDocuments);
    }

    default boolean hasRelatedDocument() {
        return CollectionUtil.notEmpty(getRelatedDocuments());
    }

}
