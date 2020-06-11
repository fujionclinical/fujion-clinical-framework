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
package org.fujionclinical.api;

import org.fujionclinical.api.core.CoreUtil;
import org.junit.Assert;
import org.junit.Test;

public class CoreUtilTest {

    interface GenericInterface<T1, T2, T3>{}

    interface GenericInterface2<T4, T5>{}

    class TestClass implements GenericInterface<String, Integer, CoreUtilTest>,
            GenericInterface2<Double, String> {};

    class TestClass2 extends TestClass {};

    class TestClass3 {}

    @Test
    public void test() {
        test(new TestClass());
        test(new TestClass2());
        Class<?>[] classes = CoreUtil.getGenericParameters(new TestClass3().getClass(), GenericInterface.class);
        Assert.assertNull(classes);
    }

    private void test(Object test) {
        Class<?>[] classes = CoreUtil.getGenericParameters(test.getClass(), GenericInterface.class);
        Assert.assertNotNull(classes);
        Assert.assertEquals(3, classes.length);
        Assert.assertEquals(String.class, classes[0]);
        Assert.assertEquals(Integer.class, classes[1]);
        Assert.assertEquals(CoreUtilTest.class, classes[2]);
        classes = CoreUtil.getGenericParameters(test.getClass(), GenericInterface2.class);
        Assert.assertNotNull(classes);
        Assert.assertEquals(2, classes.length);
        Assert.assertEquals(Double.class, classes[0]);
        Assert.assertEquals(String.class, classes[1]);
    }
}
