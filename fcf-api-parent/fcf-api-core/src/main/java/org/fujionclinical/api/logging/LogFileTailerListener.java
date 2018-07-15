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
package org.fujionclinical.api.logging;

/**
 * Provides listener notification methods when a tailed file is updated
 * 
 * @author Steven Haines
 *         <a href="http://www.informit.com/guides/content.aspx?g=java&seqNum=226">project</a>
 */
public interface LogFileTailerListener {
    
    
    /**
     * A new line has been added to the tailed file
     * 
     * @param line The new line that has been added to the tailed file
     */
    public void newFileLine(String line);
    
    /**
     * FileTailer exceeded {@link LogFileTailer#getMaxActiveInterval()} Note that this means that
     * {@link LogFileTailer#stopTailing()} was called
     */
    public void tailerTerminated();
}
