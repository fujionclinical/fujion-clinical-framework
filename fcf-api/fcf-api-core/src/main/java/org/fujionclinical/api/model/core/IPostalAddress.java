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

import org.fujion.common.CollectionUtil;
import org.fujionclinical.api.core.CoreUtil;
import org.fujionclinical.api.query.expression.QueryParameter;

import java.util.Collections;
import java.util.List;

public interface IPostalAddress extends IBaseType {

    enum PostalAddressUse {
        HOME, WORK, TEMP, OLD, BILLING;

        @Override
        public String toString() {
            return CoreUtil.enumToString(this);
        }

    }

    enum PostalAddressType {
        POSTAL, PHYSICAL, BOTH;

        @Override
        public String toString() {
            return CoreUtil.enumToString(this);
        }

    }

    @QueryParameter
    PostalAddressUse getUse();

    default void setUse(PostalAddressUse use) {
        notSupported();
    }

    default boolean hasUse() {
        return getUse() != null;
    }

    @QueryParameter
    PostalAddressType getType();

    default void setType(PostalAddressType type) {
        notSupported();
    }

    default boolean hasType() {
        return getType() != null;
    }

    default List<String> getLines() {
        return Collections.emptyList();
    }

    default void setLines(List<String> lines) {
        notSupported();
    }

    default void addLines(String... lines) {
        Collections.addAll(getLines(), lines);
    }

    default boolean hasLines() {
        return CollectionUtil.notEmpty(getLines());
    }

    @QueryParameter
    String getCity();

    default void setCity(String city) {
        notSupported();
    }

    default boolean hasCity() {
        return getCity() != null;
    }

    String getDistrict();

    default void setDistrict(String district) {
        notSupported();
    }

    default boolean hasDistrict() {
        return getDistrict() != null;
    }

    @QueryParameter
    String getState();

    default void setState(String state) {
        notSupported();
    }

    default boolean hasState() {
        return getState() != null;
    }

    @QueryParameter
    String getPostalCode();

    default void setPostalCode(String postalCode) {
        notSupported();
    }

    default boolean hasPostalCode() {
        return getPostalCode() != null;
    }

    @QueryParameter
    String getCountry();

    default void setCountry(String country) {
        notSupported();
    }

    default boolean hasCountry() {
        return getCountry() != null;
    }

    IPeriod getPeriod();

    default void setPeriod(IPeriod period) {
        notSupported();
    }

    default boolean hasPeriod() {
        return getPeriod() != null;
    }

    default String asString() {
        return toString();
    }

}
