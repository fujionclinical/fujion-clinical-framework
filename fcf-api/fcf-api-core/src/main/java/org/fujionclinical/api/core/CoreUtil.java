package org.fujionclinical.api.core;

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

    private CoreUtil() {

    }
}
