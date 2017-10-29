package heap.binomial;

import heap.AbstractHeapTest;
import org.junit.Test;

import static org.junit.Assert.*;

public class BinomialHeapTest extends AbstractHeapTest<BinomialHeap> {
    public BinomialHeapTest() {
        super(BinomialHeap.class);
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