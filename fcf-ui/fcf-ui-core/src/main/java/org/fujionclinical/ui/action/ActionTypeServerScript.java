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
package org.fujionclinical.ui.action;

import org.fujion.script.IScriptLanguage;
import org.fujion.script.IScriptLanguage.IParsedScript;
import org.fujion.script.ScriptRegistry;
import org.springframework.util.Assert;

/**
 * Action type is a server-side script.
 */
public class ActionTypeServerScript extends ActionTypeBase<IParsedScript> {
    
    public ActionTypeServerScript() {
        super("sscript", null);
    }
    
    @Override
    public boolean matches(String script) {
        return ScriptRegistry.getInstance().get(getType(script)) != null;
    }
    
    @Override
    public IParsedScript parse(String script) {
        String lang = getType(script);
        IScriptLanguage language = ScriptRegistry.getInstance().get(lang);
        Assert.notNull(language, "Unknown script language: " + lang);
        script = stripPrefix(script);
        return language.parse(script);
    }
    
    @Override
    public void execute(IParsedScript script) {
        script.run();
    }
    
}
