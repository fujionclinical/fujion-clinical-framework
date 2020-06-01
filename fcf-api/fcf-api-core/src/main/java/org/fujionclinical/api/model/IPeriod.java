package org.fujionclinical.api.model;

import org.fujion.common.DateUtil;

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

    default boolean inRange(Date date) {
        if (hasStartDate() && DateUtil.compare(date, getStartDate()) < 0) {
            return false;
        }

        if (hasEndDate() && DateUtil.compare(date, getEndDate()) >= 0) {
            return false;
        }

        return true;
    }
}
