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

import org.fujionclinical.help.HelpTopicNode;
import org.fujionclinical.help.HelpViewType;
import org.fujionclinical.help.IHelpView;

/**
 * IHelpView implementation for HTML Help navigator views.
 */
public class HelpView implements IHelpView {
    
    private final HelpViewType viewType;
    
    private final HelpTopicNode topics;
    
    /**
     * Create help view for given navigator view and view type.
     * 
     * @param topicTree The associated topic tree.
     * @param viewType View type.
     */
    public HelpView(HelpTopicTree topicTree, HelpViewType viewType) {
        this.viewType = viewType;
        
        if (viewType == HelpViewType.TOC) {
            topics = new HelpTopicNode(null);
            topics.addChild(topicTree.getRootNode());
        } else {
            topics = topicTree.getRootNode();
        }
    }
    
    /**
     * Returns the root node of the topic tree.
     */
    @Override
    public HelpTopicNode getTopicTree() {
        return topics;
    }
    
    /**
     * Returns the view type.
     */
    @Override
    public HelpViewType getViewType() {
        return viewType;
    }
    
}
