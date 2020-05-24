package org.fujionclinical.api.model;

import org.fujion.ancillary.MimeContent;

import java.util.Collection;

public interface IAttachment {

    static IAttachment getAttachment(
            Collection<? extends IAttachment> attachments,
            String... titles) {
        if (attachments != null && !attachments.isEmpty()) {
            for (String title : titles) {
                for (IAttachment attachment : attachments) {
                    if (title.equals(attachment.getTitle())) {
                        return attachment;
                    }
                }
            }
        }

        return null;
    }

    String getContentType();

    default IAttachment setContentType(String contentType) {
        throw new UnsupportedOperationException();
    }

    default boolean hasContentType() {
        return getContentType() != null;
    }

    String getTitle();

    default IAttachment setTitle(String title) {
        throw new UnsupportedOperationException();
    }

    default boolean hasTitle() {
        return getTitle() != null;
    }

    String getEncodedData();

    default IAttachment setEncodedData(String encodedData) {
        throw new UnsupportedOperationException();
    }

    byte[] getRawData();

    default IAttachment setRawData(byte[] rawData) {
        throw new UnsupportedOperationException();
    }

    boolean hasData();

    String getURL();

    default IAttachment setURL(String url) {
        throw new UnsupportedOperationException();
    }

    default boolean hasURL() {
        return getURL() != null;
    }

    default MimeContent getContent() {
        if (hasData()) {
            return new MimeContent(getContentType(), getEncodedData());
        }

        if (hasURL()) {
            return new MimeContent(getContentType(), getURL());
        }

        return null;
    }

    default IAttachment setContent(MimeContent content) {
        setURL(content.getSrc());
        setContentType(content.getMimeType());
        setRawData(content.getData());
        return this;
    }
}
