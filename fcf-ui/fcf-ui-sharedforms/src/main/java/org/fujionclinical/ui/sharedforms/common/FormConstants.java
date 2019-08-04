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
package org.fujionclinical.ui.sharedforms.common;

import org.fujionclinical.ui.util.FCFUtil;

/**
 * Package-specific constants.
 */
public class FormConstants {

    public static final String RESOURCE_PREFIX = FCFUtil.getResourcePath(FormConstants.class, 1);

    public static final String PROPERTY_ID_DATE_RANGE = "%.DATERANGE";

    public static final String PROPERTY_ID_EXPAND_DETAIL = "%.EXPAND.DETAIL";

    public static final String PROPERTY_ID_MAX_ROWS = "%.MAX.ROWS";

    public static final String PROPERTY_ID_SORT_MODE = "%.SORT";

    public static final String LABEL_ID_SORT_MODE = "%.plugin.cmbx.sort.mode.item.$.label";

    public static final String LABEL_ID_TITLE = "%.plugin.print.title";

    public static final String LABEL_ID_PAGE_ON = "%.plugin.btn.paging.label.on";

    public static final String LABEL_ID_PAGE_OFF = "%.plugin.btn.paging.label.off";

    public static final String LABEL_ID_MISSING_PARAMETER = "%.plugin.missing.parameter";

    public static final String LABEL_ID_NO_DATA = "%.plugin.no.data.found";

    public static final String LABEL_ID_FETCHING = "%.plugin.status.fetching";

    public static final String LABEL_ID_FILTERING = "%.plugin.status.filtering";

    public static final String LABEL_ID_WAITING = "%.plugin.status.waiting";

    /**
     * Enforce static class.
     */
    private FormConstants() {
    }
}
