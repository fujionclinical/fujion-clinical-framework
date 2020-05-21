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
package org.fujionclinical.api.model;

import java.io.Serializable;
import java.util.List;

/**
 * Interface for a domain object.
 */
public interface IDomainObject extends Serializable {
    
    /**
     * Returns the logical identifier for the domain object.
     * 
     * @return The logical identifier.
     */
    String getId();

    /**
     * Returns a list of all identifiers associated with the domain object.
     *
     * @return Identifiers associated with the domain object.
     */
    default List<Identifier> getIdentifiers() {
        return null;
    }

    /**
     * Returns an identifier belonging to the specified system, or null if one is not found.
     *
     * @param system The identifier system.
     * @return A matching identifier (possibly null)
     */
    default Identifier getIdentifier(String system) {
        if (getIdentifiers() != null) {
            for (Identifier identifier: getIdentifiers()) {
                if (system.equals(identifier.getSystem())) {
                    return identifier;
                }
            }
        }

        return null;
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
