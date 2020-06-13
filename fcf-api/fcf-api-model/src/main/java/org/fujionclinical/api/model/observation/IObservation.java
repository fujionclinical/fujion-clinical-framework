package org.fujionclinical.api.model.observation;

import org.fujion.common.CollectionUtil;
import org.fujion.common.DateTimeWrapper;
import org.fujionclinical.api.model.core.ICategoryType;
import org.fujionclinical.api.model.core.IDomainType;
import org.fujionclinical.api.model.encounter.IEncounter;
import org.fujionclinical.api.model.patient.IPatient;

import java.util.Collections;
import java.util.List;

public interface IObservation extends IDomainType, IObservationType, ICategoryType {

    enum ObservationStatus {REGISTERED, PRELIMINARY, FINAL, AMENDED, CORRECTED, CANCELLED, ENTERED_IN_ERROR, UNKNOWN}

    DateTimeWrapper getEffectiveDate();

    default void setEffectiveDate(DateTimeWrapper value) {
        notSupported();
    }

    default boolean hasEffectiveDate() {
        return getEffectiveDate() != null;
    }

    ObservationStatus getStatus();

    default void setStatus(ObservationStatus value) {
        notSupported();
    }

    default boolean hasStatus() {
        return getStatus() != null;
    }

    IPatient getPatient();

    default void setPatient(IPatient value) {
        notSupported();
    }

    default boolean hasPatient() {
        return getPatient() != null;
    }

    IEncounter getEncounter();

    default void setEncounter(IEncounter value) {
        notSupported();
    }

    default boolean hasEncounter() {
        return getEncounter() != null;
    }

    default List<IObservationType> getComponents() {
        return Collections.emptyList();
    }

    default void setComponents(List<IObservationType> values) {
        CollectionUtil.replaceList(getComponents(), values);
    }

    default void addComponents(IObservationType... values) {
        Collections.addAll(getComponents(), values);
    }

    default boolean hasComponent() {
        return CollectionUtil.notEmpty(getComponents());
    }
}
