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
package org.fujionclinical.help;

import org.apache.commons.io.IOUtils;
import org.fujion.common.AbstractRegistry;
import org.fujion.common.MiscUtil;
import org.fujion.common.StrUtil;
import org.junit.Test;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class HelpTest {

    private static final HelpModuleRegistry registry = HelpModuleRegistry.getInstance();

    @Test
    public void testRegistry() throws Exception {
        clearRegistry();
        HelpModule moduleDef = createHelpModule("helpModuleDefault.xml");
        HelpModule moduleEn = createHelpModule("helpModuleEn.xml");
        HelpModule moduleFr = createHelpModule("helpModuleFr.xml");
        Locale.setDefault(new Locale("en"));
        assertTrue(registry.get("testModule") == moduleEn);
        Locale.setDefault(new Locale("en", "CA"));
        assertTrue(registry.get("testModule") == moduleEn);
        Locale.setDefault(new Locale("fr"));
        assertTrue(registry.get("testModule") == moduleFr);
        Locale.setDefault(new Locale("de"));
        assertTrue(registry.get("testModule") == moduleDef);
        assertNull(registry.get("otherModule"));
        clearRegistry();
    }

    private void clearRegistry() throws Exception {
        Method method = AbstractRegistry.class.getDeclaredMethod("clear");
        method.setAccessible(true);
        method.invoke(registry);
    }

    private HelpModule createHelpModule(String file) {
        try (InputStream is = HelpTest.class.getResourceAsStream("/" + file)) {
            List<String> xml = IOUtils.readLines(is, StandardCharsets.UTF_8);
            HelpModule module = HelpXmlParser.fromXml(StrUtil.fromList(xml));
            registry.register(module);
            return module;
        } catch (Exception e) {
            throw MiscUtil.toUnchecked(e);
        }
    }
}
