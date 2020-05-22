package org.fujionclinical.api.model;

import org.fujion.ancillary.MimeContent;

public interface IPersonPhoto {

    enum PersonPhotoCategory {USUAL}

    PersonPhotoCategory getCategory();

    default IPersonPhoto setCategory(PersonPhotoCategory category)  {
        throw new UnsupportedOperationException();
    }

    default boolean hasCategory() {
        return getCategory() != null;
    }

    MimeContent getImage();

    default IPersonPhoto setImage(MimeContent content) {
        throw new UnsupportedOperationException();
    }

    default boolean hasImage() {
        return getImage() != null;
    }
}
