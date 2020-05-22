package org.fujionclinical.api.encounter;

import org.apache.commons.collections.CollectionUtils;
import org.fujion.common.MiscUtil;
import org.fujionclinical.api.location.ILocation;
import org.fujionclinical.api.model.IPeriod;
import org.fujionclinical.api.model.IDomainObject;
import org.fujionclinical.api.model.IPerson;

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

    IPeriod getPeriod();

    default IEncounter setPeriod(IPeriod period) {
        throw new UnsupportedOperationException();
    }

    default boolean hasPeriod() {
        return getPeriod() != null;
    }

    EncounterStatus getStatus();

    default IEncounter setStatus(EncounterStatus status) {
        throw new UnsupportedOperationException();
    }

    default boolean hasStatus() {
        return getStatus() != null;
    }

    default List<IPerson> getParticipants() {
        return Collections.emptyList();
    }

    default IEncounter setParticipants(List<IPerson> participants) {
        MiscUtil.replaceList(getParticipants(), participants);
        return this;
    }

    default IEncounter addParticipants(IPerson... participants) {
        Collections.addAll(getParticipants(), participants);
        return this;
    }

    default boolean hasParticipant() {
        return !CollectionUtils.isEmpty(getParticipants());
    }

    ILocation getLocation();

    default IEncounter setLocation(ILocation location) {
        throw new UnsupportedOperationException();
    }

    default boolean hasLocation() {
        return getLocation() != null;
    }
}
