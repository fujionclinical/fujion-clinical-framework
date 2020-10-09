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

import edu.utah.kmm.model.cool.core.datatype.QuantityEx;
import edu.utah.kmm.model.cool.terminology.ConceptReferenceSet;
import org.fujion.common.CollectionUtil;

import java.util.Collections;
import java.util.List;

public interface IReferenceRange<T extends Number> extends IBaseType {

    QuantityEx<T> getLow();

    default void setLow(QuantityEx<T> value) {
        notSupported();
    }

    default boolean hasLow() {
        return getLow() != null;
    }

    QuantityEx<T> getHigh();

    default void setHigh(QuantityEx<T> value) {
        notSupported();
    }

    default boolean hasHigh() {
        return getHigh() != null;
    }

    ConceptReferenceSet getType();

    default void setType(ConceptReferenceSet value) {
        notSupported();
    }

    default List<ConceptReferenceSet> getAppliesTo() {
        return Collections.emptyList();
    }

    default void setAppliesTo(List<ConceptReferenceSet> value) {
        CollectionUtil.replaceElements(getAppliesTo(), value);
    }

    default void addAppliesTo(ConceptReferenceSet... values) {
        Collections.addAll(getAppliesTo(), values);
    }

    default boolean hasAppliesTo() {
        return CollectionUtil.notEmpty(getAppliesTo());
    }

    default IRange<Double> getAgeRange() {
        return null;
    }

    default void setAgeRange(IRange<Double> value) {
        notSupported();
    }

    default boolean hasAgeRange() {
        return getAgeRange() != null;
    }

    default String getDescription() {
        return null;
    }

    default void setDescription(String value) {
        notSupported();
    }

}
