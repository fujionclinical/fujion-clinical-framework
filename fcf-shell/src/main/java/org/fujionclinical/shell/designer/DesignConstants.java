/*
 * #%L
 * Fujion Clinical Framework
 * %%
 * Copyright (C) 2018 fujionclinical.org
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

import org.fujionclinical.shell.Constants;
import org.fujionclinical.ui.util.FCFUtil;

public class DesignConstants {
    
    public static final String RESOURCE_PREFIX = FCFUtil.getResourcePath(DesignConstants.class);
    
    public static final String MSG_LAYOUT_DUP = "@fcf.shell.designer.layout.duplicate.message";
    
    public static final String MSG_LAYOUT_BADNAME = "@fcf.shell.designer.layout.badname.message";
    
    public static final String MSG_LAYOUT_OVERWRITE = "@fcf.shell.designer.layout.overwrite.message";
    
    public static final String CAP_LAYOUT_OVERWRITE = "@fcf.shell.designer.layout.overwrite.caption";
    
    public static final String MSG_LAYOUT_CLONE = "@fcf.shell.designer.layout.clone.message";
    
    public static final String CAP_LAYOUT_CLONE = "@fcf.shell.designer.layout.clone.caption";
    
    public static final String MSG_LAYOUT_RENAME = "@fcf.shell.designer.layout.rename.message";
    
    public static final String CAP_LAYOUT_RENAME = "@fcf.shell.designer.layout.rename.caption";
    
    public static final String MSG_DESKTOP_CLEAR = "@fcf.shell.designer.desktop.clear.message";
    
    public static final String CAP_DESKTOP_CLEAR = "@fcf.shell.designer.desktop.clear.caption";
    
    public static final String MSG_LAYOUT_SAVE = "@fcf.shell.designer.layout.save.message";
    
    public static final String CAP_LAYOUT_SAVE = "@fcf.shell.designer.layout.save.caption";
    
    public static final String MSG_LAYOUT_LOAD = "@fcf.shell.designer.layout.load.message";
    
    public static final String CAP_LAYOUT_LOAD = "@fcf.shell.designer.layout.load.caption";
    
    public static final String MSG_LAYOUT_MANAGE = "@fcf.shell.designer.layout.manage.message";
    
    public static final String CAP_LAYOUT_MANAGE = "@fcf.shell.designer.layout.manage.caption";
    
    public static final String MSG_LAYOUT_DELETE = "@fcf.shell.designer.layout.delete.message";
    
    public static final String MSG_LAYOUT_IMPORT = "@fcf.shell.designer.layout.import.message";
    
    public static final String CAP_LAYOUT_IMPORT = "@fcf.shell.designer.layout.import.caption";
    
    public static final String ERR_LAYOUT_IMPORT = "@fcf.shell.designer.layout.import.error.bad";
    
    public static final String DESIGN_HINT_ACTIVE = "@fcf.shell.designer.designmode.active.hint";
    
    public static final String DESIGN_HINT_INACTIVE = "@fcf.shell.designer.designmode.inactive.hint";
    
    public static final String ATTR_DESIGN_MENU = RESOURCE_PREFIX + "DesignMenu";
    
    public static final String DESIGN_ICON_INACTIVE = Constants.ICON_PATH + "designOff.png";
    
    public static final String DESIGN_ICON_ACTIVE = Constants.ICON_PATH + "designOn.png";;
    
    public static final String DESIGN_MODE_PRIVS = "PRIV_FCF_DESIGNER";
    
    public static final String DESIGN_FAVORITES_PROPERTY = "FCF.DESIGN.FAVORITES";
    
    /**
     * Enforce static class.
     */
    private DesignConstants() {
    };
}
