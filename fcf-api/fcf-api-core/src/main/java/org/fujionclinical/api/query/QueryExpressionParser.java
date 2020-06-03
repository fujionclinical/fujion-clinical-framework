package org.fujionclinical.api.query;

import org.apache.commons.lang.StringUtils;
import org.fujion.common.MiscUtil;
import org.fujion.common.StrUtil;
import org.fujionclinical.api.model.IDomainObject;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class QueryExpressionParser {

    private static final String REGEX_NOT_QUOTED = "(?=([^\"]*\"[^\"]*\")*[^\"]*$)";

    private static final Pattern TUPLE_DELIMITER = Pattern.compile("&" + REGEX_NOT_QUOTED);

    private static final Pattern OPERAND_DELIMITER = Pattern.compile("," + REGEX_NOT_QUOTED);

    private static final QueryExpressionParser instance = new QueryExpressionParser();

    private final Map<Class<?>, QueryExpressionNormalizer> normalizers = new LinkedHashMap<>();

    public static <T extends IDomainObject> QueryExpression<T> parse(
            Class<T> domainClass,
            String queryString) {
        return parse(domainClass, queryString, null);
    }

    public static <T extends IDomainObject> QueryExpression<T> parse(
            Class<T> domainClass,
            String queryString,
            IQueryContext queryContext) {
        List<QueryExpressionTuple> tuples = Arrays.stream(TUPLE_DELIMITER.split(queryString))
                .map(tuple -> instance.parseFragment(domainClass, tuple, queryContext))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return new QueryExpression<T>(domainClass, tuples);
    }

    public static void registerNormalizer(QueryExpressionNormalizer normalizer) {
        instance._registerNormalizer(normalizer);
    }

    private QueryExpressionParser() {
        _registerNormalizer(new QueryExpressionNormalizers.StringNormalizer());
        _registerNormalizer(new QueryExpressionNormalizers.BooleanNormalizer());
        _registerNormalizer(new QueryExpressionNormalizers.DateNormalizer());
        _registerNormalizer(new QueryExpressionNormalizers.DomainObjectNormalizer());
        _registerNormalizer(new QueryExpressionNormalizers.ConceptCodeNormalizer());
    }

    private void _registerNormalizer(QueryExpressionNormalizer normalizer) {
        normalizers.put(normalizer.getPropertyType(), normalizer);
    }

    private <T extends IDomainObject> QueryExpressionTuple parseFragment(
            Class<T> domainClass,
            String tuple,
            IQueryContext queryContext) {
        for (QueryOperator operator : QueryOperator.values()) {
            String[] result = null;

            for (String alias : operator.getAliases()) {
                result = split(tuple, alias, false);

                if (result != null) {
                    break;
                }
            }

            result = result == null ? split(tuple, " " + operator.name() + " ", true) : result;

            if (result != null) {
                String propName = result[0].trim();
                PropertyDescriptor dx = BeanUtils.getPropertyDescriptor(domainClass, propName);
                QueryExpressionNormalizer normalizer = getNormalizer(dx);
                String[] operands = Arrays.stream(OPERAND_DELIMITER.split(result[1]))
                        .map(StringUtils::trimToNull)
                        .filter(Objects::nonNull)
                        .map(StrUtil::stripQuotes)
                        .map(StringUtils::trimToNull)
                        .toArray(String[]::new);
                return normalizer.createTuple(dx, queryContext, operator, operands);
            }
        }

        throw new IllegalArgumentException("Invalid query syntax for '" + tuple + "'.");
    }

    private String[] split(
            String value,
            String separator,
            boolean caseInsensitive) {
        String[] result = StringUtils.splitByWholeSeparator(value, separator, 2);
        return result.length == 2 ? result : caseInsensitive ? split(value, separator.toLowerCase(), false) : null;
    }

    private QueryExpressionNormalizer getNormalizer(PropertyDescriptor dx) {
        Class<?> propertyType = dx.getPropertyType();
        Class<?> key = MiscUtil.firstAssignable(propertyType, normalizers.keySet());
        QueryExpressionNormalizer normalizer = key == null ? null : normalizers.get(key);
        Assert.notNull(normalizer, () -> "No normalizer found for type '" + propertyType + "'.");
        return normalizer;
    }

}
