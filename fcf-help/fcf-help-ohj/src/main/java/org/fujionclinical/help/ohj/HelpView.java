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
package org.fujionclinical.help.ohj;

import oracle.help.common.*;
import org.fujionclinical.help.HelpTopic;
import org.fujionclinical.help.HelpTopicNode;
import org.fujionclinical.help.HelpViewType;
import org.fujionclinical.help.IHelpView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Adaptor for Oracle help view.
 */
public class HelpView implements IHelpView {
    
    private final View view;
    
    private final HelpViewType helpViewType;
    
    private final HelpTopicNode rootNode = new HelpTopicNode(null);
    
    private final HelpSet_OHJ hs;
    
    public HelpView(HelpSet_OHJ hs, View view, HelpViewType helpViewType) {
        this.hs = hs;
        this.view = view;
        this.helpViewType = helpViewType;
        initTopicTree();
    }
    
    /**
     * Initialize the topic tree.
     */
    private void initTopicTree() {
        try {
            Object data = view.getViewData();
            
            if (!(data instanceof TopicTree)) {
                return;
            }
            
            TopicTree topicTree = (TopicTree) data;
            HelpTopicNode baseNode;
            
            if (helpViewType == HelpViewType.TOC) {
                HelpTopic topic = new HelpTopic(null, hs.getName(), hs.getName());
                baseNode = new HelpTopicNode(topic);
                rootNode.addChild(baseNode);
            } else {
                baseNode = rootNode;
            }
            
            initTopicTree(baseNode, topicTree.getRoot());
            
        } catch (IOException e) {
            // NOP
        }
        
    }
    
    /**
     * Initialize the topic tree. Converts the Oracle help TopicTreeNode-based tree to a
     * HelpTopicNode-based tree.
     * 
     * @param htnParent Current help topic node.
     * @param ttnParent Current topic tree node.
     */
    private void initTopicTree(HelpTopicNode htnParent, TopicTreeNode ttnParent) {
        initTopicTree(htnParent, ttnParent.getChildren());
        
        if (ttnParent instanceof KeywordTopicTreeNode) {
            initTopicTree(htnParent, ((KeywordTopicTreeNode) ttnParent).getEntries());
        }
    }
    
    /**
     * Initialize the topic tree. Converts the Oracle help TopicTreeNode-based tree to a
     * HelpTopicNode-based tree.
     * 
     * @param htnParent Current help topic node.
     * @param children List of child nodes.
     */
    private void initTopicTree(HelpTopicNode htnParent, List<?> children) {
        if (children != null) {
            for (Object node : children) {
                TopicTreeNode ttnChild = (TopicTreeNode) node;
                Topic topic = ttnChild.getTopic();
                Target target = topic.getTarget();
                String source = view.getBook().getBookTitle();
                URL url = null;
                
                try {
                    url = target == null ? null : target.getURL();
                } catch (MalformedURLException e) {
                    // NOP
                }
                
                HelpTopic ht = new HelpTopic(url, topic.getLabel(), source);
                HelpTopicNode htnChild = new HelpTopicNode(ht);
                htnParent.addChild(htnChild);
                initTopicTree(htnChild, ttnChild);
            }
        }
    }
    
    public View getView() {
        return view;
    }
    
    @Override
    public HelpTopicNode getTopicTree() {
        return rootNode;
    }
    
    @Override
    public HelpViewType getViewType() {
        return helpViewType;
    }
    
}
