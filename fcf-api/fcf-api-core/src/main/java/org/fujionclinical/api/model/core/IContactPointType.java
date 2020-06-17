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

import org.fujion.common.CollectionUtil;

import java.util.Collections;
import java.util.List;

public interface IContactPointType {

    /**
     * Returns all contact points for the entity.
     *
     * @return A list of all contact points (never null)
     */
    default List<IContactPoint> getContactPoints() {
        return Collections.emptyList();
    }

    default void setContactPoints(List<IContactPoint> contactPoints) {
        CollectionUtil.replaceElements(getContactPoints(), contactPoints);
    }

    /**
     * Returns the entity's contact points from one of the specified uses.  Uses are
     * searched in order until a match is found.
     *
     * @param uses Only contact points belonging to one of these uses will be returned.
     * @return The entity's contact, or null if not found.
     */
    default IContactPoint getContactPoint(IContactPoint.ContactPointUse... uses) {
        return CollectionUtil.findMatch(getContactPoints(), (contactPoint, use) ->
                contactPoint.getUse() == use, uses);
    }

    /**
     * Returns the entity's contact points from one of the specified systems.  Systems are
     * searched in order until a match is found.
     *
     * @param systems Only contact points belonging to one of these systems will be returned.
     * @return The entity's contact, or null if not found.
     */
    default IContactPoint getContactPoint(IContactPoint.ContactPointSystem... systems) {
        return CollectionUtil.findMatch(getContactPoints(), (contactPoint, system) ->
                contactPoint.getSystem() == system, systems);
    }

    /**
     * Returns the entity's home phone.
     *
     * @return The entity's home phone, or null if not found.
     */
    default IContactPoint getHomePhone() {
        return getContactPoint(IContactPoint.ContactPointUse.HOME, IContactPoint.ContactPointSystem.PHONE);
    }

    /**
     * Returns the entity's contact point matching the specified use and system.
     *
     * @param use    The contact point use being sought.
     * @param system The contact point system being sought.
     * @return The entity's contact point, or null if not found.
     */
    default IContactPoint getContactPoint(
            IContactPoint.ContactPointUse use,
            IContactPoint.ContactPointSystem system) {
        return CollectionUtil.findMatch(getContactPoints(), contactPoint ->
                contactPoint.getUse() == use && contactPoint.getSystem() == system);
    }

    default void addContactPoints(IContactPoint... contactPoints) {
        Collections.addAll(getContactPoints(), contactPoints);
    }

    default boolean hasContactPoint() {
        return CollectionUtil.notEmpty(getContactPoints());
    }

}
