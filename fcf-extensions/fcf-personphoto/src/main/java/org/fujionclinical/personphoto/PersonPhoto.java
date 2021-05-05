package org.fujionclinical.personphoto;

import edu.utah.kmm.common.utils.CommonUtils;
import edu.utah.kmm.model.cool.foundation.entity.Person;
import edu.utah.kmm.model.cool.util.PersonUtils;
import org.fujion.ancillary.MimeContent;
import org.fujion.common.LocalizedMessage;
import org.fujion.component.Image;
import org.fujion.core.CoreUtil;
import org.fujionclinical.api.cool.common.CoolUtil;

public class PersonPhoto extends Image {

    private static final String RESOURCE_PATH = CoreUtil.getResourceClassPath(PersonPhoto.class);

    private static final String IMAGE_NO_PHOTO = RESOURCE_PATH + "silhouette1.png";

    private static final String IMAGE_NO_PERSON = RESOURCE_PATH + "silhouette2.png";

    private static final LocalizedMessage MSG_NO_PERSON = new LocalizedMessage("personphoto.no.person");

    private static final LocalizedMessage MSG_NO_PHOTO = new LocalizedMessage("personphoto.no.photo");

    private static final MimeContent CONTENT_NO_PHOTO = new MimeContent("image/png", IMAGE_NO_PHOTO);

    private static final MimeContent CONTENT_NO_PERSON = new MimeContent("image/png", IMAGE_NO_PERSON);

    private Person person;

    private boolean hasPhoto;

    public PersonPhoto() {
        this(null);
    }

    public PersonPhoto(Person person) {
        setPerson(person);
    }

    public Person getPerson() {
        return person;
    }

    public boolean hasPhoto() {
        return hasPhoto;
    }

    public void setPerson(Person person) {
        this.person = person;

        if (person == null) {
            hasPhoto = false;
            setContent(CONTENT_NO_PERSON);
            setHint(MSG_NO_PERSON.toString());
        } else {
            MimeContent content = CoolUtil.toMimeContent(CommonUtils.getFirst(person.getPhoto()));
            hasPhoto = content != null;
            setContent(content == null ? CONTENT_NO_PHOTO : content);
            setHint(content == null ? MSG_NO_PHOTO.toString() : PersonUtils.getFullName(person));
        }
    }

}
