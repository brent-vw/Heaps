package heap;

import heap.binary.BinaryHeap;
import heap.binomial.BinomialHeap;
import heap.helpers.TriConsumer;
import heap.leftist.LeftistHeap;
import org.junit.Assume;
import org.junit.Test;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

import static org.junit.Assert.assertTrue;

public abstract class AbstractHeapTest<T extends Heap> {
    private static Supplier<ExtendedHeap<Integer>> testHeap;
    private static Map<Class<? extends Heap>, Supplier<ExtendedHeap<Integer>>> generators;
    private Collection<Integer> elements = ValueGenerator.generateNumbers();

    protected AbstractHeapTest(Class<T> tClass){
        testHeap = getGenerators().get(tClass);
    }

    private static Map<Class<? extends Heap>, Supplier<ExtendedHeap<Integer>>> getGenerators(){
        if(generators==null){
            HashMap<Class<? extends Heap>, Supplier<ExtendedHeap<Integer>>> _generators = new HashMap<>();
            _generators.put(BinaryHeap.class, Heaps::newBinaryHeap);
            _generators.put(BinomialHeap.class, Heaps::newBinomialHeap);
            _generators.put(LeftistHeap.class, Heaps::newLeftistHeap);
//            _generators.put(PairingHeap.class, Heaps::newPairingHeap);
//            _generators.put(SkewHeap.class, Heaps::newSkewHeap);
            generators = Collections.unmodifiableMap(_generators);
        }
        return generators;
    }

    @SafeVarargs
    private final void testAssertions(ExtendedHeap<Integer> heap, ComparableElement<Integer> assertee, String action, BiPredicate<ExtendedHeap<Integer>, ComparableElement<Integer>>... assertions){
        assertTrue("Heap was not valid after "+action+" element: "+assertee.value(), heap.isValid());
        for (BiPredicate<ExtendedHeap<Integer>, ComparableElement<Integer>> assertion : assertions) {
            assertTrue("Heap did not satisfy additional assertions after "+action+" element: "+assertee.value(), assertion.test(heap, assertee));
        }
    }

    private Entry<ExtendedHeap<Integer>, Collection<ComparableElement<Integer>>> newHeap(){
        return newHeap(false);
    }

    private Entry<ExtendedHeap<Integer>, Collection<ComparableElement<Integer>>> newHeap(boolean testValidity){
        ExtendedHeap<Integer> heap = testHeap.get();
        ArrayList<ComparableElement<Integer>> entries = new ArrayList<>();
        for (int element : elements) {
            ComparableElement<Integer> heapEntry = (ComparableElement<Integer>) heap.insert(element);
            if(testValidity) {
                testAssertions(heap, heapEntry, "inserting");
            }
            entries.add(heapEntry);
        }
        return new AbstractMap.SimpleEntry<>(heap, entries);
    }

    @SafeVarargs
    protected final void testMethod(TriConsumer<ExtendedHeap<Integer>,
            Collection<ComparableElement<Integer>>,
            BiPredicate<ExtendedHeap<Integer>, ComparableElement<Integer>>[]> consumerMethod,
                                    BiPredicate<ExtendedHeap<Integer>, ComparableElement<Integer>>... assertions)
    {
        Entry<ExtendedHeap<Integer>, Collection<ComparableElement<Integer>>> tuple = newHeap();
        consumerMethod.accept(tuple.getKey(), tuple.getValue(), assertions);
    }

    @SafeVarargs
    public final void insert(ExtendedHeap<Integer> _heap, Collection<ComparableElement<Integer>> _element, BiPredicate<ExtendedHeap<Integer>, ComparableElement<Integer>>... assertions) {
        ExtendedHeap<Integer> heap = newHeap(true).getKey();
        assertTrue("The heap did not have the expected size. Expected: "+elements.size()+", actual: "+heap.getSize(), heap.getSize()==elements.size()||heap.getSize()==-1);
    }

    @SafeVarargs
    private final void incrementKeys(ExtendedHeap<Integer> heap, Collection<ComparableElement<Integer>> comps,
                               BiPredicate<ExtendedHeap<Integer>, ComparableElement<Integer>>...assertions){
        for (ComparableElement<Integer> comp : comps){
            int val = comp.value() + comp.value()/2;
            comp.update(val);
            assertTrue("The heap did not have the expected size. Expected: "+elements.size()+", actual: "+heap.getSize(), heap.getSize()==elements.size()||heap.getSize()==-1);
            testAssertions(heap,comp, "updating with increments", assertions);

        }
    }

    @SafeVarargs
    private final void decrementKeys(ExtendedHeap<Integer> heap, Collection<ComparableElement<Integer>> comps,
                                     BiPredicate<ExtendedHeap<Integer>, ComparableElement<Integer>>... assertions){
        for (ComparableElement<Integer> comp : comps){
            int val = comp.value() - comp.value()/2;
            comp.update(val);
            assertTrue("The heap did not have the expected size. Expected: "+elements.size()+", actual: "+heap.getSize(), heap.getSize()==elements.size()||heap.getSize()==-1);
            testAssertions(heap,comp, "updating with decrements", assertions);
        }
    }

    @SafeVarargs
    public final void update(ExtendedHeap<Integer> heap, Collection<ComparableElement<Integer>> comps, BiPredicate<ExtendedHeap<Integer>,
            ComparableElement<Integer>>... assertions){
        incrementKeys(heap, comps, assertions);
        testMethod(this::decrementKeys, assertions);
    }

    @SafeVarargs
    public final void remove(ExtendedHeap<Integer> heap, Collection<ComparableElement<Integer>> comps, BiPredicate<ExtendedHeap<Integer>,
            ComparableElement<Integer>>... assertions){
        int size = elements.size();
        for (ComparableElement<Integer> comp : comps){
            comp.remove();
            size--;
            assertTrue("The heap did not have the expected size. Expected: "+size+", actual: "+heap.getSize(), heap.getSize()==size||heap.getSize()==-1);
            testAssertions(heap, comp, "removing", assertions);
        }
    }

    @SafeVarargs
    public final void removeMin(ExtendedHeap<Integer> heap, Collection<ComparableElement<Integer>> comps, BiPredicate<ExtendedHeap<Integer>,
            ComparableElement<Integer>>... assertions) {
        ArrayList<Integer> removed = new ArrayList<>();
        try {
            for (int i = 0; i < elements.size(); i++) {
                ComparableElement<Integer> comp = (ComparableElement<Integer>) heap.findMin();
                removed.add(heap.removeMin());
                testAssertions(heap, comp, "removing", assertions);
            }
            assertTrue(elements.containsAll(removed) && elements.size() == removed.size());
            for (int i = 0; i < removed.size() - 1; i++) {
                int first = removed.get(i);
                int second = removed.get(i + 1);
                assertTrue( first + " is not smaller than " + second, first <= second);

            }
        } catch (EmptyHeapException e){
            throw new AssertionError("Heap threw EmptyheapException while this was not expected.");
        }
    }

    @Test
    public void isValid(){
        Assume.assumeTrue( false);
    }
}
