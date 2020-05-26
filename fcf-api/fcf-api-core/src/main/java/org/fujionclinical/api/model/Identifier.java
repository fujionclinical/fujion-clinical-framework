package org.fujionclinical.api.model;

import org.apache.commons.collections.CollectionUtils;

public class Identifier implements IIdentifier {

    private final String system;

    private final String value;

    private final IConcept type = new Concept(null);

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
        CollectionUtils.addAll(type.getCodes(), types);
    }

    @Override
    public String getSystem() {
        return system;
    }

    @Override
    public String getValue() {
        return value;
    }

    public IConcept getType() {
        return type;
    }

    public IdentifierCategory getCategory() {
        return category;
    }

}
