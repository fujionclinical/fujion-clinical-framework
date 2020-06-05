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
import org.fujion.common.StrUtil;
import org.fujionclinical.api.model.IDomainObject;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class QueryExpressionParser {

    private static final String REGEX_NOT_QUOTED = "(?=([^\"]*\"[^\"]*\")*[^\"]*$)";

    private static final Pattern TUPLE_DELIMITER = Pattern.compile("&" + REGEX_NOT_QUOTED);

    private static final Pattern OPERAND_DELIMITER = Pattern.compile("," + REGEX_NOT_QUOTED);

    private static final QueryExpressionParser instance = new QueryExpressionParser();

    public static QueryExpressionParser getInstance() {
        return instance;
    }

    public <T extends IDomainObject> QueryExpression<T> parse(
            Class<T> domainClass,
            String queryString) {
        List<QueryExpressionFragment> fragments = Arrays.stream(TUPLE_DELIMITER.split(queryString))
                .map(fragment -> parseFragment(domainClass, fragment))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return new QueryExpression<T>(domainClass, fragments);
    }

    private <T extends IDomainObject> QueryExpressionFragment parseFragment(
            Class<T> domainClass,
            String fragment) {
        for (QueryOperator operator : QueryOperator.values()) {
            String[] result = null;

            for (String alias : operator.getAliases()) {
                result = split(fragment, alias, false);

                if (result != null) {
                    break;
                }
            }

            result = result == null ? split(fragment, " " + operator.name() + " ", true) : result;

            if (result != null) {
                String propertyPath = result[0].trim();
                Class<?> propClass = propertyPath.startsWith("_") ? QueryExpressionModifier.class : domainClass;
                PropertyDescriptor propertyDescriptor = getPropertyDescriptor(propClass, propertyPath);
                AbstractQueryExpressionResolver resolver = QueryExpressionResolvers.getResolver(propertyDescriptor);
                String[] operands = Arrays.stream(OPERAND_DELIMITER.split(result[1]))
                        .map(StringUtils::trimToNull)
                        .filter(Objects::nonNull)
                        .map(StrUtil::stripQuotes)
                        .toArray(String[]::new);
                return new QueryExpressionFragment(propertyDescriptor, propertyPath, resolver, operator, operands);
            }
        }

        throw new IllegalArgumentException("Invalid query syntax for '" + fragment + "'.");
    }

    private PropertyDescriptor getPropertyDescriptor(
            Class<?> clazz,
            String propertyPath) {
        PropertyDescriptor propertyDescriptor = null;

        for (String propertyName : propertyPath.split("\\.")) {
            propertyDescriptor = BeanUtils.getPropertyDescriptor(clazz, propertyName);
            Assert.notNull(propertyDescriptor, () -> "Cannot resolve property '" + propertyPath + "'.");
            clazz = propertyDescriptor.getPropertyType();
        }

        return propertyDescriptor;
    }

    private String[] split(
            String value,
            String separator,
            boolean caseInsensitive) {
        String[] result = StringUtils.splitByWholeSeparator(value, separator, 2);
        return result.length == 2 ? result : caseInsensitive ? split(value, separator.toLowerCase(), false) : null;
    }

}
