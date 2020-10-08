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

import org.fujionclinical.api.model.core.IPeriod;

import java.time.LocalDateTime;

public class Period implements IPeriod {

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Boolean includeLowerBound;

    private Boolean includeUpperBound;

    public Period() {
    }

    public Period(
            LocalDateTime startDate,
            LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public LocalDateTime getStart() {
        return startDate;
    }

    @Override
    public void setStart(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    @Override
    public LocalDateTime getEnd() {
        return endDate;
    }

    @Override
    public void setEnd(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    @Override
    public Boolean getIncludeLowerBound() {
        return includeLowerBound;
    }

    @Override
    public void setIncludeLowerBound(Boolean includeLowerBound) {
        this.includeLowerBound = includeLowerBound;
    }

    @Override
    public Boolean getIncludeUpperBound() {
        return includeUpperBound;
    }

    @Override
    public void setIncludeUpperBound(Boolean includeUpperBound) {
        this.includeUpperBound = includeUpperBound;
    }

}
