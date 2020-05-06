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
package org.fujionclinical.api.query;

import org.fujion.thread.ICancellable;
import org.fujion.thread.ThreadUtil;

/**
 * Implements a fetch strategy based on a simple background thread.
 *
 * @param <T> Class of query result.
 */
public class ThreadedQueryStrategy<T> implements IAsyncQueryStrategy<T> {
    
    private class Query extends Thread implements ICancellable {
        
        private final IQueryService<T> service;
        
        private final IQueryContext context;
        
        private final IQueryCallback<T> callback;
        
        private boolean cancelled;
        
        private Query(IQueryService<T> service, IQueryContext context, IQueryCallback<T> callback) {
            this.service = service;
            this.context = context;
            this.callback = callback;
        }
        
        @Override
        public void run() {
            IQueryResult<T> result;
            
            try {
                result = service.fetch(context);
            } catch (Throwable t) {
                result = QueryUtil.errorResult(t);
            }
            
            callback.onQueryFinish(this, cancelled ? QueryUtil.abortResult(null) : result);
        }

        @Override
        public boolean isCancelled() {
            return cancelled;
        }

        @Override
        public boolean cancel(boolean mayInterrupt) {
            cancelled = true;
            return true;
        }
    }
    
    public ThreadedQueryStrategy() {
        
    }
    
    @Override
    public ICancellable fetch(IQueryService<T> service, IQueryContext context, IQueryCallback<T> callback) {
        Query query = new Query(service, context, callback);
        ThreadUtil.execute(query);
        return query;
    }
}
