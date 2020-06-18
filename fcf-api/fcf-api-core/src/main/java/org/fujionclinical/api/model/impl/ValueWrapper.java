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

import org.fujion.common.MiscUtil;
import org.springframework.util.Assert;

public class ValueWrapper {

    private final Class<?>[] supportedClasses;

    private Object value;

    public ValueWrapper(Class<?>... supportedClasses) {
        this.supportedClasses = supportedClasses;
    }

    public Object getValue() {
        return value;
    }

    public <T> T getValue(Class<T> type) {
        return value == null ? null : MiscUtil.castTo(value, type);
    }

    public void setValue(Object value) {
        Assert.isTrue(isValidValue(value), () -> "Invalid data type for value: '" + value.getClass() + "'");
        this.value = value;
    }

    public boolean hasValue() {
        return value != null;
    }

    public boolean isValidValue(Object value) {
        return value == null || MiscUtil.firstAssignable(value.getClass(), supportedClasses) != null;
    }

}
