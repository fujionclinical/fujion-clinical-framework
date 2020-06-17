package org.fujionclinical.api.model.observation;

import org.fujion.common.DateTimeWrapper;
import org.fujionclinical.api.model.core.*;
import org.fujionclinical.api.model.impl.ValueWrapper;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ObservationComponent implements IObservationComponent {

    private final List<IConcept> interpretations = new ArrayList<>();

    private final List<IReferenceRange<Double>> referenceRanges = new ArrayList<>();

    private final ValueWrapper value = new ValueWrapper(String.class, Boolean.class, IConcept.class,
            DateTimeWrapper.class, Integer.class, IPeriod.class, IQuantity.class, IRange.class,
            IRatio.class, LocalTime.class);

    private IConcept code;

    private DataAbsentReason dataAbsentReason;

    @Override
    public IConcept getCode() {
        return code;
    }

    @Override
    public void setCode(IConcept code) {
        this.code = code;
    }

    @Override
    public List<IConcept> getInterpretations() {
        return interpretations;
    }

    @Override
    public List<IReferenceRange<Double>> getReferenceRanges() {
        return referenceRanges;
    }

    @Override
    public DataAbsentReason getDataAbsentReason() {
        return dataAbsentReason;
    }

    @Override
    public void setDataAbsentReason(DataAbsentReason dataAbsentReason) {
        this.dataAbsentReason = dataAbsentReason;
    }

    @Override
    public Object getValue() {
        return value.getValue();
    }

    public void setValue(Object value) {
        this.value.setValue(value);
    }

    @Override
    public void setValueAsString(String value) {
        setValue(value);
    }

    @Override
    public void setValueAsConcept(IConcept value) {
        setValue(value);
    }

    @Override
    public void setValueAsBoolean(Boolean value) {
        setValue(value);
    }

    @Override
    public void setValueAsInteger(Integer value) {
        setValue(value);
    }

    @Override
    public void setValueAsDateTime(DateTimeWrapper value) {
        setValue(value);
    }

    @Override
    public void setValueAsTime(LocalTime value) {
        setValue(value);
    }

    @Override
    public void setValueAsPeriod(IPeriod value) {
        setValue(value);
    }

    @Override
    public void setValueAsRange(IRange value) {
        setValue(value);
    }

    @Override
    public void setValueAsQuantity(IQuantity<Double> value) {
        setValue(value);
    }

    @Override
    public void setValueAsRatio(IRatio<Double> value) {
        setValue(value);
    }

}
