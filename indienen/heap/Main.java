package heap;

import heap.helpers.BenchmarkBuilder;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws EmptyHeapException, IOException {
        int k10 = 10000;
        int k500 = 500000;
        int k200 = 200000;
        int k100 = 100000;
        int mil1 = 1000000;
        Long result = new BenchmarkBuilder()
                .amount(k500)
                .pairing()
                .random()
                .testUpdateRemove()
                .build().run();
        System.out.println(result);
    }
}
