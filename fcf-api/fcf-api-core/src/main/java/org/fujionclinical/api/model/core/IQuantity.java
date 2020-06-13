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

import java.util.Arrays;

public interface IQuantity<T extends Number> extends IBaseType {

    enum QuantityComparator {
        LT("<"), LE("<="), GT(">"), GE(">=");

        private final String symbol;

        QuantityComparator(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public String toString() {
            return symbol;
        }

        public static QuantityComparator fromSymbol(String symbol) {
            return symbol == null ? null : Arrays.stream(values())
                    .filter(value -> value.symbol.equals(symbol))
                    .findFirst()
                    .orElse(null);
        }
    }

    T getValue();

    default void setValue(T value) {
        notSupported();
    }

    default boolean hasValue() {
        return getValue() != null;
    }

    QuantityComparator getComparator();

    default void setComparator(QuantityComparator value) {
        notSupported();
    }

    default boolean hasComparator() {
        return getComparator() != null;
    }

    IConceptCode getUnit();

    default void setUnit(IConceptCode value) {
        notSupported();
    }

    default boolean hasUnit() {
        return getUnit() != null;
    }

}
