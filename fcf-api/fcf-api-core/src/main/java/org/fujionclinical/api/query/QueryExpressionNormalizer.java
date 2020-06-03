package org.fujionclinical.api.query;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class QueryExpressionNormalizer<PROP, OPD> {

    private final Class<PROP> propertyType;

    private final int maxOperands;

    private final Set<QueryOperator> validOperators = new HashSet<>();

    public QueryExpressionNormalizer(
            Class<PROP> propertyType,
            int maxOperands,
            QueryOperator... validOperators) {
        this.propertyType = propertyType;
        this.maxOperands = maxOperands;
        this.validOperators.addAll(Arrays.asList(validOperators));
    }

    protected abstract OPD normalize(
            Object operand,
            OPD previousOperand);

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

        for (String operandStr : operands) {
            OPD previousOperand = index == -1 ? null : resolvedOperands[index];
            index++;
            operandStr = operandStr.trim();
            Object operand;

            if (operandStr.startsWith("{{") && operandStr.endsWith("}}")) {
                String placeholder = operandStr.substring(2, operandStr.length() - 2).trim();
                operand = queryContext == null ? null : queryContext.getParam(placeholder);
                Assert.notNull(operand, () -> "Unresolvable context placeholder: '" + placeholder + "'.");
            } else {
                operand = operandStr;
            }

            operand = normalize(operand, previousOperand);
            Assert.notNull(operand, () -> "Context value cannot be converted to " + propertyType + ".");
        }

        return new QueryExpressionTuple(propertyName, propertyType, operator, resolvedOperands);
    }

    public Class<PROP> getPropertyType() {
        return propertyType;
    }

}
