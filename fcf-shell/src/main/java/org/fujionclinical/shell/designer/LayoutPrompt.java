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
import org.fujion.component.*;
import org.fujion.dialog.DialogUtil;
import org.fujion.event.IEventListener;
import org.fujion.page.PageUtil;
import org.fujionclinical.shell.Constants;
import org.fujionclinical.shell.layout.LayoutIdentifier;
import org.fujionclinical.shell.layout.LayoutUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Supports selection and management of existing layouts.
 */
public class LayoutPrompt implements IAutoWired {
    
    private static final String DIALOG = Constants.RESOURCE_PREFIX_DESIGNER + "layoutPrompt.fsp";
    
    @WiredComponent
    private Radiogroup radioGroup;
    
    @WiredComponent
    private Radiobutton rbShared;
    
    @WiredComponent
    private Radiobutton rbPrivate;
    
    @WiredComponent
    private Label lblPrompt;
    
    @WiredComponent
    private Textbox txtLayout;
    
    private Window window;
    
    private boolean allowDups;
    
    /**
     * Prompts for a layout.
     * 
     * @param dfltLayout Default layout.
     * @param hideScope If true, hide private/shared scope selection.
     * @param allowDups If true, duplicate names are allowed.
     * @param title Prompt dialog title.
     * @param prompt Prompt dialog text.
     * @param closeListener Notified when dialog is closed.
     */
    public static void show(LayoutIdentifier dfltLayout, boolean hideScope, boolean allowDups, String title, String prompt,
                            IEventListener closeListener) {
        Map<String, Object> args = new HashMap<>();
        args.put("dfltLayout", dfltLayout);
        args.put("hideScope", hideScope);
        args.put("allowDups", allowDups);
        args.put("title", title);
        args.put("prompt", prompt);
        Window window = (Window) PageUtil.createPage(DIALOG, null, args).get(0);
        window.modal(closeListener);
    }
    
    @Override
    public void afterInitialized(BaseComponent root) {
        window = (Window) root;
        window.setTitle(StrUtil.formatMessage(root.getAttribute("title", "")));
        lblPrompt.setLabel(StrUtil.formatMessage(root.getAttribute("prompt", "")));
        LayoutIdentifier dfltLayout = root.getAttribute("dfltLayout", LayoutIdentifier.class);
        txtLayout.setValue(dfltLayout == null ? null : dfltLayout.name);
        boolean shared = dfltLayout == null ? LayoutManager.defaultIsShared() : dfltLayout.shared;
        (shared ? rbShared : rbPrivate).setChecked(true);
        radioGroup.setVisible(!root.getAttribute("hideScope", false));
        allowDups = root.getAttribute("allowDups", true);
    }
    
    @EventHandler(value = "change", target = "txtLayout")
    public void onChange$txtLayout() {
        showError(null);
    }
    
    @EventHandler(value = "click", target = "btnOK")
    public void onClick$btnOK() {
        String name = txtLayout.getValue().trim();
        
        if (!LayoutUtil.validateName(name)) {
            showError(Constants.MSG_LAYOUT_BADNAME.toString());
            return;
        }
        
        LayoutIdentifier id = new LayoutIdentifier(name, rbShared.isChecked());
        
        if (LayoutUtil.layoutExists(id)) {
            if (!allowDups) {
                showError(Constants.MSG_LAYOUT_DUP.toString());
            } else {
                DialogUtil.confirm(Constants.MSG_LAYOUT_OVERWRITE.toString(), Constants.MSG_LAYOUT_OVERWRITE_CAP.toString(), (confirm) -> {
                    if (confirm) {
                        close(id);
                    }
                });
            }
        } else {
            close(id);
        }
    }
    
    private void close(LayoutIdentifier layoutId) {
        window.setAttribute("layoutId", layoutId);
        LayoutManager.defaultIsShared(layoutId.shared);
        window.close();
    }
    
    private void showError(String message) {
        if (message == null) {
            txtLayout.setBalloon(null);
        } else {
            txtLayout.setBalloon(StrUtil.formatMessage(message));
            txtLayout.focus();
        }
    }
}
