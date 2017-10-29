package heap.skew;

import heap.Element;
import heap.EmptyHeapException;
import heap.ExtendedHeap;
import heap.Heap;

public class SkewHeap<T extends Comparable<T>> implements ExtendedHeap<T> {
    @Override
    public Element<T> insert(T value) {
        return null;
    }

    @Override
    public Element<T> findMin() throws EmptyHeapException {
        return null;
    }

    @Override
    public T removeMin() throws EmptyHeapException {
        return null;
    }
}
