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
package org.fujionclinical.security.controller;

import org.apache.commons.lang3.StringUtils;
import org.fujion.ancillary.IAutoWired;
import org.fujion.annotation.EventHandler;
import org.fujion.annotation.WiredComponent;
import org.fujion.common.StrUtil;
import org.fujion.component.BaseComponent;
import org.fujion.component.Label;
import org.fujion.component.Textbox;
import org.fujion.component.Window;
import org.fujionclinical.api.model.user.IUser;
import org.fujionclinical.api.model.user.UserContext;
import org.fujionclinical.api.security.ISecurityService;
import org.fujionclinical.ui.dialog.DialogUtil;
import org.fujionclinical.ui.util.FCFUtil;

import static org.fujionclinical.security.Constants.*;

/**
 * Controller for the password change dialog.
 */
public class PasswordChangeController implements IAutoWired {

    public static void show() {
        DialogUtil.popup(DIALOG);
    }

    private static final String DIALOG = FCFUtil.getResourcePath(PasswordChangeController.class, 1) + "passwordChange.fsp";

    private Window window;

    @WiredComponent
    private Textbox txtPassword1;

    @WiredComponent
    private Textbox txtPassword2;

    @WiredComponent
    private Label lblInfo;

    @WiredComponent
    private Label lblMessage;

    private IUser user;

    private ISecurityService securityService;

    @Override
    public void afterInitialized(BaseComponent comp) {
        window = (Window) comp;
        user = UserContext.getActiveUser();

        if (user == null) {
            window.close();
        } else {
            window.setTitle(MSG_PASSWORD_CHANGE_PAGE_TITLE + " - " + user.getFullName());
            lblInfo.setLabel(MSG_PASSWORD_DIALOG_LABEL.toString(MSG_PASSWORD_RULES));
        }
    }

    /**
     * Pressing return in the new password text box moves to the confirm password text box.
     */
    @EventHandler(value = "enter", target = "@txtPassword1")
    private void onEnter$txtPassword1() {
        txtPassword2.setFocus(true);
        txtPassword2.selectAll();
    }

    /**
     * Pressing return in confirm password text box submits the form.
     */
    @EventHandler(value = "enter", target = "@txtPassword2")
    @EventHandler(value = "click", target = "btnOK")
    private void onEnter$txtPassword2() {
        changePassword();
    }

    /**
     * Cancels the form when the Cancel button is clicked.
     */
    @EventHandler(value = "click", target = "btnCancel")
    private void onClick$btnCancel() {
        window.close();
    }

    /**
     * Submits the authentication request.
     */
    private void changePassword() {
        showMessage("");
        String password1 = StringUtils.trimToNull(txtPassword1.getValue());
        String password2 = StringUtils.trimToNull(txtPassword2.getValue());

        if (password1 == null || password2 == null) {
            (password1 == null ? txtPassword1 : txtPassword2).setFocus(true);
            showMessage(MSG_PASSWORD_REQUIRED_FIELDS.toString());
        } else if (!password1.equals(password2)) {
            showMessage(MSG_PASSWORD_CONFIRM.toString());
        } else {
            try {
                String result = securityService.changePassword(user.getPassword(), password1);

                if (!StringUtils.isEmpty(result)) {
                    showMessage(result);
                } else {
                    window.close();
                    DialogUtil.showInfo(MSG_PASSWORD_CHANGED_TEXT.toString(),
                            MSG_PASSWORD_CHANGED_TITLE.toString());
                }
            } catch (Exception e) {
                showMessage(MSG_PASSWORD_CHANGE_ERROR.toString(FCFUtil.formatExceptionForDisplay(e)));
            }
        }
    }

    /**
     * Displays the specified message text on the form.
     *
     * @param text Message text to display.
     */
    private void showMessage(String text) {
        lblMessage.setLabel(StrUtil.formatMessage(text));
    }

    /**
     * Sets the security service.
     *
     * @param securityService The security service.
     */
    public void setSecurityService(ISecurityService securityService) {
        this.securityService = securityService;
    }

}
