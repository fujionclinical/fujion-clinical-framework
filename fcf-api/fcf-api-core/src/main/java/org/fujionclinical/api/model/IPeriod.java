package org.fujionclinical.api.model;

import java.util.Date;

public interface IPeriod {

    Date getStartDate();

    default void setStartDate(Date startDate) {
        throw new UnsupportedOperationException();
    }

    default boolean hasStartDate() {
        return getStartDate() != null;
    }

    Date getEndDate();

    default void setEndDate(Date endDate) {
        throw new UnsupportedOperationException();
    }

    default boolean hasEndDate() {
        return getEndDate() != null;
    }
}
