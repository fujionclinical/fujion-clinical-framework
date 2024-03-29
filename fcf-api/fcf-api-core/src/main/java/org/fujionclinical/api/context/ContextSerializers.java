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
package org.fujionclinical.api.context;

import org.fujion.common.AbstractRegistry;
import org.fujion.common.RegistryMap.DuplicateAction;
import org.fujionclinical.api.core.ISerializer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;

/**
 * Registry for context serializers indexed by the class type they support.
 */
public class ContextSerializers extends AbstractRegistry<Class<?>, ISerializer<?>> implements DestructionAwareBeanPostProcessor {

    private static final ContextSerializers instance = new ContextSerializers();

    public static ContextSerializers getInstance() {
        return instance;
    }

    /**
     * Enforce singleton instance.
     */
    private ContextSerializers() {
        super(DuplicateAction.ERROR);
    }

    @Override
    protected Class<?> getKey(ISerializer<?> item) {
        return item.getType();
    }

    /**
     * Returns the item associated with the specified key, or null if not found.
     *
     * @param clazz The class whose serializer is sought.
     * @return The context serializer.
     */
    @Override
    public ISerializer<?> get(Class<?> clazz) {
        ISerializer<?> contextSerializer = super.get(clazz);

        if (contextSerializer != null) {
            return contextSerializer;
        }

        for (ISerializer<?> item : this) {
            if (item.getType().isAssignableFrom(clazz)) {
                return item;
            }
        }

        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ISerializer) {
            register((ISerializer<?>) bean);
        }

        return bean;
    }

    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
        if (bean instanceof ISerializer) {
            unregister((ISerializer<?>) bean);
        }
    }

    @Override
    public boolean requiresDestruction(Object bean) {
        return bean instanceof ISerializer;
    }

}
