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

import edu.utah.kmm.model.cool.terminology.ConceptReference;
import edu.utah.kmm.model.cool.terminology.ConceptReferenceSet;
import org.fujion.common.CollectionUtil;
import org.fujionclinical.api.core.CoreUtil;
import org.fujionclinical.api.model.core.IAttachment;
import org.fujionclinical.api.model.core.IContactPointType;
import org.fujionclinical.api.model.core.IDomainType;
import org.fujionclinical.api.model.core.IPostalAddressType;
import org.fujionclinical.api.query.expression.QueryParameter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface IPerson extends IDomainType, IPostalAddressType, IContactPointType, IPersonNameType {

    enum Gender {
        MALE, FEMALE, OTHER, UNKNOWN;

        @Override
        public String toString() {
            return CoreUtil.enumToString(this);
        }

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
            return CoreUtil.enumToString(this);
        }

    }

    @QueryParameter
    default Gender getGender() {
        return null;
    }

    default void setGender(Gender gender) {
        notSupported();
    }

    default boolean hasGender() {
        return getGender() != null;
    }

    @QueryParameter
    default ConceptReference getBirthSex() {
        return null;
    }

    default void setBirthSex(ConceptReference birthSex) {
        notSupported();
    }

    default boolean hasBirthSex() {
        return getBirthSex() != null;
    }

    @QueryParameter
    default ConceptReference getEthnicity() {
        return null;
    }

    default void setEthnicity(ConceptReference ethnicity) {
        notSupported();
    }

    default boolean hasEthnicity() {
        return getEthnicity() != null;
    }

    @QueryParameter
    default MaritalStatus getMaritalStatus() {
        return null;
    }

    default void setMaritalStatus(MaritalStatus maritalStatus) {
        notSupported();
    }

    default boolean hasMaritalStatus() {
        return getMaritalStatus() != null;
    }

    @QueryParameter
    default ConceptReference getRace() {
        return null;
    }

    default void setRace(ConceptReference race) {
        notSupported();
    }

    default boolean hasRace() {
        return getRace() != null;
    }

    @QueryParameter
    default LocalDateTime getBirthDate() {
        return null;
    }

    default void setBirthDate(LocalDateTime date) {
        notSupported();
    }

    default boolean hasBirthDate() {
        return getBirthDate() != null;
    }

    @QueryParameter
    default LocalDateTime getDeceasedDate() {
        return null;
    }

    default void setDeceasedDate(LocalDateTime date) {
        notSupported();
    }

    default boolean hasDeceasedDate() {
        return getDeceasedDate() != null;
    }

    default List<ConceptReferenceSet> getLanguages() {
        return Collections.emptyList();
    }

    default void setLanguages(List<ConceptReferenceSet> languages) {
        CollectionUtil.replaceElements(getLanguages(), languages);
    }

    default void addLanguages(ConceptReferenceSet... languages) {
        Collections.addAll(getLanguages(), languages);
    }

    default boolean hasLanguage() {
        return CollectionUtil.notEmpty(getLanguages());
    }

    @QueryParameter
    default ConceptReferenceSet getPreferredLanguage() {
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
        CollectionUtil.replaceElements(getPhotos(), photos);
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

    @Override
    default String getText() {
        return null;
    }

    @Override
    default void setText(String text) {
        notSupported();
    }

    default boolean hasText() {
        return getText() != null;
    }
}
