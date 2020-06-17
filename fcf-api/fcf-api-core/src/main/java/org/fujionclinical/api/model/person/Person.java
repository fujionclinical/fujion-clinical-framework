package org.fujionclinical.api.model.person;

import org.fujion.common.DateTimeWrapper;
import org.fujionclinical.api.model.core.IAttachment;
import org.fujionclinical.api.model.core.IConcept;
import org.fujionclinical.api.model.core.IContactPoint;
import org.fujionclinical.api.model.core.IPostalAddress;
import org.fujionclinical.api.model.impl.BaseDomainType;
import org.fujionclinical.api.model.impl.ConceptCode;

import java.util.ArrayList;
import java.util.List;

public abstract class Person extends BaseDomainType implements IPerson {

    private final List<IPersonName> names = new ArrayList<>();

    private final List<IAttachment> photos = new ArrayList<>();

    private final List<IConcept> languages = new ArrayList<>();

    private final List<IContactPoint> contactPoints = new ArrayList<>();

    private final List<IPostalAddress> addresses = new ArrayList<>();

    private Gender gender;

    private ConceptCode birthSex;

    private ConceptCode ethnicity;

    private MaritalStatus maritalStatus;

    private ConceptCode race;

    private DateTimeWrapper birthDate;

    private DateTimeWrapper deceasedDate;

    @Override
    public List<IPersonName> getNames() {
        return names;
    }

    @Override
    public Gender getGender() {
        return gender;
    }

    @Override
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public ConceptCode getBirthSex() {
        return birthSex;
    }

    @Override
    public void setBirthSex(ConceptCode birthSex) {
        this.birthSex = birthSex;
    }

    @Override
    public ConceptCode getEthnicity() {
        return ethnicity;
    }

    @Override
    public void setEthnicity(ConceptCode ethnicity) {
        this.ethnicity = ethnicity;
    }

    @Override
    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    @Override
    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    @Override
    public ConceptCode getRace() {
        return race;
    }

    @Override
    public void setRace(ConceptCode race) {
        this.race = race;
    }

    @Override
    public DateTimeWrapper getBirthDate() {
        return birthDate;
    }

    @Override
    public void setBirthDate(DateTimeWrapper birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public DateTimeWrapper getDeceasedDate() {
        return deceasedDate;
    }

    @Override
    public void setDeceasedDate(DateTimeWrapper deceasedDate) {
        this.deceasedDate = deceasedDate;
    }

    @Override
    public List<IPostalAddress> getAddresses() {
        return addresses;
    }

    @Override
    public List<IConcept> getLanguages() {
        return languages;
    }

    @Override
    public List<IAttachment> getPhotos() {
        return photos;
    }

    @Override
    public List<IContactPoint> getContactPoints() {
        return contactPoints;
    }

}
