package heap.leftist;

import heap.AbstractHeapTest;
import org.junit.Test;

import static org.junit.Assert.*;

public class LeftistHeapTest extends AbstractHeapTest<LeftistHeap> {

    public LeftistHeapTest() {
        super(LeftistHeap.class);
    }

    @Test
    public void insertTest() {
        testMethod(super::insert);
    }

    @Test
    public void removeMinTest() {
        testMethod(super::removeMin);
    }

    @Test
    public void updateTest(){
        testMethod(super::update);
    }

    @Test
    public void removeTest(){
        testMethod(super::remove);
    }
}