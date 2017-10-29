package heap;

public abstract class ComparableElement<T extends Comparable<T>> implements Element<T>, Comparable<ComparableElement<T>>{

    protected T value;

    public ComparableElement(T value){
        this.value = value;
    }

    @Override
    public T value() {
        return value;
    }

    @Override
    public int compareTo(ComparableElement<T> o) {
        return value.compareTo(o.value());
    }

    @Override
    public String toString(){
        return value.toString();
    }

}
