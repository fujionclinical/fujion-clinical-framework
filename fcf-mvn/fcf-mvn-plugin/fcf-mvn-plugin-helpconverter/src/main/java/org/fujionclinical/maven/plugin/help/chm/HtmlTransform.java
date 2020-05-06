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
package org.fujionclinical.maven.plugin.help.chm;

import org.fujionclinical.maven.plugin.core.BaseMojo;
import org.fujionclinical.maven.plugin.transform.AbstractTransform;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Transforms HTML pages by converting them from window-1252 encoding to UTF-8 and removing the
 * character set metadata declaration. This is necessary because FCF expects UTF-8 encoding for
 * jar-embedded resources.
 */
public class HtmlTransform extends AbstractTransform {
    
    private static final byte[] EOL = "\r\n".getBytes();
    
    public HtmlTransform(BaseMojo mojo) {
        super(mojo);
    }
    
    @Override
    public void transform(InputStream inputStream, OutputStream outputStream) throws Exception {
        boolean metaFound = false;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, BaseTransform.CS_WIN1252))) {
            
            String line;
            
            while ((line = reader.readLine()) != null) {
                if (!metaFound && line.contains("charset=")) {
                    metaFound = true;
                } else {
                    outputStream.write(line.getBytes(BaseTransform.CS_UTF8));
                    outputStream.write(EOL);
                }
            }
        }
    }
    
}
