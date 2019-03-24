/*
 * #%L
 * Fujion Clinical Framework
 * %%
 * Copyright (C) 2019 fujionclinical.org
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
package org.fujionclinical.testharness;

import org.apache.commons.lang.StringUtils;
import org.fujion.common.MiscUtil;
import org.fujion.component.BaseComponent;
import org.fujionclinical.shell.ShellEx;
import org.fujionclinical.shell.plugins.PluginDefinition;
import org.fujionclinical.shell.plugins.PluginRegistry;
import org.fujionclinical.ui.action.ActionRegistry;
import org.fujionclinical.ui.action.ActionRegistry.ActionScope;
import org.fujionclinical.ui.action.IAction;
import org.fujionclinical.ui.controller.FrameworkController;
import org.fujionclinical.ui.util.FCFUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Creates a default UI based on all detected plugins.
 */
public class TestHarnessController extends FrameworkController {
    
    private static final Comparator<PluginDefinition> pluginComparator = new Comparator<PluginDefinition>() {
        
        @Override
        public int compare(PluginDefinition def1, PluginDefinition def2) {
            return def1.getName().compareToIgnoreCase(def2.getName());
        }
        
    };
    
    private static final Comparator<IAction> actionComparator = new Comparator<IAction>() {
        
        @Override
        public int compare(IAction act1, IAction act2) {
            return act1.toString().compareToIgnoreCase(act2.toString());
        }
        
    };
    
    private ShellEx shell;
    
    @Override
    public void afterInitialized(BaseComponent comp) {
        super.afterInitialized(comp);
        
        try {
            shell = (ShellEx) comp;
            
            if (shell.getLayout() == null) {
                shell.setLayout(FCFUtil.getResourcePath(TestHarnessController.class) + "testharness-layout.xml");
            }
            
            List<PluginDefinition> plugins = new ArrayList<>();
            
            for (PluginDefinition plugin : PluginRegistry.getInstance()) {
                if (!StringUtils.isEmpty(plugin.getUrl()) && shell.getLoadedPlugin(plugin.getId()) == null) {
                    plugins.add(plugin);
                }
            }
            
            Collections.sort(plugins, pluginComparator);
            
            for (PluginDefinition plugin : plugins) {
                shell.register("Test Harness\\" + plugin.getName(), plugin);
            }
            
            List<IAction> actions = new ArrayList<IAction>(ActionRegistry.getRegisteredActions(ActionScope.BOTH));
            Collections.sort(actions, actionComparator);
            
            for (IAction action : actions) {
                shell.registerMenu("Actions\\" + action, action.getScript()).setHint(action.getScript());
            }
            
            shell.start();
        } catch (Exception e) {
            throw MiscUtil.toUnchecked(e);
        }
    }
    
}
