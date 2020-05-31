package org.fujionclinical.api.location;

import org.apache.commons.collections.CollectionUtils;
import org.fujion.common.CollectionUtil;
import org.fujionclinical.api.model.IConcept;
import org.fujionclinical.api.model.IContactPointType;
import org.fujionclinical.api.model.IDomainObject;
import org.fujionclinical.api.model.IPostalAddressType;

import java.util.Collections;
import java.util.List;

public interface ILocation extends IDomainObject, IPostalAddressType, IContactPointType {

    enum LocationType {}

    enum LocationStatus {ACTIVE, SUSPENDED, INACTIVE}

    /**
     * The activity status.
     *
     * @return The status.
     */
    LocationStatus getStatus();

    default void setStatus(LocationStatus status) {
        throw new UnsupportedOperationException();
    }

    default boolean hasStatus() {
        return getStatus() != null;
    }

    /**
     * Name of the location as used by humans.
     *
     * @return The name.
     */
    String getName();

    default void setName(String name) {
        throw new UnsupportedOperationException();
    }

    default boolean hasName() {
        return getName() != null;
    }

    /**
     * A list of alternate names that the location is known as, or was known as, in the past.
     *
     * @return List of aliases.
     */
    default List<String> getAliases() {
        return Collections.emptyList();
    }

    default void setAliases(List<String> aliases) {
        CollectionUtil.replaceList(getAliases(), aliases);
    }

    default void addAliases(String... aliases) {
        CollectionUtils.addAll(getAliases(), aliases);
    }

    default boolean hasAlias() {
        return CollectionUtil.notEmpty(getAliases());
    }

    /**
     * Additional details about the location that could be displayed as further information to identify the location
     * beyond its name.
     *
     * @return The description.
     */
    default String getDescription() {
        return null;
    }

    default void setDescription(String description) {
        throw new UnsupportedOperationException();
    }

    default boolean hasDescription() {
        return getDescription() != null;
    }

    /**
     * Type of functions performed.
     *
     * @return The types.
     */
    default List<IConcept> getTypes() {
        return Collections.emptyList();
    }

    default void setTypes(List<IConcept> types) {
        CollectionUtil.replaceList(getTypes(), types);
    }

    default void addTypes(IConcept... types) {
        CollectionUtils.addAll(getTypes(), types);
    }

    default boolean hasType() {
        return CollectionUtil.notEmpty(getTypes());
    }

}
