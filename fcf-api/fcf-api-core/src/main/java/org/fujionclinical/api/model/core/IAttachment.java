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
package org.fujionclinical.api.model.core;

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

    default void setContentType(String contentType) {
        throw new UnsupportedOperationException();
    }

    default boolean hasContentType() {
        return getContentType() != null;
    }

    String getTitle();

    default void setTitle(String title) {
        throw new UnsupportedOperationException();
    }

    default boolean hasTitle() {
        return getTitle() != null;
    }

    String getEncodedData();

    default void setEncodedData(String encodedData) {
        throw new UnsupportedOperationException();
    }

    byte[] getRawData();

    default void setRawData(byte[] rawData) {
        throw new UnsupportedOperationException();
    }

    boolean hasData();

    String getURL();

    default void setURL(String url) {
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

    default void setContent(MimeContent content) {
        setURL(content.getSrc());
        setContentType(content.getMimeType());
        setRawData(content.getData());
    }
}
