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

import org.fujion.common.CollectionUtil;
import org.fujionclinical.api.model.person.IPerson;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public interface IAnnotation extends IBaseType {

    List<IPerson> getAuthors();

    default void setAuthors(List<IPerson> authors) {
        CollectionUtil.replaceElements(getAuthors(), authors);
    }

    default boolean hasAuthor() {
        return CollectionUtil.notEmpty(getAuthors());
    }

    default void addAuthors(IPerson... authors) {
        Collections.addAll(getAuthors(), authors);
    }

    LocalDateTime getRecorded();

    default void setRecorded(LocalDateTime recorded) {
        notSupported();
    }

    default boolean hasRecorded() {
        return getRecorded() != null;
    }

    String getText();

    default void setText(String text) {
        notSupported();
    }

    default boolean hasText() {
        return getText() != null;
    }
}
