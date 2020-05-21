package org.fujionclinical.api.patient;

import org.fujionclinical.api.model.IPerson;
import org.fujionclinical.api.model.Identifier;

import java.util.Date;

public interface IPatient extends IPerson {

    Identifier getMRN();

    String getGender();

    Date getDOB();

    Date getDeceased();
}
