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

import org.springframework.util.Assert;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Base class for implementing a parameter descriptor.
 *
 * @param <PRM> The parameter type.
 * @param <OPD> The normalized operand type.
 */
public abstract class AbstractParameterDescriptor<PRM, OPD> implements IParameterDescriptor<PRM, OPD> {

    private final Class<PRM> parameterType;

    private final int maxOperands;

    private final Set<Operator> validOperators = new HashSet<>();

    public AbstractParameterDescriptor(
            Class<PRM> parameterType,
            int maxOperands,
            Operator... validOperators) {
        this.parameterType = parameterType;
        this.maxOperands = maxOperands;
        Collections.addAll(this.validOperators, validOperators);
    }

    /**
     * Validates that an expression fragment has a valid operator and the expected number of operands.
     *
     * @param fragment A query expression fragment.
     */
    public void validateFragment(ExpressionFragment fragment) {
        Assert.isTrue(validOperators.contains(fragment.operator), () -> "Not a valid operator: '" + fragment.operator + "'");
        Assert.isTrue(fragment.operands.length <= maxOperands, () -> "Operand maximum of " + maxOperands + " exceeded");
    }

    /**
     * Returns the parameter type associated with this descriptor.
     *
     * @return The parameter type associated with this descriptor.
     */
    public Class<PRM> getParameterType() {
        return parameterType;
    }

}
