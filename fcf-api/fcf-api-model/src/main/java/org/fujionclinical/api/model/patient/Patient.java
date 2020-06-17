package org.fujionclinical.api.model.patient;

import org.fujionclinical.api.model.core.IIdentifier;
import org.fujionclinical.api.model.person.Person;

public class Patient extends Person implements IPatient {

    private IIdentifier mrn;

    @Override
    public IIdentifier getMRN() {
        return mrn;
    }

    @Override
    public void setMRN(IIdentifier mrn) {
        this.mrn = mrn;
    }

}
