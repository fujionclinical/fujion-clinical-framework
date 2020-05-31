package org.fujionclinical.api.model;

import org.apache.commons.lang.StringUtils;

public interface IIdentifier {

    enum IdentifierCategory {USUAL, OFFICIAL, TEMPORARY, SECONDARY, OLD}

    String getSystem();

    default void setSystem(String system) {
        throw new UnsupportedOperationException();
    }

    default boolean hasSystem() {
        return !StringUtils.isEmpty(getSystem());
    }

    String getValue();

    default void setValue(String value) {
        throw new UnsupportedOperationException();
    }

    default boolean hasValue() {
        return !StringUtils.isEmpty(getValue());
    }

    IConcept getType();

    default void setType(IConcept type) {
        throw new UnsupportedOperationException();
    }

    default boolean hasType() {
        return getType() != null;
    }

    IdentifierCategory getCategory();

    default void setCategory(IdentifierCategory category) {
        throw new UnsupportedOperationException();
    }

    default boolean hasCategory() {
        return getCategory() != null;
    }

}
