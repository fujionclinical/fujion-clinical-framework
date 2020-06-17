package org.fujionclinical.api.model.impl;

import org.fujionclinical.api.model.core.IConcept;
import org.fujionclinical.api.model.core.IQuantity;
import org.fujionclinical.api.model.core.IRange;
import org.fujionclinical.api.model.core.IReferenceRange;

import java.util.ArrayList;
import java.util.List;

public class ReferenceRange<T extends Number> implements IReferenceRange<T> {

    private IQuantity<T> low;

    private IQuantity<T> high;

    private IConcept type;

    private String description;

    private IRange<Double> ageRange;

    private final List<IConcept> appliesTo = new ArrayList<>();

    @Override
    public IQuantity<T> getLow() {
        return low;
    }

    @Override
    public void setLow(IQuantity<T> value) {
        this.low = value;
    }

    @Override
    public IQuantity<T> getHigh() {
        return high;
    }

    @Override
    public void setHigh(IQuantity<T> value) {
        this.high = value;
    }

    @Override
    public IConcept getType() {
        return type;
    }

    @Override
    public void setType(IConcept value) {
        this.type = value;
    }

    @Override
    public List<IConcept> getAppliesTo() {
        return appliesTo;
    }

    @Override
    public IRange<Double> getAgeRange() {
        return ageRange;
    }

    @Override
    public void setAgeRange(IRange<Double> value) {
        this.ageRange = value;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String value) {
        this.description = value;
    }

}
