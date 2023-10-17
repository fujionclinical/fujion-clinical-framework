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
package org.fujionclinical.api.messaging;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.fujionclinical.api.messaging.Recipient.RecipientType;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for holding information about a publisher.
 */
public class PublisherInfo implements IPublisherInfo, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    
    private String userName;
    
    private final Map<String, String> attributes = new HashMap<>();
    
    public PublisherInfo() {
        
    }
    
    @JsonCreator
    public PublisherInfo(@JsonProperty("userName") String userName,
        @JsonProperty("attributes") Map<String, String> attributes) {
        this.userName = userName;
        this.attributes.putAll(attributes);
    }
    
    public PublisherInfo(IPublisherInfo publisherInfo) {
        this(publisherInfo.getUserName(), publisherInfo.getAttributes());
    }
    
    @Override
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    @Override
    public String getUserId() {
        return get(RecipientType.USER);
    }
    
    @JsonIgnore
    public void setUserId(String userId) {
        put(RecipientType.USER, userId);
    }
    
    @Override
    public String getAppName() {
        return get(RecipientType.APPLICATION);
    }
    
    @JsonIgnore
    public void setAppName(String appName) {
        put(RecipientType.APPLICATION, appName);
    }
    
    @Override
    public String getProducerId() {
        return attributes.get("fcf-PRODUCER");
    }
    
    @JsonIgnore
    public void setProducerId(String nodeId) {
        attributes.put("fcf-PRODUCER", nodeId);
    }
    
    @Override
    public String getConsumerId() {
        return get(RecipientType.CONSUMER);
    }
    
    @JsonIgnore
    public void setConsumerId(String nodeId) {
        put(RecipientType.CONSUMER, nodeId);
    }
    
    @Override
    public String getSessionId() {
        return get(RecipientType.SESSION);
    }
    
    @JsonIgnore
    public void setSessionId(String nodeId) {
        put(RecipientType.SESSION, nodeId);
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj == this || (obj instanceof IPublisherInfo && attributes.equals(((IPublisherInfo) obj).getAttributes()));
    }
    
    @Override
    public int hashCode() {
        return attributes.hashCode();
    }
    
    @Override
    public Map<String, String> getAttributes() {
        return attributes;
    }
    
    public String get(RecipientType recipientType) {
        return attributes.get("fcf-" + recipientType.name());
    }
    
    public void put(RecipientType recipientType, String value) {
        attributes.put("fcf-" + recipientType.name(), value);
    }
}
