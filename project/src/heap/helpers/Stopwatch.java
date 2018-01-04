package heap.helpers;

public class Stopwatch {
    private static Long startTime;

    public static void start(){
        startTime = System.currentTimeMillis();
    }

    public static Long end(){
        Long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
}
