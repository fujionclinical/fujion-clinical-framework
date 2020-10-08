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
package org.fujionclinical.api.model.core;

import edu.utah.kmm.model.cool.core.datatype.Period;

import java.time.LocalDateTime;

public interface IPeriod extends Period {

    default boolean hasStart() {
        return getStart() != null;
    }

    default boolean hasEnd() {
        return getEnd() != null;
    }

    @Override
    default boolean hasIncludeLowerBound() {
        return getIncludeLowerBound() != null;
    }

    @Override
    default boolean hasIncludeUpperBound() {
        return getIncludeUpperBound() != null;
    }

    default boolean inRange(LocalDateTime date) {
        boolean includeLower = hasIncludeLowerBound() && getIncludeLowerBound();
        boolean includeUpper = hasIncludeUpperBound() && getIncludeUpperBound();

        if (hasStart() && date.compareTo(getStart()) < (includeLower ? 1 : 0)) {
            return false;
        }

        if (hasEnd() && date.compareTo(getEnd()) > (includeUpper ? -1 : 0)) {
            return false;
        }

        return true;
    }

}
