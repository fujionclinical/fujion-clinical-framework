package org.fujionclinical.api.model;

import org.apache.commons.collections.CollectionUtils;
import org.fujion.common.MiscUtil;

import java.util.List;

public interface IConcept {

    String getText();

    default IConcept setText(String text) {
        throw new UnsupportedOperationException();
    }

    default boolean hasText() {
        return getText() != null;
    }

    List<IConceptCode> getCodes();

    default IConcept setCodes(List<IConceptCode> codes) {
        MiscUtil.replaceList(getCodes(), codes);
        return this;
    }

    default boolean hasCode() {
        return !CollectionUtils.isEmpty(getCodes());
    }

    default IConcept addCode(ConceptCode code) {
        getCodes().add(code);
        return this;
    }

}
