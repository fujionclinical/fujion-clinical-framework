/*
 * #%L
 * Fujion Clinical Framework
 * %%
 * Copyright (C) 2018 fujionclinical.org
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
package org.fujionclinical.shell.layout;

import java.util.ArrayList;
import java.util.List;

import org.fujionclinical.shell.plugins.PluginDefinition;
import org.fujionclinical.shell.plugins.PluginRegistry;

/**
 * Represents a UI element node within a layout.
 */
public class LayoutElement extends LayoutNode {

    public static class LayoutRoot extends LayoutElement {

        public LayoutRoot() {
            super();
        }
    }
    
    private final List<LayoutTrigger> triggers = new ArrayList<>();

    private LayoutElement() {
        super("layout", null, PluginRegistry.getInstance().get("_desktop"));
    }
    
    public LayoutElement(PluginDefinition pluginDefinition, LayoutElement parent) {
        super("element", parent, pluginDefinition);
    }

    protected List<LayoutTrigger> getTriggers() {
        return triggers;
    }

}
