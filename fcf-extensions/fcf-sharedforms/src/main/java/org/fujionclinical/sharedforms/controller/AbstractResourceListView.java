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
package org.fujionclinical.sharedforms.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.coolmodel.foundation.entity.Person;
import org.coolmodel.mediator.datasource.DataSource;
import org.coolmodel.mediator.datasource.DataSources;
import org.coolmodel.util.CoolUtils;
import org.fujion.annotation.WiredComponent;
import org.fujion.common.MiscUtil;
import org.fujion.component.BaseComponent;
import org.fujion.component.Html;
import org.fujion.component.Row;
import org.fujion.component.Window;
import org.fujion.core.CoreUtil;
import org.fujion.dialog.DialogUtil;
import org.fujion.page.PageUtil;
import org.fujion.thread.ThreadedTask;
import org.fujionclinical.api.cool.patient.PatientContext;
import org.fujionclinical.api.event.IEventSubscriber;
import org.fujionclinical.shell.elements.ElementPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for displaying resources in a columnar format.
 *
 * @param <R> Type of resource object.
 * @param <M> Type of model object.
 * @param <S> Type of data source.
 */
public abstract class AbstractResourceListView<R, M, S extends DataSource> extends ListFormController<M> {

    private static final Log log = LogFactory.getLog(AbstractResourceListView.class);

    private static final String DETAIL_POPUP = CoreUtil.getResourceClassPath(AbstractResourceListView.class) + "resourceListDetailPopup.fsp";

    @WiredComponent
    protected Html detailView;

    private Person patient;

    private final IEventSubscriber<Person> patientChangeListener = (eventName, patient) -> setPatient(patient);

    private Class<R> resourceClass;

    private String detailTitle;

    private String queryString;

    private S dataSource;

    protected void setup(
            Class<R> resourceClass,
            String title,
            String detailTitle,
            String queryString,
            int sortBy,
            String... headers) {
        this.detailTitle = detailTitle;
        this.queryString = queryString;
        this.resourceClass = resourceClass;
        super.setup(title, sortBy, headers);
    }

    /**
     * Override load list to clear display if no patient in context.
     */
    @Override
    protected void loadData() {
        if (patient == null) {
            asyncAbort();
            reset();
            status("No patient selected.");
        } else {
            super.loadData();
        }

        detailView.setContent(null);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void threadFinished(ThreadedTask task) {
        try {
            task.rethrow();
        } catch (Throwable e) {
            log.error(e);
            status("An unexpected error was encountered:  " + MiscUtil.formatExceptionForDisplay(e));
            return;
        }

        model.clear();
        initModel((List<R>) task.getAttribute("results"));
        renderData();
    }

    protected abstract void setup();

    protected abstract void initModel(List<R> entries);

    @Override
    protected void asyncAbort() {
        abortBackgroundThreads();
    }

    /**
     * Show detail for specified component.
     *
     * @param item The component containing the model object.
     */
    protected void showDetail(BaseComponent item) {
        @SuppressWarnings("unchecked")
        M modelObject = item == null ? null : (M) item.getData();
        String detail = modelObject == null ? null : getDetail(modelObject);

        if (!StringUtils.isEmpty(detail)) {
            if (getShowDetailPane()) {
                detailView.setContent(detail);
            } else {
                Map<String, Object> map = new HashMap<>();
                map.put("title", detailTitle);
                map.put("content", detail);
                map.put("allowPrint", getAllowPrint());
                try {
                    Window window = (Window) PageUtil
                            .createPage(DETAIL_POPUP, null, map).get(0);
                    window.modal(null);
                } catch (Exception e) {
                    DialogUtil.showError(e);
                }
            }
        }
    }

    protected String getDetail(M modelObject) {
        return null;
    }

    /**
     * Display detail when row is selected.
     */
    @Override
    protected void rowSelected(Row row) {
        showDetail(row);
    }

    @Override
    public void onLoad(ElementPlugin plugin) {
        super.onLoad(plugin);
        setup();
        PatientContext.getPatientContext().addListener(patientChangeListener);
        setPatient(PatientContext.getActivePatient());
    }

    @Override
    public void onUnload() {
        PatientContext.getPatientContext().removeListener(patientChangeListener);
    }

    public Person getPatient() {
        return patient;
    }

    private void setPatient(Person patient) {
        this.patient = patient;
        afterPatientChange(patient);
        this.refresh();
    }

    protected void afterPatientChange(Person patient) {
    }

    public S getDataSource() {
        return dataSource;
    }

    @SuppressWarnings("unchecked")
    public void setDataSource(String dataSourceId) {
        this.dataSource = (S) DataSources.get(dataSourceId);
    }

    public String getQueryString() {
        return queryString.replace("#", CoolUtils.getId(patient));
    }

    public Class<R> getResourceClass() {
        return resourceClass;
    }

}
