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

import org.fujion.common.DateTimeWrapper;
import org.fujionclinical.api.model.person.IPerson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Annotation implements IAnnotation {

    private final List<IPerson> authors = new ArrayList<>();

    private DateTimeWrapper recorded;

    private String text;

    public Annotation(
            String text,
            IPerson... authors) {
        this(text, DateTimeWrapper.now(), authors);
    }

    public Annotation(
            String text,
            DateTimeWrapper recorded,
            IPerson... authors) {
        this.text = text;
        this.recorded = recorded;
        Collections.addAll(this.authors, authors);
    }

    @Override
    public List<IPerson> getAuthors() {
        return authors;
    }

    @Override
    public DateTimeWrapper getRecorded() {
        return recorded;
    }

    @Override
    public void setRecorded(DateTimeWrapper recorded) {
        this.recorded = recorded;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

}
