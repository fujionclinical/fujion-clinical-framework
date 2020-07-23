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
package org.fujionclinical.api.query.expression;

import org.fujionclinical.api.model.core.IDomainType;
import org.fujionclinical.api.query.core.IQueryContext;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The representation of a compiled query expression, consisting of a list of query fragments and the domain type
 * to which they apply.
 *
 * @param <T> The domain class.
 */
public class Expression<T extends IDomainType> {

    private final Class<T> domainClass;

    private final List<ExpressionFragment> fragments;

    public Expression(Class<T> domainClass, List<ExpressionFragment> fragments) {
        this.domainClass = domainClass;
        this.fragments = fragments;
    }

    public Class<T> getDomainClass() {
        return domainClass;
    }

    public List<ExpressionTuple> resolve() {
        return resolve(null);
    }

    public List<ExpressionTuple> resolve(IQueryContext queryContext) {
        return fragments.stream()
                .map(fragment -> fragment.createTuple(queryContext))
                .collect(Collectors.toList());
    }

}