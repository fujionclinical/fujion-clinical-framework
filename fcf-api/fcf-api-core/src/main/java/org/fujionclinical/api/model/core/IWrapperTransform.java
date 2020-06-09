package org.fujionclinical.api.model.core;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Transform between logical and native models.
 *
 * @param <L> The logical model type.
 * @param <N> The native (wrapped) model type.
 */
public interface IWrapperTransform<L, N> {

    /**
     * Converts a logical model instance to native form.  Override if the default implementation
     * is not sufficient.
     *
     * @param value The logical model instance.
     * @return The native form.
     */
    default N _unwrap(L value) {
        N wrapped = newWrapped();
        L wrapper = wrap(wrapped);
        BeanUtils.copyProperties(value, wrapper);
        return wrapped;
    }

    /**
     * Converts a logical model instance to native form. Handles nulls and the special case where
     * the logical model instance is a wrapper.  Do not override the default implementation.
     * Rather, override the {@link #_unwrap} method instead.
     *
     * @param value The logical model instance.
     * @return The native form (null if the input is null).
     */
    default N unwrap(L value) {
        return value == null ? null : value instanceof IWrapper ? ((IWrapper<N>) value).getWrapped() : _unwrap(value);
    }

    /**
     * Converts a list of logical model instances to native form.
     * Do not override the default implementation.
     *
     * @param values A list of logical model instances.
     * @return The corresponding list of native model instances (null if the input is null).
     */
    default List<N> unwrap(List<L> values) {
        return values == null ? null : values.stream().map(value -> unwrap(value)).collect(Collectors.toList());
    }

    /**
     * Converts a native model instance to logical model form.
     *
     * @param value The native model instance.
     * @return The logical model form.
     */
    L _wrap(N value);

    /**
     * Converts a native model instance to logical model form.
     * Do not override the default implementation.
     *
     * @param value The native model instance.
     * @return The logical model form (null if the input is null).
     */
    default L wrap(N value) {
        return value == null ? null : _wrap(value);
    }

    /**
     * Converts a list of native model instances to logical model form.
     * Do not override the default implementation.
     *
     * @param values A list of native model instances.
     * @return The corresponding list of logical model instances (null if the input is null).
     */
    default List<L> wrap(List<N> values) {
        return values == null ? null : new WrappedList<L, N>(values, this);
    }

    /**
     * Returns a new native model instance.
     *
     * @return A new native model instance.
     */
    N newWrapped();

}
