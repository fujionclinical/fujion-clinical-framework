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
package org.fujionclinical.shell;

import org.apache.commons.lang3.StringUtils;
import org.fujion.annotation.EventHandler;
import org.fujion.annotation.WiredComponent;
import org.fujion.common.MiscUtil;
import org.fujion.common.StrUtil;
import org.fujion.component.*;
import org.fujion.dialog.DialogUtil;
import org.fujion.page.PageUtil;
import org.fujionclinical.shell.elements.ElementBase;
import org.fujionclinical.shell.plugins.PluginDefinition;
import org.fujionclinical.ui.controller.FrameworkController;
import org.fujionclinical.ui.manifest.ManifestViewer;
import org.fujionclinical.ui.util.FCFUtil;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Displays an "about" dialog for a given UI element.
 */
public class AboutDialog extends FrameworkController {
    
    /**
     * Internal class for passing about attributes to about dialog.
     */
    @SuppressWarnings("serial")
    private static class AboutParams extends LinkedHashMap<String, String> {
        
        private String icon;
        
        private String title;
        
        private String source;
        
        private String custom;
        
        private String description;
        
        public AboutParams(PluginDefinition def) {
            super();
            title = def.getName();
            source = def.getSource();
            icon = def.getIcon();
            description = def.getDescription();
            set("name", def.getName());
            set("version", def.getVersion());
            set("creator", def.getCreator());
            set("copyright", def.getCopyright());
            set("release", def.getReleased());
        }
        
        public AboutParams(Manifest manifest) {
            super();
            
            if (manifest != null) {
                Attributes attributes = manifest.getMainAttributes();
                title = attributes.getValue("Implementation-Title");
                source = attributes.getValue("Implementation-Vendor");
                custom = getLabel("installation.details");
                description = attributes.getValue("Description");
                set("name", title);
                set("version", attributes.getValue("Implementation-Version"));
            }
        }
        
        public AboutParams(Class<?> clazz) {
            super();
            Package pkg = clazz.getPackage();
            String name = clazz.getSimpleName();
            title = pkg.getImplementationTitle();
            title = StringUtils.isEmpty(title) ? name : title;
            source = pkg.getImplementationVendor();
            set("name", name);
            set("pkg", pkg.getName());
            set("version", pkg.getImplementationVersion());
        }
        
        /**
         * Add first non-null/non-empty value to map.
         * 
         * @param key The key to be added (is translated to a label value).
         * @param values The list of possible values.
         */
        private void set(String key, String... values) {
            String value = get(values);
            
            if (value != null) {
                put(getLabel(key), value);
            }
        }
        
        /**
         * Return first non-null/non-empty value.
         * 
         * @param values The list of possible values.
         * @return The first non-null/non-empty value, or null if there are none.
         */
        private String get(String... values) {
            if (values != null) {
                for (String value : values) {
                    if (!StringUtils.isEmpty(value)) {
                        return value;
                    }
                }
            }
            return null;
        }
        
        /**
         * Returns the label value for the specified key.
         * 
         * @param key Key for label.
         * @return The label value, or the original key value if no label value found.
         */
        private String getLabel(String key) {
            String label = StrUtil.getLabel("fcf.shell.about." + key);
            return label == null ? key : label;
        }
    }
    
    /**
     * Display an about dialog for the specified UI element.
     * 
     * @param component UI element whose attributes are to be displayed.
     */
    public static void execute(ElementBase component) {
        if (component.getDefinition() != null) {
            execute(component.getDefinition());
        } else {
            execute(component.getClass());
        }
    }
    
    /**
     * Display an about dialog for the specified class.
     * 
     * @param clazz Class whose attributes are to be displayed.
     */
    public static void execute(Class<?> clazz) {
        showDialog(new AboutParams(clazz));
    }
    
    /**
     * Display an about dialog for the specified plugin definition.
     * 
     * @param def Plugin definition whose attributes are to be displayed.
     */
    public static void execute(PluginDefinition def) {
        showDialog(new AboutParams(def));
    }
    
    /**
     * Display an about dialog for the specified manifest.
     * 
     * @param manifest Manifest whose attributes are to be displayed.
     */
    public static void execute(Manifest manifest) {
        showDialog(new AboutParams(manifest));
    }
    
    /**
     * Common entry point for displaying an about dialog.
     * 
     * @param params Parameter map for about dialog.
     */
    private static void showDialog(AboutParams params) {
        try {
            Map<String, Object> args = Collections.singletonMap("params", params);
            Window dlg = (Window) PageUtil.createPage(Constants.RESOURCE_PREFIX_SHELL + "aboutDialog.fsp", null, args).get(0);
            dlg.modal(null);
        } catch (Exception e) {
            DialogUtil.showError(MiscUtil.formatExceptionForDisplay(e));
        }
    }

    private Window window;
    
    private String defaultIcon;
    
    private String defaultSource;
    
    @WiredComponent
    private Image imgIcon;
    
    @WiredComponent
    private Label lblSource;
    
    @WiredComponent
    private Rows rows;
    
    @WiredComponent
    private Button btnCustom;
    
    @Override
    public void afterInitialized(BaseComponent comp) {
        super.afterInitialized(comp);
        window = (Window) comp;
        AboutParams aboutParams = comp.getAttribute("params", AboutParams.class);
        imgIcon.setSrc(aboutParams.icon == null ? defaultIcon : aboutParams.icon);
        lblSource.setLabel(aboutParams.source == null ? defaultSource : aboutParams.source);
        btnCustom.setLabel(aboutParams.custom);
        btnCustom.setVisible(btnCustom.getLabel() != null);
        
        if (!StringUtils.isEmpty(aboutParams.title)) {
            window.setTitle(window.getTitle() + " - " + aboutParams.title);
        }
        
        for (Entry<String, String> entry : aboutParams.entrySet()) {
            Row row = new Row();
            row.addChild(new Cell(entry.getKey()));
            row.addChild(new Cell(entry.getValue()));
            rows.addChild(row);
        }
        
        if (!StringUtils.isEmpty(aboutParams.description)) {
            Row row = new Row();
            Rowcell cell = new Rowcell();
            cell.setColspan(2);
            cell.addClass("fcf-about-description");
            row.addChild(cell);
            cell.addChild(FCFUtil.getTextComponent(aboutParams.description));
            rows.addChild(row);
        }
    }
    
    /**
     * Display the manifest viewer when detail button is clicked.
     */
    @EventHandler(value = "click", target = "btnCustom")
    private void onClick$btnCustom() {
        ManifestViewer.execute();
    }
    
    @EventHandler(value = "click", target = "btnClose")
    private void onClick$btnClose() {
        window.close();
    }
    
    /**
     * Sets the default url of the icon to display in the header.
     * 
     * @param icon Icon url.
     */
    public void setIcon(String icon) {
        defaultIcon = icon;
    }
    
    /**
     * Sets the default item source to display in the header.
     * 
     * @param source Item source.
     */
    public void setSource(String source) {
        defaultSource = source;
    }
    
}
