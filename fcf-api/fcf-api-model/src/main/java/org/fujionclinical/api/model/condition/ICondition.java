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

import org.fujion.common.DateTimeWrapper;
import org.fujionclinical.api.core.CoreUtil;
import org.fujionclinical.api.model.core.*;
import org.fujionclinical.api.model.encounter.IEncounter;
import org.fujionclinical.api.model.patient.IPatient;
import org.fujionclinical.api.model.person.IPerson;
import org.fujionclinical.api.query.QueryParameter;

public interface ICondition extends IDomainType, IAnnotationType {

    enum ClinicalStatus {
        ACTIVE, RECURRENCE, RELAPSE, INACTIVE, REMISSION, RESOLVED;

        @Override
        public String toString() {
            return CoreUtil.enumToString(this);
        }

    }

    enum VerificationStatus {
        UNCONFIRMED, PROVISIONAL, DIFFERENTIAL, CONFIRMED, REFUTED, ENTERED_IN_ERROR;

        @Override
        public String toString() {
            return CoreUtil.enumToString(this);
        }

    }

    enum Source {
        ENCOUNTER_DIAGNOSIS, PROBLEM_LIST, OTHER;

        @Override
        public String toString() {
            return CoreUtil.enumToString(this);
        }

    }

    enum Severity {
        MILD, MODERATE, SEVERE;

        @Override
        public String toString() {
            return CoreUtil.enumToString(this);
        }

    }

    @QueryParameter
    IReference<IPatient> getPatient();

    default void setPatient(IReference<IPatient> patient) {
        notSupported();
    }

    default boolean hasPatient() {
        return getPatient() != null;
    }

    @QueryParameter
    IPeriod getOnset();

    default void setOnset(IPeriod period) {
        notSupported();
    }

    default boolean hasOnset() {
        return getOnset() != null;
    }

    @QueryParameter
    DateTimeWrapper getRecordedDate();

    default void setRecordedDate(DateTimeWrapper recorded) {
        notSupported();
    }

    default boolean hasRecorded() {
        return getRecordedDate() != null;
    }

    @QueryParameter
    IReference<IPerson> getRecorder();

    default void setRecorder(IReference<IPerson> recorder) {
        notSupported();
    }

    default boolean hasRecorder() {
        return getRecorder() != null;
    }

    @QueryParameter
    IReference<IPerson> getAsserter();

    default void setAsserter(IReference<IPerson> asserter) {
        notSupported();
    }

    default boolean hasAsserter() {
        return getAsserter() != null;
    }

    @QueryParameter
    IConcept getCondition();

    default void setCondition(IConcept condition) {
        notSupported();
    }

    default boolean hasCondition() {
        return getCondition() != null;
    }

    @QueryParameter
    IReference<IEncounter> getEncounter();

    default void setEncounter(IReference<IEncounter> encounter) {
        notSupported();
    }

    default boolean hasEncounter() {
        return getEncounter() != null;
    }

    @QueryParameter
    ClinicalStatus getClinicalStatus();

    default void setClinicalStatus(ClinicalStatus clinicalStatus) {
        notSupported();
    }

    default boolean hasClinicalStatus() {
        return getClinicalStatus() != null;
    }

    @QueryParameter
    VerificationStatus getVerificationStatus();

    default void setVerificationStatus(VerificationStatus verificationStatus) {
        notSupported();
    }

    default boolean hasVerificationStatus() {
        return getVerificationStatus() != null;
    }

    @QueryParameter
    Severity getSeverity();

    default void setSeverity(Severity severity) {
        notSupported();
    }

    default boolean hasSeverity() {
        return getSeverity() != null;
    }
}
