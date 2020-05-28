package org.fujionclinical.api.model.person;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.fujion.common.MiscUtil;

import java.util.Collections;
import java.util.List;

public interface IPersonName {

    enum PersonNameUse {
        USUAL, OFFICIAL, TEMP, NICKNAME, ANONYMOUS, OLD, MAIDEN, ANY
    }

    String getFamilyName();

    default IPersonName setFamilyName(String familyName) {
        throw new UnsupportedOperationException();
    }

    default boolean hasFamilyName() {
        return !StringUtils.isEmpty(getFamilyName());
    }

    List<String> getGivenNames();

    default IPersonName setGivenNames(List<String> givenNames) {
        MiscUtil.replaceList(getGivenNames(), givenNames);
        return this;
    }

    default IPersonName addGivenNames(String... givenNames) {
        Collections.addAll(getGivenNames(), givenNames);
        return this;
    }

    default boolean hasGivenName() {
        return !CollectionUtils.isEmpty(getGivenNames());
    }

    List<String> getPrefixes();

    default IPersonName setPrefixes(List<String> prefixes) {
        MiscUtil.replaceList(getPrefixes(), prefixes);
        return this;
    }

    default IPersonName addPrefixes(String... prefixes) {
        Collections.addAll(getPrefixes(), prefixes);
        return this;
    }

    default boolean hasPrefix() {
        return !CollectionUtils.isEmpty(getPrefixes());
    }

    List<String> getSuffixes();

    default IPersonName setSuffixes(List<String> suffixes) {
        MiscUtil.replaceList(getSuffixes(), suffixes);
        return this;
    }

    default IPersonName addSuffixes(String... suffixes) {
        Collections.addAll(getSuffixes(), suffixes);
        return this;
    }

    default boolean hasSuffix() {
        return CollectionUtils.isEmpty(getSuffixes());
    }

    PersonNameUse getUse();

    default IPersonName setUse(PersonNameUse use) {
        throw new UnsupportedOperationException();
    }

    default boolean hasUse() {
        return getUse() != null;
    }

    default IPersonName fromString(String value) {
        PersonNameParser.instance.fromString(value, this);
        return this;
    }

    default String asString() {
        return PersonNameParser.instance.toString(this);
    }

    ;

}
