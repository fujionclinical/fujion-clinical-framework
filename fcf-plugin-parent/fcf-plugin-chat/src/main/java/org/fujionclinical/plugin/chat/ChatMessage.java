/*
 * #%L
 * Fujion Clinical Framework
 * %%
 * Copyright (C) 2019 fujionclinical.org
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
package org.fujionclinical.plugin.chat;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.fujionclinical.api.messaging.IPublisherInfo;

import java.io.Serializable;
import java.util.Date;

/**
 * A single chat message.
 */
public class ChatMessage implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public final IPublisherInfo sender;
    
    public final Date timestamp;
    
    public final String message;
    
    protected ChatMessage(IPublisherInfo sender, String message) {
        this(sender, message, new Date());
    }
    
    @JsonCreator
    protected ChatMessage(@JsonProperty("sender") IPublisherInfo sender, @JsonProperty("message") String message,
        @JsonProperty("timestamp") Date timestamp) {
        this.sender = sender;
        this.message = message;
        this.timestamp = timestamp;
    }
}
