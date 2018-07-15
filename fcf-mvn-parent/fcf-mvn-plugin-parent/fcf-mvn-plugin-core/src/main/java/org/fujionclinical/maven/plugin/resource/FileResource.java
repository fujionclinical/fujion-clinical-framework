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
package org.fujionclinical.maven.plugin.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Represents a file resource entry.
 */
public class FileResource implements IResource {
    
    private final File file;
    
    private final String root;
    
    public FileResource(File file, String root) {
        this.file = file;
        this.root = root;
        
        if (!file.exists()) {
            throw new RuntimeException("Failed to find file resource: " + file);
        }
    }
    
    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }
    
    @Override
    public String getSourcePath() {
        String path = file.getAbsolutePath();
        
        if (root != null && path.startsWith(root)) {
            path = path.substring(root.length());
        }
        
        return path + (isDirectory() ? "/" : "");
    }
    
    @Override
    public String getTargetPath() {
        return getSourcePath();
    }
    
    @Override
    public long getTime() {
        return file.lastModified();
    }
    
    @Override
    public boolean isDirectory() {
        return file.isDirectory();
    }
    
}
