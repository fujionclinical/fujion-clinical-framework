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
package org.fujionclinical.api.model.condition;

import org.fujionclinical.api.model.core.IAnnotationType;
import org.fujionclinical.api.model.core.IConcept;
import org.fujionclinical.api.model.core.IDomainObject;
import org.fujionclinical.api.model.core.IPeriod;
import org.fujionclinical.api.model.encounter.IEncounter;
import org.fujionclinical.api.model.patient.IPatient;
import org.fujionclinical.api.model.person.IPerson;
import org.fujionclinical.api.query.QueryParameter;

import java.util.Date;

public interface ICondition extends IDomainObject, IAnnotationType {

    enum ClinicalStatus {
        ACTIVE, RECURRENCE, RELAPSE, INACTIVE, REMISSION, RESOLVED
    }

    enum VerificationStatus {
        UNCONFIRMED, PROVISIONAL, DIFFERENTIAL, CONFIRMED, REFUTED, ENTERED_IN_ERROR
    }

    enum Source {
        ENCOUNTER_DIAGNOSIS, PROBLEM_LIST, OTHER
    }

    enum Severity {
        MILD, MODERATE, SEVERE
    }

    @QueryParameter
    IPatient getPatient();

    default void setPatient(IPatient patient) {
        throw new UnsupportedOperationException();
    }

    default boolean hasPatient() {
        return getPatient() != null;
    }

    @QueryParameter
    IPeriod getOnset();

    default void setOnset(IPeriod period) {
        throw new UnsupportedOperationException();
    }

    default boolean hasOnset() {
        return getOnset() != null;
    }

    @QueryParameter
    Date getRecorded();

    default void setRecorded(Date recorded) {
        throw new UnsupportedOperationException();
    }

    default boolean hasRecorded() {
        return getRecorded() != null;
    }

    @QueryParameter
    IPerson getRecorder();

    default void setRecorder(IPerson recorder) {
        throw new UnsupportedOperationException();
    }

    default boolean hasRecorder() {
        return getRecorder() != null;
    }

    @QueryParameter
    IPerson getAsserter();

    default void setAsserter(IPerson asserter) {
        throw new UnsupportedOperationException();
    }

    default boolean hasAsserter() {
        return getAsserter() != null;
    }

    @QueryParameter
    IConcept getCondition();

    default void setCondition(IConcept condition) {
        throw new UnsupportedOperationException();
    }

    default boolean hasCondition() {
        return getCondition() != null;
    }

    @QueryParameter
    IEncounter getEncounter();

    default void setEncounter(IEncounter encounter) {
        throw new UnsupportedOperationException();
    }

    default boolean hasEncounter() {
        return getEncounter() != null;
    }

    @QueryParameter
    ClinicalStatus getClinicalStatus();

    default void setClinicalStatus(ClinicalStatus clinicalStatus) {
        throw new UnsupportedOperationException();
    }

    default boolean hasClinicalStatus() {
        return getClinicalStatus() != null;
    }

    @QueryParameter
    VerificationStatus getVerificationStatus();

    default void setVerificationStatus(VerificationStatus verificationStatus) {
        throw new UnsupportedOperationException();
    }

    default boolean hasVerificationStatus() {
        return getVerificationStatus() != null;
    }

    @QueryParameter
    Severity getSeverity();

    default void setSeverity(Severity severity) {
        throw new UnsupportedOperationException();
    }

    default boolean hasSeverity() {
        return getSeverity() != null;
    }
}
