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
package org.fujionclinical.api.model.user;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fujionclinical.api.context.ContextManager;
import org.fujionclinical.api.context.IContextSubscriber;
import org.fujionclinical.api.context.ISharedContext;
import org.fujionclinical.api.context.ManagedContext;

/**
 * Wrapper for shared user context.
 */
public class UserContext extends ManagedContext<IUser> {

    public interface IUserContextSubscriber extends IContextSubscriber {}

    private static final Log log = LogFactory.getLog(UserContext.class);
    
    protected static final String SUBJECT_NAME = "User";
    
    protected static final String CCOW_USERNM = SUBJECT_NAME + ".Id.Logon";
    
    protected static final String CCOW_FULLNAME = SUBJECT_NAME + ".Co.Name";
    
    /**
     * Request a user context change.
     * 
     * @param user New user
     */
    public static void changeUser(IUser user) {
        try {
            getUserContext().requestContextChange(user);
        } catch (Exception e) {
            log.error("Error during user context change.", e);
        }
    }
    
    /**
     * Returns the managed user context.
     * 
     * @return User context
     */
    @SuppressWarnings("unchecked")
    public static ISharedContext<IUser> getUserContext() {
        return (ISharedContext<IUser>) ContextManager.getInstance().getSharedContext(UserContext.class.getName());
    }
    
    /**
     * Returns the user in the current context.
     * 
     * @return User in current context.
     */
    public static IUser getActiveUser() {
        return getUserContext().getContextObject(false);
    }
    
    /**
     * Create a shared user context with an initial null state.
     */
    public UserContext() {
        this(null);
    }
    
    /**
     * Create a shared user context with a specified initial state.
     * 
     * @param user User that will be the initial state.
     */
    public UserContext(IUser user) {
        super(SUBJECT_NAME, IUserContextSubscriber.class, user);
    }
    
    /**
     * Returns a priority value of 1000. Among concurrent context change transactions, a user
     * context change should generally occur last.
     * 
     * @return Priority value for context manager.
     */
    @Override
    public int getPriority() {
        return 1000;
    }
    
}
