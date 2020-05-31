package org.fujionclinical.api.model;

import org.fujion.common.CollectionUtil;

import java.util.List;
import java.util.function.Predicate;

public interface IConcept {

    String getText();

    default void setText(String text) {
        throw new UnsupportedOperationException();
    }

    default boolean hasText() {
        return getText() != null;
    }

    List<IConceptCode> getCodes();

    default void setCodes(List<IConceptCode> codes) {
        CollectionUtil.replaceList(getCodes(), codes);
    }

    default IConceptCode getCode(String system) {
        return getCode(code -> system.equals(code.getSystem()));
    }

    default IConceptCode getCode(Predicate<IConceptCode> criteria) {
        return CollectionUtil.findMatch(getCodes(), criteria);
    }

    default boolean hasCode() {
        return CollectionUtil.notEmpty(getCodes());
    }

    default void addCode(ConceptCode code) {
        getCodes().add(code);
    }

}
