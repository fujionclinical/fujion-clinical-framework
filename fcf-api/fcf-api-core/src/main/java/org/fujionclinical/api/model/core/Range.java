package org.fujionclinical.api.model.core;

public class Range<T extends Comparable<? super T>> implements IRange<T> {

    private final T low;

    private final T high;

    public Range(T low, T high) {
        this.low = low;
        this.high = high;
    }

    @Override
    public T getLow() {
        return low;
    }

    @Override
    public T getHigh() {
        return high;
    }

}
