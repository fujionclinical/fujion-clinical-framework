package org.fujionclinical.api.query;

import org.apache.commons.lang3.BooleanUtils;
import org.fujion.common.DateUtil;
import org.fujionclinical.api.model.ConceptCode;
import org.fujionclinical.api.model.IConceptCode;
import org.fujionclinical.api.model.IDomainObject;
import org.springframework.util.Assert;

import java.util.Date;

public class QueryExpressionNormalizers {

    static class StringNormalizer extends QueryExpressionNormalizer<String, String> {

        public StringNormalizer() {
            super(String.class, String.class, Integer.MAX_VALUE, QueryOperator.values());
        }

        @Override
        protected String fromString(
                String value,
                String previousValue) {
            return value;
        }

        @Override
        protected String fromPlaceholder(String value) {
            return value;
        }

    }

    static class BooleanNormalizer extends QueryExpressionNormalizer<Boolean, Boolean> {

        public BooleanNormalizer() {
            super(Boolean.class, Boolean.class, 1, QueryOperator.EQ);
        }

        @Override
        protected Boolean fromString(
                String value,
                Boolean previousValue) {
            return BooleanUtils.toBoolean(value);
        }

        @Override
        protected Boolean fromPlaceholder(Boolean value) {
            return value;
        }

    }

    static class DateNormalizer extends QueryExpressionNormalizer<Date, Date> {

        public DateNormalizer() {
            super(Date.class, Date.class, 1, QueryOperator.EQ, QueryOperator.GE, QueryOperator.GT, QueryOperator.LE, QueryOperator.LT);
        }

        @Override
        protected Date fromString(
                String value,
                Date previousValue) {
            Date date = DateUtil.parseDate(value);
            Assert.notNull(date, () -> "Invalid date specifier: '" + value + "'.");
            return date;
        }

        @Override
        protected Date fromPlaceholder(Date value) {
            return value;
        }

    }
    static class DomainObjectNormalizer extends QueryExpressionNormalizer<IDomainObject, String> {

        public DomainObjectNormalizer() {
            super(IDomainObject.class, String.class, 1, QueryOperator.EQ);
        }

        @Override
        protected String fromString(
                String value,
                String previousValue) {
            return value;
        }

        @Override
        protected String fromPlaceholder(IDomainObject value) {
            return value.getId();
        }

    }

    static class ConceptCodeNormalizer extends QueryExpressionNormalizer<IConceptCode, IConceptCode> {

        public ConceptCodeNormalizer() {
            super(IConceptCode.class, IConceptCode.class, Integer.MAX_VALUE, QueryOperator.EQ);
        }

        @Override
        protected IConceptCode fromString(
                String value,
                IConceptCode previousValue) {
            String previousSystem = previousValue == null ? null : previousValue.getSystem();
            String[] pcs = value.split("\\|");
            String system = pcs.length > 1 ? pcs[0] : null;
            String code = pcs.length == 1 ? pcs[0] : pcs[1];
            return new ConceptCode(system == null ? previousSystem : system, code);
        }

        @Override
        protected IConceptCode fromPlaceholder(IConceptCode value) {
            return value;
        }

    }

    private QueryExpressionNormalizers() {

    }

}
