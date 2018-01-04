package heap.pairing;

import heap.AbstractHeapTest;
import heap.EmptyHeapException;
import org.junit.Test;

public class PairingHeapTest extends AbstractHeapTest<PairingHeap> {
    public PairingHeapTest() {
        super(PairingHeap.class);
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
