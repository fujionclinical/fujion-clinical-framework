package org.fujionclinical.api.model;

import java.util.List;
import java.util.Objects;

public interface IPerson extends IDomainObject {

    /**
     * Returns all known names for this person.
     *
     * @return List of all known names (possibly null).
     */
    List<PersonName> getNames();

    /**
     * Returns the person's usual name in parsed form.
     *
     * @return The parsed name, or null if not found.
     */
    default PersonName getName() {
        return getName(PersonName.PersonNameCategory.USUAL);
    }

    /**
     * Returns the person's parsed name from one of the specified categories.  Categories are
     * searched in order until a match is found.
     *
     * @param categories Only names belonging to one of these categories will be returned.
     * @return The parsed name, or null if not found.
     */
    default PersonName getName(PersonName.PersonNameCategory... categories) {
        List<PersonName> names = getNames();

        if (names != null && !names.isEmpty()) {
            for (PersonName.PersonNameCategory category : categories) {
                for (PersonName name : names) {
                    if (name.getCategory() == category) {
                        return name;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Returns the usual name formatted as a string.
     *
     * @return Person's full name, or null if not found.
     */
    default String getFullName() {
        return getFullName(PersonName.PersonNameCategory.USUAL);
    }

    /**
     * Returns the person's name formatted as a string from one of the specified categories.  Categories are
     * searched in order until a match is found.
     *
     * @param categories Only names belonging to one of these categories will be returned.
     * @return Person's full name or null if none found.
     */
    default String getFullName(PersonName.PersonNameCategory... categories) {
        return Objects.toString(getName(categories));
    }

    default List<PersonPhoto> getPhotos() {
        return null;
    }

    /**
     * Returns the person's default photo.
     *
     * @return The person's default photo, or null if not found.
     */
    default PersonPhoto getPhoto() {
        return getPhoto(PersonPhoto.PersonPhotoCategory.USUAL);
    }

    /**
     * Returns the person's photo from one of the specified categories.  Categories are
     * searched in order until a match is found.
     *
     * @param categories Only photos belonging to one of these categories will be returned.
     * @return The person's photo, or null if not found.
     */
    default PersonPhoto getPhoto(PersonPhoto.PersonPhotoCategory... categories) {
        List<PersonPhoto> photos = getPhotos();

        if (photos != null && !photos.isEmpty()) {
            for (PersonPhoto.PersonPhotoCategory category : categories) {
                for (PersonPhoto photo : photos) {
                    if (photo.getCategory() == category) {
                        return photo;
                    }
                }
            }
        }

        return null;
    }


}
