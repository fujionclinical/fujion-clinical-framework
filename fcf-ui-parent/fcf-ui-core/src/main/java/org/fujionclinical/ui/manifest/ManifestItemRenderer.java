/*
 * #%L
 * Fujion Clinical Framework
 * %%
 * Copyright (C) 2018 fujionclinical.org
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

import org.fujion.component.BaseComponent;
import org.fujion.component.Row;
import org.fujion.component.Grid;
import org.fujion.event.ClickEvent;
import org.fujion.event.Event;
import org.fujion.event.EventUtil;
import org.fujion.event.IEventListener;

public class ManifestItemRenderer extends BaseRenderer<ManifestItem> {
    
    private final IEventListener listener = new IEventListener() {
        
        @Override
        public void onEvent(Event event) {
            BaseComponent target = event.getCurrentTarget();
            Event newEvent = new Event("showManifest", target.getAncestor(Grid.class), target.getData());
            EventUtil.send(newEvent);
        }
        
    };
    
    @Override
    public Row render(ManifestItem manifestItem) {
        Row row = new Row();
        row.setData(manifestItem);
        addCell(row, manifestItem.implModule);
        addCell(row, manifestItem.implVersion);
        addCell(row, manifestItem.implVendor);
        row.addEventListener(ClickEvent.TYPE, listener);
        return row;
    }
    
    @Override
    public void init(Grid grid) {
        grid.getRows().getModelAndView(ManifestItem.class).setRenderer(this);
        addColumn(grid, "Module", "40%", "@implModule").setSortColumn(true);
        addColumn(grid, "Version", "20%", "@implVersion");
        addColumn(grid, "Author", "40%", "@implVendor");
    }
}
