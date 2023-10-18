/*
 * #%L
 * Fujion Clinical Framework
 * %%
 * Copyright (C) 2023 fujionclinical.org
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
package org.fujionclinical.reports.header;

import org.apache.commons.lang3.StringUtils;
import org.coolmodel.core.complex.Identifier;
import org.coolmodel.foundation.entity.Person;
import org.coolmodel.util.PersonUtils;
import org.fujion.annotation.OnFailure;
import org.fujion.annotation.WiredComponent;
import org.fujion.common.DateUtil;
import org.fujion.component.Label;
import org.fujion.core.CoreUtil;
import org.fujionclinical.api.cool.patient.PatientContext;

import java.time.LocalDate;

/**
 * This is the generic controller for the stock report headers.
 */
public class ReportHeaderPatient extends ReportHeaderBase {

    static {
        ReportHeaderRegistry.getInstance().register("patient", CoreUtil.getResourceClassPath(ReportHeaderPatient.class) + "patientReportHeader.fsp");
    }

    @WiredComponent(onFailure = OnFailure.IGNORE)
    private Label lblPatientInfo;

    public ReportHeaderPatient() {
        super("CONTEXT.CHANGED.Patient");
    }

    /**
     * Retrieves a formatted header for the current patient.
     *
     * @return Formatted header.
     */
    public String getPatientInfo() {
        Person patient = PatientContext.getActivePatient();
        String text;

        if (patient == null) {
            text = "No Patient Selected";
        } else {
            Identifier mrn = PersonUtils.getMRN(patient); // May be null!
            text = PersonUtils.getFullName(patient);

            if (mrn != null) {
                text += "  #" + mrn.getId();
            }

            String gender = patient.getGender() == null ? "" : patient.getGender().toString();

            if (!StringUtils.isEmpty(gender)) {
                text += "   (" + gender + ")";
            }

            LocalDate dob = patient.getBirthDate();
            LocalDate deceased = patient.getDeathDate();
            String age = DateUtil.formatAge(dob == null ? null : dob, true, deceased == null ? null : deceased);
            text += "  Age: " + age;

            if (deceased != null) {
                text += "  Died: " + deceased;
            }
        }

        return text;
    }

    /**
     * Rebind form data when context changes.
     */
    @Override
    public void refresh() {
        super.refresh();
        updateLabel(lblPatientInfo, getPatientInfo());
    }

}
