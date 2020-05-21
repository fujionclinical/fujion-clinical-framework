package org.fujionclinical.api.patient.list;

import org.fujionclinical.api.patient.IPatient;

public interface IPatientListItem extends Comparable<IPatientListItem> {
    IPatient getPatient();

    String getInfo();

    @Override
    default int compareTo(IPatientListItem listItem) {
        return getPatient().getFullName().compareTo(listItem.getPatient().getFullName());
    }

    default boolean equalTo(Object object) {
        return object instanceof IPatientListItem && getPatient().getId().equals(((IPatientListItem) object).getPatient().getId());
    }
}
