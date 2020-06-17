package org.fujionclinical.api.model.encounter;

import org.fujionclinical.api.model.core.IConcept;
import org.fujionclinical.api.model.core.IPeriod;
import org.fujionclinical.api.model.core.IReference;
import org.fujionclinical.api.model.impl.BaseDomainType;
import org.fujionclinical.api.model.location.ILocation;
import org.fujionclinical.api.model.patient.IPatient;
import org.fujionclinical.api.model.person.IPerson;

import java.util.ArrayList;
import java.util.List;

public class Encounter extends BaseDomainType implements IEncounter {

    private final List<IReference<IPerson>> participants = new ArrayList<>();

    private final List<IReference<ILocation>> locations = new ArrayList<>();

    private final List<IConcept> types = new ArrayList<>();

    private IReference<IPatient> patient;

    private IPeriod period;

    private EncounterStatus status;

    @Override
    public IReference<IPatient> getPatient() {
        return patient;
    }

    @Override
    public void setPatient(IReference<IPatient> patient) {
        this.patient = patient;
    }

    @Override
    public IPeriod getPeriod() {
        return period;
    }

    @Override
    public void setPeriod(IPeriod period) {
        this.period = period;
    }

    @Override
    public EncounterStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(EncounterStatus status) {
        this.status = status;
    }

    @Override
    public List<IReference<IPerson>> getParticipants() {
        return participants;
    }

    @Override
    public List<IReference<ILocation>> getLocations() {
        return locations;
    }

    @Override
    public List<IConcept> getTypes() {
        return types;
    }

}
