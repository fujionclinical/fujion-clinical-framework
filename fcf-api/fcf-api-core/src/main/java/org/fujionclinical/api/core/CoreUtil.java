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
package org.fujionclinical.api.core;

import org.apache.commons.lang3.EnumUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

public class CoreUtil {

    public static Class<?> getPropertyType(PropertyDescriptor propertyDescriptor) {
        Class<?> propertyType = propertyDescriptor.getPropertyType();

        if (propertyType.isArray()) {
            propertyType = propertyType.getComponentType();
        }

        if (Collection.class.isAssignableFrom(propertyType)) {
            Method method = propertyDescriptor.getReadMethod();
            ParameterizedType type = (ParameterizedType) method.getGenericReturnType();
            propertyType = (Class<?>) type.getActualTypeArguments()[0];
        }

        return propertyType;
    }

    public static <T> Class<T> cast(Class<?> clazz) {
        return (Class<T>) clazz;
    }

    public static String enumToString(Enum<?> value) {
        return value == null ? null : value.name().toLowerCase().replace("_", " ");
    }

    public static <T extends Enum<T>> T stringToEnum(String value, Class<T> type) {
        return stringToEnum(value, type, null);
    }

    public static <T extends Enum<T>> T stringToEnum(String value, Class<T> type, T deflt) {
        if (value == null) {
            return deflt;
        }

        T enumValue = EnumUtils.getEnumIgnoreCase(type, value);
        enumValue = enumValue != null ? enumValue : EnumUtils.getEnumIgnoreCase(type, value.replace("-", "_"));
        enumValue = enumValue != null ? enumValue : EnumUtils.getEnumIgnoreCase(type, value.replace("-", ""));
        return enumValue != null ? enumValue : deflt;
    }

    public static <T extends Enum<T>> T enumToEnum(Enum<?> value, Class<T> type) {
        return enumToEnum(value, type, null);
    }

    public static <T extends Enum<T>> T enumToEnum(Enum<?> value, Class<T> type, T deflt) {
        return value == null ? null : stringToEnum(value.name(), type, deflt);
    }

    private CoreUtil() {

    }

}
