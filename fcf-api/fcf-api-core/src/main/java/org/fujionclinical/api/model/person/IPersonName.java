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

import edu.utah.kmm.model.cool.core.BaseType;
import edu.utah.kmm.model.cool.core.datatype.Period;
import edu.utah.kmm.model.cool.core.query.QueryParameter;
import edu.utah.kmm.model.cool.foundation.datatype.PersonName;
import edu.utah.kmm.model.cool.foundation.datatype.PersonNameUse;
import org.apache.commons.lang3.StringUtils;
import org.fujion.common.CollectionUtil;

import java.util.Collections;
import java.util.List;

public interface IPersonName extends PersonName, BaseType {

    @QueryParameter
    @Override
    String getFamily();

    @Override
    default void setFamily(String familyName) {
        notSupported();
    }

    @Override
    default boolean hasFamily() {
        return !StringUtils.isEmpty(getFamily());
    }

    @QueryParameter
    @Override
    List<String> getGiven();

    @Override
    default void setGiven(List<String> givenNames) {
        CollectionUtil.replaceElements(getGiven(), givenNames);
    }

    @Override
    default void addGiven(String... givenNames) {
        Collections.addAll(getGiven(), givenNames);
    }

    @Override
    default boolean hasGiven() {
        return CollectionUtil.notEmpty(getGiven());
    }

    @Override
    List<String> getPrefixes();

    @Override
    default void setPrefixes(List<String> prefixes) {
        CollectionUtil.replaceElements(getPrefixes(), prefixes);
    }

    @Override
    default void addPrefixes(String... prefixes) {
        Collections.addAll(getPrefixes(), prefixes);
    }

    @Override
    default boolean hasPrefixes() {
        return CollectionUtil.notEmpty(getPrefixes());
    }

    @Override
    List<String> getSuffixes();

    @Override
    default void setSuffixes(List<String> suffixes) {
        CollectionUtil.replaceElements(getSuffixes(), suffixes);
    }

    @Override
    default void addSuffixes(String... suffixes) {
        Collections.addAll(getSuffixes(), suffixes);
    }

    @Override
    default boolean hasSuffixes() {
        return CollectionUtil.notEmpty(getSuffixes());
    }

    @Override
    PersonNameUse getUse();

    @Override
    default void setUse(PersonNameUse use) {
        notSupported();
    }

    @Override
    default boolean hasUse() {
        return getUse() != null;
    }

    @Override
    Period getPeriodOfUse();

    @Override
    default void setPeriodOfUse(Period periodOfUse) {
        notSupported();
    }

    @Override
    default boolean hasPeriodOfUse() {
        return getPeriodOfUse() != null;
    }

    @Override
    String getText();

    @Override
    default void setText(String text) {
        notSupported();
    }

    @Override
    default boolean hasText() {
        return getText() != null;
    }

    default void fromString(String value) {
        PersonNameParser.instance.fromString(value, this);
    }

    default String asString() {
        return PersonNameParser.instance.toString(this);
    }

}
