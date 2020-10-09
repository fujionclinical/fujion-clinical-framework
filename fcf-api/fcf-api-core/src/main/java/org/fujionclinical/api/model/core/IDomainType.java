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
package org.fujionclinical.api.model.core;

import edu.utah.kmm.model.cool.core.BaseType;
import edu.utah.kmm.model.cool.core.datatype.Identifier;
import edu.utah.kmm.model.cool.core.datatype.IdentifierExImpl;
import edu.utah.kmm.model.cool.core.datatype.Metadata;
import edu.utah.kmm.model.cool.foundation.entity.Entity;
import edu.utah.kmm.model.cool.terminology.ConceptReferenceSet;
import org.fujion.common.CollectionUtil;
import org.fujionclinical.api.query.expression.QueryParameter;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * Interface for a domain object.
 */
public interface IDomainType extends Entity, BaseType {

    /**
     * Returns the logical identifier for the domain object.
     *
     * @return The logical identifier.
     */
    @QueryParameter
    default String getId() {
        return getDefaultId().getId();
    }

    /**
     * Sets the logical identifier for the domain object.  The default
     * implementation throws an unsupported operation exception.
     *
     * @param id The new logical identifier.
     */
    default void setId(String id) {
        notSupported();
    }

    /**
     * Returns true if a logical identifier is present.
     *
     * @return True if a logical identifier is present.
     */
    default boolean hasId() {
        return getId() != null;
    }

    /**
     * Returns a list of all identifiers associated with the domain object.
     *
     * @return Identifiers associated with the domain object (never null)
     */
    @QueryParameter
    @Override
    default List<Identifier> getIdentifiers() {
        return Collections.emptyList();
    }

    /**
     * Sets identifiers to be associated with the domain object.
     *
     * @param identifiers The identifiers.
     */
    default void setIdentifiers(List<Identifier> identifiers) {
        CollectionUtil.replaceElements(getIdentifiers(), identifiers);
    }

    /**
     * Returns an identifier belonging to the specified system, or null if one is not found.
     *
     * @param system The identifier system.
     * @return A matching identifier (possibly null)
     */
    default Identifier getIdentifier(String system) {
        return findFirst(system);
    }

    /**
     * Returns the first identifier that matches the specified criteria.
     *
     * @param criteria A function that returns true when a match is found.
     * @return A matching identifier (possibly null).
     */
    default Identifier getIdentifier(Predicate<Identifier> criteria) {
        return CollectionUtil.findMatch(getIdentifiers(), criteria);
    }

    /**
     * Add identifiers to be associated with the domain object.
     *
     * @param identifiers Identifiers to add.
     */
    default void addIdentifiers(Identifier... identifiers) {
        Collections.addAll(getIdentifiers(), identifiers);
    }

    /**
     * Returns true if at least one identifier is present.
     *
     * @return True if at least one identifier is present.
     */
    default boolean hasIdentifiers() {
        return CollectionUtil.notEmpty(getIdentifiers());
    }

    default Identifier createIdentifier(
            URI system,
            String id) {
        return new IdentifierExImpl(system, id);
    }

    /**
     * Returns any tags associated with the domain object.
     *
     * @return Tags associated with the domain object.
     */
    default List<ConceptReferenceSet> getTags() {
        return getMetadata().getGroup();
    }

    /**
     * Sets tags to be associated with the domain object.
     *
     * @param tags The tags.
     */
    default void setTags(List<ConceptReferenceSet> tags) {
        getMetadata().setGroup(tags);
    }

    /**
     * Add tags to be associated with the domain object.
     *
     * @param tags Tags to add.
     */
    default void addTags(ConceptReferenceSet... tags) {
        getMetadata().addGroup(tags);
    }

    /**
     * Returns a tag belonging to the specified system, or null if one is not found.
     *
     * @param system The code system.
     * @return A matching tag (possibly null)
     */
    default ConceptReferenceSet getTag(String system) {
        return getTag(tag -> tag.getBySystem(system) != null);
    }

    /**
     * Returns the first tag that matches the specified criteria.
     *
     * @param criteria A function that returns true when a match is found.
     * @return A matching tag (possibly null).
     */
    default ConceptReferenceSet getTag(Predicate<ConceptReferenceSet> criteria) {
        return CollectionUtil.findMatch(getTags(), criteria);
    }

    /**
     * Returns true if any tags are present.
     *
     * @return True if any tags are present.
     */
    default boolean hasTag() {
        return CollectionUtil.notEmpty(getTags());
    }

    @Override
    default String getText() {
        return null;
    }

    @Override
    default void setText(String text) {
        notSupported();
    }

    @Override
    default boolean hasText() {
        return getText() != null;
    }

    default Metadata getMetadata() {
        return notSupported();
    }

    default void setMetadata(Metadata metadata) {
        notSupported();
    }

    default boolean hasMetadata() {
        return getMetadata() != null;
    }

    /**
     * Returns true if two domain objects represent the same entity (i.e.,
     * have the same logical identifier and type).
     *
     * @param object The domain object to compare to this one.
     * @return True if both objects represent the same entity.
     */
    default boolean isSame(IDomainType object) {
        return object != null && object.getClass() == getClass() && object.getId().equals(getId());
    }

}
