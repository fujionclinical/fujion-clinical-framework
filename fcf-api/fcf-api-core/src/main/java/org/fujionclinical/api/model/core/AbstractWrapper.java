package org.fujionclinical.api.model.core;

public abstract class AbstractWrapper<T> implements IWrapper<T> {

    private final T wrapped;

    protected AbstractWrapper(T wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public T getWrapped() {
        return wrapped;
    }

}
