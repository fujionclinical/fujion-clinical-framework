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
package org.fujionclinical.plugin.sessiontracker;

import org.fujion.client.ExecutionContext;
import org.fujion.component.Cell;
import org.fujion.component.Row;
import org.fujion.model.IComponentRenderer;
import org.fujion.websocket.Session;

import java.util.Date;

/**
 * RowRenderer to define rows within the Session/Desktop Tracking Grid
 */
public class SessionRenderer implements IComponentRenderer<Row, Session> {
    
    @Override
    public Row render(Session session) {
        Row row = new Row();
        String sessionId = session.getId();
        Date creationTime = new Date(session.getCreationTime());
        Date lastAccessedTime = new Date(session.getLastActivity());
        String clientAddress = session.getSocket().getRemoteAddress().toString();
        
        createCell(row, sessionId);
        createCell(row, clientAddress);
        createCell(row, creationTime);
        createCell(row, lastAccessedTime);
        
        if (session == ExecutionContext.getSession()) {
            row.setStyles("font-weight:bold;color:blue");
        }
        
        return row;
    }
    
    private void createCell(Row row, Object data) {
        Cell cell = new Cell();
        cell.setLabel(data == null ? null : data.toString());
        row.addChild(cell);
    }
}
