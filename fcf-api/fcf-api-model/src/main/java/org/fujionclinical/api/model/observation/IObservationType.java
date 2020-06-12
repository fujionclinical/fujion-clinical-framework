package org.fujionclinical.api.model.observation;

import org.fujion.common.CollectionUtil;
import org.fujionclinical.api.model.core.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public interface IObservationType extends IBaseType, IAnnotationType {

    public enum DataAbsentReason {
        UNKNOWN("Unknown"),                             // The value is expected to exist but is not known.
        ASKED_UNKNOWN("Asked But Unknown"),             // The source was asked but does not know the value.
        TEMP_UNKNOWN("Temporarily Unknown"),            // There is reason to expect (from the workflow) that the value may become known.
        NOT_ASKED("Not Asked"),                         // The workflow didn't lead to this value being known.
        ASKED_DECLINED("Asked But Declined"),           // The source was asked but declined to answer.
        MASKED("Masked"),                               // The information is not available due to security, privacy or related reasons.
        NOT_APPLICABLE("Not Applicable"),               // There is no proper value for this element (e.g. last menstrual period for a male).
        UNSUPPORTED("Unsupported"),                     // The source system wasn't capable of supporting this element.
        AS_TEXT("As Text"),                             // The content of the data is represented in the resource narrative.
        ERROR("Error"),                                 // Some system or workflow process error means that the information is not available.
        NOT_A_NUMBER("Not a Number (NaN)"),             // The numeric value is undefined or unrepresentable due to a floating point processing error.
        NEGATIVE_INFINITY("Negative Infinity (NINF)"),   // The numeric value is excessively low and unrepresentable due to a floating point processing error.
        POSITIVE_INFINITY("Positive Infinity (PINF)"),   // The numeric value is excessively high and unrepresentable due to a floating point processing error.
        NOT_PERFORMED("Not Performed"),                 // The value is not available because the observation procedure (test, etc.) was not performed.
        NOT_PERMITTED("Not Permitted");                 // The value is not permitted in this context (e.g. due to profiles, or the base data types).

        private final String displayText;

        DataAbsentReason(String displayText) {
            this.displayText = displayText;
        }

        @Override
        public String toString() {
            return displayText;
        }
    }

    IConcept getCode();

    default void setCode(IConcept concept) {
        notSupported();
    }

    default boolean hasCode() {
        return getCode() != null;
    }

    default List<IConcept> getInterpretations() {
        return Collections.emptyList();
    }

    default void setInterpretations(List<IConcept> value) {
        CollectionUtil.replaceList(getInterpretations(), value);
    }

    default void addInterpretations(IConcept... values) {
        Collections.addAll(getInterpretations(), values);
    }

    default boolean hasInterpretation() {
        return CollectionUtil.notEmpty(getInterpretations());
    }

    default IReferenceRange getReferenceRange() {
        return null;
    }

    default void setReferenceRange(IReferenceRange value) {
        notSupported();
    }

    default DataAbsentReason getDataAbsentReason() {
        return null;
    }

    default void setDataAbsentReason(DataAbsentReason value) {
        notSupported();
    }

    default boolean hasDataAbsentReason() {
        return getDataAbsentReason() != null;
    }

    boolean hasValue();

    default String getValueAsString() {
        return null;
    }

    default void setValueAsString(String value) {
        notSupported();
    }

    default boolean hasValueAsString() {
        return getValueAsString() != null;
    }

    default IConcept getValueAsConcept() {
        return null;
    }

    default void setValueAsConcept(IConcept value) {
        notSupported();
    }

    default boolean hasValueAsConcept() {
        return getValueAsConcept() != null;
    }

    default Boolean getValueAsBoolean() {
        return null;
    }

    default void setValueAsBoolean() {
        notSupported();
    }

    default boolean hasValueAsBoolean() {
        return getValueAsBoolean() != null;
    }

    default Integer getValueAsInteger() {
        return null;
    }

    default void setValueAsInteger(Integer value) {
        notSupported();
    }

    default Boolean hasValueAsInteger() {
        return getValueAsInteger() != null;
    }

    default Date getValueAsDate() {
        return null;
    }

    default void setValueAsDate(Date value) {
        notSupported();
    }

    default boolean hasValueAsDate() {
        return getValueAsDate() != null;
    }

    default IPeriod getValueAsPeriod() {
        return null;
    }

    default void setValueAsPeriod(IPeriod value) {
        notSupported();
    }

    default boolean hasValueAsPeriod() {
        return getValueAsPeriod() != null;
    }

    default IRange getValueAsRange() {
        return null;
    }

    default void setValueAsRange(IRange value) {
        notSupported();
    }

    default boolean hasValueAsRange() {
        return getValueAsRange() != null;
    }

    // TODO: quantity, ratio, sampleddata, time
}