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
package org.fujionclinical.api.model.observation;

import edu.utah.kmm.model.cool.core.BaseType;
import edu.utah.kmm.model.cool.core.datatype.Period;
import edu.utah.kmm.model.cool.core.datatype.QuantityEx;
import edu.utah.kmm.model.cool.core.datatype.Ratio;
import edu.utah.kmm.model.cool.terminology.ConceptReferenceSet;
import org.fujion.common.CollectionUtil;
import org.fujion.common.MiscUtil;
import org.fujionclinical.api.core.CoreUtil;
import org.fujionclinical.api.model.core.IRange;
import org.fujionclinical.api.model.core.IReferenceRange;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

public interface IObservationComponent extends BaseType {

    enum DataAbsentReason {
        UNKNOWN,                              // The value is expected to exist but is not known.
        ASKED_UNKNOWN("asked but unknown"),   // The source was asked but does not know the value.
        TEMP_UNKNOWN("temporarily unknown"),  // There is reason to expect (from the workflow) that the value may become known.
        NOT_ASKED,                            // The workflow didn't lead to this value being known.
        ASKED_DECLINED("asked but declined"), // The source was asked but declined to answer.
        MASKED,                               // The information is not available due to security, privacy or related reasons.
        NOT_APPLICABLE,                       // There is no proper value for this element (e.g. last menstrual period for a male).
        UNSUPPORTED,                          // The source system wasn't capable of supporting this element.
        AS_TEXT,                              // The content of the data is represented in the resource narrative.
        ERROR,                                // Some system or workflow process error means that the information is not available.
        NOT_A_NUMBER,                         // The numeric value is undefined or unrepresentable due to a floating point processing error.
        NEGATIVE_INFINITY,                    // The numeric value is excessively low and unrepresentable due to a floating point processing error.
        POSITIVE_INFINITY,                    // The numeric value is excessively high and unrepresentable due to a floating point processing error.
        NOT_PERFORMED,                        // The value is not available because the observation procedure (test, etc.) was not performed.
        NOT_PERMITTED;                        // The value is not permitted in this context (e.g. due to profiles, or the base data types).

        private final String displayText;

        DataAbsentReason() {
            this.displayText = CoreUtil.enumToString(this);
        }

        DataAbsentReason(String displayText) {
            this.displayText = displayText;
        }

        @Override
        public String toString() {
            return displayText;
        }
    }

    ConceptReferenceSet getCode();

    default void setCode(ConceptReferenceSet concept) {
        notSupported();
    }

    default boolean hasCode() {
        return getCode() != null;
    }

    default List<ConceptReferenceSet> getInterpretations() {
        return Collections.emptyList();
    }

    default void setInterpretations(List<ConceptReferenceSet> value) {
        CollectionUtil.replaceElements(getInterpretations(), value);
    }

    default void addInterpretations(ConceptReferenceSet... values) {
        Collections.addAll(getInterpretations(), values);
    }

    default boolean hasInterpretation() {
        return CollectionUtil.notEmpty(getInterpretations());
    }

    default List<IReferenceRange<Double>> getReferenceRanges() {
        return Collections.emptyList();
    }

    default void setReferenceRanges(List<IReferenceRange<Double>> values) {
        CollectionUtil.replaceElements(getReferenceRanges(), values);
    }

    default void addReferenceRanges(IReferenceRange<Double>... values) {
        Collections.addAll(getReferenceRanges(), values);
    }

    default boolean hasReferenceRange() {
        return CollectionUtil.notEmpty(getReferenceRanges());
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

    Object getValue();

    default void setValue(Object value) {
        notSupported();
    }

    default boolean hasValue() {
        return getValue() != null;
    }

    default String getValueAsString() {
        return MiscUtil.castTo(getValue(), String.class);
    }

    default void setValueAsString(String value) {
        notSupported();
    }

    default boolean hasValueAsString() {
        return getValueAsString() != null;
    }

    default ConceptReferenceSet getValueAsConcept() {
        return MiscUtil.castTo(getValue(), ConceptReferenceSet.class);
    }

    default void setValueAsConcept(ConceptReferenceSet value) {
        notSupported();
    }

    default boolean hasValueAsConcept() {
        return getValueAsConcept() != null;
    }

    default Boolean getValueAsBoolean() {
        return MiscUtil.castTo(getValue(), Boolean.class);
    }

    default void setValueAsBoolean(Boolean value) {
        notSupported();
    }

    default boolean hasValueAsBoolean() {
        return getValueAsBoolean() != null;
    }

    default Integer getValueAsInteger() {
        return MiscUtil.castTo(getValue(), Integer.class);
    }

    default void setValueAsInteger(Integer value) {
        notSupported();
    }

    default Boolean hasValueAsInteger() {
        return getValueAsInteger() != null;
    }

    default LocalDateTime getValueAsDateTime() {
        return MiscUtil.castTo(getValue(), LocalDateTime.class);
    }

    default void setValueAsDateTime(LocalDateTime value) {
        notSupported();
    }

    default boolean hasValueAsDateTime() {
        return getValueAsDateTime() != null;
    }

    default LocalTime getValueAsTime() {
        return MiscUtil.castTo(getValue(), LocalTime.class);
    }

    default void setValueAsTime(LocalTime value) {
        notSupported();
    }

    default boolean hasValueAsTime() {
        return getValueAsTime() != null;
    }

    default Period getValueAsPeriod() {
        return MiscUtil.castTo(getValue(), Period.class);
    }

    default void setValueAsPeriod(Period value) {
        notSupported();
    }

    default boolean hasValueAsPeriod() {
        return getValueAsPeriod() != null;
    }

    default IRange getValueAsRange() {
        return MiscUtil.castTo(getValue(), IRange.class);
    }

    default void setValueAsRange(IRange value) {
        notSupported();
    }

    default boolean hasValueAsRange() {
        return getValueAsRange() != null;
    }

    default QuantityEx<Double> getValueAsQuantity() {
        return MiscUtil.castTo(getValue(), QuantityEx.class);
    }

    default void setValueAsQuantity(QuantityEx<Double> value) {
        notSupported();
    }

    default boolean hasValueAsQuantity() {
        return getValueAsQuantity() != null;
    }

    default Ratio<Double> getValueAsRatio() {
        return MiscUtil.castTo(getValue(), Ratio.class);
    }

    default void setValueAsRatio(Ratio<Double> value) {
        notSupported();
    }

    default boolean hasValueAsRatio() {
        return getValueAsRatio() != null;
    }

}
