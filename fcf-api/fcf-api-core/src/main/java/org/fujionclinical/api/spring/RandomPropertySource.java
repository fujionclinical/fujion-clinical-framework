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
package org.fujionclinical.api.spring;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.fujion.common.Assert;
import org.springframework.core.env.PropertySource;

import java.util.UUID;

/**
 * Allows generation of random property values.
 */
public class RandomPropertySource extends PropertySource<Object> {

    public static final UniformRandomProvider RANDOM_PROVIDER = RandomSource.XO_RO_SHI_RO_128_PP.create();

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
            return switch (RandomType.valueOf(name.substring(RANDOM_PREFIX.length()).toUpperCase())) {
                case INT, INTEGER -> Integer.toString(RANDOM_PROVIDER.nextInt());
                case LONG -> Long.toString(RANDOM_PROVIDER.nextLong());
                case VALUE -> RandomStringUtils.randomAlphanumeric(32);
                case UUID -> UUID.randomUUID().toString();
            };
        } catch (Exception e) {
            return Assert.fail("Unknown random property type '%s'", name);
        }
    }

}
