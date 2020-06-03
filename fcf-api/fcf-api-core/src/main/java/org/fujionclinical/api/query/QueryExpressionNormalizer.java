package org.fujionclinical.api.query;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;

public abstract class QueryExpressionNormalizer<PROP, OPD> {

    private final Class<PROP> propertyType;

    private final Class<OPD> operandType;

    private final int maxOperands;

    private final List<QueryOperator> validOperators;

    public QueryExpressionNormalizer(
            Class<PROP> propertyType,
            Class<OPD> operandType,
            int maxOperands,
            QueryOperator... validOperators) {
        this.propertyType = propertyType;
        this.operandType = operandType;
        this.maxOperands = maxOperands;
        this.validOperators = Arrays.asList(validOperators);
    }

    protected abstract OPD fromString(
            String value,
            OPD previousValue);

    protected abstract OPD fromPlaceholder(PROP value);

    public QueryExpressionTuple createTuple(
            PropertyDescriptor propDx,
            IQueryContext queryContext,
            QueryOperator operator,
            String... operands) {
        QueryParameter annot = AnnotationUtils.findAnnotation(propDx.getReadMethod(), QueryParameter.class);
        Assert.notNull(annot, () -> "The property '" + propDx.getName() + "' is not a valid query parameter.");
        String propertyName = StringUtils.defaultIfBlank(annot.value(), propDx.getName());
        Assert.isTrue(validOperators.contains(operator), () -> "Not a valid operator: '" + operator + "'.");
        Assert.isTrue(operands.length <= maxOperands, () -> "Operand maximum of " + maxOperands + " exceeded.");
        OPD[] resolvedOperands = (OPD[]) new Object[operands.length];
        int index = -1;

        for (String operand : operands) {
            OPD previousValue = index == -1 ? null : resolvedOperands[index];
            index++;
            operand = operand.trim();
            Object resolvedOperand;

            if (operand.startsWith("{{") && operand.endsWith("}}")) {
                String placeholder = operand.substring(2, operand.length() - 2).trim();
                resolvedOperand = queryContext == null ? null : queryContext.getParam(placeholder);
                Assert.notNull(resolvedOperand, () -> "Unrecognized context placeholder: '" + placeholder + "'.");
            } else {
                resolvedOperand = operand;
            }

            if (resolvedOperand instanceof String) {
                resolvedOperands[index] = fromString((String) resolvedOperand, previousValue);
            } else if (propertyType.isInstance(resolvedOperand)) {
                resolvedOperands[index] = fromPlaceholder((PROP) resolvedOperand);
            } else {
                throw new IllegalArgumentException("Context value must be of type " + propertyType + ".");
            }
        }

        return new QueryExpressionTuple(propertyName, propertyType, operator, resolvedOperands);
    }

    public Class<PROP> getPropertyType() {
        return propertyType;
    }

}
