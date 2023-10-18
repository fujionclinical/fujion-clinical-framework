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
package org.fujionclinical.patientselection.v1;

import org.coolmodel.foundation.entity.Person;
import org.fujion.ancillary.IResponseCallback;
import org.fujion.component.Window;
import org.fujionclinical.patientselection.common.Constants;
import org.fujionclinical.patientselection.common.IPatientSelector;
import org.fujionclinical.patientselection.common.PatientSelectorFactoryBase;

/**
 * This is the patient selection factory.
 */
public class PatientSelectorFactory extends PatientSelectorFactoryBase {

    public static class PatientSelector implements IPatientSelector {

        private final Window dlg = PatientSelectionUtil.createSelectionDialog();

        @Override
        public void select(IResponseCallback<Person> callback) {
            dlg.modal(callback == null ? null : (event) -> callback.onComplete(dlg.getAttribute(Constants.SELECTED_PATIENT_ATTRIB, Person.class)));
        }
    }

    protected PatientSelectorFactory() {
        super("patientSelectorFactoryV1", "Patient selector (v1)", PatientSelector.class);
    }
}
