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

import edu.utah.kmm.model.cool.core.datatype.Period;
import org.fujionclinical.api.model.core.IPostalAddress;

import java.util.ArrayList;
import java.util.List;

public class PostalAddressImpl implements IPostalAddress {

    private final List<String> lines = new ArrayList<>();

    private PostalAddressUse use;

    private PostalAddressType type;

    private String city;

    private String district;

    private String state;

    private String postalCode;

    private String country;

    private Period period;

    @Override
    public PostalAddressUse getUse() {
        return use;
    }

    @Override
    public void setUse(PostalAddressUse use) {
        this.use = use;
    }

    @Override
    public PostalAddressType getType() {
        return type;
    }

    @Override
    public void setType(PostalAddressType type) {
        this.type = type;
    }

    @Override
    public List<String> getLines() {
        return lines;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String getDistrict() {
        return district;
    }

    @Override
    public void setDistrict(String district) {
        this.district = district;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String getPostalCode() {
        return postalCode;
    }

    @Override
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public Period getPeriod() {
        return period;
    }

    @Override
    public void setPeriod(Period period) {
        this.period = period;
    }

    @Override
    public String toString() {
        return asString();
    }

}
