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

import edu.utah.kmm.model.cool.core.datatype.Identifier;
import edu.utah.kmm.model.cool.core.datatype.Period;
import edu.utah.kmm.model.cool.foundation.entity.Person;
import edu.utah.kmm.model.cool.terminology.ConceptReference;
import edu.utah.kmm.model.cool.terminology.ConceptReferenceSet;
import edu.utah.kmm.model.cool.util.PersonNameParsers;
import org.fujion.common.DateUtil;
import org.fujion.common.MiscUtil;

import java.time.temporal.Temporal;
import java.util.*;
import java.util.function.Function;

public class Formatters {

    public interface Formatter<T> extends Function<T, String> {

    }

    private static final List<Class<?>> types = new ArrayList<>();

    private static final Map<Class<?>, Formatter<?>> formatters = new HashMap<>();

    static {
        register(Date.class, DateUtil::formatDate);
        register(Temporal.class, DateUtil::formatDate);
        register(Period.class, FormatUtil::formatPeriod);
        register(ConceptReferenceSet.class, FormatUtil::formatConceptReferenceSet);
        register(ConceptReference.class, FormatUtil::formatConceptReference);
        register(Identifier.class, FormatUtil::formatIdentifier);
        register(Person.class, value -> PersonNameParsers.get().toString(value.getName().get(0)));
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
