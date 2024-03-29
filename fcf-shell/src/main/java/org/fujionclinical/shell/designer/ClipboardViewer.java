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
import org.fujion.common.MiscUtil;
import org.fujion.component.BaseComponent;
import org.fujion.component.Button;
import org.fujion.component.Memobox;
import org.fujion.component.Window;
import org.fujion.page.PageUtil;
import org.fujionclinical.shell.Constants;

import java.util.Collections;
import java.util.Map;

/**
 * Allows viewing and editing of clipboard contents.
 */
public class ClipboardViewer implements IAutoWired {

    /**
     * Show viewer.
     *
     * @param clipboard Clipboard whose contents is to be accessed.
     */
    public static void show(Clipboard clipboard) {
        Map<String, Object> args = Collections.singletonMap("clipboard", clipboard);
        Window dlg = (Window) PageUtil.createPage(Constants.RESOURCE_PREFIX_DESIGNER + "clipboardViewer.fsp", null, args)
                .get(0);
        dlg.modal(null);
    }

    private boolean modified;

    private Window window;

    private Clipboard clipboard;

    private Object data;

    @WiredComponent
    private Memobox txtData;

    @WiredComponent
    private Button btnSave;

    @WiredComponent
    private Button btnRestore;

    @Override
    public void afterInitialized(BaseComponent root) {
        window = (Window) root;
        clipboard = root.getAttribute("clipboard", Clipboard.class);
        data = clipboard.getData();
        restore();
    }

    /**
     * Commit changes in viewer to clipboard.
     *
     * @return True if successful.
     */
    private boolean commit() {
        if (modified) {
            String text = txtData.getValue();
            try {
                clipboard.copy(data instanceof String ? text
                        : data instanceof IClipboardAware ? ((IClipboardAware<?>) data).fromClipboard(text) : null);
            } catch (Exception e) {
                txtData.setBalloon(MiscUtil.formatExceptionForDisplay(e));
                txtData.focus();
                return false;
            }
            modified = false;
            updateControls();
        }

        return true;
    }

    /**
     * Restore changes from clipboard.
     */
    private void restore() {
        String text = data == null ? Constants.MSG_CLIPBOARD_EMPTY.toString()
                : data instanceof IClipboardAware ? ((IClipboardAware<?>) data).toClipboard() : data.toString();
        txtData.setValue(text);
        txtData.setReadonly(!(data instanceof String || data instanceof IClipboardAware));
        modified = false;
        updateControls();
    }

    /**
     * Update control states.
     */
    private void updateControls() {
        btnSave.setDisabled(!modified);
        btnRestore.setDisabled(!modified);
        txtData.setBalloon(null);
    }

    /**
     * Detected data edits.
     */
    @EventHandler(value = "change", target = "@txtData")
    private void onChange$txtData() {
        modified = true;
        updateControls();
    }

    /**
     * Clicking OK button commits changes and closes viewer.
     */
    @EventHandler(value = "click", target = "btnOK")
    private void onClick$btnOK() {
        if (commit()) {
            window.close();
        }
    }

    /**
     * Clicking cancel button discards changes and closes viewer.
     */
    @EventHandler(value = "click", target = "btnCancel")
    private void onClick$btnCancel() {
        window.close();
    }

    /**
     * Clicking save button commits changes.
     */
    @EventHandler(value = "click", target = "@btnSave")
    private void onClick$btnSave() {
        commit();
    }

    /**
     * Clicking restore button restores original data.
     */
    @EventHandler(value = "click", target = "@btnRestore")
    private void onClick$btnRestore() {
        restore();
    }
}
