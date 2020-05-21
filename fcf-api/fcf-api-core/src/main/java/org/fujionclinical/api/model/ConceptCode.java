package org.fujionclinical.api.model;

public class ConceptCode {

    private final String system;

    private final String code;

    private final String text;

    public ConceptCode(String system, String code) {
        this(system, code, null);
    }

    public ConceptCode(String system, String code, String text) {
        this.system = system;
        this.code = code;
        this.text = text;
    }

    public String getSystem() {
        return system;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
