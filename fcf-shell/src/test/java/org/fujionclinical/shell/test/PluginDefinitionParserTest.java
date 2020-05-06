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
package org.fujionclinical.shell.test;

import org.fujion.test.MockTest;
import org.fujion.webjar.WebJarLocator;
import org.fujionclinical.shell.plugins.*;
import org.fujionclinical.ui.test.MockUITest;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PluginDefinitionParserTest extends ShellTest {

    @Test
    public void parserTest() throws Exception {
        String xml = MockTest.getTextFromResource("pluginDefinition.xml");
        PluginDefinition def = PluginXmlParser.fromXml(xml);
        assertEquals("Test1", def.getName());
        assertEquals("plugin-test", def.getId());
        assertEquals(def.getResources().size(), 4);
        assertTrue(def.getResources().get(0) instanceof PluginResourceButton);
        assertTrue(def.getResources().get(1) instanceof PluginResourceButton);
        assertTrue(def.getResources().get(2) instanceof PluginResourceHelp);
        assertTrue(def.getResources().get(3) instanceof PluginResourcePropertyGroup);
    }
    
}
