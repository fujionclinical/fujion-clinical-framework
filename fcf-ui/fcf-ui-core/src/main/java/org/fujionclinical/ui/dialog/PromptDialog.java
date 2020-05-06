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
package org.fujionclinical.ui.dialog;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fujion.ancillary.IAutoWired;
import org.fujion.annotation.WiredComponent;
import org.fujion.client.ExecutionContext;
import org.fujion.common.MiscUtil;
import org.fujion.common.StrUtil;
import org.fujion.component.*;
import org.fujion.event.ClickEvent;
import org.fujion.event.DblclickEvent;
import org.fujion.event.Event;
import org.fujion.event.IEventListener;
import org.fujion.page.PageUtil;
import org.fujionclinical.ui.dialog.DialogControl.ChoiceFormat;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.fujionclinical.ui.dialog.DialogConstants.LABEL_ID_CANCEL;
import static org.fujionclinical.ui.dialog.DialogConstants.LABEL_ID_OK;

/**
 * Implements a simple, generic dialog to prompt for specified responses.
 */
public class PromptDialog implements IAutoWired {
    
    private static final Log log = LogFactory.getLog(PromptDialog.class);
    
    /**
     * Display the prompt dialog.
     *
     * @param control The dialog control.
     */
    public static void show(DialogControl<?> control) {
        DialogResponse<?> response = control.getLastResponse();
        
        if (response != null) {
            control.callback(response);
            return;
        }
        
        Window root = null;
        
        try {
            Map<String, Object> args = Collections.singletonMap("control", control);
            root = (Window) PageUtil
                    .createPage(DialogConstants.RESOURCE_PREFIX + "promptDialog.fsp", ExecutionContext.getPage(), args)
                    .get(0);
            root.modal(null);
        } catch (Exception e) {
            log.error("Error Displaying Dialog", e);
            
            if (root != null) {
                root.destroy();
            }
            
            throw MiscUtil.toUnchecked(e);
        }
    }
    
    /******************** Controller *******************/
    
    private final IEventListener clickListener = new IEventListener() {
        
        /**
         * When a response component is clicked, return its associated response and close the
         * dialog.
         *
         * @param event Click event.
         */
        @Override
        public void onEvent(Event event) {
            close((event.getTarget()));
        }
    };
    
    DialogControl<?> control;
    
    @WiredComponent
    private Listbox listbox;
    
    @WiredComponent
    private Checkbox chkRemember;
    
    @WiredComponent
    private Cell message;
    
    @WiredComponent
    private BaseUIComponent icon;
    
    @WiredComponent
    private Toolbar toolbar;
    
    private Window root;
    
    private DialogResponse<?> response;
    
    @Override
    public void afterInitialized(BaseComponent comp) {
        this.root = (Window) comp;
        root.setAttribute("controller", this);
        control = (DialogControl<?>) root.getAttribute("control");
        root.setTitle(control.getTitle());
        icon.addClass(control.getIconClass());
        message.addClass(control.getTextClass());
        message.setLabel(control.getMessage());
        root.addClass(control.getPanelClass());
        chkRemember.setVisible(root.hasAttribute("remember"));
        root.setOnCanClose(() -> {
            control.callback(response);
            return true;
        });
        
        if (control.getFormat() == ChoiceFormat.BUTTONS) {
            processButtonResponses();
        } else {
            processListResponses();
        }
    }
    
    private void processButtonResponses() {
        List<?> responses = control.getResponses();
        
        for (Object rsp : responses) {
            DialogResponse<?> response = (DialogResponse<?>) rsp;
            Button button = addButton(response.getLabel(), response.getFlavor(), clickListener);
            button.setData(response);
            
            if (response.isDefault()) {
                button.setFocus(true);
            }
        }
    }
    
    private void processListResponses() {
        List<?> responses = control.getResponses();
        listbox.setVisible(true);
        
        for (Object rsp : responses) {
            DialogResponse<?> response = (DialogResponse<?>) rsp;
            Listitem item = new Listitem(StrUtil.formatMessage(response.getLabel()));
            item.addEventListener(DblclickEvent.TYPE, clickListener);
            item.setData(response);
            listbox.addChild(item);
            
            if (response.isDefault()) {
                item.setSelected(true);
            }
        }
        
        if (listbox.getSelectedCount() == 0) {
            listbox.setSelectedItem(listbox.getFirstChild(Listitem.class));
        }
        
        addButton(LABEL_ID_CANCEL, "danger", (event) -> {
            close(null);
        });
        
        addButton(LABEL_ID_OK, "success", (event) -> {
            close(listbox.getSelectedItem());
        });
    }
    
    private Button addButton(String label, String flavor, IEventListener listener) {
        Button button = new Button(StrUtil.formatMessage(label));
        button.addClass("flavor:btn-" + flavor);
        button.addEventListener(ClickEvent.TYPE, listener);
        toolbar.addChild(button);
        return button;
    }
    
    /**
     * Retrieve the response and close the dialog.
     *
     * @param component The component associated with the response.
     */
    private void close(BaseComponent component) {
        response = component == null ? null : (DialogResponse<?>) component.getData();
        root.close();
    }
    
}
