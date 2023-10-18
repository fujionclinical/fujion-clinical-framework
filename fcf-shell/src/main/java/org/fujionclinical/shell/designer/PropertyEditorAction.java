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

import org.fujionclinical.api.security.SecurityUtil;
import org.fujionclinical.shell.property.PropertyInfo;
import org.fujionclinical.ui.action.ActionRegistry;
import org.fujionclinical.ui.action.ActionRegistry.ActionScope;
import org.fujionclinical.ui.action.ActionUtil;
import org.fujionclinical.ui.action.IAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Editor for actions.
 */
public class PropertyEditorAction extends PropertyEditorList {
    
    /**
     * Initialize the list from the action registry.
     */
    @Override
    protected void init(Object target, PropertyInfo propInfo, PropertyGrid propGrid) {
        propInfo.getConfig().setProperty("readonly", Boolean.toString(!SecurityUtil.hasDebugRole()));
        super.init(target, propInfo, propGrid);
        List<IAction> actions = new ArrayList<>(ActionRegistry.getRegisteredActions(ActionScope.BOTH));
        Collections.sort(actions, ActionUtil.comparator);
        
        for (IAction action : actions) {
            appendItem(action.toString(), action.getId());
        }
    }
}
