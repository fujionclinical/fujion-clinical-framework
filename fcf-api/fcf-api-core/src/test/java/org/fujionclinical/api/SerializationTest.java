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
package org.fujionclinical.api;

import org.fujion.common.JSONUtil;
import org.fujionclinical.api.event.PingFilter;
import org.fujionclinical.api.event.PingFilter.PingFilterType;
import org.fujionclinical.api.event.PingRequest;
import org.fujionclinical.api.messaging.Message;
import org.fujionclinical.api.messaging.PublisherInfo;
import org.fujionclinical.api.messaging.Recipient;
import org.fujionclinical.api.messaging.Recipient.RecipientType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SerializationTest {
    
    @Test
    public void testSerialization() {
        List<PingFilter> filters = new ArrayList<>();
        filters.add(new PingFilter(PingFilterType.APP_NAME, "testApp"));
        filters.add(new PingFilter(PingFilterType.SENTINEL_EVENT, "testEvent"));
        Recipient requestor = new Recipient(RecipientType.CONSUMER, "testRequestor");
        PingRequest pingRequest = new PingRequest("TEST.RESPONSE", filters, requestor);
        String data = JSONUtil.serialize(pingRequest);
        pingRequest = (PingRequest) JSONUtil.deserialize(data);
        assertEquals("TEST.RESPONSE", pingRequest.responseEvent);
        assertEquals(filters, pingRequest.filters);
        assertEquals(requestor, pingRequest.requestor);
        
        PublisherInfo publisherInfo = new PublisherInfo();
        publisherInfo.setAppName("appName");
        publisherInfo.setConsumerId("consumerId");
        publisherInfo.setProducerId("producerId");
        publisherInfo.setSessionId("sessionId");
        publisherInfo.setUserId("userId");
        publisherInfo.setUserName("userName");
        data = JSONUtil.serialize(publisherInfo);
        publisherInfo = (PublisherInfo) JSONUtil.deserialize(data);
        assertEquals("appName", publisherInfo.getAppName());
        assertEquals("consumerId", publisherInfo.getConsumerId());
        assertEquals("producerId", publisherInfo.getProducerId());
        assertEquals("sessionId", publisherInfo.getSessionId());
        assertEquals("userId", publisherInfo.getUserId());
        assertEquals("userName", publisherInfo.getUserName());
        
        Message message = new Message("messageType", "payload");
        message.setMetadata("test1", requestor);
        data = JSONUtil.serialize(message);
        Message message2 = (Message) JSONUtil.deserialize(data);
        assertEquals(message.getType(), message2.getType());
    }
}
