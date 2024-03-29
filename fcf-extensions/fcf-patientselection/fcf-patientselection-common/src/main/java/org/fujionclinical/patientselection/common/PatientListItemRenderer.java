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

import org.apache.commons.lang3.StringUtils;
import org.coolmodel.core.complex.Identifier;
import org.coolmodel.foundation.entity.Person;
import org.coolmodel.util.PersonUtils;
import org.fujion.common.Assert;
import org.fujion.component.Cell;
import org.fujion.component.Columns;
import org.fujion.component.Grid;
import org.fujion.component.Row;
import org.fujion.event.ChangeEvent;
import org.fujion.model.IComponentRenderer;
import org.fujionclinical.patientlist.PatientListItem;

import java.time.LocalDate;

import static org.fujionclinical.patientselection.common.Constants.MSG_UNKNOWN_PATIENT;

/**
 * Renderer for patient list items.
 */
public class PatientListItemRenderer implements IComponentRenderer<Row, Object> {

    private final Grid grid;

    /**
     * Force singleton usage.
     *
     * @param grid The grid component.
     */
    public PatientListItemRenderer(Grid grid) {
        this.grid = grid;
    }

    /**
     * Render a list item.
     *
     * @param object The associated PatientListItem or Patient object.
     */
    @Override
    public Row render(Object object) {
        PatientListItem patientListItem;

        if (object instanceof PatientListItem) {
            patientListItem = (PatientListItem) object;
        } else if (object instanceof Person) {
            patientListItem = new PatientListItem((Person) object, null);
        } else {
            return Assert.fail("Invalid object type: %s", object);
        }

        Row row = new Row();
        row.addEventForward(ChangeEvent.TYPE, grid, null);
        row.setData(patientListItem);
        Person patient = patientListItem.getPatient();
        // If columns are defined, limit rendering to that number of cells.
        Columns columns = grid.getColumns();
        int max = columns == null ? 0 : columns.getChildCount();
        String info = patientListItem.getInfo();

        if (patient != null) {
            String name = PersonUtils.getFullName(patient);
            String[] names;

            if (name == null) {
                names = new String[]{MSG_UNKNOWN_PATIENT.toString()};
            } else {
                names = name.split(",", 2);
            }

            Identifier mrn = PersonUtils.getMRN(patient);
            addCell(row, names[0].trim(), max);
            addCell(row, names.length == 1 ? "" : names[1].trim(), max);
            addCell(row, mrn == null ? "" : mrn.getId(), max);

            if (StringUtils.isEmpty(info)) {
                LocalDate dob = patient.getBirthDate();
                info = dob == null ? "" : dob.toString();
            }
        }

        addCell(row, info, max);
        return row;
    }

    /**
     * Add a cell to the row.
     *
     * @param row   Grid row to receive new cell;
     * @param label Text label for the cell.
     * @param max   Maximum # of allowable cells.
     */
    private void addCell(
            Row row,
            String label,
            int max) {
        if (max == 0 || row.getChildCount() < max) {
            row.addChild(new Cell(label));
        }
    }

}
