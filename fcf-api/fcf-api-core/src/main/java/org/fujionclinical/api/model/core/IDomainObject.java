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

import org.fujion.common.CollectionUtil;
import org.fujionclinical.api.query.QueryParameter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * Interface for a domain object.
 */
public interface IDomainObject extends Serializable {

    /**
     * Returns the logical identifier for the domain object.
     *
     * @return The logical identifier.
     */
    @QueryParameter
    String getId();

    /**
     * Sets the logical identifier for the domain object.  The default
     * implementation throws an unsupported operation exception.
     *
     * @param id The new logical identifier.
     */
    default void setId(String id) {
        throw new UnsupportedOperationException();
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
    default List<IIdentifier> getIdentifiers() {
        return Collections.emptyList();
    }

    /**
     * Returns an identifier belonging to the specified system, or null if one is not found.
     *
     * @param system The identifier system.
     * @return A matching identifier (possibly null)
     */
    default IIdentifier getIdentifier(String system) {
        return getIdentifier(identifier -> system.equals(identifier.getSystem()));
    }

    /**
     * Returns the first identifier that matches the specified criteria.
     *
     * @param criteria A function that returns true when a match is found.
     * @return A matching identifier (possibly null).
     */
    default IIdentifier getIdentifier(Predicate<IIdentifier> criteria) {
        return CollectionUtil.findMatch(getIdentifiers(), criteria);
    }

    /**
     * Returns true if at least one identifier is present.
     *
     * @return True if at least one identifier is present.
     */
    default boolean hasIdentifier() {
        return CollectionUtil.notEmpty(getIdentifiers());
    }

    /**
     * Returns true if two domain objects represent the same entity (i.e.,
     * have the same logical identifier and type).
     *
     * @param object The domain object to compare to this one.
     * @return True if both objects represent the same entity.
     */
    default boolean isSame(IDomainObject object) {
        return object != null && object.getClass() == getClass() && object.getId().equals(getId());
    }

    /**
     * Returns the native object if this is a proxy.  The default implementation
     * returns the object itself.
     *
     * @return The native object.
     */
    default Object getNative() {
        return this;
    }

}
