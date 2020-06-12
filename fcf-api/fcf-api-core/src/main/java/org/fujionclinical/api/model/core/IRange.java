package org.fujionclinical.api.model.core;

public interface IRange<T extends Comparable<? super T>> extends IBaseType {

    enum Bounds {
        EXCLUSIVE,
        INCLUSIVE,
        INCLUSIVE_LOW,
        INCLUSIVE_HIGH
    }

    T getLow();

    default void setLow(T value) {
        notSupported();
    }

    default boolean hasLow() {
        return getLow() != null;
    }

    T getHigh();

    default void setHigh(T value) {
        notSupported();
    }

    default boolean hasHigh() {
        return getHigh() != null;
    }

    default boolean inRange(IRange<T> range) {
        return inRange(range, Bounds.INCLUSIVE_LOW);
    }

    default boolean inRange(IRange<T> range, Bounds bounds) {
        T low = range.getLow();
        T high = range.getHigh();
        boolean inRange = (low == null && !hasLow()) || inRange(low, bounds);
        return inRange && (high == null && !hasHigh()) || inRange(high, bounds);
    }

    default boolean inRange(T value) {
        return inRange(value, Bounds.INCLUSIVE_LOW);
    }

    default boolean inRange(
            T value,
            Bounds bounds) {
        if (value == null) {
            return false;
        }

        int lowCompare = hasLow() ? value.compareTo(getLow()) : 1;
        int highCompare = hasHigh() ? value.compareTo(getHigh()) : -1;
        boolean inRange = false;

        switch (bounds) {
            case EXCLUSIVE:
                inRange = lowCompare > 0 && highCompare < 0;
                break;

            case INCLUSIVE:
                inRange = lowCompare >= 0 && highCompare <= 0;
                break;

            case INCLUSIVE_HIGH:
                inRange = lowCompare > 0 && highCompare <= 0;
                break;

            case INCLUSIVE_LOW:
                inRange = lowCompare >= 0 && highCompare < 0;
                break;
        }

        return inRange;
    }

}
