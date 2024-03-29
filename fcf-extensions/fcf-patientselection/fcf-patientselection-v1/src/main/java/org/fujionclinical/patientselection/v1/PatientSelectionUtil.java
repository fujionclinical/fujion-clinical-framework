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

import org.fujion.component.Window;
import org.fujion.page.PageUtil;
import org.fujionclinical.patientselection.common.Constants;

public class PatientSelectionUtil extends org.fujionclinical.patientselection.common.PatientSelectionUtil {

    /**
     * Creates an instance of the patient selection dialog.
     *
     * @return The patient selection dialog.
     */
    public static Window createSelectionDialog() {
        return (Window) PageUtil.createPage(Constants.RESOURCE_PATH + "v1/patientSelection.fsp", null).get(0);
    }

    private PatientSelectionUtil() {}
}
