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
package org.fujionclinical.ui.xml;

import org.fujion.component.BaseComponent;
import org.fujion.component.BaseUIComponent;
import org.fujion.component.Window;
import org.fujion.dialog.PopupDialog;
import org.w3c.dom.Document;

import java.util.Collections;
import java.util.Map;

/**
 * Static utility class for XML viewing functions.
 */
public class XMLViewer {

    /**
     * Show the dialog, loading the specified document.
     *
     * @param document The XML document.
     * @return The dialog.
     */
    public static Window showXML(Document document) {
        return showXML(document, null);
    }

    /**
     * Show the dialog, loading the specified document.
     *
     * @param document The XML document.
     * @param parent If specified, show viewer in embedded mode. Otherwise, show as modal dialog.
     * @return The dialog.
     */
    public static Window showXML(Document document, BaseUIComponent parent) {
        Map<String, Object> args = Collections.singletonMap("document", document);
        boolean modal = parent == null;
        Window dialog = PopupDialog.show(XMLConstants.VIEW_DIALOG, args, modal, modal, modal, null);
        
        if (parent != null) {
            dialog.setParent(parent);
        }
        
        return dialog;
    }

    /**
     * Display the FCF markup for the component tree rooted at root.
     *
     * @param root Root component of tree.
     * @return The dialog.
     */
    public static Window showFCF(BaseComponent root) {
        return showFCF(root, XMLConstants.EXCLUDED_PROPERTIES);

    }

    /**
     * Display the FCF markup for the component tree rooted at root.
     *
     * @param root Root component of tree.
     * @param excludedProperties Excluded properties.
     * @return The dialog.
     */
    public static Window showFCF(BaseComponent root, String... excludedProperties) {
        Window window = showXML(FCF2XML.toDocument(root, excludedProperties));
        window.setTitle("FCF Markup");
        return window;
    }

    /**
     * Enforce static class.
     */
    private XMLViewer() {
    }
}
