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
package org.fujionclinical.help;

import org.springframework.util.StringUtils;

import java.util.Locale;

/**
 * Each instance of this class represents a complete definition of a FCF help module.
 */
public class HelpModule {

    private String id;

    private String url;

    private Locale locale;

    private String format;

    private String title;

    private String description;

    private String creator;

    private String copyright;

    private String version;

    private String released;

    public static HelpModule getModule(String id) {
        return HelpModuleRegistry.getInstance().get(id);
    }

    /**
     * Adds locale information to a help module id.
     *
     * @param id Help module id.
     * @param locale Locale (may be null).
     * @return The id with locale information appended.
     */
    public static String getLocalizedId(String id, Locale locale) {
        String locstr = locale == null ? "" : ("_" + locale.toString());
        return id + locstr;
    }

    public HelpModule() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Adds locale information to a help module id.
     *
     * @return The id with locale information appended.
     */
    public String getLocalizedId() {
        return getLocalizedId(id, locale);
    }

    public String getUrl() {
        return encodeURL(url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = StringUtils.isEmpty(locale) ? null : new Locale(locale);
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    /**
     * Encodes the specified URL path, which may be absolute or relative.
     * <p>
     * For compressed help sets, use the web/ prefix. When using this syntax, FCF requires that the
     * root folder in the jar file be named "web" with the specified path being relative to that
     * folder.
     *
     * @param url URL to encode.
     * @return The encoded URL.
     */
    private String encodeURL(String url) {
        return url.startsWith("/") || url.startsWith(".") ? url : url.startsWith("web/") ? "/" + url : "/web/" + url;
    }

}
