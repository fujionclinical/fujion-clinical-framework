package org.fujionclinical.api.model;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class Concept implements IConcept {

    private final List<IConceptCode> codes = new ArrayList<>();

    private String text;

    public Concept(String text, IConceptCode... codes) {
        this.text = text;
        CollectionUtils.addAll(this.codes, codes);
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public IConcept setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public List<IConceptCode> getCodes() {
        return codes;
    }

}
