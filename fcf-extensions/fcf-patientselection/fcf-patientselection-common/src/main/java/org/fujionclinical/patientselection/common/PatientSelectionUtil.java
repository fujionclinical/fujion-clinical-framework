/*
 * #%L
 * Fujion Clinical Framework
 * %%
 * Copyright (C) 2020 fujionclinical.org
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This Source Code Form is also subject to the terms of the Health-Related
 * Additional Disclaimer of Warranty and Limitation of Liability available at
 *
 *      http://www.fujionclinical.org/licensing/disclaimer
 *
 * #L%
 */
package org.fujionclinical.patientselection.common;

import edu.utah.kmm.model.cool.clinical.role.Patient;
import edu.utah.kmm.model.cool.foundation.datatype.PersonName;
import edu.utah.kmm.model.cool.foundation.entity.Person;
import edu.utah.kmm.model.cool.mediator.datasource.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fujion.ancillary.IResponseCallback;
import org.fujion.common.CollectionUtil;
import org.fujion.common.StrUtil;
import org.fujionclinical.api.model.patient.PatientQueryCriteria;
import org.fujionclinical.api.query.core.QueryException;
import org.fujionclinical.ui.dialog.DialogUtil;
import org.fujionclinical.ui.util.FCFUtil;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.fujionclinical.patientselection.common.Constants.*;

/**
 * Person search services.
 */
public class PatientSelectionUtil {

    /**
     * Perform search, using the specified search text.
     *
     * @param searchText Text to use in search.
     * @param maxMatches Maximum number of allowable matches. If this value is exceeded, the user
     *                   will be given the opportunity to cancel the search. A value of zero suppresses
     *                   this feature.
     * @param dataSource The data source.
     * @param callback   Callback to receive a list of patients matching the specified search
     *                   criteria. The return value will be null if no search criteria are provided or the
     *                   search exceeds the maximum allowable matches and the user chooses to cancel the
     *                   search.
     */
    public static void execute(
            String searchText,
            int maxMatches,
            DataSource dataSource,
            IResponseCallback<List<Person>> callback) {
        PatientQueryCriteria criteria = new PatientQueryCriteria();
        criteria.reset(searchText);
        execute(criteria, maxMatches, dataSource, callback);
    }

    /**
     * Perform search, using the specified criteria.
     *
     * @param criteria   The search criteria.
     * @param maxMatches Maximum number of allowable matches. If this value is exceeded, the user
     *                   will be given the opportunity to cancel the search. A value of zero suppresses
     *                   this feature.
     * @param dataSource The data source.
     * @param callback   Callback to receive a list of patients matching the specified search
     *                   criteria. The return value will be null if no search criteria are provided or the
     *                   search exceeds the maximum allowable matches and the user chooses to cancel the
     *                   search.
     */
    public static void execute(
            PatientQueryCriteria criteria,
            int maxMatches,
            DataSource dataSource,
            IResponseCallback<List<Person>> callback) {
        if (criteria == null || criteria.isEmpty()) {
            doCallback(null, callback);
            return;
        }

        try {
            List<Patient> matches = criteria.search(dataSource);

            if (matches == null || matches.size() == 0) {
                throw new QueryException(MSG_ERROR_PATIENT_NOT_FOUND.toString());
            }

            if (maxMatches > 0 && matches.size() > maxMatches) {
                String msg = StrUtil.formatMessage(MSG_TOO_MANY_MATCHES_TEXT.toString(), matches.size());
                DialogUtil.prompt(msg, MSG_TOO_MANY_MATCHES_TITLE.toString(), REFINE_BUTTONS, (response) -> {
                    if (response.hasResponse(BTN_REFINE_LABEL)) {
                        matches.clear();
                    }

                    doCallback(matches, callback);
                });

                return;
            }

            //PatientContext.checkRequired(matches);
            doCallback(matches, callback);
        } catch (QueryException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error during patient search.", e);
            throw new QueryException(MSG_ERROR_NOT_FOUND.toString(FCFUtil.formatExceptionForDisplay(e)), e);
        }
    }

    private static List<Person> toPersons(List<Patient> patients) {
        return patients == null ? null : patients.stream()
                .map(patient -> patient.getActor())
                .collect(Collectors.toList());
    }

    private static void doCallback(
            List<Patient> matches,
            IResponseCallback<List<Person>> callback) {
        if (callback != null) {
            List<Person> persons = toPersons(matches);

            if (persons != null) {
                persons.sort(patientComparator);
            }

            callback.onComplete(persons);
        }
    }

    private static final Log log = LogFactory.getLog(PatientSelectionUtil.class);

    private static final Comparator<Person> patientComparator = new Comparator<Person>() {

        /**
         * Sort by patient full name, ignoring case.
         *
         * @param patient1 First patient to compare.
         * @param patient2 Second patient to compare.
         * @return Result of comparison.
         */
        @Override
        public int compare(
                Person patient1,
                Person patient2) {
            PersonName name1 = CollectionUtil.getFirst(patient1.getName());
            PersonName name2 = CollectionUtil.getFirst(patient2.getName());
            String cmp1 = name1 == null ? "" : name1.toString();
            String cmp2 = name2 == null ? "" : name2.toString();
            return cmp1.compareToIgnoreCase(cmp2);
        }

    };

    /**
     * Enforce static class.
     */
    protected PatientSelectionUtil() {
    }
}
