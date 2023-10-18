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
package org.fujionclinical.shell.elements;

import org.fujion.component.Span;
import org.fujionclinical.shell.designer.PropertyEditorMenubar;
import org.fujionclinical.shell.property.PropertyTypeRegistry;

/**
 * Base implementation of a menu bar.
 */
public class ElementMenubar extends ElementUI {

    static {
        registerAllowedChildClass(ElementMenubar.class, ElementMenuItem.class, Integer.MAX_VALUE);
        registerAllowedParentClass(ElementMenubar.class, ElementUI.class);
        PropertyTypeRegistry.register("menuitems", PropertyEditorMenubar.class);
    }

    private final Span menubar;

    public ElementMenubar() {
        this(new Span());
    }

    public ElementMenubar(Span menubar) {
        setOuterComponent(this.menubar = menubar);
        menubar.addClass("fcf-menubar");
    }

    /**
     * Returns a reference to the menu bar.
     *
     * @return The menu bar.
     */
    public Span getMenubar() {
        return menubar;
    }

}
