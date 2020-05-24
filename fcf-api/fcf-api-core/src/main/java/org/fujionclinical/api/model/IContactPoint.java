package org.fujionclinical.api.model;

import java.util.Collection;

public interface IContactPoint {

    enum ContactPointSystem {
        PHONE, FAX, EMAIL, PAGER, URL, SMS, OTHER
    }

    enum ContactPointUse {
        HOME, WORK, TEMP, OLD, MOBILE
    }

    static IContactPoint getContactPoint(
            Collection<? extends IContactPoint> contactPoints,
            ContactPointUse use,
            ContactPointSystem system) {
        if (contactPoints != null && !contactPoints.isEmpty()) {
            for (IContactPoint contactPoint : contactPoints) {
                if (contactPoint.getUse() == use && contactPoint.getSystem() == system) {
                    return contactPoint;
                }
            }
        }

        return null;
    }

    static IContactPoint getContactPoint(
            Collection<? extends IContactPoint> contactPoints,
            ContactPointUse... uses) {
        if (contactPoints != null && !contactPoints.isEmpty()) {
            for (ContactPointUse use : uses) {
                for (IContactPoint contactPoint : contactPoints) {
                    if (contactPoint.getUse() == use) {
                        return contactPoint;
                    }
                }
            }
        }

        return null;
    }

    static IContactPoint getContactPoint(
            Collection<? extends IContactPoint> contactPoints,
            ContactPointSystem... systems) {
        if (contactPoints != null && !contactPoints.isEmpty()) {
            for (ContactPointSystem system : systems) {
                for (IContactPoint contactPoint : contactPoints) {
                    if (contactPoint.getSystem() == system) {
                        return contactPoint;
                    }
                }
            }
        }

        return null;
    }

    ContactPointSystem getSystem();

    default IContactPoint setSystem(ContactPointSystem system) {
        throw new UnsupportedOperationException();
    }

    default boolean hasSystem() {
        return getSystem() != null;
    }

    String getValue();

    default IContactPoint setValue(String value) {
        throw new UnsupportedOperationException();
    }

    default boolean hasValue() {
        return getValue() != null;
    }

    default ContactPointUse getUse() {
        return null;
    }

    default IContactPoint setUse(ContactPointUse use) {
        throw new UnsupportedOperationException();
    }

    default boolean hasUse() {
        return getUse() != null;
    }

    default Integer getRank() {
        return 0;
    }

    default IContactPoint setRank(Integer rank) {
        throw new UnsupportedOperationException();
    }

    default boolean hasRank() {
        return getRank() != null;
    }

    default IPeriod getPeriod() {
        return null;
    }

    default IContactPoint setPeriod(IPeriod period) {
        throw new UnsupportedOperationException();
    }

    default boolean hasPeriod() {
        return getPeriod() != null;
    }
}
