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
package org.fujionclinical.api.alias;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fujion.common.MiscUtil;
import org.fujion.common.StrUtil;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Represents a specific alias type (e.g., property or authority aliases).
 */
public class AliasType {

    private static class Wildcard {

        private final String fromString;

        private final String toString;

        private final Pattern pattern;

        Wildcard(String fromString, String toString) {
            this.fromString = fromString;
            this.toString = toString;
            this.pattern = MiscUtil.globToRegex(fromString);
        }

        boolean matches(String value) {
            return pattern.matcher(value).matches();
        }

        boolean isSame(Wildcard other) {
            return other != null && other.toString.equals(toString) && other.fromString.equals(fromString);
        }
    }

    private static final Log log = LogFactory.getLog(AliasType.class);

    private static final String WILDCARD_DELIM_REGEX = "((?<=[*,?])|(?=[*,?]))";

    private final String name;

    private final Map<String, String> simpleAliasMap = new ConcurrentHashMap<>();

    private final Map<String, Wildcard> wildcardMap = new ConcurrentHashMap<>();

    protected AliasType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String get(String local) {
        String result = simpleAliasMap.get(local);

        if (result == null) {
            for (Entry<String, Wildcard> entry : wildcardMap.entrySet()) {
                Wildcard wc = entry.getValue();

                if (wc.matches(local)) {
                    result = transformKey(local, wc);
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Registers an alias for a key.
     *
     * @param local Local name.
     * @param alias Alias for the key. A null value removes any existing alias.
     */
    public void register(String local, String alias) {
        if (local.contains("*") || local.contains("?")) {
            registerWildcardAlias(local, alias);
        } else {
            registerSimpleAlias(local, alias);
        }
    }

    private void registerSimpleAlias(String local, String alias) {
        if (alias == null) {
            logIfRemoved(local, simpleAliasMap.remove(local));
            return;
        }

        String oldAlias = simpleAliasMap.put(local, alias);
        logIfReplaced(local, oldAlias, alias);
    }

    private void registerWildcardAlias(String local, String alias) {
        if (alias == null) {
            Wildcard oldAlias = wildcardMap.remove(local);
            logIfRemoved(local, oldAlias == null ? null : oldAlias.toString);
            return;
        }

        Wildcard oldAlias = wildcardMap.get(local);
        Wildcard newAlias = new Wildcard(local, alias);

        if (!newAlias.isSame(oldAlias)) {
            wildcardMap.put(local, newAlias);
            logIfReplaced(local, oldAlias == null ? null : oldAlias.toString, newAlias.toString);
        }
    }

    private void logIfReplaced(String local, String oldAlias, String newAlias) {
        if (log.isInfoEnabled() && oldAlias != null && !oldAlias.equals(newAlias)) {
            log.info(StrUtil.formatMessage("Replaced %s alias for '%s':  old value ='%s', new value ='%s'.", getName(),
                    local, oldAlias, newAlias));
        }
    }

    private void logIfRemoved(String local, String alias) {
        if (log.isInfoEnabled() && alias != null) {
            log.info(StrUtil.formatMessage("Removed %s alias for '%s':  value ='%s'.", getName(), local, alias));
        }
    }

    /**
     * Uses the source and target wildcard masks to transform an input key.
     *
     * @param key The input key.
     * @param wc  The wildcard.
     * @return The transformed key.
     */
    private String transformKey(String key, Wildcard wc) {
        StringBuilder sb = new StringBuilder();
        String[] srcTokens = wc.fromString.split(WILDCARD_DELIM_REGEX);
        String[] tgtTokens = wc.toString.split(WILDCARD_DELIM_REGEX);
        int len = Math.max(srcTokens.length, tgtTokens.length);
        int pos = 0;
        int start = 0;

        for (int i = 0; i <= len; i++) {
            String srcx = i >= srcTokens.length ? "" : srcTokens[i];
            String tgtx = i >= tgtTokens.length ? "" : tgtTokens[i];
            pos = i == len ? key.length() : pos;

            if ("*".equals(srcx) || "?".equals(srcx)) {
                start = pos;
            } else {
                pos = key.indexOf(srcx, pos);

                if (pos > start) {
                    sb.append(key, start, pos);
                }

                start = pos += srcx.length();
                sb.append(tgtx);
            }

        }

        return sb.toString();
    }

}
