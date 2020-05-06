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
package org.fujionclinical.messaging.amqp.rabbitmq;

import org.fujionclinical.api.messaging.IMessageConsumer;
import org.fujionclinical.api.messaging.Message;
import org.fujion.thread.ThreadUtil;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * AMQP implementation of a message consumer.
 */
public class MessageConsumer implements IMessageConsumer {
    
    private class Subscriber extends SimpleMessageListenerContainer implements MessageListener {
        
        private final String channel;
        
        Subscriber(String channel) {
            this.channel = channel;
            setTaskExecutor(ThreadUtil.getApplicationThreadPool());
            setMessageListener(this);
            setConnectionFactory(broker.getConnectionFactory());
            setQueueNames(channel);
        }
        
        @Override
        public void onMessage(org.springframework.amqp.core.Message message) {
            Message msg = broker.convertMessage(message);
            
            if (callback != null) {
                callback.onMessage(channel, msg);
            }
        }
    }
    
    private final Map<String, Subscriber> subscribers = Collections.synchronizedMap(new HashMap<>());
    
    private final Broker broker;
    
    private IMessageCallback callback;
    
    public MessageConsumer(Broker broker) {
        this.broker = broker;
    }
    
    @Override
    public void setCallback(IMessageCallback callback) {
        this.callback = callback;
    }
    
    @Override
    public boolean subscribe(String channel) {
        if (!subscribers.containsKey(channel)) {
            broker.ensureChannel(channel);
            Subscriber subscriber = new Subscriber(channel);
            subscriber.start();
            subscribers.put(channel, subscriber);
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean unsubscribe(String channel) {
        if (subscribers.containsKey(channel)) {
            subscribers.remove(channel).stop();
            return true;
        }
        
        return false;
    }
    
}
