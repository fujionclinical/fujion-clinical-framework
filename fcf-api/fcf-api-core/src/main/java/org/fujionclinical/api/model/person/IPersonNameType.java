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

import org.fujion.common.CollectionUtil;
import org.fujionclinical.api.query.QueryParameter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public interface IPersonNameType {

    /**
     * Returns all known names for this entity.
     *
     * @return List of all known names (never null).
     */
    List<IPersonName> getNames();

    default void setNames(List<IPersonName> names) {
        CollectionUtil.replaceList(getNames(), names);
    }

    /**
     * Returns the entity's usual name in parsed form.
     *
     * @return The parsed name, or null if not found.
     */
    @QueryParameter
    default IPersonName getName() {
        return getName(IPersonName.PersonNameUse.USUAL, IPersonName.PersonNameUse.ANY);
    }

    /**
     * Returns the entity's parsed name from one of the specified categories.  Categories are
     * searched in order until a match is found.
     *
     * @param categories Only names belonging to one of these categories will be returned.
     * @return The parsed name, or null if not found.
     */
    default IPersonName getName(IPersonName.PersonNameUse... categories) {
        return CollectionUtil.findMatch(getNames(), (name, category) ->
                category == IPersonName.PersonNameUse.ANY || name.getUse() == category, categories);
    }

    default void addNames(IPersonName... names) {
        Collections.addAll(getNames(), names);
    }

    default boolean hasName() {
        return CollectionUtil.notEmpty(getNames());
    }

    /**
     * Returns the usual name formatted as a string.
     *
     * @return Person's full name, or null if not found.
     */
    default String getFullName() {
        return getFullName(IPersonName.PersonNameUse.USUAL, IPersonName.PersonNameUse.ANY);
    }

    /**
     * Returns the entity's name formatted as a string from one of the specified categories.  Categories are
     * searched in order until a match is found.
     *
     * @param categories Only names belonging to one of these categories will be returned.
     * @return Person's full name or null if none found.
     */
    default String getFullName(IPersonName.PersonNameUse... categories) {
        return Objects.toString(getName(categories));
    }


}
