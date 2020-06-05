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
package org.fujionclinical.ui.sharedforms.controller;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fujion.annotation.WiredComponent;
import org.fujion.component.BaseComponent;
import org.fujion.component.Html;
import org.fujion.component.Row;
import org.fujion.component.Window;
import org.fujion.page.PageUtil;
import org.fujion.thread.ICancellable;
import org.fujion.thread.ThreadedTask;
import org.fujionclinical.api.event.IEventSubscriber;
import org.fujionclinical.api.model.core.DomainDAORegistry;
import org.fujionclinical.api.model.core.IDomainDAO;
import org.fujionclinical.api.model.core.IDomainObject;
import org.fujionclinical.api.model.patient.IPatient;
import org.fujionclinical.api.model.patient.PatientContext;
import org.fujionclinical.api.query.IQueryContext;
import org.fujionclinical.api.query.QueryContext;
import org.fujionclinical.api.query.QueryExpression;
import org.fujionclinical.api.query.QueryExpressionParser;
import org.fujionclinical.shell.elements.ElementPlugin;
import org.fujionclinical.ui.dialog.DialogUtil;
import org.fujionclinical.ui.util.FCFUtil;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for displaying FHIR resources in a columnar format.
 *
 * @param <R> Type of resource object.
 * @param <M> Type of model object.
 */
public abstract class ResourceListView<R extends IDomainObject, M> extends ListFormController<M> {

    private static final Log log = LogFactory.getLog(ResourceListView.class);

    private static final String DETAIL_POPUP = FCFUtil.getResourcePath(ResourceListView.class) + "resourceListDetailPopup.fsp";

    private final IQueryContext queryContext = new QueryContext();

    @WiredComponent
    protected Html detailView;

    protected IPatient patient;

    private final IEventSubscriber<IPatient> patientChangeListener = (eventName, patient) -> setPatient(patient);

    protected Class<R> resourceClass;

    private String detailTitle;

    private QueryExpression queryExpression;

    private IDomainDAO<R> dao;

    protected void setup(
            Class<R> resourceClass,
            String title,
            String detailTitle,
            String queryString,
            int sortBy,
            String... headers) {
        this.detailTitle = detailTitle;
        this.queryExpression = QueryExpressionParser.getInstance().parse(resourceClass, queryString);
        this.resourceClass = resourceClass;
        this.dao = DomainDAORegistry.getDAO(resourceClass);
        Assert.notNull(dao, () -> "Cannot find DAO for " + resourceClass);
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
    protected void requestData() {
        startBackgroundThread(map -> {
            map.put("results", dao.search(queryExpression, queryContext));
        });
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

    private void setPatient(IPatient patient) {
        this.patient = patient;
        queryContext.setParam("patient", patient == null ? null : patient.getId());
        this.refresh();
    }

}
