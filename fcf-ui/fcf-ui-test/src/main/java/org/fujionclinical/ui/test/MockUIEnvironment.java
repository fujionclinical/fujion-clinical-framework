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
package org.fujionclinical.ui.test;

import org.fujion.component.Page;
import org.fujion.test.MockConfig;
import org.fujion.test.MockEnvironment;
import org.fujionclinical.api.spring.ExternalPropertySource;
import org.fujionclinical.api.spring.LabelPropertySource;
import org.fujionclinical.api.spring.RandomPropertySource;
import org.fujionclinical.ui.spring.AppContextFinder;
import org.fujionclinical.ui.spring.FrameworkAppContext;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.context.support.XmlWebApplicationContext;

public class MockUIEnvironment extends MockEnvironment {
    
    private static class MockAppContextFinder extends AppContextFinder {

        static void setRootContext(ApplicationContext ctx) {
            rootContext = ctx;
        }

        private static void initPage(Page page, ApplicationContext ctx) {
            page.setAttribute(APP_CONTEXT_ATTRIB, ctx);
        }
    }
    
    @Override
    protected XmlWebApplicationContext createApplicationContext() {
        return new FrameworkAppContext();
    }
    
    @Override
    protected XmlWebApplicationContext initAppContext(MockConfig config, ApplicationContext parent) {
        XmlWebApplicationContext ctx = super.initAppContext(config, parent);

        if (parent == null) {
            ConfigurableEnvironment env = ctx.getEnvironment();
            env.getPropertySources().addFirst(new RandomPropertySource());
            env.getPropertySources().addFirst(new LabelPropertySource());
            env.getPropertySources().addLast(new ExternalPropertySource(ctx));
            MockAppContextFinder.setRootContext(ctx);
        } else {
            MockAppContextFinder.initPage(getSession().getPage(), ctx);
        }

        return ctx;
    }
    
}
