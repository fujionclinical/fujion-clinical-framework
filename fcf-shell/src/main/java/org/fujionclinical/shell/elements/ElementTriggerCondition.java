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
package org.fujionclinical.shell.elements;

import org.fujionclinical.shell.triggers.ITriggerCallback;
import org.fujionclinical.shell.triggers.ITriggerCondition;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Conditional logic for a trigger.
 */
public abstract class ElementTriggerCondition extends ElementBase implements ITriggerCondition {

    static {
        registerAllowedParentClass(ElementTriggerCondition.class, ElementTrigger.class);
    }
    
    private final Set<ITriggerCallback> callbacks = new HashSet<>();
    
    protected ElementTrigger getTrigger() {
        return (ElementTrigger) getParent();
    }
    
    @Override
    public void registerCallback(ITriggerCallback callback) {
        callbacks.add(callback);
    }

    @Override
    public void unregisterCallback(ITriggerCallback callback) {
        callbacks.remove(callback);
    }

    protected Set<ITriggerCallback> getCallbacks() {
        return Collections.unmodifiableSet(callbacks);
    }

    protected void invokeCallbacks() {
        if (isEnabled() && !isDesignMode()) {
            for (ITriggerCallback callback : callbacks) {
                callback.onTrigger();
            }
        }
    }

}
