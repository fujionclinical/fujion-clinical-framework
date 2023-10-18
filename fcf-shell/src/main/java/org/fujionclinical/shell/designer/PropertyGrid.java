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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fujion.ancillary.IAutoWired;
import org.fujion.ancillary.INamespace;
import org.fujion.annotation.EventHandler;
import org.fujion.annotation.WiredComponent;
import org.fujion.common.MiscUtil;
import org.fujion.common.StrUtil;
import org.fujion.component.*;
import org.fujion.event.ChangeEvent;
import org.fujion.event.ClickEvent;
import org.fujion.event.EventUtil;
import org.fujion.model.IComponentRenderer;
import org.fujion.model.IListModel;
import org.fujion.model.IModelAndView;
import org.fujion.model.ListModel;
import org.fujion.page.PageUtil;
import org.fujionclinical.shell.Constants;
import org.fujionclinical.shell.ancillary.FCFException;
import org.fujionclinical.shell.elements.ElementBase;
import org.fujionclinical.shell.plugins.PluginDefinition;
import org.fujionclinical.shell.property.PropertyInfo;
import org.fujionclinical.shell.property.PropertyType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dialog for managing property values of UI elements within the designer. Each editable property of
 * the target UI element is presented as a row in the grid with its associated property editor.
 */
public class PropertyGrid implements IAutoWired {
    
    private static final Log log = LogFactory.getLog(PropertyGrid.class);
    
    private static final String EDITOR_ATTR = "editor";
    
    private static class RowEx extends Row implements INamespace {}

    @WiredComponent
    private Grid gridProperties;
    
    @WiredComponent
    private Label lblPropertyInfo;
    
    @WiredComponent
    private Column colProperty;
    
    @WiredComponent
    private Button btnOK;
    
    @WiredComponent
    private Button btnCancel;
    
    @WiredComponent
    private Button btnApply;
    
    @WiredComponent
    private Button btnRestore;
    
    @WiredComponent
    private BaseUIComponent toolbar;
    
    @WiredComponent
    private Pane propInfo;

    @SuppressWarnings("rawtypes")
    private final IListModel<PropertyEditorBase> model = new ListModel<>();
    
    private Object target;
    
    private boolean pendingChanges;
    
    private boolean propertiesModified;
    
    private boolean embedded;
    
    private Window window;
    
    private ChangeEvent changeEvent;
    
    @SuppressWarnings("rawtypes")
    private final IComponentRenderer<Row, PropertyEditorBase> rowRenderer = (editor) -> {
        Row row = new RowEx();
        row.setData(editor);
        BaseComponent cmpt = editor.getEditor();
        PropertyInfo propInfo = editor.getPropInfo();
        Cell cell = new Cell();
        row.addChild(cell);
        cell.addEventForward(ClickEvent.TYPE, window, ChangeEvent.TYPE);
        Label lbl = new Label(propInfo.getName());
        cell.addChild(lbl);
        row.setAttribute(EDITOR_ATTR, editor);
        
        try {
            editor.setValue(propInfo.getPropertyValue(target));
        } catch (Exception e) {
            lbl = new Label(MiscUtil.formatExceptionForDisplay(e));
            lbl.setHint(lbl.getLabel());
            cmpt = lbl;
        }
        
        row.addChild(cmpt);
        return row;
    };
    
    /**
     * Creates a property grid for the given target UI element.
     *
     * @param target UI element whose properties are to be edited.
     * @param parent Parent component for property grid (may be null).
     * @return Newly created PropertyGrid instance.
     */
    public static PropertyGrid create(ElementBase target, BaseComponent parent) {
        return create(target, parent, false);
    }
    
    /**
     * Creates a property grid for the given target UI element.
     *
     * @param target UI element whose properties are to be edited.
     * @param parent Parent component for property grid (may be null).
     * @param embedded If true, the property grid is embedded within another component.
     * @return Newly created PropertyGrid instance.
     */
    public static PropertyGrid create(ElementBase target, BaseComponent parent, boolean embedded) {
        Map<String, Object> args = new HashMap<>();
        args.put("target", target);
        args.put("embedded", embedded);
        Window window = (Window) PageUtil.createPage(Constants.RESOURCE_PREFIX_DESIGNER + "propertyGrid.fsp", parent, args)
                .get(0);
        
        if (parent == null) {
            window.modal(null);
        }
        
        return window.getAttribute("controller", PropertyGrid.class);
    }
    
    /**
     * Initializes the property grid.
     *
     * @param comp The root component.
     */
    @Override
    public void afterInitialized(BaseComponent comp) {
        window = (Window) comp;
        changeEvent = new ChangeEvent(window, null);
        @SuppressWarnings("rawtypes")
        IModelAndView<Row, PropertyEditorBase> mv = gridProperties.getRows().getModelAndView(PropertyEditorBase.class);
        mv.setRenderer(rowRenderer);
        mv.setModel(model);
        comp.setAttribute("controller", this);
        this.embedded = comp.getAttribute("embedded", false);
        setTarget(comp.getAttribute("target", ElementBase.class));
        
        if (window.getParent() != null) {
            window.setClosable(false);
            window.setWidth("100%");
            window.setHeight("100%");
            window.setSizable(false);
            window.addClass("fcf-propertygrid-embedded");
            toolbar.setVisible(embedded);
        }
        
        btnOK.setVisible(!embedded);
        btnCancel.setVisible(!embedded);
    }
    
    public Window getWindow() {
        return window;
    }
    
    /**
     * Sets the target UI element for the property grid. Iterates through the target's property
     * definitions and presents a row for each editable property.
     *
     * @param target UI element whose properties are to be edited.
     */
    public void setTarget(ElementBase target) {
        if (target == null) {
            setTarget(null, null, null);
        } else {
            PluginDefinition def = target.getDefinition();
            List<PropertyInfo> props = def.getProperties();
            setTarget(target, props, target.getDisplayName());
        }
    }
    
    /**
     * Sets the target object for the property grid. Iterates through the target's property
     * definitions and presents a row for each editable property.
     *
     * @param target Object whose properties are to be edited.
     * @param props Property list.
     * @param title Title for editor.
     */
    public void setTarget(Object target, List<PropertyInfo> props, String title) {
        this.target = target;
        model.clear();
        
        if (target == null) {
            ((BaseUIComponent) window.getFirstChild()).setVisible(false);
            window.setTitle(Constants.MSG_DESIGNER_GRID_NO_SELECTION.toString());
            disableButtons(true);
            return;
        }
        
        ((BaseUIComponent) window.getFirstChild()).setVisible(true);
        window.setTitle(Constants.MSG_DESIGNER_GRID_TITLE.toString(title));
        
        if (props != null && !props.isEmpty()) {
            for (PropertyInfo prop : props) {
                addPropertyEditor(prop, true);
            }
            
            gridProperties.setVisible(true);
            setPropertyDescription(Constants.MSG_DESIGNER_GRID_PROPDX_SOME_CAP.toString(),
                    Constants.MSG_DESIGNER_GRID_PROPDX_SOME_TEXT.toString());
        } else {
            gridProperties.setVisible(false);
            setPropertyDescription(Constants.MSG_DESIGNER_GRID_PROPDX_NONE_CAP.toString(),
                    Constants.MSG_DESIGNER_GRID_PROPDX_NONE_TEXT.toString());
        }
        
        disableButtons(true);
    }
    
    /**
     * Adds a property editor to the grid for the specified property definition.
     *
     * @param propInfo Property definition information.
     * @param append If true, the property editor is appended to the end of the grid. Otherwise, it
     *            is inserted at the beginning.
     * @return The newly added property editor.
     */
    protected PropertyEditorBase<?> addPropertyEditor(PropertyInfo propInfo, boolean append) {
        PropertyEditorBase<?> propEditor = null;
        
        try {
            PropertyType type = propInfo.getPropertyType();
            
            if (type == null) {
                throw new FCFException("Unknown property type: " + propInfo.getType());
            }
            
            Class<? extends PropertyEditorBase<?>> editorClass = type.getEditorClass();
            
            if (editorClass != null && propInfo.isEditable()) {
                propEditor = editorClass.newInstance();
                propEditor.init(target, propInfo, this);
                model.add(append ? model.size() : 0, propEditor);
            }
        } catch (Exception e) {
            log.error("Error creating editor for property '" + propInfo.getName() + "'.", e);
        }
        
        return propEditor;
    }
    
    /**
     * Commit or revert all pending changes to the target object.
     *
     * @param commit True to commit. False to revert.
     * @return True if all operations completed successfully.
     */
    protected boolean commitChanges(boolean commit) {
        boolean result = true;
        
        for (Object child : gridProperties.getRows().getChildren()) {
            if (child instanceof Row) {
                Row row = (Row) child;
                PropertyEditorBase<?> editor = (PropertyEditorBase<?>) row.getAttribute(EDITOR_ATTR);
                
                if (editor != null && editor.hasChanged()) {
                    if (commit) {
                        result &= editor.commit();
                    } else {
                        result &= editor.revert();
                    }
                }
            }
        }
        
        disableButtons(result);
        
        if (commit && target instanceof ElementBase) {
            EventUtil.post(new LayoutChangedEvent(null, (ElementBase) target));
        }
        
        return result;
    }
    
    /**
     * Returns the editor associated with the named property, or null if none found.
     *
     * @param propName The property name.
     * @param select If true, select editor's the containing row.
     * @return The associated property editor (may be null).
     */
    public PropertyEditorBase<?> findEditor(String propName, boolean select) {
        for (Object child : gridProperties.getRows().getChildren()) {
            if (child instanceof Row) {
                Row row = (Row) child;
                PropertyEditorBase<?> editor = (PropertyEditorBase<?>) row.getAttribute(EDITOR_ATTR);
                
                if (editor != null && editor.getPropInfo().getId().equals(propName)) {
                    if (select) {
                        row.setSelected(true);
                    }
                    
                    return editor;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Returns number of editors in this property grid.
     *
     * @return Number of editors.
     */
    public int getEditorCount() {
        return model.size();
    }
    
    /**
     * Change button enable states to reflect whether or not pending changes exist.
     *
     * @param disable The disable status.
     */
    private void disableButtons(boolean disable) {
        btnRestore.setDisabled(disable);
        btnApply.setDisabled(disable);
        pendingChanges = !disable;
        propertiesModified |= pendingChanges;
    }
    
    /**
     * Returns true if there are uncommitted edits.
     *
     * @return True if there are uncommitted edits.
     */
    public boolean hasPendingChanges() {
        return pendingChanges;
    }
    
    public boolean getPropertiesModified() {
        return propertiesModified;
    }
    
    /**
     * Returns the target object.
     *
     * @return The target object.
     */
    public Object getTarget() {
        return target;
    }
    
    /**
     * Clicking the cancel button cancels any pending edits and, if embedded mode is not active,
     * closes the dialog.
     */
    @EventHandler(value = "click", target = "@btnCancel")
    private void onClick$btnCancel() {
        if (embedded) {
            commitChanges(false);
        } else {
            window.close();
        }
    }
    
    /**
     * Clicking the apply button commits any pending edits.
     */
    @EventHandler(value = "click", target = "@btnApply")
    private void onClick$btnApply() {
        commitChanges(true);
    }
    
    /**
     * Clicking the restore button cancels any pending edits.
     */
    @EventHandler(value = "click", target = "@btnRestore")
    private void onClick$btnRestore() {
        commitChanges(false);
    }
    
    /**
     * Clicking the OK button commits any pending edits and closes the dialog.
     */
    @EventHandler(value = "click", target = "@btnOK")
    private void onClick$btnOK() {
        if (commitChanges(true)) {
            window.close();
        }
    }
    
    public void propertyChanged() {
        disableButtons(false);
        EventUtil.post(changeEvent);
    }
    
    /**
     * Displays the description information for a property.
     *
     * @param propertyName Property name.
     * @param propertyDescription Property description.
     */
    private void setPropertyDescription(String propertyName, String propertyDescription) {
        propInfo.setTitle(StrUtil.formatMessage(propertyName));
        lblPropertyInfo.setLabel(StrUtil.formatMessage(propertyDescription));
    }
    
    /**
     * Handles the selection of a row in the property grid.
     *
     * @param event Row selection event.
     */
    @EventHandler(value = "change", target = "rowProperties")
    private void onRowSelect(ChangeEvent event) {
        Rows rows = gridProperties.getRows();
        Row selectedRow = rows.getSelectedCount() == 0 ? null : rows.getSelectedRow();
        PropertyEditorBase<?> editor = selectedRow == null ? null
                : (PropertyEditorBase<?>) (selectedRow.getAttribute(EDITOR_ATTR));
        PropertyInfo propInfo = editor == null ? null : editor.getPropInfo();
        setPropertyDescription(
            propInfo == null ? Constants.MSG_DESIGNER_GRID_PROPDX_SOME_CAP.toString() : propInfo.getName(),
            propInfo == null ? " " : propInfo.getDescription());

        if (editor != null) {
            editor.setFocus();
        }
    }
    
}
