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
package org.fujionclinical.shell.designer;

import org.apache.commons.lang3.StringUtils;
import org.fujion.ancillary.IAutoWired;
import org.fujion.ancillary.IResponseCallback;
import org.fujion.annotation.EventHandler;
import org.fujion.annotation.WiredComponent;
import org.fujion.component.*;
import org.fujion.event.ClickEvent;
import org.fujion.event.DblclickEvent;
import org.fujion.event.EventUtil;
import org.fujion.event.IEventListener;
import org.fujion.icon.IconUtil;
import org.fujion.page.PageUtil;
import org.fujionclinical.api.property.PropertyUtil;
import org.fujionclinical.shell.Constants;
import org.fujionclinical.shell.elements.ElementBase;
import org.fujionclinical.shell.elements.ElementLayout;
import org.fujionclinical.shell.elements.ElementPlugin;
import org.fujionclinical.shell.elements.ElementUI;
import org.fujionclinical.shell.layout.LayoutUtil;
import org.fujionclinical.shell.plugins.PluginDefinition;
import org.fujionclinical.shell.plugins.PluginRegistry;
import org.fujionclinical.ui.util.TreeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.fujionclinical.shell.Constants.MSG_DESIGNER_ADD_COMPONENT_TITLE;
import static org.fujionclinical.shell.Constants.MSG_PLUGIN_CATEGORY_DEFAULT;

/**
 * Dialog for adding new component to UI.
 */
public class AddComponent implements IAutoWired {

    /**
     * Display the add component dialog, presenting a list of candidate plugins that may serve as
     * children to the specified parent element.
     *
     * @param parentElement Element to serve as parent to the newly created child element.
     * @param callback      Callback to return the newly created child element.
     */
    public static void newChild(
            ElementBase parentElement,
            IResponseCallback<ElementBase> callback) {
        show(parentElement, true, (event) -> {
            if (callback != null) {
                callback.onComplete(event.getTarget().getAttribute("childElement", ElementBase.class));
            }
        });
    }

    /**
     * Display the add component dialog, presenting a list of candidate plugins that may serve as
     * children to the specified parent element.
     *
     * @param parentElement Element to serve as parent to the newly created child element.
     * @param callback      Callback to return the plugin definition.
     */
    public static void getDefinition(
            ElementBase parentElement,
            IResponseCallback<PluginDefinition> callback) {
        show(parentElement, false, (event) -> {
            if (callback != null) {
                callback.onComplete(event.getTarget().getAttribute("pluginDefinition", PluginDefinition.class));
            }
        });
    }

    /**
     * Display the add component dialog, presenting a list of candidate plugins that may serve as
     * children to the specified parent element.
     *
     * @param parentElement Element to serve as parent to the newly created child element.
     * @param createChild   If true, the selected element will be created.
     * @param callback      The close event handler.
     */
    private static void show(
            ElementBase parentElement,
            boolean createChild,
            IEventListener callback) {
        Map<String, Object> args = new HashMap<>();
        args.put("parentElement", parentElement);
        args.put("createChild", createChild);
        Window dlg = (Window) PageUtil.createPage(Constants.RESOURCE_PREFIX_DESIGNER + "addComponent.fsp", null, args).get(0);
        dlg.modal(callback);
    }
    private static final String ON_FAVORITE = "favorite";
    /**
     * Handles click on item under favorites category.
     */
    private final IEventListener favoriteListener2 = (event) -> {
        Treenode node = (Treenode) event.getTarget();
        Treenode other = (Treenode) node.getAttribute("other");
        EventUtil.send(ON_FAVORITE, other, null);
    };

    private ElementBase parentElement;

    private Window window;

    private boolean createChild;

    private List<String> favorites;

    private boolean favoritesChanged;

    @WiredComponent
    private Treenode tnFavorites;

    @WiredComponent
    private Treeview tree;

    @WiredComponent
    private Button btnOK;

    /**
     * Handles click on item not under favorites category.
     */
    private final IEventListener favoriteListener1 = (event) -> {
        Treenode node = (Treenode) event.getTarget();
        String path = (String) node.getAttribute("path");
        boolean isFavorite = !node.getAttribute("favorite", false);
        Treenode other = (Treenode) node.getAttribute("other");
        favoritesChanged = true;

        if (isFavorite) {
            favorites.add(path);
            other = addTreenode((PluginDefinition) node.getData(), node);
            node.setAttribute("other", other);
        } else {
            favorites.remove(path);
            other.detach();
        }

        setFavoriteStatus(node, isFavorite);
    };

    /**
     * Initialize the tree view based with list of plugins that may serve as children to the parent
     * element.
     *
     * @param comp The root component.
     */
    @Override
    public void afterInitialized(BaseComponent comp) {
        window = (Window) comp;
        this.parentElement = comp.getAttribute("parentElement", ElementBase.class);
        this.createChild = comp.getAttribute("createChild", false);
        Treenode defaultItem = null;
        boolean useDefault = true;
        loadFavorites();

        for (PluginDefinition def : PluginRegistry.getInstance()) {
            if (def.isInternal()) {
                continue;
            }

            Class<? extends ElementBase> clazz = def.getClazz();

            if (!parentElement.canAcceptChild(clazz) || !ElementBase.canAcceptParent(clazz, parentElement.getClass())) {
                continue;
            }

            Treenode item = addTreenode(def, null);

            if (item == null) {
                continue;
            }

            if (!item.isDisabled()) {
                if (defaultItem == null) {
                    defaultItem = item;
                } else {
                    useDefault = false;
                }
            }
        }

        if (parentElement.canAcceptChild(ElementLayout.class)) {
            addLayouts(true);
            addLayouts(false);
        }

        TreeUtil.sort(tree);
        tnFavorites.setIndex(0);

        if (useDefault && defaultItem != null) {
            defaultItem.setSelected(true);
            onChange$tree();
        }

        window.setTitle(MSG_DESIGNER_ADD_COMPONENT_TITLE.toString(parentElement.getDefinition().getName()));
        window.setOnCanClose(() -> {
            if (favoritesChanged) {
                PropertyUtil.saveValues(Constants.DESIGNER_FAVORITES_PROPERTY, null, false, favorites);
            }

            return true;
        });
    }

    private void loadFavorites() {
        try {
            favorites = PropertyUtil.getValues(Constants.DESIGNER_FAVORITES_PROPERTY);
            favorites = favorites == null ? new ArrayList<>() : favorites;
        } catch (Exception e) {
            favorites = null;
        }
    }

    private void addLayouts(boolean shared) {
        for (String layout : LayoutUtil.getLayouts(shared)) {
            ElementLayout ele = new ElementLayout(layout, shared);
            addTreenode(ele.getDefinition(), null);
        }
    }

    private Treenode addTreenode(
            PluginDefinition def,
            Treenode other) {
        String category = other != null ? Constants.MSG_PLUGIN_CATEGORY_FAVORITE.toString() : def.getCategory();

        if (StringUtils.isEmpty(category)) {
            if (ElementPlugin.class.isAssignableFrom(def.getClazz())) {
                category = MSG_PLUGIN_CATEGORY_DEFAULT.toString();
            } else {
                return null;
            }
        }

        String path = category + (category.endsWith("\\") ? "" : "\\") + def.getName();
        boolean isFavorite = other != null || (favorites != null && favorites.contains(path));
        boolean disabled = def.isDisabled() || def.isForbidden();
        Treenode node = TreeUtil.findNode(tree, path, true);
        node.setData(def);
        node.setHint(StringUtils.defaultString(def.getDescription(), Constants.MSG_DESIGNER_MISSING_HINT.toString()));
        node.addEventListener(ClickEvent.TYPE, (event) -> {
            if (event.getTargetId().endsWith("-img")) {
                EventUtil.send(ON_FAVORITE, event.getTarget(), null);
            }
        });

        if (disabled) {
            node.setDisabled(true);
        } else {
            node.addEventForward(DblclickEvent.TYPE, btnOK, ClickEvent.TYPE);
        }

        if (favorites != null) {
            node.addEventListener(ON_FAVORITE, other == null ? favoriteListener1 : favoriteListener2);

            if (isFavorite && other == null) {
                other = addTreenode(def, node);
            }

            node.setAttribute("other", other);
            node.setAttribute("path", path);
            setFavoriteStatus(node, isFavorite);
        }
        return node;
    }

    /**
     * Updates the tree node according to the favorite status.
     *
     * @param node       Tree node to update.
     * @param isFavorite If true, the node is a favorite.
     */
    private void setFavoriteStatus(
            Treenode node,
            boolean isFavorite) {
        String img = IconUtil.getIconPath(isFavorite ? "starOn.png" : "starOff.png", "16x16", null);
        node.setImage(img);
        node.setAttribute("favorite", isFavorite);
        tnFavorites.setVisible(isFavorite || tnFavorites.getFirstChild() != null);
    }

    /**
     * Returns currently selected plugin definition, or null if none selected.
     *
     * @return Definition of the currently selected plugin.
     */
    private PluginDefinition selectedPluginDefinition() {
        return (PluginDefinition) (tree.getSelectedNode() == null ? null : tree.getSelectedNode().getData());
    }

    private void returnResult(PluginDefinition definition) {
        if (definition != null) {
            ElementBase childElement = createChild ? definition.createElement(parentElement, null, false) : null;

            if (childElement instanceof ElementUI) {
                ((ElementUI) childElement).bringToFront();
            }

            window.setAttribute("pluginDefinition", definition);
            window.setAttribute("childElement", childElement);
            window.close();
        }
    }

    /**
     * Close dialog without further action.
     */
    @EventHandler(value = "click", target = "btnCancel")
    private void onClick$btnCancel() {
        window.close();
    }

    /**
     * Create new element based on selected plugin definition, add it to the parent element and
     * close the dialog.
     */
    @EventHandler(value = "click", target = "@btnOK")
    private void onClick$btnOK() {
        returnResult(selectedPluginDefinition());
    }

    /**
     * Update buttons based on current selection.
     */
    @EventHandler(value = "change", target = "@tree")
    private void onChange$tree() {
        btnOK.setDisabled(selectedPluginDefinition() == null);
    }

}
