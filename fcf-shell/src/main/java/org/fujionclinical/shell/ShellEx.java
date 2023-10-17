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
package org.fujionclinical.shell;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fujion.annotation.Component;
import org.fujion.annotation.Component.ChildTag;
import org.fujion.common.MiscUtil;
import org.fujion.core.BeanUtil;
import org.fujionclinical.api.property.IPropertyProvider;
import org.fujionclinical.api.property.PropertyUtil;
import org.fujionclinical.shell.ancillary.FCFException;
import org.fujionclinical.shell.elements.*;
import org.fujionclinical.shell.layout.Layout;
import org.fujionclinical.shell.layout.LayoutParser;
import org.fujionclinical.shell.plugins.PluginDefinition;
import org.fujionclinical.shell.plugins.PluginException;
import org.fujionclinical.shell.plugins.PluginRegistry;

import static org.fujionclinical.shell.Constants.MSG_PLUGIN_UNKNOWN;

/**
 * This class is provided primarily for backward compatibility with the old fixed layout of tab
 * views containing tree views. It can only be used with a layout that contains a tab view
 * component. Its public methods are fully compatible with version 1.0.
 */
@Component(tag = "fcfShellEx", widgetModule = "fcf-shell", widgetClass = "ShellEx", parentTag = "*", childTag = @ChildTag("*"))
public class ShellEx extends Shell {

    public static final String TOOLBAR_PATH = "@toolbar";

    private static final String delim = "\\\\";

    private static final Log log = LogFactory.getLog(ShellEx.class);

    /**
     * Locates the plugin's parent UI element given a tab pane and a path.
     */
    public class PathResolver {

        private final Class<? extends ElementUI> rootClass;

        private final Class<? extends ElementUI> childClass;

        /**
         * Creates a path resolver.
         *
         * @param rootClass  This is the class that will hold instances of the child class.
         * @param childClass This is the class that will hold the plugin.
         */
        public PathResolver(Class<? extends ElementUI> rootClass, Class<? extends ElementUI> childClass) {
            if (!ElementBase.canAcceptChild(rootClass, childClass) || !ElementBase.canAcceptParent(childClass, rootClass)
                    || !ElementBase.canAcceptParent(childClass, childClass)
                    || !ElementBase.canAcceptChild(childClass, ElementPlugin.class)) {
                throw new FCFException("Root and child classes are not compatible.");
            }
            this.rootClass = rootClass;
            this.childClass = childClass;
        }

        /**
         * Resolves the path, returning the UI element to be used as the parent of the plugin.
         *
         * @param tabPane Tab pane.
         * @param path    Path to resolve.
         * @return The plugin parent
         */
        protected ElementUI resolvePath(ElementTabPane tabPane, String path) {
            return getElement(path, getRoot(tabPane), childClass);
        }

        /**
         * Returns the root for the specified tab pane. If the tab pane does not yet have a root,
         * one will be created for it.
         *
         * @param tabPane The tab pane.
         * @return The root.
         */
        protected ElementUI getRoot(ElementTabPane tabPane) {
            ElementUI root = tabPane.findChildElement(rootClass);

            if (root == null) {
                try {
                    root = rootClass.newInstance();
                    root.setParent(tabPane);
                } catch (Exception e) {
                    throw MiscUtil.toUnchecked(e);
                }
            }

            return root;
        }

    }

    private ElementTabView tabView;

    private String defaultPluginId;

    private PathResolver pathResolver;

    private final PluginRegistry pluginRegistry = PluginRegistry.getInstance();

    public ShellEx() {
        super();
    }

    /**
     * Registers the plugin with the specified id and path. If a tree path is absent, the plugin is
     * associated with the tab itself.
     *
     * @param path Format is &lt;tab name&gt;\&lt;tree node path&gt;
     * @param id   Unique id of plugin
     * @return Container created for the plugin.
     * @throws Exception Unspecified exception.
     */
    public ElementBase registerFromId(String path, String id) throws Exception {
        return registerFromId(path, id, null);
    }

    /**
     * Registers the plugin with the specified id and path. If a tree path is absent, the plugin is
     * associated with the tab itself.
     *
     * @param path           Format is &lt;tab name&gt;\&lt;tree node path&gt;
     * @param id             Unique id of plugin
     * @param propertySource Optional source for retrieving property values.
     * @return Container created for the plugin.
     * @throws Exception Unspecified exception.
     */
    public ElementBase registerFromId(String path, String id, IPropertyProvider propertySource) throws Exception {
        return register(path, pluginById(id), propertySource);
    }

    /**
     * Lookup a plugin definition by its id. Raises a runtime exception if the plugin is not found.
     *
     * @param id Plugin id.
     * @return The plugin definition.
     */
    private PluginDefinition pluginById(String id) {
        PluginDefinition def = pluginRegistry.get(id);

        if (def == null) {
            throw new PluginException(MSG_PLUGIN_UNKNOWN.toString(), null, null, id);
        }

        return def;
    }

    /**
     * Register a plugin by specifying a path and a url.
     *
     * @param path Format is &lt;tab name&gt;\&lt;tree node path&gt;
     * @param url  Main url of plugin.
     * @return Container created for the plugin.
     * @throws Exception Unspecified exception.
     */
    public ElementBase register(String path, String url) throws Exception {
        return register(path, url, null);
    }

    /**
     * Register a plugin by specifying a path and a url.
     *
     * @param path           Format is &lt;tab name&gt;\&lt;tree node path&gt;
     * @param url            Main url of plugin.
     * @param propertySource Optional source for retrieving property values.
     * @return Container created for the plugin.
     * @throws Exception Unspecified exception.
     */
    public ElementBase register(String path, String url, IPropertyProvider propertySource) throws Exception {
        PluginDefinition def = new PluginDefinition();
        def.setUrl(url);
        return register(path, def, propertySource);
    }

    /**
     * Register a menu.
     *
     * @param path   Path for the menu.
     * @param action Associated action for the menu.
     * @return Created menu.
     */
    public ElementMenuItem registerMenu(String path, String action) {
        ElementMenuItem menu = getElement(path, getDesktop().getMenubar(), ElementMenuItem.class);
        menu.setAction(action);
        return menu;
    }

    private <T extends ElementUI> T getElement(String path, ElementBase root, Class<T> childClass) {
        ElementBase parent = root;
        T ele = null;

        try {
            for (String pc : path.split("\\\\")) {
                ele = null;

                for (ElementBase child : parent.getChildren()) {
                    if (!childClass.isInstance(child)) {
                        continue;
                    }

                    @SuppressWarnings("unchecked")
                    T ele2 = (T) child;

                    if (pc.equalsIgnoreCase(BeanUtil.getPropertyValue(ele2, "label", String.class))) {
                        ele = ele2;
                        break;
                    }
                }

                if (ele == null) {
                    ele = childClass.newInstance();
                    ele.setParent(parent);
                    BeanUtil.setPropertyValue(ele, "label", pc);
                }

                parent = ele;
            }
        } catch (Exception e) {
            throw MiscUtil.toUnchecked(e);
        }

        return ele;
    }

    /**
     * Registers the plugin with the specified definition with the specified path. If a tree path is
     * absent, the plugin is associated with the tab itself.
     *
     * @param path Format is &lt;tab name&gt;\&lt;tree node path&gt;
     * @param def  Plugin definition
     * @return The newly created plugin.
     * @throws Exception Unspecified exception.
     */
    public ElementBase register(String path, PluginDefinition def) throws Exception {
        return register(path, def, null);
    }

    /**
     * Registers the plugin with the specified definition with the specified path. If a tree path is
     * absent, the plugin is associated with the tab itself.
     *
     * @param path           Format is &lt;tab name&gt;\&lt;tree node path&gt;
     * @param def            Plugin definition
     * @param propertySource Optional source for retrieving property values.
     * @return The newly created plugin.
     * @throws Exception Unspecified exception.
     */
    public ElementBase register(String path, PluginDefinition def, IPropertyProvider propertySource) throws Exception {
        if (def.isForbidden()) {
            log.info("Access to plugin " + def.getName() + " is restricted.");
            return null;
        }

        if (def.isDisabled()) {
            log.info("Plugin " + def.getName() + " is disabled.");
            return null;
        }

        ElementBase parent = parentFromPath(path);
        ElementBase plugin = parent == null ? null : def.createElement(parent, propertySource, false);
        String defPluginId = getDefaultPluginId();

        if (plugin instanceof ElementUI && !defPluginId.isEmpty()
                && (defPluginId.equalsIgnoreCase(def.getId()) || defPluginId.equalsIgnoreCase(def.getName()))) {
            ((ElementUI) plugin).activate(true);
        }
        return plugin;
    }

    /**
     * Registers a layout at the specified path.
     *
     * @param path     Format is &lt;tab name&gt;\&lt;tree node path&gt;
     * @param resource Location of the xml layout.
     * @throws Exception Unspecified exception.
     */
    public void registerLayout(String path, String resource) throws Exception {
        Layout layout = LayoutParser.parseResource(resource);
        ElementUI parent = parentFromPath(path);

        if (parent != null) {
            layout.materialize(parent);
        }
    }

    /**
     * Returns the parent UI element based on the provided path.
     *
     * @param path Format is &lt;tab name&gt;\&lt;tree node path&gt;
     * @return The parent UI element.
     */
    private ElementUI parentFromPath(String path) {
        if (TOOLBAR_PATH.equalsIgnoreCase(path)) {
            return getDesktop().getToolbar();
        }

        String[] pieces = path.split(delim, 2);
        ElementTabPane tabPane = pieces.length == 0 ? null : findTabPane(pieces[0]);
        ElementUI parent = pieces.length < 2 ? null : getPathResolver().resolvePath(tabPane, pieces[1]);
        return parent == null ? tabPane : parent;
    }

    /**
     * Locate the tab with the corresponding label, or create one if not found.
     *
     * @param name Label text of tab to find.
     * @return Tab corresponding to label text.
     */
    private ElementTabPane findTabPane(String name) {
        ElementTabView tabView = getTabView();
        ElementTabPane tabPane = null;

        while ((tabPane = tabView.getChild(ElementTabPane.class, tabPane)) != null) {
            if (name.equalsIgnoreCase(tabPane.getLabel())) {
                return tabPane;
            }
        }

        tabPane = new ElementTabPane();
        tabPane.setParent(tabView);
        tabPane.setLabel(name);
        return tabPane;
    }

    /**
     * Returns the tab view that will receive plug-ins. Searches the UI desktop for the first
     * occurrence of a tab view that it finds. The result is cached. This may return null if a tab
     * view is not found.
     *
     * @return The target tab view, or null if not found.
     */
    private ElementTabView getTabView() {
        if (tabView == null) {
            tabView = getDesktop().findChildElement(ElementTabView.class);
        }

        return tabView;
    }

    /**
     * Returns the default plugin id as a user preference.
     *
     * @return The default plugin id.
     */
    private String getDefaultPluginId() {
        if (defaultPluginId == null) {
            try {
                defaultPluginId = PropertyUtil.getValue("FCF.INITIAL.SECTION", null);

                if (defaultPluginId == null) {
                    defaultPluginId = "";
                }
            } catch (Exception e) {
                defaultPluginId = "";
            }
        }

        return defaultPluginId;
    }

    /**
     * Returns the path resolver implementation. This implementation determines where in the layout
     * the plugin should be placed based on a path. A default implementation is provided.
     *
     * @return The path resolver.
     */
    public PathResolver getPathResolver() {
        if (pathResolver == null) {
            pathResolver = new PathResolver(ElementTreeView.class, ElementTreePane.class);
        }

        return pathResolver;
    }

    /**
     * Sets the path resolver implementation. This must be set before any resources are registered.
     *
     * @param pathResolver The path resolver.
     */
    public void setPathResolver(PathResolver pathResolver) {
        if (this.pathResolver != null) {
            throw new FCFException("A path resolver can only be set once.");
        }

        this.pathResolver = pathResolver;
    }

}
