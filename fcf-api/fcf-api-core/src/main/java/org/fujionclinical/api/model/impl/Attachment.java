package org.fujionclinical.api.model.impl;

import org.apache.commons.codec.binary.Base64;
import org.fujionclinical.api.model.core.IAttachment;

import java.nio.charset.StandardCharsets;

public class Attachment implements IAttachment {

    private String contentType;

    private String title;

    private String encodedData;

    private String url;

    private byte[] rawData;

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
    public String getEncodedData() {
        if (encodedData == null && rawData != null) {
            encodedData = Base64.encodeBase64String(rawData);
        }

        return encodedData;
    }

    @Override
    public void setEncodedData(String encodedData) {
        this.encodedData = encodedData;
        this.rawData = null;
    }

    @Override
    public byte[] getRawData() {
        if (rawData == null && encodedData != null) {
            rawData = Base64.decodeBase64(encodedData.getBytes(StandardCharsets.UTF_8));
        }

        return rawData;
    }

    @Override
    public void setRawData(byte[] rawData) {
        this.rawData = rawData;
        this.encodedData = null;
    }

    @Override
    public boolean hasData() {
        return rawData != null || encodedData != null;
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
