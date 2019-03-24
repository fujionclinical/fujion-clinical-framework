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
package org.fujionclinical.ui.manifest;

import org.fujion.component.*;
import org.fujion.model.IComponentRenderer;
import org.fujion.model.Sorting.SortOrder;
import org.fujionclinical.ui.util.FCFUtil;

/**
 * Base renderer.
 * 
 * @param <M> Class of rendered object.
 */
abstract class BaseRenderer<M> implements IComponentRenderer<Row, M> {
    
    public abstract void init(Grid grid);
    
    /**
     * Adds a cell with the specified content to the grid row.
     * 
     * @param row List row.
     * @param label Content for cell. Auto-detects type of content.
     * @return Newly created cell.
     */
    public Span addContent(Row row, String label) {
        Span cell = new Span();
        cell.addChild(FCFUtil.getTextComponent(label));
        row.addChild(cell);
        return cell;
    }
    
    /**
     * Adds a cell to the grid row.
     * 
     * @param row List row.
     * @param label Label text for cell.
     * @return Newly created cell.
     */
    public Cell addCell(Row row, String label) {
        Cell cell = new Cell(label);
        row.addChild(cell);
        return cell;
    }
    
    /**
     * Adds a column to a grid.
     * 
     * @param grid Grid.
     * @param label Label for column.
     * @param width Width for column.
     * @param sortBy Field for sorting.
     * @return Newly created column.
     */
    public Column addColumn(Grid grid, String label, String width, String sortBy) {
        Column column = new Column();
        grid.getColumns().addChild(column);
        column.setLabel(label);
        column.setWidth(width);
        column.setSortComparator(sortBy);
        column.setSortOrder(SortOrder.ASCENDING);
        return column;
    }
    
    public int compareStr(String s1, String s2) {
        return s1 == s2 ? 0 : s1 == null ? -1 : s2 == null ? 1 : s1.compareToIgnoreCase(s2);
    }
    
}
