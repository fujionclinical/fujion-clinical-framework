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
package org.fujionclinical.api.messaging;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.fujionclinical.api.messaging.IMessageConsumer.IMessageCallback;
import org.fujionclinical.api.messaging.Recipient.RecipientType;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * Aggregator for multiple consumers.
 */
public class ConsumerService implements IMessageCallback, DestructionAwareBeanPostProcessor {
    
    private static final String CACHE_NAME = ConsumerService.class.getPackage().getName();
    
    private final Set<IMessageConsumer> consumers = new LinkedHashSet<>();
    
    private final Map<String, LinkedHashSet<IMessageCallback>> callbacks = new LinkedHashMap<>();
    
    private final String nodeId = UUID.randomUUID().toString();
    
    private final Cache deliveredMessageCache;
    
    /**
     * Access the cache for tracking delivered messages.
     * 
     * @param cacheManager Cache manager used to retrieve instance of delivered message cache.
     */
    public ConsumerService(CacheManager cacheManager) {
        deliveredMessageCache = cacheManager.getCache(CACHE_NAME);
    }
    
    /**
     * @return The unique node id for this service.
     */
    public String getNodeId() {
        return nodeId;
    }
    
    /**
     * @return A list of registered consumers.
     */
    public Collection<IMessageConsumer> getRegisteredConsumers() {
        return Collections.unmodifiableCollection(consumers);
    }
    
    /**
     * Register a message consumer.
     * 
     * @param consumer The consumer to register.
     * @return True if the consumer was not already registered.
     */
    public synchronized boolean registerConsumer(IMessageConsumer consumer) {
        consumer.setCallback(this);
        return consumers.add(consumer);
    }
    
    /**
     * Unregister a message consumer.
     * 
     * @param consumer The consumer to unregister.
     * @return False if the consumer was not already registered.
     */
    public synchronized boolean unregisterConsumer(IMessageConsumer consumer) {
        consumer.setCallback(null);
        return consumers.remove(consumer);
    }
    
    /**
     * Return the callbacks associated with the specified channel.
     * 
     * @param channel The channel.
     * @param autoCreate Create the callback list if one doesn't exist.
     * @param clone Return a clone of the callback list.
     * @return The callback list (possibly null).
     */
    private synchronized LinkedHashSet<IMessageCallback> getCallbacks(String channel, boolean autoCreate, boolean clone) {
        LinkedHashSet<IMessageCallback> result = callbacks.get(channel);
        
        if (result == null && autoCreate) {
            callbacks.put(channel, result = new LinkedHashSet<>());
        }
        
        return result == null ? null : clone ? new LinkedHashSet<>(result) : result;
    }
    
    public synchronized void subscribe(String channel, IMessageCallback callback) {
        boolean newSubscription = !callbacks.containsKey(channel);
        getCallbacks(channel, true, false).add(callback);
        
        if (newSubscription) {
            for (IMessageConsumer consumer : consumers) {
                consumer.subscribe(channel);
            }
        }
    }
    
    public synchronized void unsubscribe(String channel, IMessageCallback callback) {
        LinkedHashSet<IMessageCallback> cbs = getCallbacks(channel, false, false);
        
        if (cbs != null && cbs.remove(callback) && cbs.isEmpty()) {
            callbacks.remove(channel);
            
            for (IMessageConsumer consumer : consumers) {
                consumer.unsubscribe(channel);
            }
        }
    }
    
    /**
     * Callback entry point for all registered consumers.
     */
    @Override
    public void onMessage(String channel, Message message) {
        if (MessageUtil.isMessageExcluded(message, RecipientType.CONSUMER, nodeId)) {
            return;
        }
        
        if (updateDelivered(message)) {
            LinkedHashSet<IMessageCallback> callbacks = getCallbacks(channel, false, true);
            
            if (callbacks != null) {
                dispatchMessages(channel, message, callbacks);
            }
        }
    }
    
    /**
     * Updates the delivered message cache. This avoids delivering the same message transported by
     * different messaging frameworks. If we have only one consumer registered, we don't need to
     * worry about this.
     * 
     * @param message The message being delivered.
     * @return True if the cache was updated (i.e., the message has not been previously delivered).
     */
    private boolean updateDelivered(Message message) {
        if (consumers.size() <= 1) {
            return true;
        }
        
        String pubid = (String) message.getMetadata("fcf.pub.event");
        return deliveredMessageCache.putIfAbsent(pubid, "") == null;
    }
    
    /**
     * Dispatch message to callback. Override to address special threading considerations.
     * 
     * @param channel The channel that delivered the message.
     * @param message The message to dispatch.
     * @param callbacks The callbacks to receive the message.
     */
    protected void dispatchMessages(String channel, Message message, Set<IMessageCallback> callbacks) {
        for (IMessageCallback callback : callbacks) {
            try {
                callback.onMessage(channel, message);
            } catch (Exception e) {
                
            }
        }
    }
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof IMessageConsumer) {
            registerConsumer((IMessageConsumer) bean);
        }
        
        return bean;
    }
    
    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
        if (bean instanceof IMessageConsumer) {
            unregisterConsumer((IMessageConsumer) bean);
        }
    }
    
    @Override
    public boolean requiresDestruction(Object bean) {
        return bean instanceof IMessageConsumer;
    }
    
}
