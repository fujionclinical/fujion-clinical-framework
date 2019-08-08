/*
 * #%L
 * Fujion Clinical Framework
 * %%
 * Copyright (C) 2019 fujionclinical.org
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
package org.fujionclinical.api.spring;

import org.fujion.common.MiscUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.Assert;

import java.lang.annotation.*;
import java.lang.reflect.Field;

/**
 * Base class for configurators that derive values from the application context's property store via
 * field-based annotations.
 */
public abstract class PropertyAwareConfigurator implements ApplicationContextAware {

    private static final String NULL_VALUE = "@@null@@";

    /**
     * Used to annotate fields for injection.
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Param {

        /**
         * Name of the property containing the value.
         *
         * @return The property name.
         */
        String property();

        /**
         * True if the property is required.
         *
         * @return True if the property is required.
         */
        boolean required() default false;
        
        /**
         * The default value if no property exists.
         *
         * @return The default value.
         */
        String defaultValue() default NULL_VALUE;
    }

    private PropertyProvider propertyProvider;

    private ConversionService conversionService;
    
    /**
     * Forms the full property name. The default implementation simply returns the original name.
     * Override if the provided name needs to be transformed in some way.
     *
     * @param name The property name specified in the annotation.
     * @return The expanded property name.
     */
    public String expandPropertyName(String name) {
        return name;
    }

    /**
     * Inject all annotated class members.
     *
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        propertyProvider = new PropertyProvider(applicationContext);
        conversionService = DefaultConversionService.getSharedInstance();
        wireParams(this);
        afterInitialized();
    }

    protected void afterInitialized() {
        // Override for special post-initialization requirements.
    }

    /**
     * Wire {@link PropertyAwareConfigurator.Param @Param}-annotated fields.
     *
     * @param object Object whose fields are to be wired.
     */
    public void wireParams(Object object) {
        Class<?> clazz = object.getClass();

        while (clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                Param annot = field.getAnnotation(Param.class);

                if (annot != null) {
                    String propName = expandPropertyName(annot.property());
                    String value = propertyProvider.getProperty(propName);
                    
                    if (value == null || value.isEmpty()) {
                        value = annot.defaultValue();
                        value = NULL_VALUE.equals(value) ? null : value;
                    }
                    
                    if (value == null) {
                        Assert.isTrue(!annot.required(), () -> "Required configuration parameter not specified: " + propName);
                        continue;
                    }

                    try {
                        field.set(object, conversionService.convert(value, field.getType()));
                    } catch (Exception e) {
                        throw MiscUtil.toUnchecked(e);
                    }
                }
            }

            clazz = clazz.getSuperclass();
        }
    }

}
