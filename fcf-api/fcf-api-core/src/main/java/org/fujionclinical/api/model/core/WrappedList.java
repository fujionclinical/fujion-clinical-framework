package org.fujionclinical.api.model.core;

import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

public class WrappedList<T, W> implements List<T> {

    private final List<T> outerList = new ArrayList<>();

    private final List<W> innerList;

    private final IWrapperTransform<T, W> transform;

    public WrappedList(
            List<W> innerList,
            IWrapperTransform<T, W> transform) {
        this.innerList = innerList;
        this.transform = transform;
        outerList.addAll(innerList.stream().map(element -> transform.wrap(element)).collect(Collectors.toList()));
    }

    @Override
    public int size() {
        return outerList.size();
    }

    @Override
    public boolean isEmpty() {
        return outerList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return outerList.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return listIterator();
    }

    @Override
    public Object[] toArray() {
        return outerList.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return outerList.toArray(a);
    }

    @Override
    public boolean add(T element) {
        return _add(element);
    }

    @Override
    public boolean remove(Object element) {
        return _remove(element);
    }

    @Override
    public boolean removeAll(Collection<?> elements) {
        return _remove(elements.toArray());
    }

    @Override
    public boolean containsAll(Collection<?> elements) {
        return outerList.containsAll(elements);
    }

    @Override
    public boolean addAll(Collection<? extends T> elements) {
        return _add((T[]) elements.toArray());
    }

    @Override
    public boolean addAll(
            int index,
            Collection<? extends T> elements) {
        return _add(index, (T[]) elements.toArray());
    }

    @Override
    public boolean retainAll(Collection<?> elements) {
        Object[] remove = outerList.stream().filter(element -> !elements.contains(element)).toArray();
        return _remove(remove);
    }

    @Override
    public void clear() {
        validateLists();
        outerList.clear();
        innerList.clear();
    }

    @Override
    public T get(int index) {
        return outerList.get(index);
    }

    @Override
    public T set(
            int index,
            T element) {
        validateLists();
        outerList.set(index, element);
        innerList.set(index, transform.unwrap(element));
        return element;
    }

    @Override
    public void add(
            int index,
            T element) {
        _add(index, element);
    }

    @Override
    public T remove(int index) {
        T removed = outerList.get(index);
        return _remove(removed) ? removed : null;
    }

    @Override
    public int indexOf(Object o) {
        return outerList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return outerList.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return listIterator(0);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        validateLists();
        return new ListIterator<T>() {

            private ListIterator<T> outerIterator = outerList.listIterator(index);

            private ListIterator<W> innerIterator = innerList.listIterator(index);

            @Override
            public boolean hasNext() {
                return validate(outerIterator.hasNext(), innerIterator.hasNext());
            }

            @Override
            public T next() {
                innerIterator.next();
                return outerIterator.next();
            }

            @Override
            public boolean hasPrevious() {
                return validate(outerIterator.hasPrevious(), innerIterator.hasPrevious());
            }

            @Override
            public T previous() {
                innerIterator.previous();
                return outerIterator.previous();
            }

            @Override
            public int nextIndex() {
                return validate(outerIterator.nextIndex(), innerIterator.nextIndex());
            }

            @Override
            public int previousIndex() {
                return validate(outerIterator.previousIndex(), innerIterator.previousIndex());
            }

            @Override
            public void remove() {
                innerIterator.remove();
                outerIterator.remove();
            }

            @Override
            public void set(T element) {
                innerIterator.set(transform.unwrap(element));
                outerIterator.set(element);
            }

            @Override
            public void add(T element) {
                innerIterator.add(transform.unwrap(element));
                outerIterator.add(element);
            }

            private <T> T validate(
                    T value1,
                    T value2) {
                Assert.isTrue(value1 == value2, "Inner and outer lists no longer in sync.");
                return value1;
            }
        };
    }

    @Override
    public List<T> subList(
            int fromIndex,
            int toIndex) {
        return outerList.subList(fromIndex, toIndex);
    }

    private void validateLists() {
        Assert.isTrue(outerList.size() == innerList.size(), "Inner and outer lists have different sizes.");
    }

    private <E extends T> boolean _add(E... elements) {
        return _add(-1, elements);
    }

    private <E extends T> boolean _add(
            int index,
            E... elements) {
        validateLists();
        boolean result = false;

        for (E element : elements) {
            if (index < 0) {
                if (outerList.add(element)) {
                    result = true;
                    innerList.add(transform.unwrap(element));
                }
            } else {
                result = true;
                innerList.add(index, transform.unwrap(element));
                outerList.add(index++, element);
            }
        }

        return result;
    }

    private boolean _remove(Object... elements) {
        validateLists();
        boolean changed = false;

        for (Object element : elements) {
            int index = outerList.indexOf(element);

            if (index >= 0) {
                innerList.remove(index);
                changed = true;
            }
        }

        return changed;
    }

}
