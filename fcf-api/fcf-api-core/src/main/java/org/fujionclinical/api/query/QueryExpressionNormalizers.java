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
import org.fujionclinical.api.model.ConceptCode;
import org.fujionclinical.api.model.IConceptCode;
import org.fujionclinical.api.model.IDomainObject;
import org.fujionclinical.api.model.person.IPersonName;

import java.util.Date;

class QueryExpressionNormalizers {

    static class StringNormalizer extends QueryExpressionNormalizer<String, String> {

        public StringNormalizer() {
            super(String.class, Integer.MAX_VALUE, QueryOperator.values());
        }

        @Override
        protected String normalize(
                Object operand,
                String previousOperand) {
            return operand instanceof String ? (String) operand : null;
        }

    }

    static class BooleanNormalizer extends QueryExpressionNormalizer<Boolean, Boolean> {

        public BooleanNormalizer() {
            super(Boolean.class, 1, QueryOperator.EQ);
        }

        @Override
        protected Boolean normalize(
                Object operand,
                Boolean previousOperand) {
            return operand instanceof Boolean ? (Boolean) operand
                    : operand instanceof String ? BooleanUtils.toBoolean((String) operand)
                    : null;
        }

    }

    static class DateNormalizer extends QueryExpressionNormalizer<Date, Date> {

        public DateNormalizer() {
            super(Date.class, 1, QueryOperator.EQ, QueryOperator.GE, QueryOperator.GT, QueryOperator.LE, QueryOperator.LT);
        }

        @Override
        protected Date normalize(
                Object operand,
                Date previousOperand) {
            return operand instanceof Date ? (Date) operand
                    : operand instanceof String ? DateUtil.parseDate((String) operand)
                    : null;
        }

    }

    static class DomainObjectNormalizer extends QueryExpressionNormalizer<IDomainObject, String> {

        public DomainObjectNormalizer() {
            super(IDomainObject.class, 1, QueryOperator.EQ);
        }

        @Override
        protected String normalize(
                Object operand,
                String previousOperand) {
            return operand instanceof IDomainObject ? ((IDomainObject) operand).getId()
                    : operand instanceof String ? (String) operand
                    : null;
        }

    }

    static class ConceptCodeNormalizer extends QueryExpressionNormalizer<IConceptCode, IConceptCode> {

        public ConceptCodeNormalizer() {
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
        protected IConceptCode normalize(
                Object operand,
                IConceptCode previousOperand) {
            return operand instanceof IConceptCode ? (IConceptCode) operand
                    : operand instanceof String ? fromString((String) operand, previousOperand)
                    : null;
        }

    }

    static class PersonNameNormalizer extends QueryExpressionNormalizer<IPersonName, String> {

        public PersonNameNormalizer() {
            super(IPersonName.class, 1, QueryOperator.EQ, QueryOperator.SW);
        }

        @Override
        protected String normalize(
                Object operand,
                String previousOperand) {
            return operand instanceof String ? (String) operand
                    : operand instanceof IPersonName ? ((IPersonName) operand).asString()
                    : null;
        }

    }

    static void registerNormalizers(QueryExpressionParser parser) {
        parser.registerNormalizer(new StringNormalizer());
        parser.registerNormalizer(new BooleanNormalizer());
        parser.registerNormalizer(new DateNormalizer());
        parser.registerNormalizer(new DomainObjectNormalizer());
        parser.registerNormalizer(new ConceptCodeNormalizer());
        parser.registerNormalizer(new PersonNameNormalizer());
    }

    private QueryExpressionNormalizers() {

    }

}
