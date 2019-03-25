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
package org.fujionclinical.ui.popupsupport;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * Represents a single popup data item.
 */
public class PopupData implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String title;
    
    private String message;
    
    public PopupData() {
        super();
    }
    
    /**
     * Supports the old method of serializing a popup item.
     * 
     * @param value The popup data.
     */
    @Deprecated
    public PopupData(String value) {
        String[] pcs = value.split("\\^", 2);
        
        if (pcs.length == 0) {
            return;
        }
        
        if (pcs.length == 1) {
            title = "";
            message = pcs[0];
        } else {
            title = pcs[0];
            message = pcs[1];
        }
    }
    
    public String getTitle() {
        return StringUtils.isEmpty(title) ? "Popup Message" : title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public boolean isEmpty() {
        return StringUtils.isEmpty(title) && StringUtils.isEmpty(message);
    }
}
