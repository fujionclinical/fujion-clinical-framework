package org.fujionclinical.api.model;

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
        CollectionUtil.replaceList(getContactPoints(), contactPoints);
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
