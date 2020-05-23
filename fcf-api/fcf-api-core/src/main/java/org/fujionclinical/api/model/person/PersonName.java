package org.fujionclinical.api.model.person;

import org.fujionclinical.api.model.person.IPersonName;

import java.util.ArrayList;
import java.util.List;

public class PersonName implements IPersonName {

    private String familyName;

    private PersonNameUse category;

    private final List<String> givenNames = new ArrayList<>();

    private final List<String> prefixes = new ArrayList<>();

    private final List<String> suffixes = new ArrayList<>();

    @Override
    public String getFamilyName() {
        return familyName;
    }

    @Override
    public IPersonName setFamilyName(String familyName) {
        this.familyName = familyName;
        return this;
    }

    @Override
    public List<String> getGivenNames() {
        return givenNames;
    }

    @Override
    public List<String> getPrefixes() {
        return prefixes;
    }

    @Override
    public List<String> getSuffixes() {
        return suffixes;
    }

    @Override
    public PersonNameUse getUse() {
        return category;
    }

    @Override
    public IPersonName setUse(PersonNameUse category) {
        this.category = category;
        return this;
    }

    @Override
    public String toString() {
        return asString();
    }
}
