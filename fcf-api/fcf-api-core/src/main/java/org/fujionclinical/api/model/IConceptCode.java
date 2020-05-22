package org.fujionclinical.api.model;

import org.apache.commons.lang.StringUtils;

public interface IConceptCode {

    String getSystem();

    default IConceptCode setSystem(String system) {
        throw new UnsupportedOperationException();
    }

    default boolean hasSystem() {
        return !StringUtils.isEmpty(getSystem());
    }

    String getCode();

    default IConceptCode setCode(String code) {
        throw new UnsupportedOperationException();
    }

    default boolean hasCode() {
        return !StringUtils.isEmpty(getCode());
    }

    String getText();

    default IConceptCode setText(String text) {
        throw new UnsupportedOperationException();
    }

    default boolean hasText() {
        return !StringUtils.isEmpty(getText());
    }
}
