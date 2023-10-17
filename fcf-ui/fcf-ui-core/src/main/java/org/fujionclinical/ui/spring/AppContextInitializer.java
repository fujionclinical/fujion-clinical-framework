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
package org.fujionclinical.ui.spring;

import org.apache.commons.lang3.ArrayUtils;
import org.fujion.client.ExecutionContext;
import org.fujion.component.Page;
import org.fujion.spring.ClasspathMessageSource;
import org.fujion.websocket.ISessionLifecycle;
import org.fujion.websocket.Session;
import org.fujion.websocket.SessionInitException;
import org.fujion.websocket.Sessions;
import org.fujionclinical.api.spring.Constants;
import org.fujionclinical.api.spring.ExternalPropertySource;
import org.fujionclinical.api.spring.LabelPropertySource;
import org.fujionclinical.api.spring.RandomPropertySource;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.context.support.XmlWebApplicationContext;

import jakarta.servlet.ServletContext;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class AppContextInitializer implements ApplicationContextInitializer<XmlWebApplicationContext> {
    
    public static final String[] DEFAULT_LOCATIONS = { "classpath*:/META-INF/*-spring.xml" };
    
    private final Page page;
    
    private final boolean testConfig;
    
    public AppContextInitializer() {
        this(null, false);
    }
    
    public AppContextInitializer(Page page) {
        this(page, false);
    }
    
    public AppContextInitializer(Page page, boolean testConfig) {
        this.page = page;
        this.testConfig = testConfig;
    }
    
    @Override
    public void initialize(XmlWebApplicationContext ctx) {
        ctx.setAllowBeanDefinitionOverriding(true);
        ConfigurableEnvironment env = ctx.getEnvironment();

        if (page != null) {
            page.setAttribute(AppContextFinder.APP_CONTEXT_ATTRIB, ctx);
            ServletContext sc = ExecutionContext.getSession().getServletContext();
            ctx.setDisplayName("Child XmlWebApplicationContext " + page);
            ctx.setParent(AppContextFinder.rootContext);
            ctx.setServletContext(sc);
            env.setActiveProfiles(testConfig ? Constants.PROFILES_CHILD_TEST : Constants.PROFILES_CHILD_PROD);
            env.setDefaultProfiles(Constants.PROFILE_CHILD_DEFAULT);
            ctx.setConfigLocations(DEFAULT_LOCATIONS);
        } else {
            AppContextFinder.rootContext = ctx;
            Set<String> aps = new LinkedHashSet<>();
            Collections.addAll(aps, env.getActiveProfiles());
            Collections.addAll(aps, testConfig ? Constants.PROFILES_ROOT_TEST : Constants.PROFILES_ROOT_PROD);
            env.setActiveProfiles(aps.toArray(new String[aps.size()]));
            env.getPropertySources().addFirst(new LabelPropertySource());
            env.getPropertySources().addLast(new ExternalPropertySource(ctx));
            env.getPropertySources().addLast(new RandomPropertySource());
            env.setDefaultProfiles(Constants.PROFILE_ROOT_DEFAULT);
            ctx.setConfigLocations(ArrayUtils.addAll(Constants.DEFAULT_LOCATIONS, ctx.getConfigLocations()));
            ClasspathMessageSource.getInstance().setResourceLoader(ctx);
            registerSessionListener();
        }
    }

    /**
     * Manages creation/destruction of child contexts.
     */
    private void registerSessionListener() {
        ISessionLifecycle sessionTracker = new ISessionLifecycle() {
            
            @Override
            public void onSessionCreate(Session session) {
                try {
                    AppContextFinder.createAppContext(session.getPage());
                } catch (Exception e) {
                    throw new SessionInitException(e);
                }
            }
            
            @Override
            public void onSessionDestroy(Session session) {
                AppContextFinder.destroyAppContext(session.getPage());
            }
            
        };

        Sessions.getInstance().addLifecycleListener(sessionTracker);
    }
    
}
