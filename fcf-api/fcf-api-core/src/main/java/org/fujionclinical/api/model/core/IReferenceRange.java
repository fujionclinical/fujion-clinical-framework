package org.fujionclinical.api.model.core;

import org.fujion.common.CollectionUtil;

import java.util.Collections;
import java.util.List;

public interface IReferenceRange extends IRange<Double> {

    IConcept getType();

    default void setType(IConcept value) {
        notSupported();
    }

    default List<IConcept> getAppliesTo() {
        return Collections.emptyList();
    }

    default void setAppliesTo(List<IConcept> value) {
        CollectionUtil.replaceList(getAppliesTo(), value);
    }

    default void addAppliesTo(IConcept... values) {
        Collections.addAll(getAppliesTo(), values);
    }

    default boolean hasAppliesTo() {
        return CollectionUtil.notEmpty(getAppliesTo());
    }

    default String getDescription() {
        return null;
    }

    default void setDescription(String value) {
        notSupported();
    }

}
