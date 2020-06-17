package org.fujionclinical.api.model.location;

import org.fujionclinical.api.model.core.IConcept;
import org.fujionclinical.api.model.core.IContactPoint;
import org.fujionclinical.api.model.core.IPostalAddress;
import org.fujionclinical.api.model.impl.BaseDomainType;

import java.util.ArrayList;
import java.util.List;

public class Location extends BaseDomainType implements ILocation {

    private final List<String> aliases = new ArrayList<>();

    private final List<IConcept> types = new ArrayList<>();

    private final List<IContactPoint> contactPoints = new ArrayList<>();

    private final List<IPostalAddress> addresses = new ArrayList<>();

    private LocationStatus status;

    private String name;

    private String description;

    @Override
    public LocationStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(LocationStatus status) {
        this.status = status;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public List<IConcept> getTypes() {
        return types;
    }

    @Override
    public List<IContactPoint> getContactPoints() {
        return contactPoints;
    }

    @Override
    public List<IPostalAddress> getAddresses() {
        return addresses;
    }

}
