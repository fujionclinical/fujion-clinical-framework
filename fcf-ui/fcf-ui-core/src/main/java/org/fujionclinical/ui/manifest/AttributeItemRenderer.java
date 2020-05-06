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
package org.fujionclinical.ui.manifest;

import org.fujion.component.Grid;
import org.fujion.component.Row;

public class AttributeItemRenderer extends BaseRenderer<AttributeItem> {
    
    @Override
    public Row render(AttributeItem attributeItem) {
        Row row = new Row();
        row.setData(attributeItem);
        addCell(row, attributeItem.name);
        addContent(row, attributeItem.value);
        return row;
    }
    
    @Override
    public void init(Grid grid) {
        grid.getRows().getModelAndView(AttributeItem.class).setRenderer(this);
        addColumn(grid, "Attribute", "30%", "@name");
        addColumn(grid, "Value", "70%", "@value");
    }
    
}
