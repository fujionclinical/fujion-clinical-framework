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
package org.fujionclinical.ui.xml;

import org.fujion.core.CoreUtil;

/**
 * Static utility class for XML viewing constants.
 */
public class XMLConstants {

    protected static final String RESOURCE_PATH = CoreUtil.getResourceClassPath(XMLConstants.class);

    protected static final String VIEW_DIALOG = RESOURCE_PATH + "xmlViewer.fsp";

    protected static final String STYLE_TAG = "fcf-xml-tag";

    protected static final String STYLE_ATTR_NAME = "fcf-xml-attrname";

    protected static final String STYLE_ATTR_VALUE = "fcf-xml-attrvalue";

    protected static final String STYLE_CONTENT = "fcf-xml-content";

    protected static final String STYLE_COMMENT = "fcf-xml-comment";

    public static final String[] EXCLUDED_PROPERTIES = {};

    /**
     * Enforce static class.
     */
    private XMLConstants() {
    }
}
