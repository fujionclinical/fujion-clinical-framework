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
package org.fujionclinical.patientlist;

public interface IPatientListFilter extends Comparable<IPatientListFilter> {

    String PIPE = "|";

    String REGEX_DELIM = "\\" + PIPE;

    /**
     * Returns the entity object associated with the filter.
     *
     * @return The associated entity object.
     */
    Object getEntity();

    /**
     * Sets the entity object associated with the filter.
     *
     * @param entity Entity object.
     */
    void setEntity(Object entity);

    /**
     * Help method to parse a serialized value. Guarantees the length of the return array.
     *
     * @param value  String value to parse.
     * @param pieces Number of delimited pieces.
     * @return An array of parsed elements.
     */
    String[] parse(
            String value,
            int pieces);

    /**
     * Returns an entity instance from its serialized form.
     *
     * @param value Serialized form of entity.
     * @return Deserialized entity instance.
     */
    Object deserialize(String value);

    /**
     * Returns the serialized form of the associated entity.
     *
     * @return Serialized form of the entity.
     */
    String serialize();

    /**
     * Returns the initial display name for this filter.
     *
     * @return Initial display name.
     */
    String initName();

    /**
     * Sets the display name of this filter.
     *
     * @param name The display name.
     */
    void setName(String name);

    /**
     * Returns the display name of this filter.
     *
     * @return The display name.
     */
    String getName();

}
