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

import edu.utah.kmm.model.cool.clinical.finding.Onset;
import edu.utah.kmm.model.cool.core.datatype.Identifier;
import edu.utah.kmm.model.cool.core.datatype.Period;
import edu.utah.kmm.model.cool.terminology.ConceptReference;
import edu.utah.kmm.model.cool.terminology.ConceptReferenceSet;
import org.fujion.common.DateUtil;

import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.Objects;

public class FormatUtil {

    public static String formatOnset(Onset onset) {
        Object value = onset == null ? null : onset.getValue();

        if (value instanceof Temporal) {
            return DateUtil.formatDate((Temporal) value);
        }

        return Objects.toString(value, "");
    }

    public static String formatPeriod(Period period) {
        int i = 0;
        LocalDateTime startDate = period.getStart();
        LocalDateTime endDate = period.getEnd();

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

    public static String formatIdentifier(Identifier identifier) {
        return identifier.getId();
    }

    public static String formatConceptReferenceSet(ConceptReferenceSet value) {
        return value.hasText() ? value.getText() : formatConceptReference(value.getFirstConcept());
    }

    public static String formatConceptReference(ConceptReference value) {
        return value.hasPreferredName() ? value.getPreferredName() : value.hasCode() ? value.getCode() : "";
    }

    private FormatUtil() {

    }
}
