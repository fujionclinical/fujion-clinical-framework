package org.fujionclinical.api.model;

import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface IPostalAddress {

    enum PostalAddressUse {
        HOME, WORK, TEMP, OLD, BILLING
    }

    static IPostalAddress getPostalAddress(
            Collection<? extends IPostalAddress> addresses,
            PostalAddressUse... uses) {
        if (addresses != null && !addresses.isEmpty()) {
            for (PostalAddressUse use : uses) {
                for (IPostalAddress address : addresses) {
                    if (address.getUse() == use) {
                        return address;
                    }
                }
            }
        }

        return null;
    }

    PostalAddressUse getUse();

    default IPostalAddress setUse(PostalAddressUse use) {
        throw new UnsupportedOperationException();
    }

    default boolean hasUse() {
        return getUse() != null;
    }

    default List<String> getLines() {
        return Collections.emptyList();
    }

    default IPostalAddress setLines(List<String> lines) {
        throw new UnsupportedOperationException();
    }

    default IPostalAddress addLines(String... lines) {
        CollectionUtils.addAll(getLines(), lines);
        return this;
    }

    default boolean hasLines() {
        return !CollectionUtils.isEmpty(getLines());
    }

    String getCity();

    default IPostalAddress setCity(String city) {
        throw new UnsupportedOperationException();
    }

    default boolean hasCity() {
        return getCity() != null;
    }

    String getDistrict();

    default IPostalAddress setDistrict(String district) {
        throw new UnsupportedOperationException();
    }

    default boolean hasDistrict() {
        return getDistrict() != null;
    }

    String getState();

    default IPostalAddress setState(String state) {
        throw new UnsupportedOperationException();
    }

    default boolean hasState() {
        return getState() != null;
    }

    String getPostalCode();

    default IPostalAddress setPostalCode(String postalCode) {
        throw new UnsupportedOperationException();
    }

    default boolean hasPostalCode() {
        return getPostalCode() != null;
    }

    String getCountry();

    default IPostalAddress setCountry(String country) {
        throw new UnsupportedOperationException();
    }

    default boolean hasCountry() {
        return getCountry() != null;
    }

    IPeriod getPeriod();

    default IPostalAddress setPeriod(IPeriod period) {
        throw new UnsupportedOperationException();
    }

    default boolean hasPeriod() {
        return getPeriod() != null;
    }

    default String asString() {
        return toString();
    }
}
