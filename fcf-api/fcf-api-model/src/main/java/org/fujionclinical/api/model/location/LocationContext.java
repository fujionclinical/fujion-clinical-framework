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
package org.fujionclinical.api.model.location;

import edu.utah.kmm.model.cool.foundation.entity.Location;
import org.fujionclinical.api.context.ContextItems;
import org.fujionclinical.api.context.ContextManager;
import org.fujionclinical.api.context.IContextSubscriber;
import org.fujionclinical.api.model.common.AbstractIdentifiableContext;

/**
 * Wrapper for shared location context.
 */
public class LocationContext extends AbstractIdentifiableContext<Location> {

    private static final String SUBJECT_NAME = "Location";

    //private static final String PROPERTY_DEFAULT_LOCATION = "FCF LOCATION DEFAULT";

    public interface ILocationContextEvent extends IContextSubscriber {

    }

    /**
     * Create a shared location context with an initial null state.
     */
    public LocationContext() {
        this(null);
    }

    /**
     * Returns the managed location context.
     *
     * @return Location context.
     */
    public static LocationContext getLocationContext() {
        return ContextManager.getSharedContext(LocationContext.class);
    }

    /**
     * Returns the location in the current context.
     *
     * @return Location object (may be null).
     */
    public static Location getActiveLocation() {
        return ContextManager.getCurrentValue(LocationContext.class);
    }

    /**
     * Request a location context change.
     *
     * @param location New location.
     */
    public static void changeLocation(Location location) {
        ContextManager.changeContext(LocationContext.class, location);
    }

    /**
     * Create a shared location context with a specified initial state.
     *
     * @param location Location that will be the initial state.
     */
    public LocationContext(Location location) {
        super(SUBJECT_NAME, ILocationContextEvent.class, location);
    }

    /**
     * Creates a CCOW context from the specified location object.
     */
    @Override
    protected ContextItems toCCOWContext(Location location) {
        //TODO: contextItems.setItem(...);
        return contextItems;
    }

    /**
     * Returns a location object based on the specified CCOW context.
     */
    @Override
    protected Location fromCCOWContext(ContextItems contextItems) {
        return null;
    }

}
