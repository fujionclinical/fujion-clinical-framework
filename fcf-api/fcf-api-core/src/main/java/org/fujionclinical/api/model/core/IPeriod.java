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

import org.fujion.common.DateTimeWrapper;

public interface IPeriod extends IBaseType {

    DateTimeWrapper getStart();

    default void setStart(DateTimeWrapper startDate) {
        notSupported();
    }

    default boolean hasStart() {
        return getStart() != null;
    }

    DateTimeWrapper getEnd();

    default void setEnd(DateTimeWrapper endDate) {
        notSupported();
    }

    default boolean hasEnd() {
        return getEnd() != null;
    }

    default boolean inRange(DateTimeWrapper date) {
        if (hasStart() && date.compareTo(getStart()) < 0) {
            return false;
        }

        if (hasEnd() && date.compareTo(getEnd()) >= 0) {
            return false;
        }

        return true;
    }

}
