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

import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractQueryExpressionResolver<TGT, OPD> {

    private final Class<TGT> targetClass;

    private final int maxOperands;

    private final Set<QueryOperator> validOperators = new HashSet<>();

    public AbstractQueryExpressionResolver(
            Class<TGT> targetClass,
            int maxOperands,
            QueryOperator... validOperators) {
        this.targetClass = targetClass;
        this.maxOperands = maxOperands;
        this.validOperators.addAll(Arrays.asList(validOperators));
    }

    protected abstract OPD resolve(
            Class<TGT> propertyType,
            Object operand,
            OPD previousOperand);

    public void validate(QueryExpressionFragment fragment) {
        Assert.isTrue(validOperators.contains(fragment.operator), () -> "Not a valid operator: '" + fragment.operator + "'");
        Assert.isTrue(fragment.operands.length <= maxOperands, () -> "Operand maximum of " + maxOperands + " exceeded");
    }

    public Class<TGT> getTargetClass() {
        return targetClass;
    }

}
