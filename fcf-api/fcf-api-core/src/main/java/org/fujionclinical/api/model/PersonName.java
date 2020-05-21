package org.fujionclinical.api.model;

import org.fujion.common.MiscUtil;

import java.util.List;

/**
 * Represents a person's name as individual components.
 */
public class PersonName {

    private String familyName;
    private List<String> givenNames;
    private List<String> prefixes;
    private List<String> suffixes;
    private PersonNameCategory category;

    public PersonName() {

    }

    public PersonName(
            String familyName,
            String[] givenNames) {
        this(familyName, givenNames, null, null, null);
    }

    public PersonName(
            String familyName,
            String[] givenNames,
            PersonNameCategory category) {
        this(familyName, givenNames, null, null, category);
    }

    public PersonName(
            String familyName,
            String[] givenNames,
            String[] prefixes,
            String[] suffixes,
            PersonNameCategory category) {
        this.familyName = familyName;
        this.givenNames = MiscUtil.toList(givenNames);
        this.prefixes = MiscUtil.toList(prefixes);
        this.suffixes = MiscUtil.toList(suffixes);
        this.category = category;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public boolean hasFamilyName() {
        return this.familyName != null && !this.familyName.isEmpty();
    }

    public List<String> getGivenNames() {
        return givenNames;
    }

    public void setGivenNames(List<String> givenNames) {
        this.givenNames = givenNames;
    }

    public void addGivenName(String givenName) {
        givenNames = MiscUtil.ensureList(givenNames);
        givenNames.add(givenName);
    }

    public boolean hasGivenName() {
        return givenNames != null && !givenNames.isEmpty();
    }

    public List<String> getPrefixes() {
        return prefixes;
    }

    public void setPrefixes(List<String> prefixes) {
        this.prefixes = prefixes;
    }

    public void addPrefix(String prefix) {
        prefixes = MiscUtil.ensureList(prefixes);
        prefixes.add(prefix);
    }

    public boolean hasPrefix() {
        return prefixes != null && !prefixes.isEmpty();
    }

    public List<String> getSuffixes() {
        return suffixes;
    }

    public void setSuffixes(List<String> suffixes) {
        this.suffixes = suffixes;
    }

    public void addSuffix(String suffix) {
        suffixes = MiscUtil.ensureList(suffixes);
        suffixes.add(suffix);
    }

    public boolean hasSuffix() {
        return suffixes != null && !suffixes.isEmpty();
    }

    public PersonNameCategory getCategory() {
        return category;
    }

    public void setCategory(PersonNameCategory category) {
        this.category = category;
    }

    public boolean hasCategory() {
        return this.category != null;
    }

    @Override
    public String toString() {
        return PersonNameParser.instance.toString(this);
    }

    public enum PersonNameCategory {
        USUAL, OFFICIAL, TEMP, NICKNAME, ANONYMOUS, OLD, MAIDEN
    }
}
