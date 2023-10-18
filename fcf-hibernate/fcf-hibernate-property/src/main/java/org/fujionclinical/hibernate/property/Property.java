/*
 * #%L
 * Fujion Clinical Framework
 * %%
 * Copyright (C) 2023 fujionclinical.org
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This Source Code Form is also subject to the terms of the Health-Related
 * Additional Disclaimer of Warranty and Limitation of Liability available at
 *
 *      http://www.fujionclinical.org/licensing/disclaimer
 *
 * #L%
 */
package org.fujionclinical.hibernate.property;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Date;

@Entity
@Table(name = "FCF_PROPERTY")
public class Property implements Serializable {

    @EmbeddedId
    private final PropertyId id;

    @Version
    private OffsetDateTime timestamp;

    @Lob
    private String value;

    public Property() {
        id = new PropertyId();
    }

    public Property(String name) {
        this(name, null, null);
    }

    public Property(String name, String value) {
        this(name, value, null);
    }

    public Property(String name, String value, String instance) {
        this(name, value, instance, null);
    }

    public Property(String name, String value, String instance, String user) {
        this(new PropertyId(name, instance, user), value);
    }

    public Property(PropertyId id) {
        this(id, null);
    }

    public Property(PropertyId id, String value) {
        this.id = id;
        setValue(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return id.getName();
    }

    public String getInstance() {
        return id.getInstance();
    }

    public String getUser() {
        return id.getUser();
    }

}
