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
package org.fujionclinical.api.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fujionclinical.api.event.EventUtil;
import org.fujionclinical.api.event.IEventSubscriber;
import org.fujionclinical.api.messaging.IPublisherInfo;
import org.fujionclinical.api.messaging.Recipient;
import org.fujionclinical.api.messaging.Recipient.RecipientType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class GenericEventTest extends CommonTest {
    
    private static final Log log = LogFactory.getLog(GenericEventTest.class);
    
    private static final String EVENT_NAME1 = "Event";
    
    private static final String EVENT_NAME2 = "Event.Subevent";
    
    protected static int pollingInterval = 500;
    
    private int eventCount;
    
    private int expectedUndelivered;
    
    private boolean pingResponded;
    
    private final boolean remote;
    
    private final Recipient validRecipient;
    
    private final Recipient invalidRecipient = new Recipient(RecipientType.USER, "invalid");
    
    private final List<TestPacket> tests = Collections.synchronizedList(new ArrayList<>());
    
    private AssertionError assertionError;
    
    private Recipient currentRecipient;
    
    private final IEventSubscriber<TestPacket> subscriber = new IEventSubscriber<TestPacket>() {
        
        @Override
        public void eventCallback(String eventName, TestPacket testPacket) {
            try {
                log.info("Received: " + testPacket);
                assertTrue(testPacket + ": unexpected test packet", tests.remove(testPacket));
                assertEquals(testPacket + ": name does not match.", testPacket.getEventName(), eventName);
                assertTrue(testPacket + ": should not have been received.", testPacket.isShouldReceive());
            } catch (AssertionError e) {
                if (assertionError == null) {
                    assertionError = e;
                }
            }
        }
    };
    
    private final IEventSubscriber<IPublisherInfo> pingSubscriber = new IEventSubscriber<IPublisherInfo>() {
        
        @Override
        public void eventCallback(String eventName, IPublisherInfo publisherInfo) {
            log.info(publisherInfo);
            pingResponded = true;
        }
        
    };
    
    /**
     * Fire the specified event.
     * 
     * @param eventName Name of the event to fire.
     * @param shouldReceive Indicates whether or not the event should be received.
     */
    private void fireEvent(String eventName, boolean shouldReceive) {
        TestPacket testPacket = new TestPacket(++eventCount, eventName, shouldReceive, remote);
        tests.add(testPacket);
        
        if (!shouldReceive) {
            expectedUndelivered++;
        }
        
        log.info("Sending: " + testPacket);
        
        if (remote) {
            if (currentRecipient == null) {
                eventManager.fireRemoteEvent(eventName, testPacket);
            } else {
                eventManager.fireRemoteEvent(eventName, testPacket, currentRecipient);
            }
        } else {
            eventManager.fireLocalEvent(eventName, testPacket);
        }
    }
    
    public GenericEventTest(boolean testRemote, Recipient testRecipient) {
        remote = testRemote;
        validRecipient = testRecipient;
    }
    
    @Test
    public void testEvents() {
        if (remote) {
            pingTest();
        }
        
        fireTestEvents();
        assertEquals("Error testing ping response.", remote, pingResponded);
    }
    
    public void pingTest() {
        eventManager.subscribe("PING.TEST", pingSubscriber);
        EventUtil.ping("PING.TEST", null, validRecipient);
    }
    
    private void fireTestEvents() {
        //content based routing via Message Selector tests
        fireEvent(EVENT_NAME1, false); // Subscriber not registered, should not receive
        fireEvent(EVENT_NAME2, false); // Subevent should also not be received
        subscribe(EVENT_NAME1, true);
        fireEvent(EVENT_NAME1, true); // Subscriber registered, should receive
        fireEvent(EVENT_NAME2, true); // Subevent should also be received
        if (remote) {
            //Additional routing options / recipient filtering
            currentRecipient = validRecipient;
            fireEvent(EVENT_NAME1, true); // Subscriber registered, should receive due to defined/intended recipients
            fireEvent(EVENT_NAME2, true); // Subevent w/ defined/intended recipients should also be received
            //not null bogus recipients
            currentRecipient = invalidRecipient;
            fireEvent(EVENT_NAME1, false); // Subscriber registered, but should not receive due to defined unintended recipients
            fireEvent(EVENT_NAME2, false); // Subevent should not also be received
            currentRecipient = null;
        }
        subscribe(EVENT_NAME1, false);
        fireEvent(EVENT_NAME1, false); // Subscriber not registered, should not receive
        fireEvent(EVENT_NAME2, false); // Subevent should also not be received
        subscribe(EVENT_NAME2, true);
        fireEvent(EVENT_NAME1, false); // Subscriber not registered, should not receive
        fireEvent(EVENT_NAME2, true); // Subevent should be received
        if (remote) {
            //Additional routing options / recipient filtering
            currentRecipient = validRecipient;
            fireEvent(EVENT_NAME1, false); // Subscriber not registered, should not receive
            fireEvent(EVENT_NAME2, true); // Subevent w/ defined/intended recipients should also be received
            //not null bogus recipients
            currentRecipient = invalidRecipient;
            fireEvent(EVENT_NAME1, false); // Subscriber registered, but should not receive due to defined unintended recipients
            fireEvent(EVENT_NAME2, false); // Subevent should not also be received
            currentRecipient = null;
        }
        subscribe(EVENT_NAME1, true);
        subscribe(EVENT_NAME2, false);
        fireEvent(EVENT_NAME1, true); // Subscriber registered, should receive
        fireEvent(EVENT_NAME2, true); // Subevent should also be received
        if (remote) {
            //Additional routing options / recipient filtering
            currentRecipient = invalidRecipient;
            fireEvent(EVENT_NAME1, true); // Subscriber registered, should receive due to defined/intended recipients
            fireEvent(EVENT_NAME2, true); // Subevent w/ defined/intended recipients should also be received
            //not null bogus recipients
            currentRecipient = invalidRecipient;
            fireEvent(EVENT_NAME1, false); // Subscriber registered, but should not receive due to defined unintended recipients
            fireEvent(EVENT_NAME2, false); // Subevent should not also be received
            currentRecipient = null;
        }
        subscribe(EVENT_NAME1, false);
        fireEvent(EVENT_NAME1, false); // Subscriber not registered, should not receive
        fireEvent(EVENT_NAME2, false); // Subevent should also not be received
        undeliveredEvents(remote);
        checkAssertion();
    }
    
    private void checkAssertion() {
        if (assertionError != null) {
            throw assertionError;
        }
    }
    
    private void subscribe(String eventName, boolean subscribe) {
        if (remote) {
            doWait(30);
        }
        
        if (subscribe) {
            eventManager.subscribe(eventName, subscriber);
        } else {
            eventManager.unsubscribe(eventName, subscriber);
        }
    }
    
    private void doWait(int count) {
        while ((count-- > 0) && (tests.size() > expectedUndelivered)) {
            try {
                Thread.sleep(pollingInterval);
            } catch (InterruptedException e) {
                continue;
            }
        }
        
        assertFalse("Timed out waiting for packet delivery.", tests.size() > expectedUndelivered);
    }
    
    /**
     * Verify all undelivered events.
     * 
     * @param wait If true, wait before verifying.
     */
    private void undeliveredEvents(boolean wait) {
        if (wait) {
            doWait(20);
        }
        
        while (!tests.isEmpty()) {
            TestPacket testPacket = tests.remove(0);
            assertFalse(testPacket + ": was not received.", testPacket.isShouldReceive());
        }
        
        tests.clear();
    }
}
