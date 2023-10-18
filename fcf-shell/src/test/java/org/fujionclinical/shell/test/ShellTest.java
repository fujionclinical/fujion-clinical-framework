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
package org.fujionclinical.shell.test;

import org.fujion.common.MiscUtil;
import org.fujion.webjar.WebJarLocator;
import org.fujionclinical.api.spring.SpringUtil;
import org.fujionclinical.ui.test.MockUITest;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.springframework.core.io.Resource;

class ShellTest extends MockUITest {

    private static boolean initialized;

    @BeforeClass
    public static void beforeClass() {
        if (!initialized) {
            initialized = true;
            try {
                Resource[] resources = SpringUtil.getResources("file:target/classes/META-INF/resources/webjars/fcf-shell/?*");
                Assert.assertEquals("Could not find fcf-shell webjar.", 1, resources.length);
                Resource resource = resources[0];
                WebJarLocator.getInstance().processWebjar(resource);
            } catch (Exception e) {
                throw MiscUtil.toUnchecked(e);
            }
        }

        MockUITest.beforeClass();
    }

}
