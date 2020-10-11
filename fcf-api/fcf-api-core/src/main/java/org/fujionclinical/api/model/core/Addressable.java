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

import java.util.Collections;
import java.util.List;

public interface Addressable {

    /**
     * Returns all addresses..
     *
     * @return A list of all addresses (never null)
     */
    default List<Address> getAddresses() {
        return Collections.emptyList();
    }

    default void setAddresses(List<Address> addresses) {
        CollectionUtil.replaceElements(getAddresses(), addresses);
    }

    /**
     * Returns the home address.
     *
     * @return The home address, or null if not found.
     */
    default Address getAddress() {
        return getAddress(Address.PostalAddressUse.HOME);
    }

    /**
     * Returns the address from one of the specified uses.  Uses are
     * searched in order until a match is found.
     *
     * @param uses Only addresses belonging to one of these uses will be returned.
     * @return The matching address, or null if not found.
     */
    default Address getAddress(Address.PostalAddressUse... uses) {
        return CollectionUtil.findMatch(getAddresses(), (address, use) ->
                address.getUse() == use, uses);
    }

    default void addAddresses(Address... addresses) {
        Collections.addAll(getAddresses(), addresses);
    }

    default boolean hasAddress() {
        return CollectionUtil.notEmpty(getAddresses());
    }

}
