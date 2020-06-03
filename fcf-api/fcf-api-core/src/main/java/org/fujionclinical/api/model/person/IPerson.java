/*
 * #%L
 * Fujion Clinical Framework
 * %%
 * Copyright (C) 2020 fujionclinical.org
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * This Source Code Form is also subject to the terms of the Health-Related
 * Additional Disclaimer of Warranty and Limitation of Liability available at
 *
 *      http://www.fujionclinical.org/licensing/disclaimer
 *
 * #L%
 */
package org.fujionclinical.api.model.person;

import org.apache.commons.collections.CollectionUtils;
import org.fujion.common.CollectionUtil;
import org.fujion.common.StrUtil;
import org.fujionclinical.api.model.*;
import org.fujionclinical.api.query.QueryParameter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public interface IPerson extends IDomainObject, IPostalAddressType, IContactPointType, IPersonNameType {

    enum Gender {
        MALE, FEMALE, OTHER, UNKNOWN
    }

    enum MaritalStatus {
        ANNULLED("A"), DIVORCED("D"), INTERLOCUTORY("I"), LEGALLY_SEPARATED("L"), MARRIED("M"),
        POLYGAMOUS("P"), NEVER_MARRIED("S"), DOMESTIC_PARTNER("T"), UNMARRIED("U"), WIDOWED("W"), UNKNOWN("UNK");

        private final String code;

        public static MaritalStatus forCode(String code) {
            return Arrays.stream(MaritalStatus.values())
                    .filter(maritalStatus -> maritalStatus.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
        }

        MaritalStatus(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        @Override
        public String toString() {
            return StrUtil.toCamelCaseUpper(name());
        }
    }

    @QueryParameter
    default Gender getGender() {
        return null;
    }

    default void setGender(Gender gender) {
        throw new UnsupportedOperationException();
    }

    default boolean hasGender() {
        return getGender() != null;
    }

    @QueryParameter
    default ConceptCode getBirthSex() {
        return null;
    }

    default void setBirthSex(ConceptCode birthSex) {
        throw new UnsupportedOperationException();
    }

    default boolean hasBirthSex() {
        return getBirthSex() != null;
    }

    @QueryParameter
    default ConceptCode getEthnicity() {
        return null;
    }

    default void setEthnicity(ConceptCode ethnicity) {
        throw new UnsupportedOperationException();
    }

    default boolean hasEthnicity() {
        return getEthnicity() != null;
    }

    @QueryParameter
    default MaritalStatus getMaritalStatus() {
        return null;
    }

    default void setMaritalStatus(MaritalStatus maritalStatus) {
        throw new UnsupportedOperationException();
    }

    default boolean hasMaritalStatus() {
        return getMaritalStatus() != null;
    }

    @QueryParameter
    default ConceptCode getRace() {
        return null;
    }

    default void setRace(ConceptCode race) {
        throw new UnsupportedOperationException();
    }

    default boolean hasRace() {
        return getRace() != null;
    }

    @QueryParameter
    default Date getBirthDate() {
        return null;
    }

    default void setBirthDate(Date date) {
        throw new UnsupportedOperationException();
    }

    default boolean hasBirthDate() {
        return getBirthDate() != null;
    }

    @QueryParameter
    default Date getDeceasedDate() {
        return null;
    }

    default void setDeceasedDate(Date date) {
        throw new UnsupportedOperationException();
    }

    default boolean hasDeceasedDate() {
        return getDeceasedDate() != null;
    }

    default List<IConcept> getLanguages() {
        return Collections.emptyList();
    }

    default void setLanguages(List<IConcept> languages) {
        CollectionUtil.replaceList(getLanguages(), languages);
    }

    default void addLanguages(IConcept... languages) {
        CollectionUtils.addAll(getLanguages(), languages);
    }

    default boolean hasLanguage() {
        return CollectionUtil.notEmpty(getLanguages());
    }

    @QueryParameter
    default IConcept getPreferredLanguage() {
        return CollectionUtil.getFirst(getLanguages());
    }

    /**
     * Returns all photos for the person.
     *
     * @return A list of all photos (never null)
     */
    default List<IAttachment> getPhotos() {
        return Collections.emptyList();
    }

    default void setPhotos(List<IAttachment> photos) {
        CollectionUtil.replaceList(getPhotos(), photos);
    }

    /**
     * Returns the person's default photo.
     *
     * @return The person's default photo, or null if not found.
     */
    default IAttachment getPhoto() {
        return CollectionUtil.getFirst(getPhotos());
    }

    /**
     * Returns the person's photo from one of the specified titles.  Titles are
     * searched in order until a match is found.
     *
     * @param titles Only photos belonging to one of these titles will be returned.
     * @return The person's photo, or null if not found.
     */
    default IAttachment getPhoto(String... titles) {
        return IAttachment.getAttachment(getPhotos(), titles);
    }

    default void addPhotos(IAttachment... photos) {
        Collections.addAll(getPhotos(), photos);
    }

    default boolean hasPhoto() {
        return CollectionUtil.notEmpty(getPhotos());
    }

}
