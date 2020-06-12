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
import org.apache.commons.lang3.ClassUtils;
import org.fujion.common.DateTimeWrapper;
import org.fujion.common.MiscUtil;
import org.fujionclinical.api.core.CoreUtil;
import org.fujionclinical.api.model.core.*;
import org.fujionclinical.api.model.person.IPersonName;
import org.springframework.util.Assert;
import org.springframework.util.NumberUtils;

import java.beans.PropertyDescriptor;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A registry of query expression resolvers, indexed by the property type resolved.  Includes several predefined
 * resolvers, but additional ones may be registered.
 */
class QueryExpressionResolvers {

    static class EnumResolver extends AbstractQueryExpressionResolver<Enum, Enum> {

        public EnumResolver() {
            super(Enum.class, Integer.MAX_VALUE, QueryOperator.EQ, QueryOperator.SW);
        }

        @Override
        protected Enum resolve(
                Class<Enum> propertyType,
                Object operand,
                Enum previousOperand) {
            return operand instanceof String ? QueryUtil.findMatchingMember(propertyType, (String) operand)
                    : propertyType.isInstance(operand) ? propertyType.cast(operand)
                    : null;
        }

    }

    static class StringResolver extends AbstractQueryExpressionResolver<String, String> {

        public StringResolver() {
            super(String.class, Integer.MAX_VALUE, QueryOperator.EQ, QueryOperator.SW);
        }

        @Override
        protected String resolve(
                Class<String> propertyType,
                Object operand,
                String previousOperand) {
            return operand instanceof String ? (String) operand : null;
        }

    }

    static class BooleanResolver extends AbstractQueryExpressionResolver<Boolean, Boolean> {

        public BooleanResolver() {
            super(Boolean.class, 1, QueryOperator.EQ);
        }

        @Override
        protected Boolean resolve(
                Class<Boolean> propertyType,
                Object operand,
                Boolean previousOperand) {
            return operand instanceof Boolean ? (Boolean) operand
                    : operand instanceof String ? BooleanUtils.toBoolean((String) operand)
                    : null;
        }

    }

    static class NumberResolver extends AbstractQueryExpressionResolver<Number, Number> {

        public NumberResolver() {
            super(Number.class, 1, QueryOperator.EQ);
        }

        @Override
        protected Number resolve(
                Class<Number> propertyType,
                Object operand,
                Number previousOperand) {
            return propertyType.isInstance(operand) ? NumberUtils.convertNumberToTargetClass((Number) operand, propertyType)
                    : operand instanceof String ? NumberUtils.parseNumber((String) operand, propertyType)
                    : null;
        }

    }

    static class DateResolver extends AbstractQueryExpressionResolver<DateTimeWrapper, DateTimeWrapper> {

        public DateResolver() {
            super(DateTimeWrapper.class, 1, QueryOperator.EQ, QueryOperator.GE, QueryOperator.GT, QueryOperator.LE, QueryOperator.LT);
        }

        @Override
        protected DateTimeWrapper resolve(
                Class<DateTimeWrapper> propertyType,
                Object operand,
                DateTimeWrapper previousOperand) {
            return operand instanceof DateTimeWrapper ? (DateTimeWrapper) operand
                    : operand instanceof String ? DateTimeWrapper.parse((String) operand)
                    : null;
        }

    }

    static class DomainObjectResolver extends AbstractQueryExpressionResolver<IDomainObject, String> {

        public DomainObjectResolver() {
            super(IDomainObject.class, 1, QueryOperator.EQ);
        }

        @Override
        protected String resolve(
                Class<IDomainObject> propertyType,
                Object operand,
                String previousOperand) {
            return propertyType.isInstance(operand) ? propertyType.cast(operand).getId()
                    : operand instanceof String ? (String) operand
                    : null;
        }

    }

    static class ConceptCodeResolver extends AbstractQueryExpressionResolver<IConceptCode, IConceptCode> {

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
                Class<IConceptCode> propertyType,
                Object operand,
                IConceptCode previousOperand) {
            return operand instanceof IConceptCode ? (IConceptCode) operand
                    : operand instanceof String ? fromString((String) operand, previousOperand)
                    : null;
        }

    }

    static class IdentifierResolver extends AbstractQueryExpressionResolver<IIdentifier, IIdentifier> {

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
                Class<IIdentifier> propertyType,
                Object operand,
                IIdentifier previousOperand) {
            return operand instanceof IIdentifier ? (IIdentifier) operand
                    : operand instanceof String ? fromString((String) operand, previousOperand)
                    : null;
        }

    }

    static class PersonNameResolver extends AbstractQueryExpressionResolver<IPersonName, String> {

        public PersonNameResolver() {
            super(IPersonName.class, 1, QueryOperator.EQ, QueryOperator.SW);
        }

        @Override
        protected String resolve(
                Class<IPersonName> propertyType,
                Object operand,
                String previousOperand) {
            return operand instanceof String ? (String) operand
                    : operand instanceof IPersonName ? ((IPersonName) operand).asString()
                    : null;
        }

    }

    private static final Map<Class<?>, AbstractQueryExpressionResolver> resolvers = new LinkedHashMap<>();

    static {
        registerResolver(new StringResolver());
        registerResolver(new BooleanResolver());
        registerResolver(new NumberResolver());
        registerResolver(new DateResolver());
        registerResolver(new EnumResolver());
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
    public static void registerResolver(AbstractQueryExpressionResolver resolver) {
        resolvers.put(resolver.getTargetClass(), resolver);
    }

    /**
     * Retrieve the query expression resolver for the property type specified by the property descriptor.
     *
     * @param propertyDescriptor The property descriptor.
     * @return The associated query expression resolver (never null).
     * @throws IllegalArgumentException If no query expression resolved was found.
     */
    public static AbstractQueryExpressionResolver getResolver(
            PropertyDescriptor propertyDescriptor) {
        Class<?> propertyType = CoreUtil.getPropertyType(propertyDescriptor);
        propertyType = ClassUtils.primitiveToWrapper(propertyType);
        Class<?> key = propertyType == null ? null : MiscUtil.firstAssignable(propertyType, resolvers.keySet());
        AbstractQueryExpressionResolver resolver = key == null ? null : resolvers.get(key);
        Assert.notNull(resolver, () -> "No resolver found for property '" + propertyDescriptor.getName() + "'.");
        return resolver;
    }

    private QueryExpressionResolvers() {

    }

}
