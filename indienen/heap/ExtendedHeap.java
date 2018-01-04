package heap;

public interface ExtendedHeap<T extends Comparable<T>> extends Heap<T>{
    default String getName(){
        return "Undefined";
    }
    default void printHeap(){

    }
    default boolean isValid(){
        return false;
    }
    default int getSize(){
        return -1;
    }
}
