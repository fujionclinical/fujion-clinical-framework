package org.fujionclinical.api.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Identifier implements IIdentifier {

    private final String system;

    private final String value;

    private final List<IConceptCode> types = new ArrayList<>();

    private final IdentifierCategory category;

    public Identifier(
            String system,
            String value) {
        this(system, value, null);
    }

    public Identifier(
            String system,
            String value,
            IdentifierCategory category,
            IConceptCode... types) {
        this.system = system;
        this.value = value;
        this.category = category;
        Collections.addAll(this.types, types);
    }

    @Override
    public String getSystem() {
        return system;
    }

    @Override
    public String getValue() {
        return value;
    }

    public List<IConceptCode> getTypes() {
        return Collections.unmodifiableList(types);
    }

    public IdentifierCategory getCategory() {
        return category;
    }
}
