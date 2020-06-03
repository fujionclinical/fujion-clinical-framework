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
package org.fujionclinical.api;

import org.fujionclinical.api.model.person.IPerson;
import org.fujionclinical.api.model.user.IUser;
import org.fujionclinical.api.model.user.User;
import org.fujionclinical.api.query.QueryContext;
import org.fujionclinical.api.query.QueryExpression;
import org.fujionclinical.api.query.QueryExpressionParser;
import org.junit.Assert;
import org.junit.Test;

public class QueryTest {

    @Test
    public void test() {
        User user = new User("123", "Martin, Douglas K", "username", "password", null);
        QueryContext queryContext = new QueryContext();
        queryContext.setParam("user", user);
        QueryExpression result = QueryExpressionParser.getInstance().parse(IUser.class,
                "id={{user.id}} & name ~ {{user.name.familyName}} & birthDate >= 1/27/2000 & race=system1|code1,code2,system3|code3,|code4",
                queryContext);
        Assert.assertEquals(4, result.getTuples().size());
    }

}
