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
package org.fujionclinical.help.viewer;

import org.fujion.ancillary.IAutoWired;
import org.fujion.common.MiscUtil;
import org.fujion.component.BaseComponent;
import org.fujion.component.BaseUIComponent;
import org.fujion.component.Namespace;
import org.fujion.page.PageUtil;
import org.fujionclinical.help.HelpTopic;
import org.fujionclinical.help.HelpViewType;
import org.fujionclinical.help.IHelpView;
import org.fujionclinical.help.viewer.HelpHistory.ITopicListener;

/**
 * Abstract base class for all help tabs. It descends from Tab.
 */
public abstract class HelpViewBase implements IAutoWired, ITopicListener {
    
    private final HelpViewType viewType;
    
    private final HelpViewer viewer;
    
    private final Namespace container = new Namespace();
    
    private boolean initialized;
    
    /**
     * Creates a new tab for the specified view type.
     * 
     * @param viewer The help viewer instance.
     * @param viewType The view type supported by the created tab.
     * @return The help tab that supports the specified view type.
     */
    public static HelpViewBase createView(HelpViewer viewer, HelpViewType viewType) {
        Class<? extends HelpViewBase> viewClass = viewType == null ? null : getViewClass(viewType);
        
        if (viewClass == null) {
            return null;
        }
        
        try {
            return viewClass.getConstructor(HelpViewer.class, HelpViewType.class).newInstance(viewer, viewType);
        } catch (Exception e) {
            throw MiscUtil.toUnchecked(e);
        }
    }
    
    /**
     * Returns the help view class that services the specified view type. For unsupported view
     * types, returns null.
     * 
     * @param viewType The view type.
     * @return A help tab class.
     */
    private static Class<? extends HelpViewBase> getViewClass(HelpViewType viewType) {
        switch (viewType) {
            case TOC:
                return HelpViewContents.class;
            
            case KEYWORD:
            case INDEX:
                return HelpViewIndex.class;

            case SEARCH:
                return HelpViewSearch.class;
            
            case HISTORY:
                return HelpViewHistory.class;
            
            case GLOSSARY:
                return HelpViewIndex.class;
            
            default:
                return null;
        }
    }
    
    /**
     * Main constructor.
     * 
     * @param viewer The help viewer.
     * @param viewType The view type supported by the created tab.
     * @param fcfTemplate The template that specifies the layout for the tab.
     */
    public HelpViewBase(HelpViewer viewer, HelpViewType viewType, String fcfTemplate) {
        this.viewer = viewer;
        this.viewType = viewType;
        PageUtil.createPage(HelpUtil.RESOURCE_PREFIX + fcfTemplate, container);
        container.wireController(this);
    }
    
    @Override
    public void afterInitialized(BaseComponent comp) {
    }
    
    public BaseUIComponent getContainer() {
        return container;
    }
    
    /**
     * Adds a view to the tab. Subclasses should override this to merge resources from the view.
     * When doing so, always call the super method.
     * 
     * @param view The view to add.
     */
    public void addView(IHelpView view) {
        initialized = false;
    }
    
    /**
     * Convenience method for setting the currently selected topic in the viewer.
     * 
     * @param topic HelpTopic to select in the viewer.
     */
    protected void setTopic(HelpTopic topic) {
        viewer.setTopic(topic);
    }
    
    /**
     * Called when the tab is selected.
     */
    public void onSelect() {
        if (!initialized) {
            initialized = true;
            init();
        }
    }
    
    /**
     * Called prior to initial display of the tab to provide any final initialization (e.g., to sort
     * a list or tree).
     */
    protected void init() {
    }
    
    /**
     * Returns the view type associated with the tab.
     * 
     * @return The associated view type.
     */
    public HelpViewType getViewType() {
        return viewType;
    }
    
    public HelpViewer getViewer() {
        return viewer;
    }
    
    /**
     * Called when a new topic is selected in the viewer. Override to provide any special actions
     * (like selecting the corresponding UI element). Note that the originator of the topic change
     * may be the same tab.
     */
    @Override
    public void onTopicSelected(HelpTopic topic) {
        
    }
    
}
