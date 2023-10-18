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
package org.fujionclinical.help.chm;

import org.fujion.test.MockTest;
import org.fujionclinical.help.*;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestCHM extends MockTest {
    
    private static final String URL1 = "org/fujionclinical/help/content/vistaPatientGoalsHelp/#SYSTEM";
    
    @Test
    public void test() throws Exception {
        testHelpSet(URL1);
    }
    
    private void testHelpSet(String url) throws Exception {
        HelpModule module = new HelpModule();
        module.setUrl(url);
        module.setFormat("chm");
        module.setId("test");
        module.setTitle("test");
        IHelpSet hs = new HelpSet_CHMHelp(module);
        assertEquals("Patient Goals", hs.getTopic(hs.getHomeID()).getLabel());
        Collection<IHelpView> views = hs.getAllViews();
        assertEquals(1, views.size());
        
        for (IHelpView view : views) {
            HelpViewType type = view.getViewType();
            HelpTopicNode tree = view.getTopicTree();
            
            switch (type) {
                case TOC:
                    assertEquals(1, tree.getChildren().size());
                    assertEquals(7, tree.getChildren().get(0).getChildren().size());
                    break;
                
                case INDEX:
                    assertEquals(0, tree.getChildren().size());
                    break;
                
                default:
                    fail("Unexpected view type: " + type);
            }
        }
    }
}
