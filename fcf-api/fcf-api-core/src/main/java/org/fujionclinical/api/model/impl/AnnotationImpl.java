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

import edu.utah.kmm.model.cool.foundation.core.Party;
import edu.utah.kmm.model.cool.foundation.datatype.Annotation;

import java.time.LocalDateTime;

public class AnnotationImpl implements Annotation {

    private Party author;

    private LocalDateTime timestamp;

    private String content;

    public AnnotationImpl() {
    }

    public AnnotationImpl(String content) {
        this(content, LocalDateTime.now(), null);
    }

    public AnnotationImpl(
            String content,
            Party author) {
        this(content, LocalDateTime.now(), author);
    }

    public AnnotationImpl(
            String content,
            LocalDateTime timestamp,
            Party author) {
        this.content = content;
        this.timestamp = timestamp;
        this.author = author;
    }

    @Override
    public Party getAuthor() {
        return author;
    }

    @Override
    public void setAuthor(Party author) {
        this.author = author;
    }

    @Override
    public boolean hasAuthor() {
        return author != null;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(LocalDateTime recorded) {
        this.timestamp = recorded;
    }

    @Override
    public boolean hasTimestamp() {
        return timestamp != null;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean hasContent() {
        return content != null;
    }

}
