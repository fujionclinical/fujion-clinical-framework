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

import edu.utah.kmm.model.cool.terminology.ConceptReference;
import edu.utah.kmm.model.cool.terminology.ConceptReferenceSet;

import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Concept implements ConceptReferenceSet {

    private final Set<ConceptReference> codes = new HashSet<>();

    private String text;

    public Concept() {
    }

    public Concept(ConceptReference... codes) {
        this(null, codes);
    }

    public Concept(
            String text,
            ConceptReference... codes) {
        this.text = text;
        Collections.addAll(this.codes, codes);
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean hasText() {
        return this.getText() != null;
    }

    @Override
    public Set<ConceptReference> getConceptReferences() {
        return codes;
    }

    @Override
    public void setConceptReferences(Set<ConceptReference> conceptReferences) {
        codes.clear();
        codes.addAll(conceptReferences);
    }

    @Override
    public boolean hasConceptReferences() {
        return !codes.isEmpty();
    }

    @Override
    public void addConceptReference(ConceptReference conceptReference) {
        codes.add(conceptReference);
    }

    @Override
    public Set<ConceptReference> getBySystem(String system) {
        return getBySystem(URI.create(system));
    }

    @Override
    public Set<ConceptReference> getBySystem(URI system) {
        return codes.stream().filter(code -> code.getSystem().equals(system)).collect(Collectors.toSet());
    }

    @Override
    public ConceptReference getFirstConcept() {
        return codes.stream().findFirst().orElse(null);
    }

    @Override
    public ConceptReference getSoleConcept() {
        return codes.stream().findFirst().orElse(null);
    }

}
