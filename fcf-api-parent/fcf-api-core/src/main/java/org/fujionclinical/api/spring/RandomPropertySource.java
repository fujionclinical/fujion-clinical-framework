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
package org.fujionclinical.api.spring;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.core.env.PropertySource;

import java.util.UUID;

/**
 * Allows generation of random property values.
 */
public class RandomPropertySource extends PropertySource<Object> {

    private enum RandomType {INT, INTEGER, LONG, VALUE, UUID}

    private static final String RANDOM_PREFIX = "random.";

    public RandomPropertySource() {
        super("Random");
    }
    
    /**
     * Label names must be prefixed with "random." to be recognized as such.
     */
    @Override
    public String getProperty(String name) {
        if (!name.startsWith(RANDOM_PREFIX)) {
            return null;
        }

        try {
            switch (RandomType.valueOf(name.substring(RANDOM_PREFIX.length()).toUpperCase())) {
                case INT:
                case INTEGER:
                    return Integer.toString(RandomUtils.nextInt());
                case LONG:
                    return Long.toString(RandomUtils.nextLong());
                case VALUE:
                    return RandomStringUtils.randomAlphanumeric(32);
                case UUID:
                    return UUID.randomUUID().toString();
                default:
                    return "";
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Unknown random property type: " + name);
        }
    }
    
}
