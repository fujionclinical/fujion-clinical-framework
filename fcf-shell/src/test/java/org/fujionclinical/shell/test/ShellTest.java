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
