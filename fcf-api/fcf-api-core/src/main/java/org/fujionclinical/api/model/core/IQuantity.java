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
