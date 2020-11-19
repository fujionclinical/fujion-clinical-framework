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
import edu.utah.kmm.model.cool.core.datatype.Attachment;
import org.fujion.component.*;
import org.fujion.model.IComponentRenderer;
import org.fujionclinical.sharedforms.common.FormConstants;

/**
 * Renderer for the document display.
 */
public class DocumentDisplayRenderer implements IComponentRenderer<Row, Document> {

    /**
     * Render the list item for the specified document.
     *
     * @param doc The document associated with the list item.
     */
    @Override
    public Row render(Document doc) {
        Row row = new Row();
        row.setData(doc);
        Cell cell = new Cell();
        row.addChild(cell);
        Div sep = new Div();
        sep.addClass("fcf-documents-sep");
        cell.addChild(sep);
        Div div = new Div();
        div.addClass(FormConstants.SCLASS_DOCUMENT_TITLE);
        cell.addChild(div);
        Div boxHeader = new Div();
        div.addClass("fujion-layout-horizontal");
        Label header = new Label(doc.getDescription());
        header.addClass(FormConstants.SCLASS_DOCUMENT_TITLE);
        boxHeader.addChild(header);
        div.addChild(boxHeader);

        for (Attachment content : doc.getContent()) {
            if (content.getContentType().equals("text/html")) {
                Html html = new Html();
                html.setContent(content.toString());
                cell.addChild(html);
            } else if (content.getContentType().equals("text/plain")) {
                Label lbl = new Label(content.toString());
                cell.addChild(lbl);
            } else {
                Iframe frame = new Iframe();
                frame.setContent(content.getContent());
                cell.addChild(frame);
            }
        }

        return row;
    }

}
