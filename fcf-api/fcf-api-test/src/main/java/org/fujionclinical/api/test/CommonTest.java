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

import org.fujionclinical.api.core.AppFramework;
import org.fujionclinical.api.context.ContextManager;
import org.fujionclinical.api.event.EventManager;
import org.fujionclinical.api.spring.AppContextInitializer;
import org.fujionclinical.api.spring.FrameworkAppContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class CommonTest {

    public static FrameworkAppContext appContext;

    public static AppFramework appFramework;

    public static EventManager eventManager;

    public static ContextManager contextManager;

    private static int initCount;

    @BeforeClass
    public static void beforeClass$CommonTest() {
        if (initCount++ == 0) {
            System.out.println("Initializing test IOC container...");
            try {
                appContext = new FrameworkAppContext();
                new AppContextInitializer(true).initialize(appContext);
                appContext.refresh();
            } catch (Throwable e) {
                appContext = null;
                e.printStackTrace();
                throw e;
            }
            appFramework = appContext.getBean("appFramework", AppFramework.class);
            eventManager = appContext.getBean("eventManager", EventManager.class);
            contextManager = appContext.getBean("contextManager", ContextManager.class);
        }
    }

    @AfterClass
    public static void afterClass$CommonTest() {
        if (--initCount == 0) {
            System.out.println("Closing test IOC container...");
            try {
                appContext.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            appContext = null;
            appFramework = null;
            eventManager = null;
            contextManager = null;
        }
    }
}
