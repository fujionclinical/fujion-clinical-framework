package org.fujionclinical.api.model;

import org.apache.commons.collections.CollectionUtils;
import org.fujion.common.CollectionUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface IPostalAddress {

    enum PostalAddressUse {
        HOME, WORK, TEMP, OLD, BILLING
    }

    PostalAddressUse getUse();

    default void setUse(PostalAddressUse use) {
        throw new UnsupportedOperationException();
    }

    default boolean hasUse() {
        return getUse() != null;
    }

    default List<String> getLines() {
        return Collections.emptyList();
    }

    default void setLines(List<String> lines) {
        throw new UnsupportedOperationException();
    }

    default void addLines(String... lines) {
        CollectionUtils.addAll(getLines(), lines);
    }

    default boolean hasLines() {
        return CollectionUtil.notEmpty(getLines());
    }

    String getCity();

    default void setCity(String city) {
        throw new UnsupportedOperationException();
    }

    default boolean hasCity() {
        return getCity() != null;
    }

    String getDistrict();

    default void setDistrict(String district) {
        throw new UnsupportedOperationException();
    }

    default boolean hasDistrict() {
        return getDistrict() != null;
    }

    String getState();

    default void setState(String state) {
        throw new UnsupportedOperationException();
    }

    default boolean hasState() {
        return getState() != null;
    }

    String getPostalCode();

    default void setPostalCode(String postalCode) {
        throw new UnsupportedOperationException();
    }

    default boolean hasPostalCode() {
        return getPostalCode() != null;
    }

    String getCountry();

    default void setCountry(String country) {
        throw new UnsupportedOperationException();
    }

    default boolean hasCountry() {
        return getCountry() != null;
    }

    IPeriod getPeriod();

    default void setPeriod(IPeriod period) {
        throw new UnsupportedOperationException();
    }

    default boolean hasPeriod() {
        return getPeriod() != null;
    }

    default String asString() {
        return toString();
    }

}
