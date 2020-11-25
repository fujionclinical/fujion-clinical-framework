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
package org.fujionclinical.sharedforms.controller;

import edu.utah.kmm.model.cool.foundation.entity.Person;
import edu.utah.kmm.model.cool.mediator.datasource.DataSource;
import edu.utah.kmm.model.cool.mediator.datasource.DataSources;
import org.apache.commons.lang3.StringUtils;
import org.fujion.annotation.WiredComponent;
import org.fujion.component.BaseComponent;
import org.fujion.component.Html;
import org.fujion.component.Row;
import org.fujion.component.Window;
import org.fujion.page.PageUtil;
import org.fujion.thread.ICancellable;
import org.fujion.thread.ThreadedTask;
import org.fujionclinical.api.event.IEventSubscriber;
import org.fujionclinical.api.model.patient.PatientContext;
import org.fujionclinical.shell.elements.ElementPlugin;
import org.fujionclinical.ui.dialog.DialogUtil;
import org.fujionclinical.ui.util.FCFUtil;

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

    private static final String DETAIL_POPUP = FCFUtil.getResourcePath(AbstractResourceListView.class) + "resourceListDetailPopup.fsp";

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
    protected void threadFinished(ICancellable thread) {
        ThreadedTask task = (ThreadedTask) thread;

        try {
            task.rethrow();
        } catch (Throwable e) {
            status("An unexpected error was encountered:  " + FCFUtil.formatExceptionForDisplay(e));
            return;
        }

        model.clear();
        initModel(task);
        renderData();
    }

    protected abstract void setup();

    protected abstract void initModel(ThreadedTask task);

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
        return queryString;
    }

    public Class<R> getResourceClass() {
        return resourceClass;
    }

}
