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
package org.fujionclinical.ui.command;

import org.fujion.component.BaseComponent;
import org.fujion.core.BeanUtil;
import org.fujion.event.Event;

/**
 * Event object for sending commands to components.
 */
public class CommandEvent extends Event {

    public static final String TYPE = "command";

    private final Event triggerEvent;

    private final String commandName;

    public CommandEvent(String commandName, Event triggerEvent, BaseComponent target) {
        super(TYPE, target);
        this.commandName = commandName;
        this.triggerEvent = triggerEvent;
    }

    public String getCommandName() {
        return commandName;
    }

    public Event getTriggerEvent() {
        return triggerEvent;
    }

    public BaseComponent getReference() {
        try {
            return triggerEvent == null ? null : BeanUtil.getPropertyValue(triggerEvent, "reference", BaseComponent.class);
        } catch (Exception e) {
            return null;
        }
    }
}
