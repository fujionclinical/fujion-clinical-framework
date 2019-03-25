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

import org.fujion.component.Label;
import org.fujionclinical.ui.util.ThemeUtil;

/**
 * Simple button stock object.
 */
public class ElementLabel extends ElementUI {
    
    static {
        registerAllowedParentClass(ElementLabel.class, ElementUI.class);
    }
    
    private final Label label = new Label();
    
    private ThemeUtil.LabelSize size = ThemeUtil.LabelSize.MEDIUM;
    
    private ThemeUtil.LabelStyle style = ThemeUtil.LabelStyle.DEFAULT;
    
    public ElementLabel() {
        setOuterComponent(label);
        updateStyle();
        setMaskMode(null);
    }
    
    private void updateStyle() {
        ThemeUtil.applyThemeClass(label, style, size);
    }
    
    @Override
    public String getInstanceName() {
        return getDisplayName() + " (" + getLabel() + ")";
    }
    
    /**
     * Returns the button's label text.
     *
     * @return The label text.
     */
    public String getLabel() {
        return label.getLabel();
    }
    
    /**
     * Sets the button's label text.
     *
     * @param value The label text.
     */
    public void setLabel(String value) {
        label.setLabel(value);
    }
    
    public ThemeUtil.LabelSize getSize() {
        return size;
    }
    
    public void setSize(ThemeUtil.LabelSize size) {
        this.size = size;
        updateStyle();
    }
    
    public ThemeUtil.LabelStyle getStyle() {
        return style;
    }
    
    public void setStyle(ThemeUtil.LabelStyle style) {
        this.style = style;
        updateStyle();
    }
    
    @Override
    protected void applyColor() {
        label.addStyle("color", getColor());
    }
    
}
