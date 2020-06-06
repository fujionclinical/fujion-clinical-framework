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

import java.util.List;
import java.util.function.Predicate;

public interface IConcept {

    String getText();

    default void setText(String text) {
        throw new UnsupportedOperationException();
    }

    default boolean hasText() {
        return getText() != null;
    }

    List<IConceptCode> getCodes();

    default void setCodes(List<IConceptCode> codes) {
        CollectionUtil.replaceList(getCodes(), codes);
    }

    default IConceptCode getCode(String system) {
        return getCode(code -> system.equals(code.getSystem()));
    }

    default IConceptCode getCode(Predicate<IConceptCode> criteria) {
        return CollectionUtil.findMatch(getCodes(), criteria);
    }

    default boolean hasCode() {
        return CollectionUtil.notEmpty(getCodes());
    }

    default void addCode(ConceptCode code) {
        getCodes().add(code);
    }

}