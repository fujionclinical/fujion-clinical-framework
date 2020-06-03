package org.fujionclinical.api.query;

public class QueryExpressionTuple {

    public final String propertyName;

    public final Class<?> propertyType;

    public final QueryOperator operator;

    public final Object[] operands;

    public QueryExpressionTuple(
            String propertyName,
            Class<?> propertyType,
            QueryOperator operator,
            Object... operands) {
        this.propertyName = propertyName;
        this.propertyType = propertyType;
        this.operator = operator;
        this.operands = operands;
    }

}
