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

import edu.utah.kmm.model.cool.foundation.entity.Person;
import edu.utah.kmm.model.cool.foundation.entity.PersonImpl;
import edu.utah.kmm.model.cool.mediator.expression.Expression;
import edu.utah.kmm.model.cool.mediator.expression.ExpressionParser;
import edu.utah.kmm.model.cool.mediator.expression.ExpressionTuple;
import edu.utah.kmm.model.cool.mediator.query.QueryContext;
import edu.utah.kmm.model.cool.mediator.query.QueryContextImpl;
import edu.utah.kmm.model.cool.util.PersonNameParsers;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class QueryTest {

    @Test
    public void test() {
        Person user = new PersonImpl();
        user.setDefaultId("123");
        user.addName(PersonNameParsers.get().fromString("Martin, Douglas K"));
        QueryContext queryContext = new QueryContextImpl();
        queryContext.setParam("user", user);
        queryContext.setParam("count", 123);
        Expression expression = ExpressionParser.getInstance().parse(Person.class,
                "id=={{user.id}} & name.family ~ {{user.name.family}} & birthDate >= 2000-01-27 & gender=system1|code1,code2,system3|code3,|code4" +
                        "& identifiers=system1|value1 & _count={{count}}");
        List<ExpressionTuple> tuples = expression.resolve(queryContext);
        Assert.assertEquals(6, tuples.size());
    }

}
