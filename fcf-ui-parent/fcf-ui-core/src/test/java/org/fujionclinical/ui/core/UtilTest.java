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
package org.fujionclinical.ui.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.fujionclinical.ui.util.FCFUtil;
import org.fujion.component.BaseComponent;
import org.fujion.component.Cell;
import org.fujion.component.Html;
import org.fujion.component.Hyperlink;
import org.junit.Test;

public class UtilTest {

    public interface ArgumentMapTest {

        void doAssertions();

        void setTest2Variable(String value);
    }

    @Test
    public void wireArgumentMapTest() {
        Map<Object, Object> map = new HashMap<>();
        map.put("test1Variable", 123);
        map.put("test2Variable", "testing");
        ArgumentMapTest controller = new ArgumentMapTest() {

            public int test1Variable;

            public String test2;

            @Override
            public void setTest2Variable(String value) {
                test2 = value;
            }

            @Override
            public void doAssertions() {
                assertEquals(123, test1Variable);
                assertEquals("testing", test2);
            }
        };

        FCFUtil.wireController(map, controller);
        controller.doAssertions();
    }

    @Test
    public void getTextComponentTest() {
        BaseComponent cmp;
        cmp = FCFUtil.getTextComponent("general text");
        assertTrue(cmp instanceof Cell);
        cmp = FCFUtil.getTextComponent("<html>html text</html>");
        assertTrue(cmp instanceof Html);
        cmp = FCFUtil.getTextComponent("https://url");
        assertTrue(cmp instanceof Hyperlink);
        cmp = FCFUtil.getTextComponent("http://url");
        assertTrue(cmp instanceof Hyperlink);
    }

    @Test
    public void getResourcePathTest() {
        assertEquals("web/org/fujionclinical/ui/core/", FCFUtil.getResourcePath(UtilTest.class));
        assertEquals("web/org/fujionclinical/ui/", FCFUtil.getResourcePath(UtilTest.class, 1));
        assertEquals("web/org/fujionclinical/ui/core/", FCFUtil.getResourcePath(UtilTest.class.getPackage()));
        assertEquals("web/org/fujionclinical/", FCFUtil.getResourcePath(UtilTest.class.getPackage(), 2));
        assertEquals("web/org/fujionclinical/ui/core/", FCFUtil.getResourcePath("org.fujionclinical.ui.core"));
        assertEquals("web/org/fujionclinical/ui/core/", FCFUtil.getResourcePath("org.fujionclinical.ui.core", -2));
        assertEquals("web/org/fujionclinical/ui/", FCFUtil.getResourcePath("org.fujionclinical.ui.core", 1));
    }
}
