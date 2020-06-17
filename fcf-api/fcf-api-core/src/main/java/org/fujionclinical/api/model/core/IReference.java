package org.fujionclinical.api.model.core;

public interface IReference<T extends IDomainType> extends IBaseType {

    T getReferenced();

    boolean hasReferenced();

    Class<T> getDomainType();

    String getId();

    default void setId(String value) {
        notSupported();
    }

    default void setReferenced(T value) {
        notSupported();
    }

}
