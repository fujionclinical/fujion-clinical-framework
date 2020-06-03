package org.fujionclinical.api.query;

import org.fujionclinical.api.model.IDomainObject;

import java.util.Collections;
import java.util.List;

public class QueryExpression<T extends IDomainObject> {

    private final Class<T> domainClass;

    private final List<QueryExpressionTuple> tuples;

    public QueryExpression(Class<T> domainClass, List<QueryExpressionTuple> tuples) {
        this.domainClass = domainClass;
        this.tuples = Collections.unmodifiableList(tuples);
    }

    public Class<T> getDomainClass() {
        return domainClass;
    }

    public List<QueryExpressionTuple> getTuples() {
        return tuples;
    }

}
