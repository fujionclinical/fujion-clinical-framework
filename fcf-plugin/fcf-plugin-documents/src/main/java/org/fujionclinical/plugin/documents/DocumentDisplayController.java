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
package org.fujionclinical.plugin.documents;

import org.fujion.annotation.EventHandler;
import org.fujion.annotation.WiredComponent;
import org.fujion.component.Combobox;
import org.fujion.component.Label;
import org.fujion.component.Row;
import org.fujion.event.EventUtil;
import org.fujion.model.IListModel;
import org.fujionclinical.api.model.core.DateTimeWrapper;
import org.fujionclinical.api.model.document.IDocument;
import org.fujionclinical.api.query.DateQueryFilter;
import org.fujionclinical.api.query.IQueryContext;
import org.fujionclinical.api.query.InMemoryQueryService;
import org.fujionclinical.sharedforms.controller.AbstractGridController;

import java.util.List;

/**
 * Controller for displaying the contents of selected documents.
 */
public class DocumentDisplayController extends AbstractGridController<IDocument, IDocument> {

    private final DocumentDisplayComboRenderer comboRenderer = new DocumentDisplayComboRenderer();

    private List<IDocument> documents;

    @WiredComponent
    private Label lblInfo;

    @WiredComponent
    private Combobox cboHeader;

    public DocumentDisplayController() {
        super(new InMemoryQueryService<IDocument>(), "fcfdocuments", "DOCUMENT", "documentsPrint.css", "patient");
        ((InMemoryQueryService) getService()).setQueryResult(() -> documents);
    }

    @Override
    protected void initializeController() {
        super.initializeController();
        cboHeader.setRenderer(comboRenderer);
        grid.getRows().setRenderer(new DocumentDisplayRenderer());
    }

    @Override
    protected void prepareQueryContext(IQueryContext context) {
        context.setParam("documents", documents);
    }

    /**
     * This view should be closed when the patient context changes.
     */
    @Override
    protected void onParameterChanged(SupplementalQueryParam<?> param) {
        closeView();
    }

    /**
     * Suppress data fetch if there are no documents in the view.
     */
    @Override
    protected void fetchData() {
        if (documents != null) {
            super.fetchData();
        }
    }

    @Override
    protected List<IDocument> toModel(List<IDocument> results) {
        return results;
    }

    /**
     * Scroll to document with same header.
     */
    @EventHandler(value = "change", target = "@cboHeader")
    private void onChange$cboHeader() {
        IDocument doc = (IDocument) cboHeader.getSelectedItem().getData();

        for (Row row : grid.getRows().getChildren(Row.class)) {
            IDocument doc2 = (IDocument) row.getData();

            if (doc == doc2) {
                row.scrollIntoView();
                break;
            }
        }
    }

    /**
     * Clears any displayed documents and reverts to document selection mode.
     */
    @EventHandler(value = "click", target = "btnReturn")
    private void onClick$btnReturn() {
        closeView();
    }

    /**
     * Clears any displayed documents and reverts to document selection mode.
     */
    protected void closeView() {
        documents = null;
        setModel(null);
        EventUtil.post("viewOpen", root, false);
    }

    /**
     * Sets the documents to be displayed and updates the displayed count.
     *
     * @param documents The documents to be displayed.
     */
    protected void setDocuments(List<IDocument> documents) {
        this.documents = documents;
        lblInfo.setLabel((documents == null ? 0 : documents.size()) + " document(s)");
        refresh();
    }

    /**
     * Updates the header selector when the model changes.
     */
    @Override
    protected void setModel(IListModel<IDocument> model) {
        super.setModel(model);
        cboHeader.setModel(model);
        cboHeader.setValue(null);
    }

    @Override
    public DateTimeWrapper getDateByType(
            IDocument result,
            DateQueryFilter.DateType dateType) {
        return null;
    }

}