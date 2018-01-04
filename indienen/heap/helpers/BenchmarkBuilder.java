package heap.helpers;

import heap.Element;
import heap.EmptyHeapException;
import heap.Heap;
import heap.Heaps;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;


public class BenchmarkBuilder {
    private static final ArrayList<BiFunction<Heap<Integer>,Element<Integer>, Element<Integer>>> items;
    static {
        ArrayList<BiFunction<Heap<Integer>,Element<Integer>, Element<Integer>>> predicates = new ArrayList<>();
        predicates.add((h, e)->{
            Element<Integer> el = null;
            try {
                el = h.findMin();
                h.removeMin();
            } catch (EmptyHeapException e1) {
                e1.printStackTrace();
            }
            return el;
        });

        predicates.add((h, e)->{
            e.remove();
            return e;
        });

        predicates.add((h, e)->{
            e.update(e.value()/2);
            return null;
        });

        predicates.add((h, e)->{
            e.update(e.value()*2);
            return null;
        });

        items = predicates;
    }
    private Heap<Integer> heap;
    private DataSets.Type type;
    private int amount = 10000;
    private boolean removeMin;
    private boolean random;
    private boolean insert;
    private boolean notInOrder;
    private boolean remove;
    private boolean decrease;
    private boolean increase;

    public BenchmarkBuilder binary(){
        heap = Heaps.newBinaryHeap();
        return this;
    }

    public BenchmarkBuilder binomial(){
        heap = Heaps.newBinomialHeap();
        return this;
    }

    public BenchmarkBuilder leftist(){
        heap = Heaps.newLeftistHeap();
        return this;
    }

    public BenchmarkBuilder pairing(){
        heap = Heaps.newPairingHeap();
        return this;
    }

    public BenchmarkBuilder skew(){
        heap = Heaps.newSkewHeap();
        return this;
    }

    public BenchmarkBuilder random(){
        type = DataSets.Type.Random;
        return this;
    }

    public BenchmarkBuilder increasing(){
        type = DataSets.Type.Increasing;
        return this;
    }

    public BenchmarkBuilder decreasing(){
        type = DataSets.Type.Decreasing;
        return this;
    }

    public BenchmarkBuilder amount(int amount){
        this.amount = amount;
        return this;
    }

    public BenchmarkBuilder testRemoveMin(){
        resetOptions();
        removeMin = true;
        return this;
    }

    public BenchmarkBuilder testRandomOperation(){
        resetOptions();
        random = true;
        return this;
    }

    public BenchmarkBuilder testInsert(){
        resetOptions();
        insert = true;
        return this;
    }

    public BenchmarkBuilder testUpdateIncrease(){
        resetOptions();
        increase = true;
        return this;
    }

    public BenchmarkBuilder testUpdateDecrease(){
        resetOptions();
        decrease = true;
        return this;
    }

    public BenchmarkBuilder testUpdateRemove(){
        resetOptions();
        remove = true;
        return this;
    }

    private void resetOptions(){
        random = false;
        removeMin = false;
        insert = false;
        remove = false;
        decrease = false;
        increase = false;
    }

    public BenchmarkBuilder testInOrder(){
        notInOrder = false;
        return this;
    }

    public BenchmarkBuilder testInRandomOrder(){
        notInOrder = true;
        return this;
    }

    private void addOptions(int number, List<BiFunction<Heap<Integer>,Element<Integer>, Element<Integer>>> operations){
        for (int i=0; i<amount; i++){
            operations.add(items.get(number));
        }
    }

    public Benchmark build(){
        if (heap == null) {
            binary();
        }

        List<BiFunction<Heap<Integer>,Element<Integer>, Element<Integer>>> operations = new ArrayList<>(amount);
        if(random){
//            Random random = new Random(187982);
            Random random = new Random();
            for (int i=0; i<amount; i++){
                int index = random.nextInt(items.size());
                operations.add(items.get(index));
            }
        }

        if(removeMin){
            addOptions(0, operations);
        }

        if(remove){
            addOptions(1, operations);
        }

        if(decrease){
            addOptions(2, operations);
        }

        if(increase){
            addOptions(3, operations);
        }

        List<Integer> items = type.getGenerator().apply(amount);
        return new Benchmark(heap, items, operations, notInOrder, insert);
    }
}
