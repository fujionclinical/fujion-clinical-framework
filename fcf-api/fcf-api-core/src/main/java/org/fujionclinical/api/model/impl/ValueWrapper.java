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
