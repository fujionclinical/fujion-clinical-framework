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
package org.fujionclinical.ui.action;

import java.util.regex.Pattern;

public abstract class ActionTypeBase<T> implements IActionType<T> {

    private final String name;

    private final Pattern pattern;

    public ActionTypeBase(String name, String pattern) {
        this.name = name;
        this.pattern = pattern == null ? null : Pattern.compile(pattern);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean matches(String script) {
        return pattern != null && pattern.matcher(script).matches();
    }

    /**
     * Strips the prefix from a script.
     *
     * @param script The script.
     * @return The script without the prefix.
     */
    protected String stripPrefix(String script) {
        return script.substring(script.indexOf(':') + 1);
    }

    /**
     * Extracts the action type prefix.
     *
     * @param script The script.
     * @return The action type, or empty string if none.
     */
    protected String getType(String script) {
        int i = script.indexOf(':');
        return i < 0 ? "" : script.substring(0, i);
    }

}
