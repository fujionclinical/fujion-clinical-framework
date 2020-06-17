package org.fujionclinical.api.model.impl;

import org.fujionclinical.api.model.core.IConceptCode;
import org.fujionclinical.api.model.core.IQuantity;

public class Quantity<T extends Number> implements IQuantity<T> {

    private T value;

    private QuantityComparator comparator;

    private IConceptCode unit;

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public QuantityComparator getComparator() {
        return comparator;
    }

    @Override
    public void setComparator(QuantityComparator value) {
        this.comparator = value;
    }

    @Override
    public IConceptCode getUnit() {
        return unit;
    }

    @Override
    public void setUnit(IConceptCode value) {
        this.unit = value;
    }

}
