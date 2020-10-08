/*
 * #%L
 * Fujion Clinical Framework
 * %%
 * Copyright (C) 2020 fujionclinical.org
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This Source Code Form is also subject to the terms of the Health-Related
 * Additional Disclaimer of Warranty and Limitation of Liability available at
 *
 *      http://www.fujionclinical.org/licensing/disclaimer
 *
 * #L%
 */
package org.fujionclinical.api.model.observation;

import edu.utah.kmm.model.cool.terminology.ConceptReference;
import edu.utah.kmm.model.cool.terminology.ConceptReferenceSet;
import org.fujionclinical.api.model.core.*;
import org.fujionclinical.api.model.impl.ValueWrapper;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ObservationComponent implements IObservationComponent {

    private final List<ConceptReferenceSet> interpretations = new ArrayList<>();

    private final List<IReferenceRange<Double>> referenceRanges = new ArrayList<>();

    private final ValueWrapper value = new ValueWrapper(String.class, Boolean.class, ConceptReference.class,
            LocalDateTime.class, Integer.class, IPeriod.class, IQuantity.class, IRange.class,
            IRatio.class, LocalTime.class);

    private ConceptReferenceSet code;

    private DataAbsentReason dataAbsentReason;

    @Override
    public ConceptReferenceSet getCode() {
        return code;
    }

    @Override
    public void setCode(ConceptReferenceSet code) {
        this.code = code;
    }

    @Override
    public List<ConceptReferenceSet> getInterpretations() {
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
    public void setValueAsConcept(ConceptReferenceSet value) {
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
    public void setValueAsDateTime(LocalDateTime value) {
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
