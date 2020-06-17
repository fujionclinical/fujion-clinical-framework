package org.fujionclinical.api.model.observation;

import org.fujion.common.DateTimeWrapper;
import org.fujionclinical.api.model.core.IConcept;
import org.fujionclinical.api.model.core.IConceptCode;
import org.fujionclinical.api.model.core.IIdentifier;
import org.fujionclinical.api.model.encounter.IEncounter;
import org.fujionclinical.api.model.patient.IPatient;

import java.util.ArrayList;
import java.util.List;

public class Observation extends ObservationComponent implements IObservation {

    private final List<IObservationComponent> components = new ArrayList<>();

    private final List<IConceptCode> tags = new ArrayList<>();

    private final List<IConcept> categories = new ArrayList<>();

    private final List<IIdentifier> identifiers = new ArrayList<>();

    private DateTimeWrapper effectiveDate;

    private ObservationStatus status;

    private IPatient patient;

    private IEncounter encounter;

    private String id;

    @Override
    public DateTimeWrapper getEffectiveDate() {
        return effectiveDate;
    }

    @Override
    public void setEffectiveDate(DateTimeWrapper effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    @Override
    public ObservationStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(ObservationStatus status) {
        this.status = status;
    }

    @Override
    public IPatient getPatient() {
        return patient;
    }

    @Override
    public void setPatient(IPatient patient) {
        this.patient = patient;
    }

    @Override
    public IEncounter getEncounter() {
        return encounter;
    }

    @Override
    public void setEncounter(IEncounter encounter) {
        this.encounter = encounter;
    }

    @Override
    public List<IObservationComponent> getComponents() {
        return components;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public List<IIdentifier> getIdentifiers() {
        return identifiers;
    }

    @Override
    public List<IConceptCode> getTags() {
        return tags;
    }

    @Override
    public List<IConcept> getCategories() {
        return categories;
    }

}
