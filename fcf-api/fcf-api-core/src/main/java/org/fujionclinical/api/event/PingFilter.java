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
package org.fujionclinical.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Filter criterion for responding to a ping request.
 */
public class PingFilter implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public enum PingFilterType {
        APP_NAME, SENTINEL_EVENT
    }

    public final PingFilterType type;
    
    public final String value;
    
    @JsonCreator
    public PingFilter(@JsonProperty("type") PingFilterType type, @JsonProperty("value") String value) {
        this.type = type;
        this.value = value;
    }
    
    @Override
    public boolean equals(Object object) {
        if (object instanceof PingFilter) {
            PingFilter filter = (PingFilter) object;
            return filter.type == type && filter.value.equals(value);
        }
        
        return false;
    }
}
