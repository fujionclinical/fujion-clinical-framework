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
package org.fujionclinical.patientselection.common;


import org.fujion.common.LocalizedMessage;
import org.fujion.core.CoreUtil;

/**
 * Constants for patient selection.
 */
public class Constants {

    public static final String RESOURCE_PATH = CoreUtil.getResourceClassPath(Constants.class, 1);

    public static final String FILTER_DROP_ID = "patientselection.filter.drop.id";

    public static final String PROP_PREFIX = Constants.class.getName() + ".";

    public static final String RESULT_ATTRIB = Constants.PROP_PREFIX + "result";

    public static final String SELECTOR_ATTRIB = PROP_PREFIX + "selector";

    public static final String SELECTED_PATIENT_ATTRIB = PROP_PREFIX + "patient";

    public static final String PATIENT_LIST_ATTRIB = PROP_PREFIX + "list";

    public static final LocalizedMessage MSG_CANNOT_SELECT_TITLE = new LocalizedMessage("patientselection.error.noselect.title");

    public static final LocalizedMessage MSG_CANNOT_SELECT_MESSAGE = new LocalizedMessage("patientselection.error.noselect.message");

    public static final LocalizedMessage MSG_LIST_WAIT_MESSAGE = new LocalizedMessage("patientselection.list.wait.message");

    public static final LocalizedMessage MSG_DATE_RANGE_LABEL = new LocalizedMessage("patientselection.daterange.label");

    public static final LocalizedMessage MSG_DATE_RANGE_VALUES = new LocalizedMessage("patientselection.daterange.values");

    public static final LocalizedMessage MSG_WARN_NO_FILTERS = new LocalizedMessage("patientselection.warn.no.filters");

    public static final LocalizedMessage MSG_WARN_NO_PATIENTS = new LocalizedMessage("patientselection.warn.no.patients");

    public static final LocalizedMessage MSG_WARN_NO_LIST_SELECTED = new LocalizedMessage("patientselection.warn.no.list.selected");

    public static final LocalizedMessage MSG_DEMOGRAPHIC_TITLE = new LocalizedMessage("patientselection.right.pane.title.demo");

    public static final LocalizedMessage MSG_MANAGE_TITLE = new LocalizedMessage("patientselection.right.pane.title.manage");

    public static final LocalizedMessage MSG_FILTER_RENAME_TITLE = new LocalizedMessage("patientselection.filter.rename.title");

    public static final LocalizedMessage MSG_FILTER_NEW_TITLE = new LocalizedMessage("patientselection.filter.new.title");

    public static final LocalizedMessage MSG_FILTER_NAME_PROMPT = new LocalizedMessage("patientselection.filter.name.prompt");

    public static final LocalizedMessage MSG_FILTER_DELETE_TITLE = new LocalizedMessage("patientselection.filter.deletion.confirm.title");

    public static final LocalizedMessage MSG_FILTER_DELETE_PROMPT = new LocalizedMessage("patientselection.filter.deletion.confirm.prompt");

    public static final LocalizedMessage MSG_ACTION_SELECT = new LocalizedMessage("patientselection.action.select.label");

    public static final LocalizedMessage MSG_SEARCH_MESSAGE = new LocalizedMessage("patientselection.search.message");

    public static final LocalizedMessage MSG_ERROR_NO_SELECTORS = new LocalizedMessage("patientselection.error.no.selectors");

    public static final LocalizedMessage MSG_ERROR_PATIENT_NOT_FOUND = new LocalizedMessage("patientsearch.error.patient.not.found");

    public static final LocalizedMessage MSG_ERROR_NOT_FOUND = new LocalizedMessage("patientsearch.error.unknown");

    public static final LocalizedMessage MSG_ERROR_MISSING_REQUIRED = new LocalizedMessage("patientsearch.error.missing.required");

    public static final LocalizedMessage MSG_UNKNOWN_PATIENT = new LocalizedMessage("patientselection.warn.unknown.patient");

    public static final LocalizedMessage MSG_TOO_MANY_MATCHES_TEXT = new LocalizedMessage("patientsearch.warn.too.many.matches.text");

    public static final LocalizedMessage MSG_TOO_MANY_MATCHES_TITLE = new LocalizedMessage("patientsearch.warn.too.many.matches.title");

    public static final String BTN_REFINE_LABEL = "patientsearch.btn.refine.label";

    public static final String BTN_CONTINUE_LABEL = "patientsearch.btn.continue.label";

    public static final String REFINE_BUTTONS = BTN_REFINE_LABEL + "|" + BTN_CONTINUE_LABEL;

    /**
     * Enforce static class.
     */
    private Constants() {
    }

}
