package org.fujionclinical.ui.util;

import org.fujion.common.DateUtil;
import org.fujion.common.MiscUtil;
import org.fujionclinical.api.model.IConceptCode;
import org.fujionclinical.api.model.IPeriod;
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
        register(IConceptCode.class, FormatUtil::formatConceptCode);
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
