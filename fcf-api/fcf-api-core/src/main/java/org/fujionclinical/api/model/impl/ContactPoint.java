package org.fujionclinical.api.model.impl;

import org.fujionclinical.api.model.core.IContactPoint;
import org.fujionclinical.api.model.core.IPeriod;

public class ContactPoint implements IContactPoint {

    private ContactPointSystem system;

    private ContactPointUse use;

    private Integer rank;

    private String value;

    private IPeriod period;

    @Override
    public ContactPointSystem getSystem() {
        return system;
    }

    @Override
    public void setSystem(ContactPointSystem system) {
        this.system = system;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public ContactPointUse getUse() {
        return use;
    }

    @Override
    public void setUse(ContactPointUse use) {
        this.use = use;
    }

    @Override
    public Integer getRank() {
        return rank;
    }

    @Override
    public void setRank(Integer rank) {
        this.rank = rank;
    }

    @Override
    public IPeriod getPeriod() {
        return period;
    }

    @Override
    public void setPeriod(IPeriod period) {
        this.period = period;
    }

}
