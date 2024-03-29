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
package org.fujionclinical.patientlist;

import org.coolmodel.foundation.entity.Person;
import org.coolmodel.util.CoolUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility methods for patient lists.
 */
public class PatientListUtil {

    public static final String DELIM = "^";

    private static final String REGEX_DELIM = "\\" + DELIM;

    protected PatientListUtil() {
    }

    /**
     * Creates an immutable set from a list of elements.
     *
     * @param <T>      Type of element.
     * @param elements Elements to add to the set.
     * @return An immutable set containing the specified elements.
     */
    @SafeVarargs
    public static <T> Set<T> createImmutableSet(T... elements) {
        return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(elements)));
    }

    /**
     * Appends an object to the string builder using the default delimiter.
     *
     * @param sb    String builder to receive value.
     * @param value Value to add to the string builder.
     */
    public static void append(
            StringBuilder sb,
            Object value) {
        append(sb, value, DELIM);
    }

    /**
     * Appends an object to the string builder using the specified delimiter.
     *
     * @param sb        String builder to receive value.
     * @param value     Value to add to the string builder.
     * @param delimiter Delimiter to separate consecutive values.
     */
    public static void append(
            StringBuilder sb,
            Object value,
            String delimiter) {
        if (delimiter != null) {
            sb.append(delimiter);
        }

        if (value != null) {
            sb.append(value.toString());
        }
    }

    /**
     * Split the specified value using the default delimiter. The result is guaranteed to have the
     * number of elements specified by the pieces parameter. If the specified value has fewer
     * elements than requested, the result array will be expanded with null elements.
     *
     * @param value  Value to split.
     * @param pieces Number of pieces to return.
     * @return An array of string values resulting from the operation.
     */
    public static String[] split(
            String value,
            int pieces) {
        return split(value, pieces, REGEX_DELIM);
    }

    /**
     * Split the specified value using the specified delimiter. The result is guaranteed to have the
     * number of elements specified by the pieces parameter. If the specified value has fewer
     * elements than requested, the result array will be expanded with null elements.
     *
     * @param value     Value to split.
     * @param pieces    Number of pieces to return.
     * @param delimiter The delimiter to use in the split operation.
     * @return An array of string values resulting from the operation.
     */
    public static String[] split(
            String value,
            int pieces,
            String delimiter) {
        String[] pcs = value == null ? new String[pieces] : value.split(delimiter, pieces);
        return pcs.length < pieces ? Arrays.copyOf(pcs, pieces) : pcs;
    }

    /**
     * Returns the deserialized form of a list.
     *
     * @param serializedValue The serialized value.
     * @return Deserialized form.
     */
    public static IPatientList deserializePatientList(String serializedValue) {
        String[] pcs = split(serializedValue, 2);
        IPatientList list = PatientListRegistry.getInstance().findByName(pcs[0]);

        if (list == null) {
            throw new PatientListException("Unable to create patient list: " + pcs[0]);
        }

        return list.copy(serializedValue);
    }

    /**
     * Finds a list item associated with the specified patient.
     *
     * @param patient Patient to find.
     * @param items   List of items to search.
     * @return The patient list item associated with the specified patient, or null if not found.
     */
    public static IPatientListItem findListItem(
            Person patient,
            Iterable<IPatientListItem> items) {
        if (items == null || patient == null) {
            return null;
        }

        for (IPatientListItem item : items) {
            if (CoolUtils.areSame(patient, item.getPatient())) {
                return item;
            }
        }

        return null;
    }


}
