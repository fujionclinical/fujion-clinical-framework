package org.fujionclinical.api.model;

public interface IWrapper<T> {

    /**
     * Returns the wrapped object.
     *
     * @return The wrapped object.
     */
    T getWrapped();

}
