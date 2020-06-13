package org.fujionclinical.api.model.core;

public interface IRatio<T extends Number> extends IBaseType {

    IQuantity<T> getNumerator();

    default void setNumerator(IQuantity<T> value) {
        notSupported();
    }

    default boolean hasNumerator() {
        return getNumerator() != null;
    }

    IQuantity<T> getDenominator();

    default void setDenominator(IQuantity<T> value) {
        notSupported();
    }

    default boolean hasDenominator() {
        return getDenominator() != null;
    }

}
