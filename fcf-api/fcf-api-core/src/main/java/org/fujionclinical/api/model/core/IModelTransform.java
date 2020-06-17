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

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Transform between logical and native models.
 *
 * @param <L> The logical model type.
 * @param <N> The native  model type.
 */
public interface IModelTransform<L, N> extends IBaseType {

    /**
     * Returns the logical model type.
     *
     * @return The logical model type.
     */
    Class<L> getLogicalType();

    /**
     * Returns the native model type.
     *
     * @return The native model type.
     */
    Class<N> getNativeType();

    /**
     * Converts a logical model instance to native form.  Override if the default implementation
     * is not sufficient.
     *
     * @param value The logical model instance.
     * @return The native model instance.
     */
    N _fromLogicalModel(L value);

    /**
     * Converts a logical model instance to native form, handling nulls.
     * <p>
     * Do not override the default implementation.  Rather, override the {@link #_fromLogicalModel} method instead.
     *
     * @param value The logical model instance (possibly null).
     * @return The native form (null if the input is null).
     */
    default N fromLogicalModel(L value) {
        return value == null ? null : _fromLogicalModel(value);
    }

    /**
     * Converts a list of logical model instances to native form, handling nulls.
     * <p>
     * Do not override the default implementation.  Rather, override the {@link #_fromLogicalModel} method instead.
     *
     * @param values A list of logical model instances (possibly null).
     * @return The corresponding list of native model instances (null if the input is null).
     */
    default List<N> fromLogicalModel(Collection<L> values) {
        return values == null ? null : values.stream()
                .map(value -> fromLogicalModel(value))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Converts native to logical model.
     *
     * @param value The native model instance.
     * @return The logical model form.
     */
    L _toLogicalModel(N value);

    /**
     * Converts native to logical model, handling null input.
     * <p>
     * Do not override the default implementation.  Rather, override the {@link #_toLogicalModel} method instead.
     *
     * @param value The native model instance (possibly null).
     * @return The logical model form (null if the input is null).
     */
    default L toLogicalModel(N value) {
        return value == null ? null : _toLogicalModel(value);
    }

    /**
     * Converts a list of native model instances to logical model form, handling null input.
     * <p>
     * Do not override the default implementation.  Rather, override the {@link #_toLogicalModel} method instead.
     *
     * @param values A list of native model instances (possibly null).
     * @return The corresponding list of logical model instances (null if the input is null).
     */
    default List<L> toLogicalModel(Collection<N> values) {
        return values == null ? null : values.stream()
                .map(value -> toLogicalModel(value))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
