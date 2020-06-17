package org.fujionclinical.api.model.impl;

import org.fujionclinical.api.model.core.IQuantity;
import org.fujionclinical.api.model.core.IRatio;

public class Ratio<T extends Number> implements IRatio<T> {

    private IQuantity<T> numerator;

    private IQuantity<T> denominator;

    @Override
    public IQuantity<T> getNumerator() {
        return numerator;
    }

    @Override
    public void setNumerator(IQuantity<T> value) {
        this.numerator = value;
    }

    @Override
    public IQuantity<T> getDenominator() {
        return denominator;
    }

    @Override
    public void setDenominator(IQuantity<T> value) {
        this.denominator = value;
    }

}
