package org.fujionclinical.api.user;

import edu.utah.kmm.model.cool.foundation.entity.Person;
import edu.utah.kmm.model.cool.util.PersonUtils;
import org.fujionclinical.api.security.ISecurityDomain;

import java.util.Collections;
import java.util.List;

public interface User extends Person {

    default String getFullName() {
        return PersonUtils.getFullName(this);
    }

    default String getId() {
        return hasDefaultId() ? getDefaultId().getId() : null;
    }

    default void setId(String id) {
        setDefaultId(id);
    }

    String getUsername();

    String getPassword();

    ISecurityDomain getSecurityDomain();

    default List<String> getGrantedAuthorities() {
        return getSecurityDomain().getGrantedAuthorities(this);
    }
}
