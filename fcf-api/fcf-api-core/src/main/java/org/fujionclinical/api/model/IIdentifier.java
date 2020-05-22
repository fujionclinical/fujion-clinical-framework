package org.fujionclinical.api.model;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.fujion.common.MiscUtil;

import java.util.Collections;
import java.util.List;

public interface IIdentifier {

    enum IdentifierCategory {USUAL, OFFICIAL, TEMPORARY, SECONDARY, OLD}

    String getSystem();

    default IIdentifier setSystem(String system) {
        throw new UnsupportedOperationException();
    }

    default boolean hasSystem() {
        return !StringUtils.isEmpty(getSystem());
    }

    String getValue();

    default IIdentifier setValue(String value) {
        throw new UnsupportedOperationException();
    }

    default boolean hasValue() {
        return !StringUtils.isEmpty(getValue());
    }

    default List<IConceptCode> getTypes() {
        return Collections.emptyList();
    }

    default IIdentifier addTypes(IConceptCode... types) {
        Collections.addAll(getTypes(), types);
        return this;
    }

    default IIdentifier setTypes(List<IConceptCode> types) {
        MiscUtil.replaceList(getTypes(), types);
        return this;
    }

    default boolean hasType() {
        return !CollectionUtils.isEmpty(getTypes());
    }

    IdentifierCategory getCategory();

    default IIdentifier setCategory(IdentifierCategory category) {
        throw new UnsupportedOperationException();
    }

    default boolean hasCategory() {
        return getCategory() != null;
    }
}
