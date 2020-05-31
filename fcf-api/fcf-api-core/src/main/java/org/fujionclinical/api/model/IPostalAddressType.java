package org.fujionclinical.api.model;

import org.fujion.common.CollectionUtil;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public interface IPostalAddressType {

    /**
     * Returns all addresses..
     *
     * @return A list of all addresses (never null)
     */
    default List<IPostalAddress> getAddresses() {
        return Collections.emptyList();
    }

    default void setAddresses(List<IPostalAddress> addresses) {
        CollectionUtil.replaceList(getAddresses(), addresses);
    }

    /**
     * Returns the home address.
     *
     * @return The home address, or null if not found.
     */
    default IPostalAddress getAddress() {
        return getAddress(IPostalAddress.PostalAddressUse.HOME);
    }

    /**
     * Returns the address from one of the specified uses.  Uses are
     * searched in order until a match is found.
     *
     * @param uses Only addresses belonging to one of these uses will be returned.
     * @return The matching address, or null if not found.
     */
    default IPostalAddress getAddress(IPostalAddress.PostalAddressUse... uses) {
        return CollectionUtil.findMatch(getAddresses(), (address, use) ->
                address.getUse() == use, uses);
    }

    default void addAddresses(IPostalAddress... addresses) {
        Collections.addAll(getAddresses(), addresses);
    }

    default boolean hasAddress() {
        return CollectionUtil.notEmpty(getAddresses());
    }

}
