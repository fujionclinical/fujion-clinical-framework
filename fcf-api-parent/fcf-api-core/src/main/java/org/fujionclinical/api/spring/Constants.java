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
package org.fujionclinical.api.spring;

/**
 * Constants class
 */
public class Constants {
    
    /**
     * Constant for beans profile which identifies beans to be processed by Spring's root
     * application context.
     */
    public static final String PROFILE_ROOT = "root";
    
    /**
     * Constant for beans profile which identifies beans to be processed by Spring's root
     * application context, in a production setting.
     */
    public static final String PROFILE_ROOT_PROD = "root-prod";
    
    /**
     * Constant for beans profile which identifies beans to be processed by Spring's root
     * application context, in a test setting.
     */
    public static final String PROFILE_ROOT_TEST = "root-test";
    
    /**
     * Constant for beans profile which specifies the default profile for a root Spring application
     * context if none is specified.
     */
    public static final String PROFILE_ROOT_DEFAULT = PROFILE_ROOT;
    
    /**
     * Constant for beans profile which identifies beans to be processed by a child Spring
     * application context.
     */
    public static final String PROFILE_CHILD = "child";
    
    /**
     * Constant for beans profile which identifies beans to be processed by a child Spring
     * application context, in a production setting.
     */
    public static final String PROFILE_CHILD_PROD = "child-prod";
    
    /**
     * Constant for beans profile which identifies beans to be processed by a child Spring
     * application context, in a test setting.
     */
    public static final String PROFILE_CHILD_TEST = "child-test";
    
    /**
     * Constant for beans profile which specifies the default profile for a child Spring application
     * context if none is specified. This is set to a non-existent profile to suppress processing a
     * bean in an unspecified profile in a child context (it should only be processed in the root
     * context).
     */
    public static final String PROFILE_CHILD_DEFAULT = "dummy";
    
    /**
     * All root profiles.
     */
    public static final String[] PROFILES_ROOT = { PROFILE_ROOT, PROFILE_ROOT_PROD, PROFILE_ROOT_TEST };
    
    /**
     * All child profiles.
     */
    public static final String[] PROFILES_CHILD = { PROFILE_CHILD, PROFILE_CHILD_PROD, PROFILE_CHILD_TEST };
    
    /**
     * All production profiles.
     */
    public static final String[] PROFILES_PROD = { PROFILE_ROOT, PROFILE_ROOT_PROD, PROFILE_CHILD, PROFILE_CHILD_PROD };
    
    /**
     * All test profiles.
     */
    public static final String[] PROFILES_TEST = { PROFILE_ROOT, PROFILE_ROOT_TEST, PROFILE_CHILD, PROFILE_CHILD_TEST };
    
    /**
     * All production root profiles.
     */
    public static final String[] PROFILES_ROOT_PROD = { PROFILE_ROOT, PROFILE_ROOT_PROD };
    
    /**
     * All production child profiles.
     */
    public static final String[] PROFILES_CHILD_PROD = { PROFILE_CHILD, PROFILE_CHILD_PROD };
    
    /**
     * All test root profiles.
     */
    public static final String[] PROFILES_ROOT_TEST = { PROFILE_ROOT, PROFILE_ROOT_TEST };
    
    /**
     * All test child profiles.
     */
    public static final String[] PROFILES_CHILD_TEST = { PROFILE_CHILD, PROFILE_CHILD_TEST };
    
    /**
     * Default locations to search for configurations files for the Spring application context.
     */
    public static final String[] DEFAULT_LOCATIONS = { "classpath*:/META-INF/*-spring.xml" };
    
    /**
     * Enforce static class.
     */
    private Constants() {
    }
    
}
