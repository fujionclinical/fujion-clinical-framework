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

import org.fujion.common.DateTimeWrapper;
import org.fujionclinical.api.model.core.IAnnotation;
import org.fujionclinical.api.model.core.IConcept;
import org.fujionclinical.api.model.core.IPeriod;
import org.fujionclinical.api.model.core.IReference;
import org.fujionclinical.api.model.encounter.IEncounter;
import org.fujionclinical.api.model.impl.BaseDomainType;
import org.fujionclinical.api.model.patient.IPatient;
import org.fujionclinical.api.model.person.IPerson;

import java.util.ArrayList;
import java.util.List;

public class Condition extends BaseDomainType implements ICondition {

    private final List<IAnnotation> annotations = new ArrayList<>();

    private IReference<IPatient> patient;

    private IPeriod onset;

    private IReference<IPerson> recorder;

    private DateTimeWrapper recordedDate;

    private IReference<IPerson> asserter;

    private IConcept condition;

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
    public IPeriod getOnset() {
        return onset;
    }

    @Override
    public void setOnset(IPeriod onset) {
        this.onset = onset;
    }

    @Override
    public DateTimeWrapper getRecordedDate() {
        return recordedDate;
    }

    @Override
    public void setRecordedDate(DateTimeWrapper recordedDate) {
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
    public IConcept getCondition() {
        return condition;
    }

    @Override
    public void setCondition(IConcept condition) {
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
    public List<IAnnotation> getAnnotations() {
        return annotations;
    }

}