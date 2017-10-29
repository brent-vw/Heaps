package heap;

import heap.binary.BinaryHeap;
import heap.binomial.BinomialHeap;
import heap.leftist.LeftistHeap;
import heap.skew.SkewHeap;
import heap.pairing.PairingHeap;

public class Heaps {
    public static <T extends Comparable<T>> BinaryHeap<T> newBinaryHeap() {
	    return new BinaryHeap<>();
    }

    public static <T extends Comparable<T>> BinomialHeap<T> newBinomialHeap() {
	    return new BinomialHeap<>();
    }

    public static <T extends Comparable<T>> LeftistHeap<T> newLeftistHeap() {
	    return new LeftistHeap<>();
    }

    public static <T extends Comparable<T>> SkewHeap<T> newSkewHeap() {
	    return new SkewHeap<>();
    }

    public static <T extends Comparable<T>> PairingHeap<T> newPairingHeap() {
	    return new PairingHeap<>();
    }
}
