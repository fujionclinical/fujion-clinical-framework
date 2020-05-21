package org.fujionclinical.api.model;

import org.fujion.ancillary.MimeContent;

public class PersonPhoto {

    public enum PersonPhotoCategory {USUAL}

    private final PersonPhotoCategory category;

    private final MimeContent image;

    public PersonPhoto(MimeContent image, PersonPhotoCategory category) {
        this.image = image;
        this.category = category;
    }

    public PersonPhotoCategory getCategory() {
        return category;
    }

    public MimeContent getImage() {
        return image;
    }

}
