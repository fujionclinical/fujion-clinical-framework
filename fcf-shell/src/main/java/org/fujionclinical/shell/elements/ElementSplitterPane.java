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
package org.fujionclinical.shell.elements;

import org.apache.commons.lang.StringUtils;
import org.fujion.annotation.EventHandler;
import org.fujion.component.Pane;
import org.fujion.event.Event;
import org.fujion.event.EventUtil;
import org.fujion.event.ResizeEvent;

/**
 * A child of the ElementSplitterView.
 */
public class ElementSplitterPane extends ElementUI {
    
    static {
        registerAllowedParentClass(ElementSplitterPane.class, ElementSplitterView.class);
        registerAllowedChildClass(ElementSplitterPane.class, ElementUI.class, 1);
    }
    
    private final Pane pane = new Pane();
    
    private final Event deferredUpdateEvent = new Event("deferredUpdate", pane);
    
    private double size = 100.0;
    
    private boolean relative = true;
    
    private boolean resizable = true;
    
    public ElementSplitterPane() {
        super();
        setResizable(resizable);
        setOuterComponent(pane);
        pane.wireController(this);
        updateSize(true);
    }
    
    public double getSize() {
        return size;
    }
    
    public void setSize(double size) {
        this.size = size;
        updateSize();
    }
    
    @Override
    public String getInstanceName() {
        return "Pane #" + (getParent().indexOfChild(this) + 1);
    }
    
    public void setRelative(boolean relative) {
        this.relative = relative;
        updateSize();
    }
    
    public boolean isRelative() {
        return relative;
    }
    
    public String getCaption() {
        return pane.getTitle();
    }
    
    public void setCaption(String caption) {
        pane.setTitle(caption);
    }
    
    /*package*/ void updateSize(boolean isHorizontal) {
        String sz = StringUtils.removeEnd(Double.toString(size), ".0");
        boolean isFlex = !isDesignMode() && relative;
        pane.setFlex(isFlex ? sz : null);
        pane.setHeight(isFlex || isHorizontal ? null : sz + "px");
        pane.setWidth(isFlex || !isHorizontal ? null : sz + "px");
    }
    
    private void updateSize() {
        if (getParent() != null) {
            updateSize(isHorizontal());
        }
    }
    
    @EventHandler(value = "resize", target = "@pane")
    private void resize(ResizeEvent event) {
        if (getParent() != null) {
            size = isHorizontal() ? event.getWidth() : event.getHeight();
            updateSize();
        }
    }

    @EventHandler(value = "deferredUpdate", target = "@pane")
    private void deferredUpdate() {
        pane.reportSize();
    }

    private boolean isHorizontal() {
        return getParent() != null && ((ElementSplitterView) getParent()).isHorizontal();
    }

    public boolean isResizable() {
        return resizable;
    }
    
    public void setResizable(boolean resizable) {
        this.resizable = resizable;
        pane.setSplittable(resizable || isDesignMode());
    }
    
    @Override
    public void setDesignMode(boolean designMode) {
        super.setDesignMode(designMode);
        setResizable(resizable);
        EventUtil.echo(deferredUpdateEvent);
    }
    
}
