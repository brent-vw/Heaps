package heap.skew;

import heap.AbstractHeapTest;
import heap.EmptyHeapException;
import org.junit.Test;

public class SkewHeapTest extends AbstractHeapTest<SkewHeap> {
    public SkewHeapTest() {
        super(SkewHeap.class);
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
        testMethod(super::remove);
    }

    @Test
    public void removeMinTest() throws EmptyHeapException {
        testMethod(super::removeMin);
    }
}
