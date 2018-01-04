package heap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;

public class ValueGenerator {
    private static Collection<Integer> elements;
    private static int ELEMENTS = 1500;
    private static int MAX = 1000000;

    public static Collection<Integer> generateNumbers() {
        if(elements==null){
            Random random = new Random(7823123);
            elements = Collections.unmodifiableCollection(
                    random.ints(ELEMENTS, 0, MAX)
                            .boxed()
                            .collect(Collectors.toList()
                            )
            );
        }
        return elements;
    }
}
