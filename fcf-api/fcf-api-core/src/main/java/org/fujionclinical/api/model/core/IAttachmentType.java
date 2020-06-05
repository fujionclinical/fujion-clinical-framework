package org.fujionclinical.api.model.core;

import org.fujion.common.CollectionUtil;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public interface IAttachmentType {

    /**
     * Returns all attachments-.
     *
     * @return A list of all attachments (never null)
     */
    default List<IAttachment> getAttachments() {
        return Collections.emptyList();
    }

    default void setAttachments(List<IAttachment> attachments) {
        CollectionUtil.replaceList(getAttachments(), attachments);
    }

    default List<IAttachment> getAttachments(Predicate<IAttachment> criteria) {
        return CollectionUtil.findMatches(getAttachments(), criteria);
    }

    /**
     * Returns the person's attachment from one of the specified titles.  Titles are
     * searched in order until a match is found.
     *
     * @param titles Only attachments belonging to one of these titles will be returned.
     * @return The person's attachment, or null if not found.
     */
    default IAttachment getAttachment(String... titles) {
        return CollectionUtil.findMatch(getAttachments(), (attachment, title) ->
                title.equalsIgnoreCase(attachment.getTitle()), titles);
    }

    default void addAttachments(IAttachment... attachments) {
        Collections.addAll(getAttachments(), attachments);
    }

    default boolean hasAttachment() {
        return CollectionUtil.notEmpty(getAttachments());
    }

}
