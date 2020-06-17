package org.fujionclinical.api.model.impl;

import org.fujionclinical.api.model.core.IConceptCode;
import org.fujionclinical.api.model.core.IDomainType;
import org.fujionclinical.api.model.core.IIdentifier;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseDomainType implements IDomainType {

    private String id;

    private final List<IIdentifier> identifiers = new ArrayList<>();

    private final List<IConceptCode> tags = new ArrayList<>();

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public List<IIdentifier> getIdentifiers() {
        return identifiers;
    }

    @Override
    public List<IConceptCode> getTags() {
        return tags;
    }

}
