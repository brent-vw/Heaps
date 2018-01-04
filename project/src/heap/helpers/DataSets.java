package heap.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class DataSets {
    private static final int SEED = 987711;

    public static List<Integer> generateRandomNumbers(int amount){
        List<Integer> numbers = new ArrayList<>();
        Random random = new Random(SEED);
        int max = amount*10;
        for (int i = 0; i<amount; i++){
            numbers.add(random.nextInt(max));
        }
        return numbers;
    }

    public static List<Integer> generateIncreasingNumbers(int amount){
        List<Integer> numbers = new ArrayList<>();
        for (int i=0; i<amount; i++){
            numbers.add(i);
        }
        return numbers;
    }

    public static List<Integer> generateDecreasingNumbers(int amount){
        List<Integer> numbers = new ArrayList<>();
        for (int i=amount; i>0; i--){
            numbers.add(i);
        }
        return numbers;
    }

    public enum Type {
        Random(DataSets::generateRandomNumbers),
        Increasing(DataSets::generateIncreasingNumbers),
        Decreasing(DataSets::generateDecreasingNumbers);

        private Function<Integer, List<Integer>> generator;

        Type(Function<Integer,List<Integer>> generator) {
            this.generator = generator;
        }

        public Function<Integer, List<Integer>> getGenerator() {
            return generator;
        }
    }
}
