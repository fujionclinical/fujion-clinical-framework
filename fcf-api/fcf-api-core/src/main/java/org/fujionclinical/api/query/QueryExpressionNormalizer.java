package org.fujionclinical.api.query;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
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

        for (String opd : operands) {
            OPD previousOperand = index == -1 ? null : resolvedOperands[index];
            index++;
            String operandStr = opd.trim();
            Object operand;

            if (operandStr.startsWith("{{") && operandStr.endsWith("}}")) {
                String placeholder = operandStr.substring(2, operandStr.length() - 2).trim();
                operand = queryContext == null ? null : resolvePlaceholder(queryContext, placeholder);
                Assert.notNull(operand, () -> "Unresolvable context placeholder: '" + placeholder + "'.");
            } else {
                operand = operandStr;
            }

            OPD resolvedOperand = normalize(operand, previousOperand);
            Assert.notNull(resolvedOperand, () -> "Operand '" + operandStr + "' cannot be converted to " + propertyType + ".");
            resolvedOperands[index] = resolvedOperand;
        }

        return new QueryExpressionTuple(propertyName, propertyType, operator, resolvedOperands);
    }

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

    public Class<PROP> getPropertyType() {
        return propertyType;
    }

}
