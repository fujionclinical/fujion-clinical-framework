package org.fujionclinical.api.model.core;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Transform between logical and native models.
 *
 * @param <L> The logical model type.
 * @param <W> The type of the native (wrapped) object.
 */
public interface IWrapperTransform<L, W> {

    W _unwrap(L value);

    default W unwrap(L value) {
        return value == null ? null : value instanceof IWrapper ? ((IWrapper<W>) value).getWrapped() : _unwrap(value);
    }

    default List<W> unwrap(List<L> values) {
        return values == null ? null : values.stream().map(value -> unwrap(value)).collect(Collectors.toList());
    }

    L _wrap(W value);

    default L wrap(W value) {
        return value == null ? null : _wrap(value);
    }

    default List<L> wrap(List<W> values) {
        return values == null ? null : new WrappedList<L, W>(values, this);
    }
}
