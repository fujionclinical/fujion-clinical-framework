/*
 * #%L
 * Fujion Clinical Framework
 * %%
 * Copyright (C) 2020 fujionclinical.org
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
package org.fujionclinical.ui.dialog;

import org.apache.commons.lang3.math.NumberUtils;
import org.fujion.dialog.DialogControl;
import org.fujionclinical.api.property.PropertyUtil;

/**
 * Dialog response provider that uses the underlying property store for persisting response.
 */
public class DialogReponseProvider implements DialogControl.IDialogResponseProvider {

    private static final String SAVED_RESPONSE_PROP_NAME = "FCF.SAVED.RESPONSES";

    protected DialogReponseProvider() {
        DialogControl.dialogResponseProvider = this;
    }

    @Override
    public int get(String responseId) {
        return NumberUtils.toInt(PropertyUtil.getValue(SAVED_RESPONSE_PROP_NAME, responseId), -1);
    }

    @Override
    public void set(
            String responseId,
            int index) {
        PropertyUtil.saveValue(SAVED_RESPONSE_PROP_NAME, responseId, false,
                index < 0 ? null : Integer.toString(index));
    }

}
