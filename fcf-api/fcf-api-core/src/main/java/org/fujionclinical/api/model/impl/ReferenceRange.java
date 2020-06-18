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
