package heap.helpers;

import heap.Element;
import heap.Heap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

public class Benchmark {
    private Heap<Integer> heap;
    private List<Integer> values;
    private List<Element<Integer>> elements;
    private List<BiFunction<Heap<Integer>,Element<Integer>, Element<Integer>>> operations;
    private int seed = 1234567;
    private boolean random;
    private boolean insert;

    Benchmark(Heap<Integer> heap, List<Integer> values, List<BiFunction<Heap<Integer>,Element<Integer>, Element<Integer>>> operations, boolean random, boolean insert) {
        this.heap = heap;
        this.values = values;
        elements = new ArrayList<>(values.size());
        this.operations = operations;
        this.random = random;
        this.insert = insert;
    }

    private Long addBenchmark(){
        Stopwatch.start();

        for (Integer value : values) {
            elements.add(heap.insert(value));
        }

        return Stopwatch.end();
    }

    private void add(){
        for (Integer value : values) {
            elements.add(heap.insert(value));
        }
    }

    public Long run(){
        if(insert){
            return addBenchmark();
        }

        if(elements.isEmpty()){
            add();
        }

        if(random){
            return runRandom();
        } else {
            return runInOrder();
        }
    }

    private Long runRandom(){
        Random random = new Random(seed);

        Stopwatch.start();
        for (BiFunction<Heap<Integer>, Element<Integer>, Element<Integer>> operation : operations) {
            Element<Integer> el = elements.get(random.nextInt(elements.size()));
            Element<Integer> result = operation.apply(heap,  elements.get(random.nextInt(elements.size())));
            if(result!=null){
                elements.remove(result);
            }
        }

        return Stopwatch.end();
    }

    private Long runInOrder(){
        Stopwatch.start();

        for (int i = 0; i < operations.size()-1; i++) {
            int index = i%elements.size();
            Element<Integer> el = elements.get(index);
            Element<Integer> result = operations.get(i).apply(heap,  el);
            if(result!=null){
                elements.remove(result);
            }
        }

        return Stopwatch.end();
    }
}
