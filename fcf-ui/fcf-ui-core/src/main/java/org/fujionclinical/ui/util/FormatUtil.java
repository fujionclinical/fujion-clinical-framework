package org.fujionclinical.ui.util;

import org.fujionclinical.api.model.IConceptCode;
import org.fujionclinical.api.model.IPeriod;

import java.util.Date;

public class FormatUtil {

    public static String formatPeriod(IPeriod period) {
        int i = 0;
        Date startDate = period.getStartDate();
        Date endDate = period.getEndDate();

        if (startDate != null && endDate != null && startDate.equals(endDate)) {
            endDate = null;
        }

        String startDateStr = Formatters.format(startDate, "");
        i += startDateStr.isEmpty() ? 0 : 1;
        String endDateStr = Formatters.format(endDate, "");
        i += endDateStr.isEmpty() ? 0 : 1;
        String dlm = i == 2 ? " to " : "";
        return startDateStr + dlm + endDateStr;
    }

    public static String formatConceptCode(IConceptCode code) {
        return code.hasText() ? code.getText() : code.getCode();
    }

    private FormatUtil() {

    }
}
