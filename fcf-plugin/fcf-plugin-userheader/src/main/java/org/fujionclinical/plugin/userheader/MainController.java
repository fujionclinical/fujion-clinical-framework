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
package org.fujionclinical.plugin.userheader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fujion.annotation.EventHandler;
import org.fujion.annotation.WiredComponent;
import org.fujion.component.Hyperlink;
import org.fujion.component.Label;
import org.fujionclinical.api.event.IEventSubscriber;
import org.fujionclinical.api.user.IUser;
import org.fujionclinical.api.user.UserContext;
import org.fujionclinical.api.security.SecurityUtil;
import org.fujionclinical.shell.ShellUtil;
import org.fujionclinical.shell.elements.ElementPlugin;
import org.fujionclinical.shell.plugins.PluginController;

/**
 * Controller for user header plugin.
 */
public class MainController extends PluginController {

    private static final Log log = LogFactory.getLog(MainController.class);

    @WiredComponent
    private Label userHeader;

    @WiredComponent
    private Hyperlink password;

    private IUser currentUser;

    private final IEventSubscriber<IUser> userChangeListener = (event, user) -> {
        setUser(user);
    };

    @Override
    public void onLoad(ElementPlugin plugin) {
        super.onLoad(plugin);
        setUser(UserContext.getActiveUser());
        UserContext.getUserContext().addListener(userChangeListener);
    }

    @Override
    public void onUnload() {
        UserContext.getUserContext().removeListener(userChangeListener);
    }
    
    /**
     * Event handler for logout link
     */
    @EventHandler(value = "click", target = "logout")
    private void onClick$logout() {
        ShellUtil.getShell().logout();
    }

    /**
     * Event handler for lock link
     */
    @EventHandler(value = "click", target = "lock")
    private void onClick$lock() {
        ShellUtil.getShell().lock();
    }

    /**
     * Event handler for change password link
     */
    @EventHandler(value = "click", target = "password")
    private void onClick$password() {
        SecurityUtil.getSecurityService().changePassword();
    }

    private void setUser(IUser user) {
        if (log.isDebugEnabled()) {
            log.debug("user: " + user);
        }

        if (currentUser != null && currentUser.equals(user)) {
            return;
        }

        currentUser = user;
        String text = user == null ? "" : user.getFullName();

        if (user != null && user.getSecurityDomain() != null) {
            text += "@" + user.getSecurityDomain().getName();
        }

        userHeader.setLabel(text);
        password.setVisible(SecurityUtil.getSecurityService().canChangePassword());
    }

}
