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
package org.fujionclinical.api.context;

import java.util.Collection;

/**
 * Tracks survey responses from context change subscribers.
 */
public interface ISurveyResponse {
    
    /**
     * Callback interface for asynchronous responses.
     */
    public interface ISurveyCallback {
        
        void response(ISurveyResponse response);
    }
    
    /**
     * Register a rejection message.
     * 
     * @param message The rejection message. If null or empty, a default message will be supplied
     *            (not recommended).
     */
    void reject(String message);
    
    /**
     * Register acceptance as survey response.
     */
    void accept();
    
    /**
     * Defer the survey response. Must later call accept or reject with deferred response. A
     * subscriber may not defer a response if in silent mode.
     */
    void defer();
    
    /**
     * Returns the current list of rejection responses.
     * 
     * @return List of rejection responses.
     */
    Collection<String> getResponses();
    
    /**
     * True if any subscriber issued a rejection.
     * 
     * @return True if rejected.
     */
    boolean rejected();
    
    /**
     * True if user interaction is not permitted during subscriber survey.
     * 
     * @return True if user interaction is not permitted.
     */
    boolean isSilent();
    
    /**
     * Merges rejection responses from another response object.
     * 
     * @param response Response object whose rejection responses are to be merged.
     */
    void merge(ISurveyResponse response);
}
