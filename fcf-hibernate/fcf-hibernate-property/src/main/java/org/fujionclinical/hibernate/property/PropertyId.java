package org.fujionclinical.hibernate.property;

import jakarta.persistence.Embeddable;

import java.io.Serial;
import java.io.Serializable;

@Embeddable
public class PropertyId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String name;

    private String instance;

    private String user;

    public PropertyId(String name, String instance, String user) {
        this.name = name;
        this.instance = instance == null ? "" : instance;
        this.user = user == null ? "" : user;
    }

    protected PropertyId() {

    }

    public String getName() {
        return name;
    }

    public String getInstance() {
        return instance;
    }

    public String getUser() {
        return user;
    }
}
