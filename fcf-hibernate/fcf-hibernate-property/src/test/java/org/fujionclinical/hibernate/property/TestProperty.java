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
package org.fujionclinical.hibernate.property;

import org.fujionclinical.api.spring.SpringUtil;
import org.fujionclinical.ui.test.MockUITest;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestProperty extends MockUITest {

    private static PropertyService service;

    @Before
    public void init() {
        service = service != null ? service : SpringUtil.getBean(PropertyService.class);
    }

    @Test
    public void testService() {
        test1(null);
        test1("instance1");
        test1("instance2");
        test2("instance1");
        test2("instance2");
        test3("prop2", true, 2);
        test3("prop2", false, 2);
        test3("prop1", true, 0);
    }

    private void test1(String instanceName) {
        saveValue("prop1", instanceName, true, "global1", 1);
        //saveValue("prop1", instanceName, false, null, 1);
        assertEquals("global1", service.getValue("prop1", instanceName));
        saveValue("prop1", instanceName, false, "local1", 2);
        assertEquals("local1", service.getValue("prop1", instanceName));
        saveValue("prop1", instanceName, false, "local1", 2);
        assertEquals("local1", service.getValue("prop1", instanceName));
        saveValue("prop1", instanceName, true, null, 1);
        assertEquals("local1", service.getValue("prop1", instanceName));
        saveValue("prop1", instanceName, false, null, 0);
        assertNull(service.getValue("prop1", instanceName));
        saveValue("prop2", instanceName, false, "local2", 1);
        saveValue("prop2", instanceName, true, "global2", 2);
    }

    private void saveValue(String propertyName, String instanceName, boolean asGlobal, String value, int expectedCount) {
        service.saveValue(propertyName, instanceName, asGlobal, value);
        List<Property> all = service.getAllProperties();
        assertEquals(expectedCount, all.size());
    }

    private void test2(String instanceName) {
        List<String> localValues = createValueList(false);
        service.saveValues("multi1", instanceName, false, localValues);
        List<String> globalValues = createValueList(true);
        service.saveValues("multi1", instanceName, true, globalValues);
        assertEquals(localValues, service.getValues("multi1", instanceName));
        service.saveValues("multi1", instanceName, false, null);
        assertEquals(globalValues, service.getValues("multi1", instanceName));
    }

    private List<String> createValueList(boolean asGlobal) {
        String value = asGlobal ? "global" : "local";
        List<String> list = new ArrayList<>();

        for (int i = 1; i < 10; i++) {
            list.add(value + i);
        }

        return list;
    }

    private void test3(String propertyName, boolean asGlobal, int count) {
        List<String> instances = service.getInstances(propertyName, asGlobal);
        assertEquals(count, instances.size());
    }

}
