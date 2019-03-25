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
package org.fujionclinical.help.viewer;

import org.apache.commons.lang.StringUtils;
import org.fujion.annotation.EventHandler;
import org.fujion.annotation.WiredComponent;
import org.fujion.component.BaseComponent;
import org.fujion.component.Listbox;
import org.fujion.component.Listitem;
import org.fujion.component.Textbox;
import org.fujion.event.ChangeEvent;
import org.fujion.model.IComponentRenderer;
import org.fujion.model.IModelAndView;
import org.fujion.model.ListModel;
import org.fujion.model.ModelAndView;
import org.fujionclinical.help.HelpTopic;
import org.fujionclinical.help.HelpTopicNode;
import org.fujionclinical.help.HelpViewType;
import org.fujionclinical.help.IHelpView;

import java.util.*;

/**
 * Tab for displaying the keyword index. This tab consists of two list boxes: one that displays the
 * keywords and one the displays the topics associated with the selected keyword. There is also a
 * quick find feature that allows locating a keyword by entering the first letters of the keyword.
 */
public class HelpViewIndex extends HelpViewBase {
    
    @WiredComponent
    private Listbox lstKeywords;
    
    @WiredComponent
    private Listbox lstTopics;
    
    @WiredComponent
    private Textbox txtFind;
    
    private final Map<String, List<HelpTopic>> topicIndex = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    
    private ListModel<String> keywordList;
    
    private IModelAndView<Listitem, HelpTopic> modelAndView;
    
    private final IComponentRenderer<Listitem, String> keywordRenderer = term -> new Listitem(term);
    
    /**
     * Create the help tab for the specified viewer and viewType.
     * 
     * @param viewer The viewer.
     * @param viewType The help view type.
     */
    public HelpViewIndex(HelpViewer viewer, HelpViewType viewType) {
        super(viewer, viewType, "helpIndexTab.fsp");
    }
    
    @Override
    public void afterInitialized(BaseComponent comp) {
        super.afterInitialized(comp);
        modelAndView = lstTopics.getModelAndView(HelpTopic.class);
        modelAndView.setRenderer(new HelpTopicRenderer());
    }
    
    /**
     * Initialize the topic list when a keyword is selected. The last topic selected for the keyword
     * is automatically selected.
     */
    @EventHandler(value = "change", target = "@lstKeywords")
    private void onSelect$lstKeywords() {
        Listitem item = lstKeywords.getSelectedItem();
        
        if (item == null) {
            lstTopics.destroyChildren();
            setTopic(null);
            return;
        }
        
        List<HelpTopic> topics = getTopics(item.getLabel());
        
        if (!item.hasAttribute("sorted")) {
            item.setAttribute("sorted", true);
            Collections.sort(topics);
        }
        
        modelAndView.setModel(new ListModel<>(topics));
        onSelect$lstTopics();
    }
    
    /**
     * Set the topic view when a topic selection is made.
     */
    @EventHandler(value = "change", target = "@lstTopics")
    private void onSelect$lstTopics() {
        Listitem item = lstTopics.getSelectedItem();
        Listitem keywordItem = lstKeywords.getSelectedItem();
        
        if (item == null) {
            int i = keywordItem.hasAttribute("last") ? (Integer) keywordItem.getAttribute("last") : 0;
            item = (Listitem) lstTopics.getChildAt(i);
            lstTopics.setSelectedItem(item);
        }
        
        if (item != null) {
            keywordItem.setAttribute("last", lstTopics.getSelectedIndex());
            setTopic((HelpTopic) item.getData());
        } else {
            setTopic(null);
        }
    }
    
    /**
     * Locates and selects a matching keyword as the user types in the quick find text box.
     * 
     * @param event The input event.
     */
    @EventHandler(value = "change", target = "@txtFind")
    private void onChange$txtFind(ChangeEvent event) {
        String find = event.getValue(String.class).toLowerCase();
        
        if (StringUtils.isEmpty(find)) {
            return;
        }
        
        int match = -1;
        
        for (int i = 0; i < keywordList.size(); i++) {
            if (keywordList.get(i).toLowerCase().startsWith(find)) {
                match = i;
                break;
            }
        }
        
        if (match != lstKeywords.getSelectedIndex()) {
            Listitem item = match == -1 ? null : (Listitem) lstKeywords.getChildAt(match);
            lstKeywords.setSelectedItem(item);
            onSelect$lstKeywords();
        }
    }
    
    /**
     * Initialize the tab the first time it is selected. This method creates a model list from the
     * current keyword list and assigns it to the keyword list box.
     * 
     * @see org.fujionclinical.help.viewer.HelpViewBase#init()
     */
    @Override
    protected void init() {
        super.init();
        keywordList = new ListModel<>(topicIndex.keySet());
        new ModelAndView<>(lstKeywords, keywordList, keywordRenderer);
    }
    
    /**
     * Sets the focus to the quick find text box when the tab is selected.
     * 
     * @see org.fujionclinical.help.viewer.HelpViewBase#onSelect()
     */
    @Override
    public void onSelect() {
        super.onSelect();
        txtFind.setFocus(true);
        txtFind.selectAll();
    }
    
    /**
     * Add keywords and topics from the specified view to the tab.
     * 
     * @see org.fujionclinical.help.viewer.HelpViewBase#addView(IHelpView)
     */
    @Override
    public void addView(IHelpView view) {
        super.addView(view);
        HelpTopicNode topNode = view.getTopicTree();
        
        for (HelpTopicNode child : topNode.getChildren()) {
            addKeywordTopic(child, null);
        }
    }
    
    /**
     * Traverse the keyword topic tree, adding keywords and topics to the list.
     * 
     * @param node Help topic node.
     * @param topics Topic list to receive results.
     */
    private void addKeywordTopic(HelpTopicNode node, List<HelpTopic> topics) {
        HelpTopic topic = node.getTopic();
        
        if (topics == null) {
            topics = getTopics(topic.getLabel());
        }
        
        if (topic.getURL() != null) {
            topics.add(topic);
        }
        
        for (HelpTopicNode child : node.getChildren()) {
            addKeywordTopic(child, topics);
        }
    }
    
    /**
     * Returns the topic list for the specified keyword, creating one if it does not already exist.
     * 
     * @param keyword The key word.
     * @return Topic list for key word.
     */
    private List<HelpTopic> getTopics(String keyword) {
        List<HelpTopic> topics = topicIndex.computeIfAbsent(keyword, k -> new ArrayList<>());

        return topics;
    }
    
}
