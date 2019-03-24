/*
 * #%L
 * Fujion Clinical Framework
 * %%
 * Copyright (C) 2019 fujionclinical.org
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
package org.fujionclinical.ui.session;

/**
 * Events used to control session state via administrator functions.
 */
public enum SessionControl {
    
    SHUTDOWN_START, SHUTDOWN_ABORT, SHUTDOWN_PROGRESS, LOCK;
    
    public static final String EVENT_ROOT = "SESSION_CONTROL";

    private static final String EVENT_PREFIX = EVENT_ROOT + ".";

    /**
     * Returns the enum member corresponding to the event name.
     *
     * @param eventName The event name.
     * @return The corresponding member, or null if none.
     */
    public static SessionControl fromEvent(String eventName) {
        if (eventName.startsWith(EVENT_PREFIX)) {
            String name = eventName.substring(EVENT_PREFIX.length()).replace(".", "_");

            try {
                return SessionControl.valueOf(name);
            } catch (Exception e) {
                return null;
            }
        }
        
        return null;
    }

    private final String eventName;
    
    SessionControl() {
        eventName = EVENT_PREFIX + name().replace("_", ".");
    }
    
    /**
     * Returns the event name for this member.
     *
     * @return The event name.
     */
    public String getEventName() {
        return eventName;
    }
}
