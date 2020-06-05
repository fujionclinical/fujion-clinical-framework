package org.fujionclinical.api.model.core;

import org.fujion.common.CollectionUtil;
import org.fujionclinical.api.model.person.IPerson;

import java.util.Date;
import java.util.List;

public interface IAnnotation {

    List<IPerson> getAuthors();

    default void setAuthors(List<IPerson> authors) {
        CollectionUtil.replaceList(getAuthors(), authors);
    }

    default boolean hasAuthor() {
        return CollectionUtil.notEmpty(getAuthors());
    }

    default void addAuthor(IPerson author) {
        getAuthors().add(author);
    }

    Date getRecorded();

    default void setRecorded(Date recorded) {
        throw new UnsupportedOperationException();
    }

    default boolean hasRecorded() {
        return getRecorded() != null;
    }

    String getText();

    default void setText(String text) {
        throw new UnsupportedOperationException();
    }

    default boolean hasText() {
        return getText() != null;
    }
}
