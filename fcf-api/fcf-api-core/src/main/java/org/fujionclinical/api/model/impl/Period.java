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
package org.fujionclinical.api.model.impl;

import org.fujion.common.DateTimeWrapper;
import org.fujionclinical.api.model.core.IPeriod;

public class Period implements IPeriod {

    private DateTimeWrapper startDate;

    private DateTimeWrapper endDate;

    public Period() {
    }

    public Period(
            DateTimeWrapper startDate,
            DateTimeWrapper endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public DateTimeWrapper getStart() {
        return startDate;
    }

    @Override
    public void setStart(DateTimeWrapper startDate) {
        this.startDate = startDate;
    }

    @Override
    public DateTimeWrapper getEnd() {
        return endDate;
    }

    @Override
    public void setEnd(DateTimeWrapper endDate) {
        this.endDate = endDate;
    }

}
