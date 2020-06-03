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

    public static QueryExpressionParser getInstance() {
        return instance;
    }

    private QueryExpressionParser() {
        QueryExpressionNormalizers.registerNormalizers(this);
    }

    public <T extends IDomainObject> QueryExpression<T> parse(
            Class<T> domainClass,
            String queryString) {
        return parse(domainClass, queryString, null);
    }

    public <T extends IDomainObject> QueryExpression<T> parse(
            Class<T> domainClass,
            String queryString,
            IQueryContext queryContext) {
        List<QueryExpressionTuple> tuples = Arrays.stream(TUPLE_DELIMITER.split(queryString))
                .map(tuple -> parseFragment(domainClass, tuple, queryContext))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return new QueryExpression<T>(domainClass, tuples);
    }

    public void registerNormalizer(QueryExpressionNormalizer normalizer) {
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
                PropertyDescriptor dx = getPropertyDescriptor(domainClass, propName);
                QueryExpressionNormalizer normalizer = getNormalizer(dx, propName);
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

    private PropertyDescriptor getPropertyDescriptor(
            Class<?> clazz,
            String propName) {
        PropertyDescriptor dx = BeanUtils.getPropertyDescriptor(clazz, propName);
        Assert.notNull(dx, () -> "Cannot resolve property '" + propName + "'.");
        return dx;
    }

    private String[] split(
            String value,
            String separator,
            boolean caseInsensitive) {
        String[] result = StringUtils.splitByWholeSeparator(value, separator, 2);
        return result.length == 2 ? result : caseInsensitive ? split(value, separator.toLowerCase(), false) : null;
    }

    private QueryExpressionNormalizer getNormalizer(
            PropertyDescriptor dx,
            String propName) {
        Class<?> propertyType = dx.getPropertyType();
        Class<?> key = propertyType == null ? null : MiscUtil.firstAssignable(propertyType, normalizers.keySet());
        QueryExpressionNormalizer normalizer = key == null ? null : normalizers.get(key);
        Assert.notNull(normalizer, () -> "No normalizer found for property '" + propName + "'.");
        return normalizer;
    }

}
