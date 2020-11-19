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
package org.fujionclinical.reports.header;

import org.fujion.annotation.OnFailure;
import org.fujion.annotation.WiredComponent;
import org.fujion.component.Label;
import org.fujionclinical.api.user.User;
import org.fujionclinical.api.user.UserContext;
import org.fujionclinical.ui.util.FCFUtil;

/**
 * This is a user-based header for reports.
 */
public class ReportHeaderUser extends ReportHeaderBase {

    static {
        ReportHeaderRegistry.getInstance().register("user", FCFUtil.getResourcePath(ReportHeaderUser.class) + "userReportHeader.fsp");
    }

    @WiredComponent(onFailure = OnFailure.IGNORE)
    private Label lblUserInfo;

    public ReportHeaderUser() {
        super("CONTEXT.CHANGED.User");
    }

    /**
     * Retrieves a formatted header for the current user.
     *
     * @return Formatted header.
     */
    public String getUserInfo() {
        User user = UserContext.getActiveUser();
        return user == null ? "No User Selected" : user.getFullName();
    }
    
    /**
     * Rebind form data when context changes.
     */
    @Override
    public void refresh() {
        super.refresh();
        updateLabel(lblUserInfo, getUserInfo());
    }
}
