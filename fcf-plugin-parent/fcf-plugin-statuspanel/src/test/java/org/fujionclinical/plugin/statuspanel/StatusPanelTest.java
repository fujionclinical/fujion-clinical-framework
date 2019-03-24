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
package org.fujionclinical.plugin.statuspanel;

import org.fujion.component.BaseComponent;
import org.fujion.component.Label;
import org.fujionclinical.api.event.EventManager;
import org.fujionclinical.shell.Shell;
import org.fujionclinical.shell.elements.ElementPlugin;
import org.fujionclinical.ui.controller.FrameworkController;
import org.fujionclinical.ui.test.MockUITest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StatusPanelTest extends MockUITest {
    
    @Test
    public void test() throws Exception {
        Shell shell = new Shell();
        shell.setParent(getMockEnvironment().getSession().getPage());
        shell.setLayout("/StatusPanelTest.xml");
        getMockEnvironment().flushEvents();
        ElementPlugin plugin = shell.getActivatedPlugin("fcfStatusPanel");
        BaseComponent root = plugin.getOuterComponent().getFirstChild();
        StatusPanel controller = (StatusPanel) FrameworkController.getController(root);
        assertNotNull("Controller must not be null.", controller);
        assertEquals(1, root.getChildren().size());
        test(root, "STATUS", 1, 1);
        test(root, "STATUS.TEST1", 1, 2);
        test(root, "STATUS.TEST1", 2, 2);
        test(root, "STATUS.TEST2", 1, 3);
        test(root, "STATUS.TEST2", 2, 3);
    }
    
    private void test(BaseComponent root, String eventName, int eventData, int expectedSize) {
        String labelText = eventName + "." + eventData;
        EventManager.getInstance().fireLocalEvent(eventName, labelText);
        getMockEnvironment().flushEvents();
        assertEquals(expectedSize, root.getChildren().size());
        Label label = (Label) root.getChildren().get(expectedSize - 1).getFirstChild();
        assertEquals(labelText, label.getLabel());
    }
}
