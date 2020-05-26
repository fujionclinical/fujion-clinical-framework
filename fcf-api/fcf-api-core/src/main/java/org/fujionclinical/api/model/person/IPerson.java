package org.fujionclinical.api.model.person;

import org.apache.commons.collections.CollectionUtils;
import org.fujion.common.MiscUtil;
import org.fujion.common.StrUtil;
import org.fujionclinical.api.model.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public interface IPerson extends IDomainObject {

    enum Gender {
        MALE, FEMALE, OTHER, UNKNOWN
    }

    enum MaritalStatus {
        ANNULLED("A"), DIVORCED("D"), INTERLOCUTORY("I"), LEGALLY_SEPARATED("L"), MARRIED("M"),
        POLYGAMOUS("P"), NEVER_MARRIED("S"), DOMESTIC_PARTNER("T"), UNMARRIED("U"), WIDOWED("W"), UNKNOWN("UNK");

        private final String code;

        public static MaritalStatus forCode(String code) {
            for (MaritalStatus maritalStatus : MaritalStatus.values()) {
                if (maritalStatus.getCode().equals(code)) {
                    return maritalStatus;
                }
            }

            return null;
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

    default Gender getGender() {
        return null;
    }

    default IPerson setGender(Gender gender) {
        throw new UnsupportedOperationException();
    }

    default boolean hasGender() {
        return getGender() != null;
    }

    default ConceptCode getBirthSex() {
        return null;
    }

    default IPerson setBirthSex(ConceptCode birthSex) {
        throw new UnsupportedOperationException();
    }

    default boolean hasBirthSex() {
        return getBirthSex() != null;
    }

    default ConceptCode getEthnicity() {
        return null;
    }

    default IPerson setEthnicity(ConceptCode ethnicity) {
        throw new UnsupportedOperationException();
    }

    default boolean hasEthnicity() {
        return getEthnicity() != null;
    }

    default MaritalStatus getMaritalStatus() {
        return null;
    }

    default IPerson setMaritalStatus(MaritalStatus maritalStatus) {
        throw new UnsupportedOperationException();
    }

    default boolean hasMaritalStatus() {
        return getMaritalStatus() != null;
    }

    default ConceptCode getRace() {
        return null;
    }

    default IPerson setRace(ConceptCode race) {
        throw new UnsupportedOperationException();
    }

    default boolean hasRace() {
        return getRace() != null;
    }

    default Date getBirthDate() {
        return null;
    }

    default IPerson setBirthDate(Date date) {
        throw new UnsupportedOperationException();
    }

    default boolean hasBirthDate() {
        return getBirthDate() != null;
    }

    default Date getDeceasedDate() {
        return null;
    }

    default IPerson setDeceasedDate(Date date) {
        throw new UnsupportedOperationException();
    }

    default boolean hasDeceasedDate() {
        return getDeceasedDate() != null;
    }

    /**
     * Returns all known names for this person.
     *
     * @return List of all known names (never null).
     */
    List<IPersonName> getNames();

    default IPerson setNames(List<IPersonName> names) {
        MiscUtil.replaceList(getNames(), names);
        return this;
    }

    /**
     * Returns the person's usual name in parsed form.
     *
     * @return The parsed name, or null if not found.
     */
    default IPersonName getName() {
        return getName(IPersonName.PersonNameUse.USUAL);
    }

    /**
     * Returns the person's parsed name from one of the specified categories.  Categories are
     * searched in order until a match is found.
     *
     * @param categories Only names belonging to one of these categories will be returned.
     * @return The parsed name, or null if not found.
     */
    default IPersonName getName(IPersonName.PersonNameUse... categories) {
        List<IPersonName> names = getNames();

        if (names != null && !names.isEmpty()) {
            for (IPersonName.PersonNameUse category : categories) {
                for (IPersonName name : names) {
                    if (name.getUse() == category) {
                        return name;
                    }
                }
            }
        }

        return null;
    }

    default IPerson addNames(IPersonName... names) {
        Collections.addAll(getNames(), names);
        return this;
    }

    default boolean hasName() {
        return !CollectionUtils.isEmpty(getNames());
    }

    /**
     * Returns the usual name formatted as a string.
     *
     * @return Person's full name, or null if not found.
     */
    default String getFullName() {
        return getFullName(IPersonName.PersonNameUse.USUAL);
    }

    /**
     * Returns the person's name formatted as a string from one of the specified categories.  Categories are
     * searched in order until a match is found.
     *
     * @param categories Only names belonging to one of these categories will be returned.
     * @return Person's full name or null if none found.
     */
    default String getFullName(IPersonName.PersonNameUse... categories) {
        return Objects.toString(getName(categories));
    }

    /**
     * Returns all addresses for the person.
     *
     * @return A list of all addresses (never null)
     */
    default List<IPostalAddress> getAddresses() {
        return Collections.emptyList();
    }

    default IPerson setAddresses(List<IPostalAddress> addresses) {
        MiscUtil.replaceList(getAddresses(), addresses);
        return this;
    }

    /**
     * Returns the person's home address.
     *
     * @return The person's home address, or null if not found.
     */
    default IPostalAddress getAddress() {
        return getAddress(IPostalAddress.PostalAddressUse.HOME);
    }

    /**
     * Returns the person's address from one of the specified uses.  Uses are
     * searched in order until a match is found.
     *
     * @param uses Only addresses belonging to one of these uses will be returned.
     * @return The person's address, or null if not found.
     */
    default IPostalAddress getAddress(IPostalAddress.PostalAddressUse... uses) {
        return IPostalAddress.getPostalAddress(getAddresses(), uses);

    }

    default IPerson addAddresses(IPostalAddress... addresses) {
        Collections.addAll(getAddresses(), addresses);
        return this;
    }

    default boolean hasAddress() {
        return !CollectionUtils.isEmpty(getAddresses());
    }

    /**
     * Returns all contact points for the person.
     *
     * @return A list of all contact points (never null)
     */
    default List<IContactPoint> getContactPoints() {
        return Collections.emptyList();
    }

    default IPerson setContactPoints(List<IContactPoint> contactPoints) {
        MiscUtil.replaceList(getContactPoints(), contactPoints);
        return this;
    }

    /**
     * Returns the person's contact points from one of the specified uses.  Uses are
     * searched in order until a match is found.
     *
     * @param uses Only contact points belonging to one of these uses will be returned.
     * @return The person's contact, or null if not found.
     */
    default IContactPoint getContactPoint(IContactPoint.ContactPointUse... uses) {
        return IContactPoint.getContactPoint(getContactPoints(), uses);

    }

    /**
     * Returns the person's contact points from one of the specified systems.  Systems are
     * searched in order until a match is found.
     *
     * @param systems Only contact points belonging to one of these systems will be returned.
     * @return The person's contact, or null if not found.
     */
    default IContactPoint getContactPoint(IContactPoint.ContactPointSystem... systems) {
        return IContactPoint.getContactPoint(getContactPoints(), systems);

    }

    /**
     * Returns the person's home phone.
     *
     * @return The person's home phone, or null if not found.
     */
    default IContactPoint getHomePhone() {
        return getContactPoint(IContactPoint.ContactPointUse.HOME, IContactPoint.ContactPointSystem.PHONE);
    }

    /**
     * Returns the person's contact point matching the specified use and system.
     *
     * @param use    The contact point use being sought.
     * @param system The contact point system being sought.
     * @return The person's contact point, or null if not found.
     */
    default IContactPoint getContactPoint(
            IContactPoint.ContactPointUse use,
            IContactPoint.ContactPointSystem system) {
        return IContactPoint.getContactPoint(getContactPoints(), use, system);
    }

    default IPerson addContactPoints(IContactPoint... contactPoints) {
        Collections.addAll(getContactPoints(), contactPoints);
        return this;
    }

    default boolean hasContactPoint() {
        return !CollectionUtils.isEmpty(getContactPoints());
    }

    default List<IConcept> getLanguages() {
        return Collections.emptyList();
    }

    default IPerson setLanguages(List<IConcept> languages) {
        MiscUtil.replaceList(getLanguages(), languages);
        return this;
    }

    default IPerson addLanguages(IConcept... languages) {
        CollectionUtils.addAll(getLanguages(), languages);
        return this;
    }

    default boolean hasLanguage() {
        return !CollectionUtils.isEmpty(getLanguages());
    }

    default IConcept getPreferredLanguage() {
        return hasLanguage() ? getLanguages().get(0) : null;
    }

    /**
     * Returns all photos for the person.
     *
     * @return A list of all photos (never null)
     */
    default List<IAttachment> getPhotos() {
        return Collections.emptyList();
    }

    default IPerson setPhotos(List<IAttachment> photos) {
        MiscUtil.replaceList(getPhotos(), photos);
        return this;
    }

    /**
     * Returns the person's default photo.
     *
     * @return The person's default photo, or null if not found.
     */
    default IAttachment getPhoto() {
        return hasPhoto() ? getPhotos().get(0) : null;
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

    default IPerson addPhotos(IAttachment... photos) {
        Collections.addAll(getPhotos(), photos);
        return this;
    }

    default boolean hasPhoto() {
        return !CollectionUtils.isEmpty(getPhotos());
    }

}
