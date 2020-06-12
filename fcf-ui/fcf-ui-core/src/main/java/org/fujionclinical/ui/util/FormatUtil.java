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
package org.fujionclinical.ui.util;

import org.fujionclinical.api.model.core.*;

public class FormatUtil {

    public static String formatPeriod(IPeriod period) {
        int i = 0;
        DateTimeWrapper startDate = period.getStartDate();
        DateTimeWrapper endDate = period.getEndDate();

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

    public static String formatIdentifier(IIdentifier identifier) {
        return identifier.getValue();
    }

    public static String formatConceptCode(IConceptCode code) {
        return code.hasText() ? code.getText() : code.getCode();
    }

    public static String formatConcept(IConcept code) {
        return code.hasText() ? code.getText() : code.hasCode() ? formatConceptCode(code.getCodes().get(0)) : "";
    }

    private FormatUtil() {

    }
}
