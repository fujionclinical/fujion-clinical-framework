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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
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

    public static Class<?>[] getGenericParameters(Class<?> clazz, Class<?> source) {
        Type[] paramTypes = null;

        while (paramTypes == null && clazz != null && clazz != Object.class) {
            Type[] types = clazz.getGenericInterfaces();

            paramTypes = Arrays.stream(types)
                    .filter(type -> type instanceof ParameterizedType)
                    .filter(type -> ((ParameterizedType) type).getRawType() == source)
                    .map(type -> ((ParameterizedType) type).getActualTypeArguments())
                    .findFirst()
                    .orElse(null);

            clazz = clazz.getSuperclass();
        }

        return paramTypes == null ? null : Arrays.stream(paramTypes)
                .map(type -> (Class<?>) type)
                .toArray(size -> new Class[size]);
    }

    private CoreUtil() {

    }

}
