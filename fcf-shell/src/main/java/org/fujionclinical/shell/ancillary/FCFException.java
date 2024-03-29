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
package org.fujionclinical.shell.ancillary;

import org.fujion.common.MiscUtil;

import java.io.Serial;

public class FCFException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    
    public static void raise(String text, Throwable t) throws FCFException {
        raise(text + "\n" + MiscUtil.formatExceptionForDisplay(t));
    }
    
    public static void raise(String text) throws FCFException {
        throw new FCFException(text);
    }
    
    public FCFException(String text) {
        super(text);
    }
}
