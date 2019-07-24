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
package org.fujionclinical.ui.xml;

import org.fujion.ancillary.IAutoWired;
import org.fujion.annotation.EventHandler;
import org.fujion.annotation.WiredComponent;
import org.fujion.component.*;
import org.w3c.dom.Document;

import java.util.Iterator;

/**
 * Controller for XMLViewer.
 */
public class XMLViewerController implements IAutoWired {

    /**
     * Renderer for tree.
     */
    private static final XMLViewerRenderer renderer = new XMLViewerRenderer();

    @WiredComponent
    private Treeview tree;

    @WiredComponent
    private Textbox txtSearch;

    @WiredComponent
    private Label lblNotFound;

    private Iterator<Treenode> iterator;

    /**
     * Sets the renderer.
     */
    @Override
    public void afterInitialized(BaseComponent comp) {
        Document document = comp.getAttribute("document", Document.class);
        tree.setRenderer(renderer);
        tree.setModel(new XMLTreeModel(document.getDocumentElement()));
    }

    /**
     * Selecting an item sets it as the starting point for a search.
     */
    @EventHandler(value = "change", target = "@tree")
    private void onChange$tree() {
        Treenode node = tree.getSelectedNode();
        iterator = tree.iterator();

        while (iterator.next() != node) {
            //NOP
        }
    }

    /**
     * Perform a search.
     */
    @EventHandler(value = "click", target = "btnSearch")
    private void onClick$btnSearch() {
        txtSearch.setFocus(true);
        lblNotFound.setVisible(false);
        String text = txtSearch.getValue();
        tree.setSelectedNode(null);

        if (text != null && !text.isEmpty()) {
            text = text.toLowerCase();
            iterator = iterator == null ? tree.iterator() : iterator;

            while (iterator.hasNext()) {
                Treenode node = iterator.next();

                if (node.getLabel().toLowerCase().contains(text)) {
                    node.makeVisible();
                    node.setSelected(true);
                    return;
                }
            }

            iterator = null;
            lblNotFound.setVisible(true);
        }
    }

}
