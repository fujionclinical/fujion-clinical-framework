package org.fujionclinical.hibernate.property;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PropertyId implements Serializable {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyId that = (PropertyId) o;
        return Objects.equals(name, that.name) && Objects.equals(instance, that.instance) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, instance, user);
    }
}
