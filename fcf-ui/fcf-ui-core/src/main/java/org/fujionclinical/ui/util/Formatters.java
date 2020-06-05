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

import org.fujion.common.DateUtil;
import org.fujion.common.MiscUtil;
import org.fujionclinical.api.model.core.IConcept;
import org.fujionclinical.api.model.core.IConceptCode;
import org.fujionclinical.api.model.core.IIdentifier;
import org.fujionclinical.api.model.core.IPeriod;
import org.fujionclinical.api.model.person.IPerson;

import java.util.*;
import java.util.function.Function;

public class Formatters {

    public interface Formatter<T> extends Function<T, String> {

    }

    private static final List<Class<?>> types = new ArrayList<>();

    private static final Map<Class<?>, Formatter<?>> formatters = new HashMap<>();

    static {
        register(Date.class, DateUtil::formatDate);
        register(IPeriod.class, FormatUtil::formatPeriod);
        register(IConcept.class, FormatUtil::formatConcept);
        register(IConceptCode.class, FormatUtil::formatConceptCode);
        register(IIdentifier.class, FormatUtil::formatIdentifier);
        register(IPerson.class, person -> person.getFullName());
    }

    public static <T> void register(
            Class<T> type,
            Formatter<T> formatter) {
        register(type, formatter, false);
    }

    public static <T> void register(
            Class<T> type,
            Formatter<T> formatter,
            boolean first) {
        if (formatters.containsKey(type)) {
            types.remove(type);
        }
        formatters.put(type, formatter);
        types.add(first ? 0 : types.size(), type);
    }

    public static <T> Formatter<T> getFormatter(Class<T> type) {
        Class<?> key = MiscUtil.firstAssignable(type, types);
        return key == null ? null : (Formatter<T>) formatters.get(key);
    }

    public static <T> String format(T object) {
        return format(object, null);
    }

    public static <T> String format(T object, String deflt) {
        if (object == null) {
            return deflt;
        }

        if (object.getClass().isArray()) {
            return formatArray((Object[]) object);
        }

        if (object instanceof Collection) {
            return formatCollection((Collection<?>) object);
        }

        Formatter<T> formatter = (Formatter<T>) getFormatter(object.getClass());
        return formatter == null ? object.toString() : formatter.apply(object);
    }

    private static String formatArray(Object[] data) {
        return formatCollection(Arrays.asList(data));
    }

    private static String formatCollection(Collection<?> data) {
        StringBuilder sb = new StringBuilder();

        for (Object element : data) {
            String value = format(element);

            if (value != null && !value.isEmpty()) {
                sb.append(sb.length() == 0 ? "" : ", ").append(value);
            }
        }

        return sb.toString();
    }

    private Formatters() {

    }


}
