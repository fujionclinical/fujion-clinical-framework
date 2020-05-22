package org.fujionclinical.api.model;

import org.apache.commons.collections.CollectionUtils;
import org.fujion.common.MiscUtil;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public interface IPerson extends IDomainObject {

    enum Gender {MALE, FEMALE, OTHER, UNKNOWN};

    default Gender getGender() {
        return null;
    }

    default IPerson setGender(Gender gender) {
        throw new UnsupportedOperationException();
    }
    
    default boolean hasGender() {
        return getGender() != null;
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

    default IPerson setNames(List<IPersonName> names) {
        MiscUtil.replaceList(getNames(), names);
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

    /**
     * Returns the person's home address.
     *
     * @return The person's home address, or null if not found.
     */
    default IPostalAddress getAddress() {
        return getAddress(IPostalAddress.PostalAddressUse.HOME);
    }

    /**
     * Returns the person's address from one of the specified categories.  Categories are
     * searched in order until a match is found.
     *
     * @param categories Only addresses belonging to one of these categories will be returned.
     * @return The person's address, or null if not found.
     */
    default IPostalAddress getAddress(IPostalAddress.PostalAddressUse... categories) {
        List<IPostalAddress> addresses = getAddresses();

        if (addresses != null && !addresses.isEmpty()) {
            for (IPostalAddress.PostalAddressUse category : categories) {
                for (IPostalAddress address : addresses) {
                    if (address.getUse() == category) {
                        return address;
                    }
                }
            }
        }

        return null;
    }

    default IPerson addAddresses(IPostalAddress... addresses) {
        Collections.addAll(getAddresses(), addresses);
        return this;
    }

    default IPerson setAddresses(List<IPostalAddress> addresses) {
        MiscUtil.replaceList(getAddresses(), addresses);
        return this;
    }

    default boolean hasAddress() {
        return !CollectionUtils.isEmpty(getAddresses());
    }

    /**
     * Returns all photos for the person.
     *
     * @return A list of all photos (never null)
     */
    default List<IPersonPhoto> getPhotos() {
        return Collections.emptyList();
    }

    /**
     * Returns the person's default photo.
     *
     * @return The person's default photo, or null if not found.
     */
    default IPersonPhoto getPhoto() {
        return getPhoto(IPersonPhoto.PersonPhotoCategory.USUAL);
    }

    /**
     * Returns the person's photo from one of the specified categories.  Categories are
     * searched in order until a match is found.
     *
     * @param categories Only photos belonging to one of these categories will be returned.
     * @return The person's photo, or null if not found.
     */
    default IPersonPhoto getPhoto(IPersonPhoto.PersonPhotoCategory... categories) {
        List<IPersonPhoto> photos = getPhotos();

        if (photos != null && !photos.isEmpty()) {
            for (IPersonPhoto.PersonPhotoCategory category : categories) {
                for (IPersonPhoto photo : photos) {
                    if (photo.getCategory() == category) {
                        return photo;
                    }
                }
            }
        }

        return null;
    }

    default IPerson addPhotos(IPersonPhoto... photos) {
        Collections.addAll(getPhotos(), photos);
        return this;
    }

    default IPerson setPhotos(List<IPersonPhoto> photos) {
        MiscUtil.replaceList(getPhotos(), photos);
        return this;
    }
}
