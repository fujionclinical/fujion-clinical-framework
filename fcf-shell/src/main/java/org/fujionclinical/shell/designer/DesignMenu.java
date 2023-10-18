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

import org.fujion.ancillary.IAutoWired;
import org.fujion.annotation.EventHandler;
import org.fujion.annotation.WiredComponent;
import org.fujion.common.StrUtil;
import org.fujion.component.BaseComponent;
import org.fujion.component.BaseUIComponent;
import org.fujion.component.Menu;
import org.fujion.component.Menuitem;
import org.fujion.core.WebUtil;
import org.fujion.dialog.DialogUtil;
import org.fujion.page.PageUtil;
import org.fujionclinical.api.security.SecurityUtil;
import org.fujionclinical.shell.Constants;
import org.fujionclinical.shell.Shell;
import org.fujionclinical.shell.elements.ElementDesktop;
import org.fujionclinical.shell.layout.Layout;
import org.fujionclinical.shell.layout.LayoutIdentifier;
import org.fujionclinical.shell.layout.LayoutParser;
import org.fujionclinical.ui.xml.XMLViewer;

import java.util.Collections;
import java.util.Map;

/**
 * This is the controller for the design menu that appears in the desktop's menu bar.
 */
public class DesignMenu implements IAutoWired {
    
    private Shell shell;
    
    private ElementDesktop owner;
    
    @WiredComponent
    private Menu menu;
    
    @WiredComponent
    private Menuitem mnuDesignMode;
    
    @WiredComponent
    private Menuitem mnuShowMarkup;
    
    /**
     * Creates the design menu with the specified desktop as owner.
     *
     * @param owner Desktop UI element owner.
     * @param parent The parent for the design menu.
     */
    public static void create(ElementDesktop owner, BaseUIComponent parent) {
        Map<String, Object> args = Collections.singletonMap("owner", owner);
        PageUtil.createPage(Constants.RESOURCE_PREFIX_DESIGNER + "designMenu.fsp", parent, args).get(0);
    }
    
    /**
     * Initialize the design menu.
     */
    @Override
    public void afterInitialized(BaseComponent comp) {
        this.owner = comp.getAttribute("owner", ElementDesktop.class);
        shell = owner.getShell();
        
        if (!WebUtil.isDebugEnabled() && !SecurityUtil.hasDebugRole()) {
            mnuShowMarkup.destroy();
        }
        
        updateMenus(false);
    }
    
    /**
     * Toggles design mode.
     */
    @EventHandler(value = "click", target = "mnuDesignMode")
    private void onClick$mnuDesignMode() {
        boolean enabled = !mnuDesignMode.isChecked();
        mnuDesignMode.setChecked(enabled);
        owner.setDesignMode(enabled);
        updateMenus(enabled);
        
        if (!enabled) {
            LayoutDesigner.closeDialog();
            menu.close();
        }
    }
    
    /**
     * Clear desktop.
     */
    @EventHandler(value = "click", target = "mnuClearDesktop")
    private void onClick$mnuClearDesktop() {
        DialogUtil.confirm(Constants.MSG_DESIGNER_DESKTOP_CLEAR_TEXT.toString(), Constants.MSG_DESIGNER_DESKTOP_CLEAR_CAP.toString(), (confirm) -> {
            if (confirm) {
                shell.reset();
            }
        });
    }
    
    /**
     * Brings up property editor for desktop.
     */
    @EventHandler(value = "click", target = "mnuDesktopProperties")
    private void onClick$mnuDesktopProperties() {
        PropertyGrid.create(owner, null);
    }
    
    /**
     * Brings up layout designer for desktop.
     */
    @EventHandler(value = "click", target = "mnuLayoutDesigner")
    private void onClick$mnuLayoutDesigner() {
        LayoutDesigner.execute(owner);
    }
    
    /**
     * Brings up layout manager.
     */
    @EventHandler(value = "click", target = "mnuLayoutManager")
    private void onClick$mnuLayoutManager() {
        LayoutManager.show(true, shell.getUILayout().getName(), null);
    }
    
    /**
     * Performs logout.
     */
    @EventHandler(value = "click", target = "mnuLogout")
    private void onClick$mnuLogout() {
        shell.logout();
    }
    
    /**
     * Prompts to save a layout.
     */
    @EventHandler(value = "click", target = "mnuSaveLayout")
    private void onClick$mnuSaveLayout() {
        LayoutManager.saveLayout(new Layout(LayoutParser.parseElement(owner)),
            new LayoutIdentifier(shell.getUILayout().getName(), LayoutManager.defaultIsShared()), false, null);
    }
    
    /**
     * Prompts to load layout.
     */
    @EventHandler(value = "click", target = "mnuLoadLayout")
    private void onClick$mnuLoadLayout() {
        LayoutManager.show(false, shell.getUILayout().getName(), (event) -> {
            LayoutIdentifier layoutId = event.getTarget().getAttribute("layoutId", LayoutIdentifier.class);
            
            if (layoutId != null) {
                Layout newLayout = new Layout(LayoutParser.parseProperty(layoutId));
                shell.buildUI(newLayout);
            }
        });
    }
    
    /**
     * Shows FCF markup for current page.
     */
    @EventHandler(value = "click", target = "mnuShowMarkup")
    private void onClick$mnuShowMarkup() {
        XMLViewer.showFCF(owner.getOuterComponent());
    }
    
    /**
     * Updates the visibility of menu items
     *
     * @param enabled The enabled status.
     */
    private void updateMenus(boolean enabled) {
        menu.setImage(enabled ? Constants.DESIGN_ICON_ACTIVE : Constants.DESIGN_ICON_INACTIVE);
        mnuDesignMode.addStyle("border-bottom", enabled ? "2px solid lightgray" : null);
        menu.setHint(
            StrUtil.formatMessage(enabled ? Constants.MSG_DESIGNER_HINT_ACTIVE.toString() : Constants.MSG_DESIGNER_HINT_INACTIVE.toString()));
        BaseUIComponent child = (BaseUIComponent) menu.getFirstChild();
        
        while (child != null) {
            child.setVisible(enabled || child == mnuDesignMode);
            child = (BaseUIComponent) child.getNextSibling();
        }
    }
}
