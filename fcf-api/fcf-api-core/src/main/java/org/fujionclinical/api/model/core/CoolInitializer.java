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
package org.fujionclinical.api.model.core;

import edu.utah.kmm.model.cool.dao.core.EntityDAO;
import edu.utah.kmm.model.cool.dao.core.EntityDAORegistry;
import edu.utah.kmm.model.cool.dao.query.ParameterDescriptor;
import edu.utah.kmm.model.cool.dao.query.ParameterDescriptorRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Populates all Cool registries with beans from application context.
 */
public class CoolInitializer implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(
            Object bean,
            String beanName) throws BeansException {
        if (bean instanceof EntityDAO) {
            EntityDAORegistry.register((EntityDAO) bean);
        } else if (bean instanceof ParameterDescriptor) {
            ParameterDescriptorRegistry.register((ParameterDescriptor) bean);
        }

        return bean;
    }

}
