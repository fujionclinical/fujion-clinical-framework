package org.fujionclinical.api.model.core;

import org.fujion.common.CollectionUtil;

import java.util.Collections;
import java.util.List;

public interface ICategoryType {

    default List<IConcept> getCategories() {
        return Collections.emptyList();
    }

    default void setCategories(List<IConcept> values) {
        CollectionUtil.replaceList(getCategories(), values);
    }

    default void addCategories(IConcept... values) {
        Collections.addAll(getCategories(), values);
    }

    default boolean hasCategory() {
        return CollectionUtil.notEmpty(getCategories());
    }

}
