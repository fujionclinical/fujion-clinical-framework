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
package org.fujionclinical.api.model.condition;

import edu.utah.kmm.model.cool.core.datatype.Period;
import edu.utah.kmm.model.cool.foundation.common.AnnotatableImpl;
import edu.utah.kmm.model.cool.foundation.datatype.Annotation;
import edu.utah.kmm.model.cool.terminology.ConceptReferenceSet;
import org.fujionclinical.api.model.core.IReference;
import org.fujionclinical.api.model.encounter.IEncounter;
import org.fujionclinical.api.model.impl.BaseDomainType;
import org.fujionclinical.api.model.patient.IPatient;
import org.fujionclinical.api.model.person.IPerson;

import java.time.LocalDateTime;
import java.util.List;

public class Condition extends BaseDomainType implements ICondition {

    private final AnnotatableImpl annotatable = new AnnotatableImpl();

    private IReference<IPatient> patient;

    private Period onset;

    private IReference<IPerson> recorder;

    private LocalDateTime recordedDate;

    private IReference<IPerson> asserter;

    private ConceptReferenceSet condition;

    private IReference<IEncounter> encounter;

    private ClinicalStatus clinicalStatus;

    private VerificationStatus verificationStatus;

    private Severity severity;

    @Override
    public IReference<IPatient> getPatient() {
        return patient;
    }

    @Override
    public void setPatient(IReference<IPatient> patient) {
        this.patient = patient;
    }

    @Override
    public Period getOnset() {
        return onset;
    }

    @Override
    public void setOnset(Period onset) {
        this.onset = onset;
    }

    @Override
    public LocalDateTime getRecordedDate() {
        return recordedDate;
    }

    @Override
    public void setRecordedDate(LocalDateTime recordedDate) {
        this.recordedDate = recordedDate;
    }

    @Override
    public IReference<IPerson> getRecorder() {
        return recorder;
    }

    @Override
    public void setRecorder(IReference<IPerson> recorder) {
        this.recorder = recorder;
    }

    @Override
    public IReference<IPerson> getAsserter() {
        return asserter;
    }

    @Override
    public void setAsserter(IReference<IPerson> asserter) {
        this.asserter = asserter;
    }

    @Override
    public ConceptReferenceSet getCondition() {
        return condition;
    }

    @Override
    public void setCondition(ConceptReferenceSet condition) {
        this.condition = condition;
    }

    @Override
    public IReference<IEncounter> getEncounter() {
        return encounter;
    }

    @Override
    public void setEncounter(IReference<IEncounter> encounter) {
        this.encounter = encounter;
    }

    @Override
    public ClinicalStatus getClinicalStatus() {
        return clinicalStatus;
    }

    @Override
    public void setClinicalStatus(ClinicalStatus clinicalStatus) {
        this.clinicalStatus = clinicalStatus;
    }

    @Override
    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    @Override
    public void setVerificationStatus(VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    @Override
    public Severity getSeverity() {
        return severity;
    }

    @Override
    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    @Override
    public List<Annotation> getNotes() {
        return annotatable.getNotes();
    }

    @Override
    public void setNotes(List<Annotation> list) {
        annotatable.setNotes(list);
    }

    @Override
    public boolean hasNotes() {
        return annotatable.hasNotes();
    }

    @Override
    public void addNotes(Annotation... annotations) {
        annotatable.addNotes(annotations);
    }

}
