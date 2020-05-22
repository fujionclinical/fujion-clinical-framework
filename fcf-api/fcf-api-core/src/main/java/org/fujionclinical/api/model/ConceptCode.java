package org.fujionclinical.api.model;

public class ConceptCode implements IConceptCode {

    private final String system;

    private final String code;

    private final String text;

    public ConceptCode(
            String system,
            String code) {
        this(system, code, null);
    }

    public ConceptCode(
            String system,
            String code,
            String text) {
        this.system = system;
        this.code = code;
        this.text = text;
    }

    @Override
    public String getSystem() {
        return system;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getText() {
        return text;
    }
}
