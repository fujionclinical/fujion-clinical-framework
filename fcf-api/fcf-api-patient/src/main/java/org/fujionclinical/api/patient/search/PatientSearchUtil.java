package org.fujionclinical.api.patient.search;

import org.fujionclinical.api.patient.IPatient;
import org.fujionclinical.api.spring.SpringUtil;

import java.util.List;

public class PatientSearchUtil {

    /**
     * Returns a reference to the patient search engine.
     *
     * @return Patient search engine.
     */
    @SuppressWarnings("unchecked")
    public static IPatientSearchEngine getSearchEngine() {
        return SpringUtil.getBean(IPatientSearchEngine.class);
    }

    /**
     * Perform a search based on given criteria.
     *
     * @param criteria Search criteria.
     * @return Resources matching the search criteria.
     */
    public static List<IPatient> search(PatientSearchCriteria criteria) {
        return getSearchEngine().search(criteria);
    }

    private PatientSearchUtil() {}
}
