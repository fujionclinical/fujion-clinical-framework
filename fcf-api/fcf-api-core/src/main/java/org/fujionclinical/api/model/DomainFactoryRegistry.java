/*
 * #%L
 * Fujion Clinical Framework
 * %%
 * Copyright (C) 2020 fujionclinical.org
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
package org.fujionclinical.api.model;

import org.fujionclinical.api.spring.BeanRegistry;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Tracks all domain factory implementations.
 */
@SuppressWarnings("rawtypes")
public class DomainFactoryRegistry extends BeanRegistry<Class<?>, IDomainFactory> {

    private static final DomainFactoryRegistry instance = new DomainFactoryRegistry();

    private DomainFactoryRegistry() {
        super(IDomainFactory.class);
    }

    public static DomainFactoryRegistry getInstance() {
        return instance;
    }

    /**
     * Creates a new instance of an object of this domain.
     *
     * @param <T>   Class of domain object.
     * @param clazz Class of object to create.
     * @return The new domain object instance.
     */
    public static <T extends IDomainObject> T newObject(Class<T> clazz) {
        return getFactory(clazz).create();
    }

    /**
     * Fetches an object, identified by its unique id, from the underlying data store.
     *
     * @param <T>   Class of domain object.
     * @param clazz Class of object to create.
     * @param id    Unique id of the object.
     * @return The requested object.
     */
    public static <T extends IDomainObject> T fetchObject(
            Class<T> clazz,
            String id) {
        return getFactory(clazz).fetchObject(id);
    }

    /**
     * Fetches multiple domain objects as specified by an array of identifier values.
     *
     * @param <T>   Class of domain object.
     * @param clazz Class of object to create.
     * @param ids   An array of unique identifiers.
     * @return A list of domain objects in the same order as requested in the ids parameter.
     */
    public static <T extends IDomainObject> List<T> fetchObjects(
            Class<T> clazz,
            String[] ids) {
        return getFactory(clazz).fetchObjects(ids);
    }

    /**
     * Returns a domain factory for the specified class.
     *
     * @param <T>   Class of domain object.
     * @param clazz Class of object created by factory.
     * @return A domain object factory.
     */
    public static <T extends IDomainObject> IDomainFactory<T> getFactory(Class<T> clazz) {
        IDomainFactory factory = instance.get(clazz);
        Assert.notNull(factory, "Domain class has no registered factory: " + clazz.getName());
        return factory;
    }

    @Override
    protected Class<?> getKey(IDomainFactory item) {
        return item.getDomainClass();
    }

}
