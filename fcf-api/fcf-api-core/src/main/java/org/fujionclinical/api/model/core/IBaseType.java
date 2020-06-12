package org.fujionclinical.api.model.core;

/**
 * Base interface for model objects.
 */
public interface IBaseType {

    /**
     * Called by methods that are not supported to throw the appropriate exception.
     *
     * @throws UnsupportedOperationException Always thrown.
     */
    default <T> T notSupported() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

}
