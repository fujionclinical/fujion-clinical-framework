package org.fujionclinical.api.patient.search;

import org.fujionclinical.api.patient.IPatient;

import java.util.List;

public interface IPatientSearchEngine {

    List<IPatient> search(PatientSearchCriteria criteria);

}
