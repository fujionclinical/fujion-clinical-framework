package org.fujionclinical.api.model.impl;

import org.fujionclinical.api.model.person.IPersonName;

import java.util.ArrayList;
import java.util.List;

public class PersonName implements IPersonName {

    private final List<String> givenNames = new ArrayList<>();

    private final List<String> prefixes = new ArrayList<>();

    private final List<String> suffixes = new ArrayList<>();

    private String familyName;

    private PersonNameUse use;

    @Override
    public String getFamilyName() {
        return familyName;
    }

    @Override
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
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
        return use;
    }

    @Override
    public void setUse(PersonNameUse use) {
        this.use = use;
    }

}
