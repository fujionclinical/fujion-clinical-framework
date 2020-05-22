package org.fujionclinical.api.patient;

import org.fujionclinical.api.model.IIdentifier;
import org.fujionclinical.api.model.IPerson;

public interface IPatient extends IPerson {

    IIdentifier getMRN();

    default IPatient setMRN(IIdentifier mrn) {
        throw new UnsupportedOperationException();
    }

    default boolean hasMRN() {
        return getMRN() != null;
    }
}
