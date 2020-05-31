package org.fujionclinical.api.model;

import org.fujion.common.CollectionUtil;

import java.util.Collection;

public interface IContactPoint {

    enum ContactPointSystem {
        PHONE, FAX, EMAIL, PAGER, URL, SMS, OTHER
    }

    enum ContactPointUse {
        HOME, WORK, TEMP, OLD, MOBILE
    }

    ContactPointSystem getSystem();

    default void setSystem(ContactPointSystem system) {
        throw new UnsupportedOperationException();
    }

    default boolean hasSystem() {
        return getSystem() != null;
    }

    String getValue();

    default void setValue(String value) {
        throw new UnsupportedOperationException();
    }

    default boolean hasValue() {
        return getValue() != null;
    }

    default ContactPointUse getUse() {
        return null;
    }

    default void setUse(ContactPointUse use) {
        throw new UnsupportedOperationException();
    }

    default boolean hasUse() {
        return getUse() != null;
    }

    default Integer getRank() {
        return 0;
    }

    default void setRank(Integer rank) {
        throw new UnsupportedOperationException();
    }

    default boolean hasRank() {
        return getRank() != null;
    }

    default IPeriod getPeriod() {
        return null;
    }

    default void setPeriod(IPeriod period) {
        throw new UnsupportedOperationException();
    }

    default boolean hasPeriod() {
        return getPeriod() != null;
    }
}
