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

import edu.utah.kmm.model.cool.clinical.finding.Document;
import edu.utah.kmm.model.cool.mediator.query.QueryContext;
import edu.utah.kmm.model.cool.terminology.ConceptReference;
import org.fujion.annotation.EventHandler;
import org.fujion.annotation.WiredComponent;
import org.fujion.common.StrUtil;
import org.fujion.component.*;
import org.fujion.event.Event;
import org.fujion.event.EventUtil;
import org.fujion.model.IListModel;
import org.fujionclinical.api.query.filter.AbstractQueryFilter;
import org.fujionclinical.api.query.filter.DateQueryFilter.DateType;
import org.fujionclinical.api.query.service.DAOQueryService;
import org.fujionclinical.sharedforms.controller.AbstractGridController;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller for the list-based display of clinical documents.
 */
public class DocumentListController extends AbstractGridController<Document, Document> {

    /**
     * Handles filtering by document type.
     */
    private class DocumentTypeFilter extends AbstractQueryFilter<Document> {

        @Override
        public boolean include(Document document) {
            String filter = getCurrentFilter();
            return filter == null || (document.hasType() && filter.equals(document.getType().getDisplayText()));
        }

        @Override
        public boolean updateContext(QueryContext context) {
            context.setParam("type", getCurrentFilter());
            return true;
        }

    }

    private static final String DOCUMENT_QUERY = "patient={{patient}}";

    @WiredComponent
    private Button btnClear;

    @WiredComponent
    private Button btnView;

    @WiredComponent
    private Combobox cboFilter;

    @WiredComponent
    private Comboitem cbiSeparator;

    @WiredComponent
    private Label lblFilter;

    @WiredComponent
    private Label lblInfo;

    private String viewText; //default view selected documents

    private final String lblBtnViewSelectAll = StrUtil.getLabel("fcfdocuments.plugin.btn.view.selectall.label");

    private String fixedFilter;

    private final Collection<String> allTypes;

    public DocumentListController() {
        super(new DAOQueryService<>(Document.class, DOCUMENT_QUERY), "fcfdocuments", "DOCUMENT", "documentsPrint.css", "patient");
        registerQueryFilter(new DocumentTypeFilter());
        allTypes = Collections.emptyList(); //service.getTypes();
    }

    @Override
    public void initializeController() {
        super.initializeController();
        grid.getRows().setRenderer(new DocumentListRenderer(grid));
        viewText = btnView.getLabel();
        getPlugin().registerProperties(this, "fixedFilter");
        addFilters(allTypes, null, null);
        updateSelectCount();
    }

    /**
     * This is a good place to update the filter list.
     */
    @Override
    protected List<Document> toModel(List<Document> queryResult) {
        if (queryResult != null) {
            updateListFilter(queryResult);
        }

        return queryResult;
    }

    /**
     * Presents a quick pick list limited to types present in the unfiltered document list.
     *
     * @param documents The unfiltered document list.
     */
    private void updateListFilter(List<Document> documents) {
        if (fixedFilter != null) {
            return;
        }

        List<Comboitem> items = new ArrayList<>();
        
        cboFilter.getChildren(Comboitem.class).forEach(items::add);
        
        items.remove(0);
        Set<String> types = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        String currentFilter = getCurrentFilter();

        for (Comboitem item : items) {
            if (item == cbiSeparator) {
                break;
            }
            
            item.destroy();
        }

        cboFilter.setSelectedIndex(0);

        if (documents != null) {
            for (Document doc : documents) {
                types.addAll(doc.getType().getConceptReferences().stream().map(ConceptReference::getCode).collect(Collectors.toList()));
            }
        }

        addFilters(types, cbiSeparator, currentFilter);

        if (currentFilter != null && cboFilter.getSelectedIndex() < 1) {
            Comboitem item = (Comboitem) cboFilter.findChildByLabel(currentFilter);
            cboFilter.setSelectedItem(item);
        }
    }

    private void addFilters(Collection<String> types, BaseComponent ref, String selected) {
        for (String type : types) {
            Comboitem item = new Comboitem(type);
            item.setValue(type);
            cboFilter.addChild(item, ref);

            if (type.equals(selected)) {
                cboFilter.setSelectedItem(item);
            }
        }
    }

    /**
     * Returns the currently active type filter.
     *
     * @return The currently active type filter.
     */
    private String getCurrentFilter() {
        return fixedFilter != null ? fixedFilter
                : cboFilter.getSelectedIndex() > 0 ? cboFilter.getSelectedItem().getValue() : null;
    }

    /**
     * Handle change in type filter selection.
     */
    @EventHandler(value = "change", target = "cboFilter")
    private void onChange$cboFilter() {
        applyFilters();
    }

    /**
     * Update the display count of selected documents.
     */
    protected void updateSelectCount() {
        int selCount = grid.getRows().getSelectedCount();

        if (selCount == 0) {
            btnView.setLabel(lblBtnViewSelectAll);
            btnClear.setDisabled(true);
        } else {
            btnView.setLabel(viewText + " (" + selCount + ")");
            btnClear.setDisabled(false);
        }

        btnView.setDisabled(grid.getRows().getChildCount() == 0);
    }

    /**
     * Update selection count.
     */
    @EventHandler(value = "change", target = "@grid.rows")
    private void onChange$grid() {
        updateSelectCount();
    }

    @Override
    protected void clearSelection() {
        super.clearSelection();
        updateSelectCount();
    }
    
    /**
     * Double-clicking enters document view mode.
     *
     * @param event The double click event.
     */
    @EventHandler(value = "dblclick", target = "@grid")
    private void onDoubleClick$grid(Event event) {
        BaseComponent cmpt = event.getCurrentTarget();

        if (cmpt instanceof Row) {
            EventUtil.post("deferredOpen", grid, cmpt);
        }
    }

    /**
     * Opening the display view after a double-click is deferred to avoid anomalies with selection
     * of the associated list item.
     *
     * @param event The deferred open event.
     */
    @EventHandler(value = "deferredOpen", target = "@grid")
    private void onDeferredOpen$grid(Event event) {
        Row row = (Row) event.getData();
        row.setSelected(true);
        updateSelectCount();
        onClick$btnView();
    }

    /**
     * Triggers document view mode.
     */
    @EventHandler(value = "click", target = "@btnView")
    private void onClick$btnView() {
        EventUtil.post("viewOpen", root, true);
    }

    /**
     * Returns a list of currently selected documents, or if no documents are selected, of all
     * documents.
     *
     * @return The currently selected documents.
     */
    protected List<Document> getSelectedDocuments() {
        return getObjects(grid.getRows().getSelectedCount() > 0);
    }

    /**
     * Returns the fixed filter, if any.
     *
     * @return The fixed filter.
     */
    public String getFixedFilter() {
        return fixedFilter;
    }

    /**
     * Sets the fixed filter.
     *
     * @param name The fixed filter.
     */
    public void setFixedFilter(String name) {
        fixedFilter = name;
        cboFilter.setVisible(fixedFilter == null);
        lblFilter.setVisible(fixedFilter != null);
        lblFilter.setLabel(fixedFilter);
        refresh();
    }

    @Override
    protected void setModel(IListModel<Document> model) {
        super.setModel(model);
        int docCount = model == null ? 0 : model.size();
        lblInfo.setLabel(docCount + " document(s)");
        btnView.setDisabled(docCount == 0);
        updateSelectCount();
    }

    @Override
    public LocalDateTime getDateByType(Document result, DateType dateMode) {
        return result.getRecordedDate();
    }

}
