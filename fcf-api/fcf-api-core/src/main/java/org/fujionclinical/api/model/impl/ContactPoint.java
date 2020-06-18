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
