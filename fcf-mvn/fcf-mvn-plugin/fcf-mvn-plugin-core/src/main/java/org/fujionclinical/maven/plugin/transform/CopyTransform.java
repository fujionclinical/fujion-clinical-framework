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
package org.fujionclinical.maven.plugin.transform;

import org.apache.commons.io.IOUtils;
import org.fujionclinical.maven.plugin.core.BaseMojo;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Performs a simple copy of a resource from the source to the destination.
 */
public class CopyTransform extends AbstractTransform {
    
    public CopyTransform(BaseMojo mojo) {
        super(mojo);
    }
    
    @Override
    public void transform(InputStream inputStream, OutputStream outputStream) throws Exception {
        IOUtils.copy(inputStream, outputStream);
    }
}
