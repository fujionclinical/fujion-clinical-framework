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

import edu.utah.kmm.model.cool.core.datatype.Identifier;
import edu.utah.kmm.model.cool.core.datatype.IdentifierExImpl;
import edu.utah.kmm.model.cool.terminology.ConceptReference;
import edu.utah.kmm.model.cool.terminology.ConceptReferenceImpl;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ClassUtils;
import org.fujion.common.DateUtil;
import org.fujion.common.MiscUtil;
import org.fujionclinical.api.core.CoreUtil;
import org.fujionclinical.api.model.core.IDomainType;
import org.fujionclinical.api.model.core.IReference;
import org.fujionclinical.api.model.person.IPersonName;
import org.fujionclinical.api.query.core.QueryUtil;
import org.fujionclinical.api.spring.BeanRegistry;
import org.springframework.util.Assert;
import org.springframework.util.NumberUtils;

import java.beans.PropertyDescriptor;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * A registry of parameter descriptors, indexed by the parameter type.  Includes several predefined
 * parameter descriptors, but additional ones may be registered either programmatically or via the
 * application context..
 */
class ParameterDescriptors extends BeanRegistry<Class<?>, IParameterDescriptor> {

    static class EnumParameter extends AbstractParameterDescriptor<Enum, Enum> {

        public EnumParameter() {
            super(Enum.class, Integer.MAX_VALUE, Operator.EQ, Operator.SW);
        }

        @Override
        public Enum normalizeOperand(
                Class<Enum> parameterType,
                Object operand,
                Enum previousOperand) {
            return operand instanceof String ? QueryUtil.findMatchingMember(parameterType, (String) operand)
                    : parameterType.isInstance(operand) ? parameterType.cast(operand)
                    : null;
        }

    }

    static class StringParameter extends AbstractParameterDescriptor<String, String> {

        public StringParameter() {
            super(String.class, Integer.MAX_VALUE, Operator.EQ, Operator.SW);
        }

        @Override
        public String normalizeOperand(
                Class<String> parameterType,
                Object operand,
                String previousOperand) {
            return operand instanceof String ? (String) operand : null;
        }

    }

    static class BooleanParameter extends AbstractParameterDescriptor<Boolean, Boolean> {

        public BooleanParameter() {
            super(Boolean.class, 1, Operator.EQ);
        }

        @Override
        public Boolean normalizeOperand(
                Class<Boolean> parameterType,
                Object operand,
                Boolean previousOperand) {
            return operand instanceof Boolean ? (Boolean) operand
                    : operand instanceof String ? BooleanUtils.toBoolean((String) operand)
                    : null;
        }

    }

    static class NumberParameter extends AbstractParameterDescriptor<Number, Number> {

        public NumberParameter() {
            super(Number.class, 1, Operator.EQ);
        }

        @Override
        public Number normalizeOperand(
                Class<Number> parameterType,
                Object operand,
                Number previousOperand) {
            return parameterType.isInstance(operand) ? NumberUtils.convertNumberToTargetClass((Number) operand, parameterType)
                    : operand instanceof String ? NumberUtils.parseNumber((String) operand, parameterType)
                    : null;
        }

    }

    static class DateParameter extends AbstractParameterDescriptor<LocalDate, LocalDate> {

        public DateParameter() {
            super(LocalDate.class, 1, Operator.EQ, Operator.GE, Operator.GT, Operator.LE, Operator.LT);
        }

        @Override
        public LocalDate normalizeOperand(
                Class<LocalDate> parameterType,
                Object operand,
                LocalDate previousOperand) {
            return operand instanceof LocalDate ? (LocalDate) operand
                    : operand instanceof String ? DateUtil.parseLocalDate((String) operand).toLocalDate()
                    : null;
        }

    }

    static class DateTimeParameter extends AbstractParameterDescriptor<LocalDateTime, LocalDateTime> {

        public DateTimeParameter() {
            super(LocalDateTime.class, 1, Operator.EQ, Operator.GE, Operator.GT, Operator.LE, Operator.LT);
        }

        @Override
        public LocalDateTime normalizeOperand(
                Class<LocalDateTime> parameterType,
                Object operand,
                LocalDateTime previousOperand) {
            return operand instanceof LocalDateTime ? (LocalDateTime) operand
                    : operand instanceof String ? DateUtil.parseLocalDate((String) operand)
                    : null;
        }

    }

    static class DomainTypeParameter extends AbstractParameterDescriptor<IDomainType, String> {

        public DomainTypeParameter() {
            super(IDomainType.class, 1, Operator.EQ);
        }

        @Override
        public String normalizeOperand(
                Class<IDomainType> parameterType,
                Object operand,
                String previousOperand) {
            return parameterType.isInstance(operand) ? parameterType.cast(operand).getId()
                    : operand instanceof String ? (String) operand
                    : null;
        }

    }

    static class ConceptCodeParameter extends AbstractParameterDescriptor<ConceptReference, ConceptReference> {

        public ConceptCodeParameter() {
            super(ConceptReference.class, Integer.MAX_VALUE, Operator.EQ);
        }

        private ConceptReference fromString(
                String value,
                ConceptReference previousValue) {
            URI previousSystem = previousValue == null ? null : previousValue.getSystem();
            String[] pcs = value.split("\\|", 3);
            String system = pcs.length > 1 ? pcs[0] : null;
            String code = pcs.length == 1 ? pcs[0] : pcs[1];
            return new ConceptReferenceImpl(system == null ? previousSystem : URI.create(system), code, null);
        }

        @Override
        public ConceptReference normalizeOperand(
                Class<ConceptReference> parameterType,
                Object operand,
                ConceptReference previousOperand) {
            return operand instanceof ConceptReference ? (ConceptReference) operand
                    : operand instanceof String ? fromString((String) operand, previousOperand)
                    : null;
        }

    }

    static class IdentifierParameter extends AbstractParameterDescriptor<Identifier, Identifier> {

        public IdentifierParameter() {
            super(Identifier.class, Integer.MAX_VALUE, Operator.EQ);
        }

        private Identifier fromString(
                String value,
                Identifier previousValue) {
            String previousSystem = previousValue == null ? null : previousValue.getSystem().toString();
            String[] pcs = value.split("\\|", 3);
            String system = pcs.length > 1 ? pcs[0] : null;
            String code = pcs.length == 1 ? pcs[0] : pcs[1];
            return new IdentifierExImpl(system == null ? previousSystem : system, code);
        }

        @Override
        public Identifier normalizeOperand(
                Class<Identifier> parameterType,
                Object operand,
                Identifier previousOperand) {
            return operand instanceof Identifier ? (Identifier) operand
                    : operand instanceof String ? fromString((String) operand, previousOperand)
                    : null;
        }

    }

    static class PersonNameParameter extends AbstractParameterDescriptor<IPersonName, String> {

        public PersonNameParameter() {
            super(IPersonName.class, 1, Operator.EQ, Operator.SW);
        }

        @Override
        public String normalizeOperand(
                Class<IPersonName> parameterType,
                Object operand,
                String previousOperand) {
            return operand instanceof String ? (String) operand
                    : operand instanceof IPersonName ? ((IPersonName) operand).asString()
                    : null;
        }

    }

    static class ReferenceParameter extends AbstractParameterDescriptor<IReference, String> {

        public ReferenceParameter() {
            super(IReference.class, 1, Operator.EQ);
        }

        @Override
        public String normalizeOperand(
                Class<IReference> parameterType,
                Object operand,
                String previousOperand) {
            return parameterType.isInstance(operand) ? parameterType.cast(operand).getId()
                    : operand instanceof String ? (String) operand
                    : null;
        }

    }

    private static final ParameterDescriptors instance = new ParameterDescriptors();

    public static ParameterDescriptors getInstance() {
        return instance;
    }

    /**
     * Create a registry that tracks parameter descriptors.  Several built-in descriptors are registered here.
     */
    private ParameterDescriptors() {
        super(IParameterDescriptor.class);
        register(new StringParameter());
        register(new BooleanParameter());
        register(new NumberParameter());
        register(new DateParameter());
        register(new DateTimeParameter());
        register(new EnumParameter());
        register(new DomainTypeParameter());
        register(new ConceptCodeParameter());
        register(new IdentifierParameter());
        register(new PersonNameParameter());
        register(new ReferenceParameter());
    }

    /**
     * Retrieve the parameter descriptor for the parameter type specified by the property descriptor.
     *
     * @param propertyDescriptor The property descriptor.
     * @return The associated parameter descriptor (never null).
     * @throws IllegalArgumentException If no descriptor was found.
     */
    public IParameterDescriptor getDescriptor(PropertyDescriptor propertyDescriptor) {
        Class<?> parameterType = CoreUtil.getPropertyType(propertyDescriptor);
        parameterType = ClassUtils.primitiveToWrapper(parameterType);
        Class<?> key = parameterType == null ? null : MiscUtil.firstAssignable(parameterType, map.keySet());
        IParameterDescriptor descriptor = key == null ? null : get(key);
        Assert.notNull(descriptor, () -> "No parameter descriptor found for property '" + propertyDescriptor.getName() + "'");
        return descriptor;
    }

    @Override
    protected Class<?> getKey(IParameterDescriptor descriptor) {
        return descriptor.getParameterType();
    }

}
