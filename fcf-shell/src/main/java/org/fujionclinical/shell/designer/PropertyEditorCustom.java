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

import org.fujion.common.MiscUtil;
import org.fujion.component.Popup;
import org.fujion.component.Popupbox;
import org.fujion.event.CloseEvent;
import org.fujion.event.OpenEvent;
import org.fujion.page.PageUtil;
import org.fujionclinical.shell.Constants;
import org.fujionclinical.shell.property.PropertyInfo;

/**
 * Allows registration of custom editors for complex property types.
 */
public abstract class PropertyEditorCustom extends PropertyEditorBase<Popupbox> {

    protected final Popup popup;

    protected PropertyEditorCustom() {
        super(new Popupbox());
        //editor.setAutodrop(false);
        editor.setReadonly(true);
        editor.setPlaceholder(Constants.MSG_DESIGNER_CUSTOM_COMP_PROMPT.toString());
        editor.addEventListener(OpenEvent.class, (event) -> {
            doOpen();
        });
        editor.addEventListener(CloseEvent.class, (event) -> {
            doClose();
        });
        popup = new Popup();
        editor.addChild(popup);
    }

    protected PropertyEditorCustom(String template) {
        this();
        PageUtil.createPage(template, popup);
    }

    /**
     * Invoked when the associated component is opened.
     */
    protected void doOpen() {
    }

    /**
     * Invoked when the associated component is closed;
     */
    protected void doClose() {
    }

    /**
     * For custom property editors, if a property getter is specified, the value returned by the
     * getter is the real target.
     *
     * @param target   Target object.
     * @param propInfo The property information.
     * @param propGrid The parent property grid.
     */
    @Override
    protected void init(
            Object target,
            PropertyInfo propInfo,
            PropertyGrid propGrid) {
        if (propInfo.getGetter() != null) {
            try {
                target = propInfo.getPropertyValue(target);
                propInfo.setSetter(null);
            } catch (Exception e) {
                throw MiscUtil.toUnchecked(e);
            }
        }

        super.init(target, propInfo, propGrid);
    }

    @Override
    public void setFocus() {
        editor.open();
        doOpen();
    }

}
