package org.fujionclinical.api.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Identifier {

    public enum IdentifierCategory {USUAL, OFFICIAL, TEMPORARY, SECONDARY, OLD}

    private final String system;

    private final String value;

    private final List<ConceptCode> types;

    private final IdentifierCategory category;

    public Identifier(String system, String value) {
        this(system, value, null, null);
    }

    public Identifier(String system, String value, IdentifierCategory category) {
        this(system, value, category, null);
    }

    public Identifier(String system, String value, IdentifierCategory category, ConceptCode... types) {
        this.system = system;
        this.value = value;
        this.category = category;
        this.types = types == null ? null : Collections.unmodifiableList(Arrays.asList(types));
    }

    public String getSystem() {
        return system;
    }

    public String getValue() {
        return value;
    }

    public List<ConceptCode> getTypes() {
        return types;
    }

    public IdentifierCategory getCategory() {
        return category;
    }

}
