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
package org.fujionclinical.shell;

import org.fujion.common.LocalizedMessage;
import org.fujion.core.CoreUtil;

/**
 * Package-wide constants.
 */
public class Constants {

    // ---------------------------- Event Names ----------------------------

    public static final String EVENT_PREFIX = "FCF";
    public static final String EVENT_RESOURCE_PREFIX = EVENT_PREFIX + ".RESOURCE";
    public static final String EVENT_RESOURCE_PROPGROUP_PREFIX = EVENT_RESOURCE_PREFIX + ".PROPGROUP";
    public static final String EVENT_RESOURCE_PROPGROUP_ADD = EVENT_RESOURCE_PROPGROUP_PREFIX + ".ADD";
    public static final String EVENT_RESOURCE_PROPGROUP_REMOVE = EVENT_RESOURCE_PROPGROUP_PREFIX + ".REMOVE";
    private static final String EVENT_INFO_ROOT = EVENT_PREFIX + ".INFO";
    public static final String EVENT_INFO_SHOW = EVENT_INFO_ROOT + ".SHOW";
    public static final String EVENT_INFO_HIDE = EVENT_INFO_ROOT + ".HIDE";

    // ---------------------------- Resources ----------------------------

    public static final String RESOURCE_PREFIX_SHELL = CoreUtil.getResourceClassPath(Constants.class);
    public static final String ICON_PATH = "webjars/fcf-shell/icons/";
    public static final String DESIGN_ICON_ACTIVE = ICON_PATH + "designOn.png";
    public static final String DESIGN_ICON_INACTIVE = ICON_PATH + "designOff.png";

    // ---------------------------- Shell Constants ----------------------------

    public static final String SHELL_INSTANCE = "FCF.SHELL";
    public static final String ATTR_CONTAINER = "FCF.CONTAINER";
    public static final String ATTR_VISIBLE = "FCF.VISIBLE";

    // ---------------------------- Shell Constants ----------------------------

    public static final LocalizedMessage MSG_ACTION_LOCK = new LocalizedMessage("fcf.shell.action.lock.label");
    public static final LocalizedMessage MSG_ACTION_LOGOUT = new LocalizedMessage("fcf.shell.action.logout.label");
    public static final LocalizedMessage MSG_CLIPBOARD_EMPTY = new LocalizedMessage("fcf.shell.clipboard.viewer.message.empty");
    public static final LocalizedMessage MSG_LOGOUT_CANCEL = new LocalizedMessage("fcf.shell.logout.cancel.message");
    public static final LocalizedMessage MSG_LOGOUT_CONFIRMATION_CAP = new LocalizedMessage("fcf.shell.logout.confirmation.caption");
    public static final LocalizedMessage MSG_LOGOUT_CONFIRMATION_TEXT = new LocalizedMessage("fcf.shell.logout.confirmation.message");

    // ---------------------------- Plugin Constants ----------------------------

    public static final LocalizedMessage MSG_PLUGIN_CATEGORY_DEFAULT = new LocalizedMessage("fcf.shell.plugin.category.default");
    public static final LocalizedMessage MSG_PLUGIN_CATEGORY_FAVORITE = new LocalizedMessage("fcf.shell.plugin.category.favorite");
    public static final LocalizedMessage MSG_PLUGIN_TREEVIEW_DX = new LocalizedMessage("fcf.shell.plugin.treeview.description");
    public static final LocalizedMessage MSG_PLUGIN_UNKNOWN = new LocalizedMessage("fcf.shell.plugin.error.unknown");

    // ---------------------------- Designer Constants ----------------------------

    public static final String RESOURCE_PREFIX_DESIGNER = RESOURCE_PREFIX_SHELL + "designer/";
    public static final String ATTR_DESIGNER_MENU = RESOURCE_PREFIX_DESIGNER + "DesignMenu";

    public static final String DESIGNER_PRIVS = "PRIV_FCF_DESIGNER";
    public static final String DESIGNER_FAVORITES_PROPERTY = "FCF.DESIGN.FAVORITES";

    public static final LocalizedMessage MSG_DESIGNER_ADD_COMPONENT_TITLE = new LocalizedMessage("fcf.shell.designer.add.component.title");
    public static final LocalizedMessage MSG_DESIGNER_CUSTOM_COMP_PROMPT = new LocalizedMessage("fcf.shell.designer.propedit.custom.component.prompt");
    public static final LocalizedMessage MSG_DESIGNER_DESKTOP_CLEAR_CAP = new LocalizedMessage("fcf.shell.designer.desktop.clear.caption");
    public static final LocalizedMessage MSG_DESIGNER_DESKTOP_CLEAR_TEXT = new LocalizedMessage("fcf.shell.designer.desktop.clear.message");
    public static final LocalizedMessage MSG_DESIGNER_GRID_NO_SELECTION = new LocalizedMessage("fcf.shell.designer.property.grid.noselection");
    public static final LocalizedMessage MSG_DESIGNER_GRID_PROPDX_NONE_CAP = new LocalizedMessage("fcf.shell.designer.property.grid.propdx.none.caption");
    public static final LocalizedMessage MSG_DESIGNER_GRID_PROPDX_NONE_TEXT = new LocalizedMessage("fcf.shell.designer.property.grid.propdx.none.message");
    public static final LocalizedMessage MSG_DESIGNER_GRID_PROPDX_SOME_CAP = new LocalizedMessage("fcf.shell.designer.property.grid.propdx.some.caption");
    public static final LocalizedMessage MSG_DESIGNER_GRID_PROPDX_SOME_TEXT = new LocalizedMessage("fcf.shell.designer.property.grid.propdx.some.message");
    public static final LocalizedMessage MSG_DESIGNER_GRID_TITLE = new LocalizedMessage("fcf.shell.designer.property.grid.title");
    public static final LocalizedMessage MSG_DESIGNER_HINT_ACTIVE = new LocalizedMessage("fcf.shell.designer.designmode.active.hint");
    public static final LocalizedMessage MSG_DESIGNER_HINT_INACTIVE = new LocalizedMessage("fcf.shell.designer.designmode.inactive.hint");
    public static final LocalizedMessage MSG_DESIGNER_MENU_TITLE = new LocalizedMessage("fcf.shell.designer.menu.title");
    public static final LocalizedMessage MSG_DESIGNER_MISSING_HINT = new LocalizedMessage("fcf.shell.designer.add.component.description.missing.hint");

    // ---------------------------- Layout Constants ----------------------------

    public static final LocalizedMessage MSG_LAYOUT_BADNAME = new LocalizedMessage("fcf.shell.layout.badname.message");
    public static final LocalizedMessage MSG_LAYOUT_CLONE = new LocalizedMessage("fcf.shell.layout.clone.message");
    public static final LocalizedMessage MSG_LAYOUT_CLONE_CAP = new LocalizedMessage("fcf.shell.layout.clone.caption");
    public static final LocalizedMessage MSG_LAYOUT_DELETE = new LocalizedMessage("fcf.shell.layout.delete.message");
    public static final LocalizedMessage MSG_LAYOUT_DUP = new LocalizedMessage("fcf.shell.layout.duplicate.message");
    public static final LocalizedMessage MSG_LAYOUT_IMPORT = new LocalizedMessage("fcf.shell.layout.import.message");
    public static final LocalizedMessage MSG_LAYOUT_IMPORT_CAP = new LocalizedMessage("fcf.shell.layout.import.caption");
    public static final LocalizedMessage MSG_LAYOUT_IMPORT_ERR = new LocalizedMessage("fcf.shell.layout.import.error.bad");
    public static final LocalizedMessage MSG_LAYOUT_LOAD = new LocalizedMessage("fcf.shell.layout.load.message");
    public static final LocalizedMessage MSG_LAYOUT_LOAD_CAP = new LocalizedMessage("fcf.shell.layout.load.caption");
    public static final LocalizedMessage MSG_LAYOUT_MANAGE = new LocalizedMessage("fcf.shell.layout.manage.message");
    public static final LocalizedMessage MSG_LAYOUT_MANAGE_CAP = new LocalizedMessage("fcf.shell.layout.manage.caption");
    public static final LocalizedMessage MSG_LAYOUT_MISSING = new LocalizedMessage("fcf.shell.layout.missing");
    public static final LocalizedMessage MSG_LAYOUT_OVERWRITE = new LocalizedMessage("fcf.shell.layout.overwrite.message");
    public static final LocalizedMessage MSG_LAYOUT_OVERWRITE_CAP = new LocalizedMessage("fcf.shell.layout.overwrite.caption");
    public static final LocalizedMessage MSG_LAYOUT_RENAME = new LocalizedMessage("fcf.shell.layout.rename.message");
    public static final LocalizedMessage MSG_LAYOUT_RENAME_CAP = new LocalizedMessage("fcf.shell.layout.rename.caption");
    public static final LocalizedMessage MSG_LAYOUT_SAVE = new LocalizedMessage("fcf.shell.layout.save.message");
    public static final LocalizedMessage MSG_LAYOUT_SAVE_CAP = new LocalizedMessage("fcf.shell.layout.save.caption");

    /**
     * Enforce static class.
     */
    private Constants() {
    }
}
