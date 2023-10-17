package org.fujionclinical.api.test;

import org.fujion.convert.ConversionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestUtil {

    private static final Pattern VALUE_ANNOTATION_PATTERN = Pattern.compile("^\\$\\{.+:(.*)\\}$");

    /**
     * Inject field values provided in a map.
     *
     * @param target The object to inject.
     * @param values A map of values to be injected where the key is the field name.
     */
    public static void setFields(Object target, Map<String, Object> values) {
        values.forEach((k, v) -> ReflectionTestUtils.setField(target, k, v));
    }

    /**
     * Initializes all fields that have a @Value annotation of the form "${prop:default}" to the
     * default value.
     *
     * @param target The object to inject.
     */
    public static void initValueAnnotatedFields(Object target) {
        ReflectionUtils.doWithFields(target.getClass(), f -> {
            f.setAccessible(true);
            Value valueAnnotation = f.getAnnotation(Value.class);

            if (valueAnnotation != null) {
                String value = valueAnnotation.value();
                Matcher matcher = VALUE_ANNOTATION_PATTERN.matcher(value);

                if (matcher.matches()) {
                    String defaultValue = matcher.group(1);
                    Object convertedValue = ConversionService.getInstance().convert(defaultValue, f.getType());
                    f.set(target, convertedValue);
                }
            }
        });
    }

    /**
     * Extracts all non-static field values from the target object, saving them into a map.
     *
     * @param target The target object.
     * @return A map of all field values.
     */
    public static Map<String, Object> getFieldValues(Object target) {
        return getFieldValues(target, true);
    }

    /**
     * Extracts all field values from the target object, saving them into a map.
     *
     * @param target        The target object.
     * @param excludeStatic If true, static fields are ignored.
     * @return A map of all field values.
     */
    public static Map<String, Object> getFieldValues(Object target, boolean excludeStatic) {
        Map<String, Object> values = new HashMap<>();
        ReflectionUtils.doWithFields(target.getClass(), f -> {
            if (!excludeStatic || !Modifier.isStatic(f.getModifiers())) {
                f.setAccessible(true);
                values.put(f.getName(), f.get(target));
            }
        });
        return values;
    }

    private TestUtil() {

    }
}
