package org.fujionclinical.api.model.impl;

import org.fujionclinical.api.model.core.IDomainType;
import org.fujionclinical.api.model.core.IReference;
import org.fujionclinical.api.model.dao.DomainDAORegistry;
import org.fujionclinical.api.model.dao.IDomainDAO;

import java.util.Objects;

public class Reference<T extends IDomainType> implements IReference<T> {

    private final IDomainDAO<T> domainDAO;

    private T referenced;

    private String id;

    public Reference(
            Class<T> domainType,
            String id) {
        this.id = id;
        this.domainDAO = DomainDAORegistry.getDAO(domainType);
    }

    public Reference(T domainObject) {
        this((Class<T>) domainObject.getClass(), domainObject.getId());
        this.referenced = domainObject;
    }

    @Override
    public T getReferenced() {
        if (referenced == null && id != null) {
            referenced = domainDAO.read(id);
        }

        return referenced;
    }

    @Override
    public void setReferenced(T value) {
        this.referenced = value;
        this.id = value == null ? null : value.getId();
    }

    @Override
    public boolean hasReferenced() {
        return referenced != null;
    }

    @Override
    public Class<T> getDomainType() {
        return domainDAO.getDomainType();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String value) {
        if (!Objects.equals(id, value)) {
            id = value;
            referenced = null;
        }
    }

}
