package org.fujionclinical.ui.patientselection.v1;

import org.fujion.component.Window;
import org.fujion.page.PageUtil;
import org.fujionclinical.ui.patientselection.common.Constants;

public class PatientSelectionUtil extends org.fujionclinical.ui.patientselection.common.PatientSelectionUtil {

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
