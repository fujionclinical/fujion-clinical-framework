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
package org.fujionclinical.ui.dialog;

import org.fujion.ancillary.IAutoWired;
import org.fujion.ancillary.IResponseCallback;
import org.fujion.annotation.EventHandler;
import org.fujion.annotation.WiredComponent;
import org.fujion.common.DateRange;
import org.fujion.common.DateUtil;
import org.fujion.component.BaseComponent;
import org.fujion.component.Datebox;
import org.fujion.component.Window;
import org.fujion.core.CoreUtil;
import org.fujion.page.PageUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Presents a date range dialog.
 */
public class DateRangeDialog implements IAutoWired {

    private static final String RESOURCE_PREFIX = CoreUtil.getResourceClassPath(DateRangeDialog.class);

    /**
     * Displays the date range dialog.
     *
     * @param callback Callback for returning a date range reflecting the inputs from the dialog.
     *                 This will be null if the input is cancelled or if an unexpected error occurs.
     */
    public static void show(IResponseCallback<DateRange> callback) {
        Date today = DateUtil.today();
        show(today, today, callback);
    }

    /**
     * Displays the date range dialog.
     *
     * @param startDefault Default start date.
     * @param endDefault Default end date.
     * @param callback Callback for returning a date range reflecting the inputs from the dialog.
     *            This will be null if the input is cancelled or if an unexpected error occurs.
     */
    public static void show(Date startDefault, Date endDefault, IResponseCallback<DateRange> callback) {
        show(new DateRange(startDefault, endDefault), callback);
    }

    /**
     * Displays the date range dialog.
     *
     * @param dateRange The initial date range.
     * @param callback Callback for returning a date range reflecting the inputs from the dialog.
     *            This will be null if the input is cancelled or if an unexpected error occurs.
     */
    public static void show(DateRange dateRange, IResponseCallback<DateRange> callback) {
        Map<String, Object> args = new HashMap<>();
        args.put("dateRange", dateRange);
        Window dlg = (Window) PageUtil.createPage(RESOURCE_PREFIX + "dateRangeDialog.fsp", null, args)
                .get(0);
        dlg.modal((event) -> {
            callback.onComplete((DateRange) dlg.getAttribute("result"));
        });
    }

    @WiredComponent
    private Datebox startDate;

    @WiredComponent
    private Datebox endDate;

    private Window root;

    /**
     * Set default values for input boxes.
     */
    @Override
    public void afterInitialized(BaseComponent comp) {
        root = (Window) comp;
        DateRange dateRange = (DateRange) comp.getAttribute("dateRange");
        startDate.setValue(dateRange.getStartDate());
        endDate.setValue(dateRange.getEndDate());
    }

    /**
     * Entering return in the start date box sets focus to the end date box.
     */
    @EventHandler(value = "enter", target = "@startDate")
    private void onOK$startDate() {
        endDate.setFocus(true);
    }

    /**
     * Entering return in the end date box is same as clicking the OK button.
     */
    @EventHandler(value = "enter", target = "@endDate")
    private void onOK$endDate() {
        onClick$btnOK();
    }

    /**
     * Clicking the cancel button aborts the input.
     */
    @EventHandler(value = "click", target = "btnCancel")
    private void onClick$btnCancel() {
        root.close();
    }

    /**
     * Clicking the OK button creates a DateRangeItem object with the responses from the dialog and
     * closes the dialog.
     */
    @EventHandler(value = "click", target = "btnOK")
    private void onClick$btnOK() {
        if (startDate.getValue().after(endDate.getValue())) {
            Datebox temp = startDate;
            startDate = endDate;
            endDate = temp;
        }

        DateRange dateRange = new DateRange(null, startDate.getValue(), endDate.getValue());
        root.setAttribute("result", dateRange);
        root.close();
    }

    /**
     * Sets the end date value equal to the start date value.
     */
    @EventHandler(value = "click", target = "btnSameAsStart")
    private void onClick$btnSameAsStart() {
        endDate.setValue(startDate.getValue());
    }

    /**
     * Sets the start date value equal to the end date value.
     */
    @EventHandler(value = "click", target = "btnSameAsEnd")
    public void onClick$btnSameAsEnd() {
        startDate.setValue(endDate.getValue());
    }
}
