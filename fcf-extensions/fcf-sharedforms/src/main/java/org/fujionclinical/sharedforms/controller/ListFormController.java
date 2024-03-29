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

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.coolmodel.mediator.common.Formatters;
import org.fujion.annotation.EventHandler;
import org.fujion.annotation.WiredComponent;
import org.fujion.common.MiscUtil;
import org.fujion.common.StrUtil;
import org.fujion.component.*;
import org.fujion.component.Paneview.Orientation;
import org.fujion.event.ChangeEvent;
import org.fujion.event.ClickEvent;
import org.fujion.event.DblclickEvent;
import org.fujion.model.IComponentRenderer;
import org.fujion.model.ListModel;
import org.fujionclinical.shell.elements.ElementPlugin;
import org.fujionclinical.ui.command.CommandUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Controller for list view based forms.
 *
 * @param <T> The type of data object being rendered.
 */
public abstract class ListFormController<T> extends CaptionedFormController {

    private static final String SORT_TYPE_ATTR = "@sort_type";

    private static final String COL_INDEX_ATTR = "@col_index";

    protected final ListModel<T> model = new ListModel<>();

    @WiredComponent
    protected Columns columns;

    @WiredComponent
    protected Rows rows;

    @WiredComponent
    private Paneview mainView;

    @WiredComponent
    private Grid grid;

    @WiredComponent
    private Pane detailPane;

    @WiredComponent
    private Pane listPane;

    @WiredComponent
    private Label status;

    private boolean allowPrint;

    private String alternateColor = "#F0F0F0";

    private int colCount;

    private final IComponentRenderer<Row, T> renderer = new IComponentRenderer<Row, T>() {

        @Override
        public Row render(T object) {
            Row row = new Row();
            row.setData(object);
            row.addEventForward(ClickEvent.TYPE, grid, ChangeEvent.TYPE);
            ListFormController.this.renderRow(row, object);
            return row;
        }

    };

    private String dataName;

    private boolean deferUpdate = true;

    private boolean dataNeedsUpdate = true;

    private int sortColumn;

    private boolean sortAscending;

    /**
     * Abort any pending async call.
     */
    protected abstract void asyncAbort();

    /**
     * Async request to fetch data.
     */
    protected abstract void requestData();

    @Override
    public void onLoad(ElementPlugin plugin) {
        super.onLoad(plugin);
        root = detailPane;
        rows.setModel(model);
        rows.setRenderer(renderer);
        CommandUtil.associateCommand("REFRESH", grid);
        plugin.registerProperties(this, "allowPrint", "alternateColor", "deferUpdate", "showDetailPane", "layout",
                "horizontal");
    }

    public void destroy() {
        asyncAbort();
    }

    protected void setup(
            String title,
            String... headers) {
        setup(title, 1, headers);
    }

    protected void setup(
            String title,
            int sortBy,
            String... headers) {
        setCaption(title);
        dataName = title;
        String defWidth = (100 / headers.length) + "%";

        for (String header : headers) {
            String[] pcs = StrUtil.split(header, "^", 3);
            Column lhdr = new Column(pcs[0]);
            columns.addChild(lhdr);
            lhdr.setAttribute(SORT_TYPE_ATTR, NumberUtils.toInt(pcs[1]));
            lhdr.setAttribute(COL_INDEX_ATTR, colCount++);
            String width = pcs[2];

            if (!width.isEmpty()) {
                if (NumberUtils.isDigits(width) || "min".equals(width)) {
                    //lhdr.setHflex(width);
                } else {
                    lhdr.setWidth(width);
                }
            } else {
                lhdr.setWidth(defWidth);
            }
        }

        sortColumn = Math.abs(sortBy) - 1;
        sortAscending = sortBy > 0;
        doSort();
    }

    public boolean getHorizontal() {
        return mainView.getOrientation() == Orientation.HORIZONTAL;
    }

    public void setHorizontal(boolean value) {
        mainView.setOrientation(value ? Orientation.HORIZONTAL : Orientation.VERTICAL);
    }

    public String getAlternateColor() {
        return alternateColor;
    }

    public void setAlternateColor(String value) {
        this.alternateColor = value;
    }

    public boolean getShowDetailPane() {
        return detailPane.isVisible();
    }

    public void setShowDetailPane(boolean value) {
        detailPane.setVisible(value);
    }

    public boolean getAllowPrint() {
        return allowPrint;
    }

    public void setAllowPrint(boolean value) {
        this.allowPrint = value;
    }

    public boolean getDeferUpdate() {
        return deferUpdate;
    }

    public void setDeferUpdate(boolean value) {
        this.deferUpdate = value;
    }

    /**
     * Getter method for Layout property. Format:
     *
     * <pre>
     *   List Pane Size:Sort Column:Sort Direction;Column 0 Index:Column 0 Width;...
     * </pre>
     *
     * @return The layout data.
     */
    public String getLayout() {
        StringBuilder sb = new StringBuilder();
        sb.append(':').append(sortColumn).append(':').append(sortAscending);

        for (BaseComponent comp : columns.getChildren()) {
            Column lhdr = (Column) comp;
            sb.append(';').append(lhdr.getAttribute(COL_INDEX_ATTR)).append(':').append(lhdr.getWidth());
        }
        return sb.toString();
    }

    /**
     * Setter method for Layout property. This property allows an application to control the
     * position of the splitter bar and ordering of columns.
     *
     * @param layout The layout data.
     */
    public void setLayout(String layout) {
        String[] pcs = StrUtil.split(layout, ";");

        if (pcs.length > 0) {
            String[] spl = StrUtil.split(pcs[0], ":", 3);
            sortColumn = NumberUtils.toInt(spl[1]);
            sortAscending = BooleanUtils.toBoolean(spl[2]);
        }

        for (int i = 1; i < pcs.length; i++) {
            String[] col = StrUtil.split(pcs[i], ":", 2);
            Column lhdr = getColumnByIndex(NumberUtils.toInt(col[0]));

            if (lhdr != null) {
                lhdr.setWidth(col[1]);
                lhdr.setIndex(i - 1);
            }
        }

        doSort();
    }

    private void doSort() {
        getColumnByIndex(sortColumn).sort();
    }

    /**
     * Returns the column corresponding to the specified index.
     *
     * @param index Column index.
     * @return List header at index.
     */
    private Column getColumnByIndex(int index) {
        for (BaseComponent comp : columns.getChildren()) {
            if ((Integer) comp.getAttribute(COL_INDEX_ATTR) == index) {
                return (Column) comp;
            }
        }

        return null;
    }

    /**
     * Clears the list and status.
     */
    protected void reset() {
        model.clear();
        status(null);
    }

    protected Row getSelectedRow() {
        return grid.getRows().getSelectedRow();
    }

    @SuppressWarnings("unchecked")
    protected T getSelectedValue() {
        Row item = getSelectedRow();

        if (item != null) {
            return (T) item.getData();
        }

        return null;
    }

    /**
     * Initiate asynchronous call to retrieve data from host.
     */
    protected void loadData() {
        dataNeedsUpdate = false;
        asyncAbort();
        reset();
        status("Retrieving " + dataName + "...");

        try {
            requestData();
        } catch (Throwable t) {
            status("Error Retrieving " + dataName + "...^" + t.getMessage());
        }
    }

    /**
     * Converts a data object for rendering.
     *
     * @param object  T data object to be rendered.
     * @param columns Returns a list of objects to render, one per column.
     */
    protected abstract void populate(
            T object,
            List<Object> columns);

    /**
     * Adds a column using a factory wrapped in an exception handler.  Any exception results in a null value being added.
     *
     * @param factory The factory.
     * @param columns The column list.
     */
    protected void addColumn(
            Supplier<Object> factory,
            List<Object> columns) {
        Object value;

        try {
            value = factory.get();
        } catch (Exception e) {
            value = null;
        }

        columns.add(value);
    }

    /**
     * Render a single row.
     *
     * @param row    Row being rendered.
     * @param object Data object
     */
    protected void renderRow(
            Row row,
            T object) {
        List<Object> columns = new ArrayList<>();
        boolean error = false;

        try {
            populate(object, columns);
        } catch (Exception e) {
            columns.clear();
            columns.add(MiscUtil.formatExceptionForDisplay(e));
            error = true;
        }

        row.setVisible(!columns.isEmpty());
        row.addEventListener(ChangeEvent.class, (event) -> {
            if (getShowDetailPane() && row.isSelected()) {
                rowSelected(row);
            }
        });
        row.addEventListener(DblclickEvent.class, (event) -> {
            if (!getShowDetailPane() && row.isSelected()) {
                rowSelected(row);
            }
        });

        for (Object colData : columns) {
            Object data = transformData(colData);
            Rowcell cell = new Rowcell();
            cell.setLabel(data == null ? null : data.toString());
            row.addChild(cell);
            cell.setData(colData);

            if (error) {
                cell.setColspan(colCount);
            }
        }
    }

    /**
     * Override to perform any necessary transforms on data before rendering.
     *
     * @param data Data to transform.
     * @return Transformed data.
     */
    protected String transformData(Object data) {
        return Formatters.format(data);
    }

    /**
     * Render the model data.
     */
    protected void renderData() {
        if (model.isEmpty()) {
            status("No " + dataName + " Found");
        } else {
            status(null);
            alphaSort();
        }
    }

    /**
     * Implement to sort the data before displaying.
     */
    protected void alphaSort() {

    }

    /**
     * Forces an update of displayed list.
     */
    @Override
    public void refresh() {
        dataNeedsUpdate = true;

        if (mainView != null) {
            if (!deferUpdate || isActive()) {
                loadData();
            } else {
                reset();
            }
        }
    }

    protected void status(String message) {
        if (message != null) {
            grid.setVisible(false);
            status.setLabel(StrUtil.piece(message, "^"));
            status.setHint(StrUtil.piece(message, "^", 2, 999));
            status.setVisible(true);
        } else {
            status.setVisible(false);
            status.setLabel(null);
            status.setHint(null);
            grid.setVisible(true);
        }
    }

    @EventHandler(value = "click", target = "menupopup.mnuRefresh")
    private void onClick$mnuRefresh() {
        refresh();
    }

    @EventHandler(value = "command", target = "@grid")
    private void onCommand$grid() {
        refresh();
    }

    /**
     * Called when a row is selected. Override for specialized handling.
     *
     * @param row The selected row.
     */
    protected void rowSelected(Row row) {

    }

    @Override
    public void onActivate() {
        super.onActivate();

        if (dataNeedsUpdate) {
            loadData();
        }
    }

}
