package org.fujionclinical.api.model;

import org.apache.commons.lang.StringUtils;

public interface IConceptCode {

    String getSystem();

    default void setSystem(String system) {
        throw new UnsupportedOperationException();
    }

    default boolean hasSystem() {
        return !StringUtils.isEmpty(getSystem());
    }

    String getCode();

    default void setCode(String code) {
        throw new UnsupportedOperationException();
    }

    default boolean hasCode() {
        return !StringUtils.isEmpty(getCode());
    }

    String getText();

    default void setText(String text) {
        throw new UnsupportedOperationException();
    }

    default boolean hasText() {
        return !StringUtils.isEmpty(getText());
    }
}
