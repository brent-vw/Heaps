package heap.binary;

import heap.AbstractHeapTest;
import heap.EmptyHeapException;
import org.junit.Test;

import static org.junit.Assert.*;

public class BinaryHeapTest extends AbstractHeapTest<BinaryHeap> {
    public BinaryHeapTest() {
        super(BinaryHeap.class);
    }

    @Test
    public void insertTest() {
        testMethod(super::insert);
    }

    @Test
    public void updateTest(){
        testMethod(super::update);
    }

    @Test
    public void removeTest(){
        testMethod(super::remove, (h,cp)->!((BinaryHeap<Integer>) h).contains(cp));
    }

    @Test
    public void removeMinTest() throws EmptyHeapException {
        testMethod(super::removeMin);
    }
}