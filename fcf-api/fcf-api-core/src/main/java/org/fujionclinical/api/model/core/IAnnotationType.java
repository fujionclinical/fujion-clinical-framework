package org.fujionclinical.api.model.core;

import org.apache.commons.collections.CollectionUtils;
import org.fujion.common.CollectionUtil;

import java.util.List;

public interface IAnnotationType {

    List<IAnnotation> getAnnotations();

    default void setAnnotations(List<IAnnotation> annotations) {
        CollectionUtil.replaceList(getAnnotations(), annotations);
    }

    default void addAnnotations(IAnnotation... annotations) {
        CollectionUtils.addAll(getAnnotations(), annotations);
    }

    default boolean hasAnnotation() {
        return CollectionUtil.notEmpty(getAnnotations());
    }
}
