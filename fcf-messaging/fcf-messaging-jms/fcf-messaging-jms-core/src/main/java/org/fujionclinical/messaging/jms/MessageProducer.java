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
package org.fujionclinical.messaging.jms;

import org.fujionclinical.api.messaging.IMessageProducer;
import org.fujionclinical.api.messaging.Message;

public class MessageProducer implements IMessageProducer {
    
    private final JMSService service;
    
    public MessageProducer(JMSService service) {
        this.service = service;
    }
    
    @Override
    public boolean publish(String channel, Message message) {
        jakarta.jms.Message msg = service.createObjectMessage(message, null, null);
        service.sendMessage(channel, msg);
        return true;
    }
    
}
