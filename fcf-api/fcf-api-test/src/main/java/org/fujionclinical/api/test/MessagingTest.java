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
package org.fujionclinical.api.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fujionclinical.api.messaging.ConsumerService;
import org.fujionclinical.api.messaging.IMessageConsumer.IMessageCallback;
import org.fujionclinical.api.messaging.IMessageProducer;
import org.fujionclinical.api.messaging.Message;
import org.fujionclinical.api.messaging.ProducerService;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MessagingTest extends CommonTest {
    
    private static final Log log = LogFactory.getLog(MessagingTest.class);
    
    protected static int pollingInterval = 500;
    
    private static final String CHANNEL1 = "channel1";
    
    private static final String CHANNEL2 = "channel2";
    
    private int messageCount;
    
    private final List<Message> messages = Collections.synchronizedList(new ArrayList<>());
    
    private AssertionError assertionError;
    
    private ConsumerService consumerService;
    
    private ProducerService producerService;
    
    private final Class<? extends IMessageProducer> producerClass;
    
    private final IMessageCallback callback = new IMessageCallback() {
        
        @Override
        public void onMessage(String channel, Message message) {
            try {
                log.info("Received on channel " + channel + ": " + message);
                assertTrue("  : unexpected message", messages.remove(message));
            } catch (AssertionError e) {
                if (assertionError == null) {
                    assertionError = e;
                }
            }
        }
        
    };
    
    public MessagingTest(Class<? extends IMessageProducer> producerClass) {
        this.producerClass = producerClass;
    }
    
    public MessagingTest() {
        this(null);
    }
    
    @Test
    public void testMessaging() {
        publishMessages();
    }
    
    private void publishMessages() {
        publish(CHANNEL1, false);
        publish(CHANNEL2, false);
        subscribe(CHANNEL1, true);
        publish(CHANNEL1, true);
        publish(CHANNEL2, false);
        subscribe(CHANNEL1, false);
        publish(CHANNEL1, false);
        publish(CHANNEL2, false);
        subscribe(CHANNEL2, true);
        publish(CHANNEL1, false);
        publish(CHANNEL2, true);
        subscribe(CHANNEL1, true);
        subscribe(CHANNEL2, false);
        publish(CHANNEL1, true);
        publish(CHANNEL2, false);
        subscribe(CHANNEL1, false);
        publish(CHANNEL1, false);
        publish(CHANNEL2, false);
        doWait(30);
    }
    
    private void checkAssertion() {
        if (assertionError != null) {
            throw assertionError;
        }
    }
    
    private void subscribe(String channel, boolean subscribe) {
        doWait(30); // Wait for delivery of outstanding messages.
        
        if (subscribe) {
            getConsumerService().subscribe(channel, callback);
        } else {
            getConsumerService().unsubscribe(channel, callback);
        }
    }
    
    /**
     * Publish a message.
     * 
     * @param channel The channel on which to publish.
     * @param shouldReceive Indicates whether or not the message should be received.
     */
    private void publish(String channel, boolean shouldReceive) {
        Message message = new Message("test", "Test Message");
        message.setMetadata("count", ++messageCount);
        
        if (shouldReceive) {
            messages.add(message);
        }
        
        log.info("Sending: " + message);
        
        if (producerClass != null) {
            getProducerService().publish(channel, message, producerClass);
        } else {
            getProducerService().publish(channel, message);
        }
    }
    
    private void doWait(int count) {
        checkAssertion();
        
        while (count-- > 0 && !messages.isEmpty()) {
            try {
                Thread.sleep(pollingInterval);
            } catch (InterruptedException e) {
                // NOP
            }
        }
        
        if (!messages.isEmpty()) {
            undeliveredMessages();
        }
    }
    
    /**
     * Verify all undelivered messages.
     */
    private void undeliveredMessages() {
        StringBuilder sb = new StringBuilder();
        
        while (!messages.isEmpty()) {
            Message message = messages.remove(0);
            sb.append(message + ": was not received.\n");
        }
        
        assertFalse(sb.toString(), sb.length() > 0);
    }
    
    private ConsumerService getConsumerService() {
        return consumerService == null ? consumerService = (ConsumerService) appContext.getBean("messageConsumerService")
                : consumerService;
    }
    
    private ProducerService getProducerService() {
        return producerService == null ? producerService = (ProducerService) appContext.getBean("messageProducerService")
                : producerService;
    }
}
