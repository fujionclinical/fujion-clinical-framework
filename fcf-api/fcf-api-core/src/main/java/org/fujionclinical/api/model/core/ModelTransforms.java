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

import org.fujion.common.Logger;
import org.fujionclinical.api.spring.BeanRegistry;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Indexes all wrapper transforms by their native and model types.
 */
public class ModelTransforms extends BeanRegistry<Class<? extends IModelTransform>, IModelTransform> {

    private static final Logger log = Logger.create(ModelTransforms.class);

    private static final ModelTransforms instance = new ModelTransforms();

    public static ModelTransforms getInstance() {
        return instance;
    }

    private ModelTransforms() {
        super(IModelTransform.class);
    }

    @Override
    protected Class<? extends IModelTransform> getKey(IModelTransform item) {
        return item.getClass();
    }

    public <L, N> IModelTransform<L, N> get(
            Class<L> logicalType,
            Class<N> nativeType) {
        List<IModelTransform> candidates = new ArrayList<>();

        for (IModelTransform transform : this) {
            if (logicalType.isAssignableFrom(transform.getLogicalType()) && nativeType.isAssignableFrom(transform.getNativeType())) {
                candidates.add(transform);
            }
        }

        Assert.isTrue(candidates.size() == 1,
                () -> (candidates.isEmpty() ? "No model transform " : "Multiple model transforms")
                        + " found for converting between '" + logicalType + "' and '" + nativeType + "'");
        return candidates.get(0);
    }

}
