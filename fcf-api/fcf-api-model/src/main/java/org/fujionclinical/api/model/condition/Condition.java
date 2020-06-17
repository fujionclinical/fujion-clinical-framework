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
