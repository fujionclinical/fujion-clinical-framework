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
package org.fujionclinical.api.model.encounter;

import org.fujion.common.CollectionUtil;
import org.fujionclinical.api.model.core.IConcept;
import org.fujionclinical.api.model.core.IDomainObject;
import org.fujionclinical.api.model.core.IPeriod;
import org.fujionclinical.api.model.location.ILocation;
import org.fujionclinical.api.model.patient.IPatient;
import org.fujionclinical.api.model.person.IPerson;
import org.fujionclinical.api.query.QueryParameter;

import java.util.Collections;
import java.util.List;

public interface IEncounter extends IDomainObject {

    enum EncounterStatus {
        /**
         * The Encounter has not yet started.
         */
        PLANNED,
        /**
         * The Patient is present for the encounter, however is not currently meeting with a practitioner.
         */
        ARRIVED,
        /**
         * The patient has been assessed for the priority of their treatment based on the severity of their condition.
         */
        TRIAGED,
        /**
         * The Encounter has begun and the patient is present / the practitioner and the patient are meeting.
         */
        INPROGRESS,
        /**
         * The Encounter has begun, but the patient is temporarily on leave.
         */
        ONLEAVE,
        /**
         * The Encounter has ended.
         */
        FINISHED,
        /**
         * The Encounter has ended before it has begun.
         */
        CANCELLED,
        /**
         * This instance should not have been part of this patient's medical record.
         */
        ENTEREDINERROR,
        /**
         * The encounter status is unknown. Note that "unknown" is a value of last resort and every attempt should be made to provide a meaningful value other than "unknown".
         */
        UNKNOWN
    }

    @QueryParameter
    IPatient getPatient();

    default void setPatient(IPatient patient) {
        throw new UnsupportedOperationException();
    }

    default boolean hasPatient() {
        return getPatient() != null;
    }

    IPeriod getPeriod();

    default void setPeriod(IPeriod period) {
        throw new UnsupportedOperationException();
    }

    default boolean hasPeriod() {
        return getPeriod() != null;
    }

    @QueryParameter
    EncounterStatus getStatus();

    default void setStatus(EncounterStatus status) {
        throw new UnsupportedOperationException();
    }

    default boolean hasStatus() {
        return getStatus() != null;
    }

    default List<IPerson> getParticipants() {
        return Collections.emptyList();
    }

    default void setParticipants(List<IPerson> participants) {
        CollectionUtil.replaceList(getParticipants(), participants);
    }

    default void addParticipants(IPerson... participants) {
        Collections.addAll(getParticipants(), participants);
    }

    default boolean hasParticipant() {
        return CollectionUtil.notEmpty(getParticipants());
    }

    default List<ILocation> getLocations() {
        return Collections.emptyList();
    }

    default void setLocations(List<ILocation> locations) {
        CollectionUtil.replaceList(getLocations(), locations);
    }

    default void addLocations(ILocation... locations) {
        Collections.addAll(getLocations(), locations);
    }

    default boolean hasLocation() {
        return CollectionUtil.notEmpty(getLocations());
    }

    List<IConcept> getTypes();

    default IEncounter addTypes(IConcept... types) {
        Collections.addAll(getTypes(), types);
        return this;
    }

    default IEncounter setType(List<IConcept> types) {
        CollectionUtil.replaceList(getTypes(), types);
        return this;
    }

    default boolean hasType() {
        return CollectionUtil.notEmpty(getTypes());
    }

}
