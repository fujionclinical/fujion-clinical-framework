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

import org.apache.commons.lang.StringUtils;
import org.fujion.common.CollectionUtil;

import java.util.Collections;
import java.util.List;

public interface IPersonName {

    enum PersonNameUse {
        USUAL, OFFICIAL, TEMP, NICKNAME, ANONYMOUS, OLD, MAIDEN, ANY
    }

    String getFamilyName();

    default void setFamilyName(String familyName) {
        throw new UnsupportedOperationException();
    }

    default boolean hasFamilyName() {
        return !StringUtils.isEmpty(getFamilyName());
    }

    List<String> getGivenNames();

    default void setGivenNames(List<String> givenNames) {
        CollectionUtil.replaceList(getGivenNames(), givenNames);
    }

    default void addGivenNames(String... givenNames) {
        Collections.addAll(getGivenNames(), givenNames);
    }

    default boolean hasGivenName() {
        return CollectionUtil.notEmpty(getGivenNames());
    }

    List<String> getPrefixes();

    default void setPrefixes(List<String> prefixes) {
        CollectionUtil.replaceList(getPrefixes(), prefixes);
    }

    default void addPrefixes(String... prefixes) {
        Collections.addAll(getPrefixes(), prefixes);
    }

    default boolean hasPrefix() {
        return CollectionUtil.notEmpty(getPrefixes());
    }

    List<String> getSuffixes();

    default void setSuffixes(List<String> suffixes) {
        CollectionUtil.replaceList(getSuffixes(), suffixes);
    }

    default void addSuffixes(String... suffixes) {
        Collections.addAll(getSuffixes(), suffixes);
    }

    default boolean hasSuffix() {
        return CollectionUtil.isEmpty(getSuffixes());
    }

    PersonNameUse getUse();

    default void setUse(PersonNameUse use) {
        throw new UnsupportedOperationException();
    }

    default boolean hasUse() {
        return getUse() != null;
    }

    default void fromString(String value) {
        PersonNameParser.instance.fromString(value, this);
    }

    default String asString() {
        return PersonNameParser.instance.toString(this);
    }

}
