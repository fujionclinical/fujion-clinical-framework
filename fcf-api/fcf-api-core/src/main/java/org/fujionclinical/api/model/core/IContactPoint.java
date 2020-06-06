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
package org.fujionclinical.api.model.core;

public interface IContactPoint {

    enum ContactPointSystem {
        PHONE, FAX, EMAIL, PAGER, URL, SMS, OTHER
    }

    enum ContactPointUse {
        HOME, WORK, TEMP, OLD, MOBILE
    }

    ContactPointSystem getSystem();

    default void setSystem(ContactPointSystem system) {
        throw new UnsupportedOperationException();
    }

    default boolean hasSystem() {
        return getSystem() != null;
    }

    String getValue();

    default void setValue(String value) {
        throw new UnsupportedOperationException();
    }

    default boolean hasValue() {
        return getValue() != null;
    }

    default ContactPointUse getUse() {
        return null;
    }

    default void setUse(ContactPointUse use) {
        throw new UnsupportedOperationException();
    }

    default boolean hasUse() {
        return getUse() != null;
    }

    default Integer getRank() {
        return 0;
    }

    default void setRank(Integer rank) {
        throw new UnsupportedOperationException();
    }

    default boolean hasRank() {
        return getRank() != null;
    }

    default IPeriod getPeriod() {
        return null;
    }

    default void setPeriod(IPeriod period) {
        throw new UnsupportedOperationException();
    }

    default boolean hasPeriod() {
        return getPeriod() != null;
    }
}