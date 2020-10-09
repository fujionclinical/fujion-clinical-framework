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
package org.fujionclinical.api.model.person;

import edu.utah.kmm.model.cool.core.datatype.Period;
import edu.utah.kmm.model.cool.foundation.datatype.PersonNameUse;

import java.util.ArrayList;
import java.util.List;

public class PersonNameImpl implements IPersonName {

    private String text;

    private String familyName;

    private PersonNameUse category;

    private Period periodOfUse;

    private final List<String> givenNames = new ArrayList<>();

    private final List<String> prefixes = new ArrayList<>();

    private final List<String> suffixes = new ArrayList<>();

    @Override
    public String getFamily() {
        return familyName;
    }

    @Override
    public void setFamily(String familyName) {
        this.familyName = familyName;
    }

    @Override
    public List<String> getGiven() {
        return givenNames;
    }

    @Override
    public List<String> getPrefixes() {
        return prefixes;
    }

    @Override
    public List<String> getSuffixes() {
        return suffixes;
    }

    @Override
    public Period getPeriodOfUse() {
        return periodOfUse;
    }

    @Override
    public void setPeriodOfUse(Period periodOfUse) {
        this.periodOfUse = periodOfUse;
    }

    @Override
    public PersonNameUse getUse() {
        return category;
    }

    @Override
    public void setUse(PersonNameUse category) {
        this.category = category;
    }

    @Override
    public String getText() {
        return text == null ? asString() : text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
        PersonNameParser.instance.fromString(text, this);
    }

    @Override
    public String toString() {
        return asString();
    }
}
