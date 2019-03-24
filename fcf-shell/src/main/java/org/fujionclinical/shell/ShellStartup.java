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
package org.fujionclinical.shell;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fujionclinical.api.IRegisterEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * This class maintains a list of startup routines (those that implement the IShellStartup
 * interface) and executes those routines upon startup of the shell.
 */
public class ShellStartup implements IRegisterEvent {
    
    private static final Log log = LogFactory.getLog(ShellStartup.class);
    
    private final List<IShellStartup> startupRoutines = new ArrayList<>();
    
    /**
     * Register a startup routine.
     */
    @Override
    public void registerObject(Object object) {
        if (object instanceof IShellStartup) {
            startupRoutines.add((IShellStartup) object);
        }
    }
    
    /**
     * Unregister a startup routine.
     */
    @Override
    public void unregisterObject(Object object) {
        if (object instanceof IShellStartup) {
            startupRoutines.remove(object);
        }
    }
    
    /**
     * Execute registered startup routines.
     */
    public void execute() {
        List<IShellStartup> temp = new ArrayList<>(startupRoutines);
        
        for (IShellStartup startupRoutine : temp) {
            try {
                if (startupRoutine.execute()) {
                    unregisterObject(startupRoutine);
                }
            } catch (Throwable t) {
                log.error("Error executing startup routine.", t);
                unregisterObject(startupRoutine);
            }
        }
    }
}
