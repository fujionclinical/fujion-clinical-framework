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

import org.apache.commons.lang.ClassUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;

/**
 * Represents a fragment of a query expression (i.e., property name, operator, operands).  Because a query fragment
 * stores operands in their original, unresolved form, it is independent of any query context and may be used repeatedly
 * with different contexts.  When a query context is applied to a fragment (via the createTuple method), a query
 * expression tuple is produced, which is essentially a fragment with all operands fully resolved.  In this way,
 * a query expression may be compiled once, and used repeatedly with different query contexts.
 */
public class QueryExpressionFragment<PROP, OPD> {

    private final PropertyDescriptor propertyDescriptor;

    private final String propertyPath;

    protected final QueryOperator operator;

    protected final String[] operands;

    private final AbstractQueryExpressionResolver<PROP, OPD> resolver;

    public QueryExpressionFragment(
            PropertyDescriptor propertyDescriptor,
            String propertyPath,
            AbstractQueryExpressionResolver<PROP, OPD> resolver,
            QueryOperator operator,
            String... operands) {
        QueryParameter annot = AnnotationUtils.findAnnotation(propertyDescriptor.getReadMethod(), QueryParameter.class);
        Assert.notNull(annot, () -> "The property '" + propertyDescriptor.getName() + "' is not a valid query parameter.");
        this.propertyDescriptor = propertyDescriptor;
        this.propertyPath = propertyPath;
        this.operator = operator;
        this.resolver = resolver;
        this.operands = operands;
        resolver.validate(this);
    }

    /**
     * Creates an expression tuple (i.e., an expression fragment with all operands fully resolved).
     *
     * @param queryContext The query context.
     * @return An expression tuple.
     */
    public QueryExpressionTuple createTuple(IQueryContext queryContext) {
        Class<PROP> propertyType = ClassUtils.primitiveToWrapper(propertyDescriptor.getPropertyType());
        OPD[] resolvedOperands = (OPD[]) new Object[operands.length];
        int index = 0;
        OPD resolvedOperand = null;

        for (String opd : operands) {
            String operandStr = opd.trim();
            Object operand;

            if (operandStr.startsWith("{{") && operandStr.endsWith("}}")) {
                String placeholder = operandStr.substring(2, operandStr.length() - 2).trim();
                operand = queryContext == null ? null : resolvePlaceholder(queryContext, placeholder);
                Assert.notNull(operand, () -> "Unresolvable context placeholder: '" + placeholder + "'.");
            } else {
                operand = operandStr;
            }

            resolvedOperand = resolver.resolve(propertyType, operand, resolvedOperand);
            Assert.notNull(resolvedOperand, () -> "Operand '" + operandStr + "' cannot be converted to " + propertyType + ".");
            resolvedOperands[index++] = resolvedOperand;
        }

        return new QueryExpressionTuple(propertyDescriptor, propertyPath, operator, resolvedOperands);
    }

    /**
     * Resolves a placeholder using the query context.  Can specify a property path (dot-notation) to access
     * a property (possibly nested) of the resolved placeholder value.
     *
     * @param queryContext The query context.
     * @param placeholder  The placeholder (with optional property path).
     * @return The resolved value, or null if could not be resolved.
     */
    private Object resolvePlaceholder(
            IQueryContext queryContext,
            String placeholder) {
        try {
            String[] pcs = placeholder.split("\\.");
            Object result = queryContext.getParam(pcs[0]);

            for (int i = 1; i < pcs.length; i++) {
                if (result == null) {
                    break;
                }

                PropertyDescriptor dx = BeanUtils.getPropertyDescriptor(result.getClass(), pcs[i]);
                result = dx.getReadMethod().invoke(result);
            }

            return result;
        } catch (Exception e) {
            return null;
        }
    }

}
