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
package org.fujionclinical.api.model.impl;

import org.apache.commons.codec.binary.Base64;
import org.fujionclinical.api.model.core.IAttachment;

import java.nio.charset.StandardCharsets;

public class AttachmentImpl implements IAttachment {

    private String contentType;

    private String title;

    private String encodedContent;

    private byte[] rawContent;

    private String url;

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getEncodedContent() {
        if (encodedContent == null && rawContent != null) {
            encodedContent = Base64.encodeBase64String(rawContent);
        }

        return encodedContent;
    }

    @Override
    public void setEncodedContent(String encodedData) {
        this.encodedContent = encodedData;
        this.rawContent = null;
    }

    @Override
    public byte[] getRawContent() {
        if (rawContent == null && encodedContent != null) {
            rawContent = Base64.decodeBase64(encodedContent.getBytes(StandardCharsets.UTF_8));
        }

        return rawContent;
    }

    @Override
    public void setRawContent(byte[] rawData) {
        this.rawContent = rawData;
        this.encodedContent = null;
    }

    @Override
    public boolean hasContent() {
        return rawContent != null || encodedContent != null;
    }

    @Override
    public String getURL() {
        return url;
    }

    @Override
    public void setURL(String url) {
        this.url = url;
    }

}
