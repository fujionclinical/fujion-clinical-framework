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

import org.apache.commons.lang3.BooleanUtils;
import org.fujion.common.DateUtil;
import org.fujion.common.MiscUtil;
import org.fujionclinical.api.core.CoreUtil;
import org.fujionclinical.api.model.*;
import org.fujionclinical.api.model.person.IPersonName;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A registry of query expression resolvers, indexed by the property type resolved.  Includes several predefined
 * resolvers, but additional ones may be registered.
 */
class QueryExpressionResolvers {

    static class StringResolver extends QueryExpressionResolver<String, String> {

        public StringResolver() {
            super(String.class, Integer.MAX_VALUE, QueryOperator.EQ, QueryOperator.SW);
        }

        @Override
        protected String resolve(
                Object operand,
                String previousOperand) {
            return operand instanceof String ? (String) operand : null;
        }

    }

    static class BooleanResolver extends QueryExpressionResolver<Boolean, Boolean> {

        public BooleanResolver() {
            super(Boolean.class, 1, QueryOperator.EQ);
        }

        @Override
        protected Boolean resolve(
                Object operand,
                Boolean previousOperand) {
            return operand instanceof Boolean ? (Boolean) operand
                    : operand instanceof String ? BooleanUtils.toBoolean((String) operand)
                    : null;
        }

    }

    static class DateResolver extends QueryExpressionResolver<Date, Date> {

        public DateResolver() {
            super(Date.class, 1, QueryOperator.EQ, QueryOperator.GE, QueryOperator.GT, QueryOperator.LE, QueryOperator.LT);
        }

        @Override
        protected Date resolve(
                Object operand,
                Date previousOperand) {
            return operand instanceof Date ? (Date) operand
                    : operand instanceof String ? DateUtil.parseDate((String) operand)
                    : null;
        }

    }

    static class DomainObjectResolver extends QueryExpressionResolver<IDomainObject, String> {

        public DomainObjectResolver() {
            super(IDomainObject.class, 1, QueryOperator.EQ);
        }

        @Override
        protected String resolve(
                Object operand,
                String previousOperand) {
            return operand instanceof IDomainObject ? ((IDomainObject) operand).getId()
                    : operand instanceof String ? (String) operand
                    : null;
        }

    }

    static class ConceptCodeResolver extends QueryExpressionResolver<IConceptCode, IConceptCode> {

        public ConceptCodeResolver() {
            super(IConceptCode.class, Integer.MAX_VALUE, QueryOperator.EQ);
        }

        private IConceptCode fromString(
                String value,
                IConceptCode previousValue) {
            String previousSystem = previousValue == null ? null : previousValue.getSystem();
            String[] pcs = value.split("\\|", 3);
            String system = pcs.length > 1 ? pcs[0] : null;
            String code = pcs.length == 1 ? pcs[0] : pcs[1];
            return new ConceptCode(system == null ? previousSystem : system, code);
        }

        @Override
        protected IConceptCode resolve(
                Object operand,
                IConceptCode previousOperand) {
            return operand instanceof IConceptCode ? (IConceptCode) operand
                    : operand instanceof String ? fromString((String) operand, previousOperand)
                    : null;
        }

    }

    static class IdentifierResolver extends QueryExpressionResolver<IIdentifier, IIdentifier> {

        public IdentifierResolver() {
            super(IIdentifier.class, Integer.MAX_VALUE, QueryOperator.EQ);
        }

        private IIdentifier fromString(
                String value,
                IIdentifier previousValue) {
            String previousSystem = previousValue == null ? null : previousValue.getSystem();
            String[] pcs = value.split("\\|", 3);
            String system = pcs.length > 1 ? pcs[0] : null;
            String code = pcs.length == 1 ? pcs[0] : pcs[1];
            return new Identifier(system == null ? previousSystem : system, code);
        }

        @Override
        protected IIdentifier resolve(
                Object operand,
                IIdentifier previousOperand) {
            return operand instanceof IIdentifier ? (IIdentifier) operand
                    : operand instanceof String ? fromString((String) operand, previousOperand)
                    : null;
        }

    }

    static class PersonNameResolver extends QueryExpressionResolver<IPersonName, String> {

        public PersonNameResolver() {
            super(IPersonName.class, 1, QueryOperator.EQ, QueryOperator.SW);
        }

        @Override
        protected String resolve(
                Object operand,
                String previousOperand) {
            return operand instanceof String ? (String) operand
                    : operand instanceof IPersonName ? ((IPersonName) operand).asString()
                    : null;
        }

    }

    private static final Map<Class<?>, QueryExpressionResolver> resolvers = new LinkedHashMap<>();

    static {
        registerResolver(new StringResolver());
        registerResolver(new BooleanResolver());
        registerResolver(new DateResolver());
        registerResolver(new DomainObjectResolver());
        registerResolver(new ConceptCodeResolver());
        registerResolver(new IdentifierResolver());
        registerResolver(new PersonNameResolver());
    }

    /**
     * Register a query expression resolver.
     *
     * @param resolver The query expression resolver.
     */
    public static void registerResolver(QueryExpressionResolver resolver) {
        resolvers.put(resolver.getPropertyType(), resolver);
    }

    /**
     * Retrieve the query expression resolver for the property type specified by the property descriptor.
     *
     * @param propertyDescriptor The property descriptor.
     * @return The associated query expression resolver (never null).
     * @exception IllegalArgumentException If no query expression resolved was found.
     */
    public static QueryExpressionResolver getResolver(
            PropertyDescriptor propertyDescriptor) {
        Class<?> propertyType = CoreUtil.getPropertyType(propertyDescriptor);
        Class<?> key = propertyType == null ? null : MiscUtil.firstAssignable(propertyType, resolvers.keySet());
        QueryExpressionResolver resolver = key == null ? null : resolvers.get(key);
        Assert.notNull(resolver, () -> "No resolver found for property '" + propertyDescriptor.getName() + "'.");
        return resolver;
    }

    private QueryExpressionResolvers() {

    }

}
