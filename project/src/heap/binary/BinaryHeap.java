package heap.binary;

import heap.*;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Binaire hoop die als array geïmplemteert wordt, we beginnen vanaf 1 te tellen om het vinden van kinderen intuïtiever te maken.
 * Voldoet aan de min-heap eigenschap (elke ouder is maximaal gelijk aan zijn kind).
 * Verder is deze hoop ook altijd een complete binaire boom.
 * @param <T> Het type van de Heap, wat Comparable moet zijn.
 */
public class BinaryHeap<T extends Comparable<T>> implements ExtendedHeap<T> {

    private int size;
    private Element[] heap;

    /**
     * We negeren hier de compilerwaarschuwing die gegenereerd wordt omdat door type Erasure het type van de elementen binnen het array niet vastgesteld kan worden.
     * Aangezien we dit array enkel intern gebruiken weten we dat we nooit het foute type gebruiken.
     * Door Array.newInstance() op te roepen weten we dat het array van het correcte type gemaakt wordt.
     */
    @SuppressWarnings("reflect")
    public BinaryHeap(){
        //noinspection unchecked
        this.heap = (Element[]) Array.newInstance(Element.class, 5);
        this.size = 0;
    }

    /**
     * We voegen een element toe onderaan de boom.
     * Zolang deze het element kleiner is dan zijn ouder wisselen we het nieuwe element met zijn ouder.
     * @param value Het element dat we willen toevoegen.
     * @return De referentie naar het element dat we toegevoegd hebben.
     */
    @Override
    public ComparableElement<T> insert(T value) {
        Element newVal = new Element(value, ++size);
        if(size>=heap.length-1){
            heap = Arrays.copyOf(heap, heap.length * 2);
        }
        heap[size] = newVal;
        moveUp(size, false);

        return newVal;
    }

    /**
     * We zoeken het kleinste element, gezien de heap eigenschap is dit altijd de wortel van de boom, die zich op index 1 bevindt.
     * @return Het kleinste element van de hoop.
     * @throws EmptyHeapException De hoop is leeg.
     */
    @Override
    public Element findMin() throws EmptyHeapException {
        if(isEmpty()){
            throw new EmptyHeapException();
        }

        return heap[1];
    }

    /**
     * Dankzij het feit dat de integerdeling naar 0 afrondt kunnen we eenvoudig de ouder van een kind vinden door zijn index door 2 te delen.
     * @param index De index van het kind.
     * @return De index van de ouder.
     */
    private int parent(int index){
        return index / 2;
    }

    /**
     * Kijk of de ouder een geldig element binnen het array is, indien niet heeft het kind geen ouder.
     * @param index De index van het kind.
     * @return Of de ouder een kind heeft.
     */
    private boolean hasParent(int index){
        return parent(index) > 0;
    }

    /**
     * We zoeken het linkerkind van een ouder, aangezien we een complete binaire boom hebben en elke ouder 2 kinderen heeft bevindt dit zich 2* zo ver.
     * @param index De index van de ouder.
     * @return De index van het linkerkind.
     */
    private int left(int index){
        return index * 2;
    }

    /**
     * Indien de index van het linkerkind buiten het array ligt heeft deze ouder geen linkerkind.
     * @param index De index van de ouder.
     * @return Of de ouder een linkerkind heeft.
     */
    private boolean hasLeft(int index){
        return left(index)<=size;
    }

    /**
     * Het rechterkind bevindt zich altijd naast het linkerkind in deze implementatie.
     * @param index De index van de ouder.
     * @return De index van het rechterkind.
     */
    private int right(int index){
        return index * 2 + 1;
    }

    /**
     * Indien de index van het rechterkind buiten het array ligt heeft deze ouder geen rechterkind.
     * @param index De index van de ouder.
     * @return Of de ouder een rechterkind heeft.
     */
    private boolean hasRight(int index){
        return right(index) <= size;
    }

    /**
     * We herstellen de hoop door de ouder met het kind om te wisselen tot er terug aan de heapvoorwaarde voldaan wordt.
     * In het slechtste geval wordt de wortel naar een blad verplaatst, dus dit heeft complexiteit O(log(n)).
     * @param i Index van het te verplaatsen element.
     */
    private void fixHeap(int i){
        while (hasLeft(i)){ //the element can only have a right child if it has a left child
            int min = left(i);
            if(hasRight(i)&&heap[left(i)].compareTo(heap[right(i)]) > 0){
                min = right(i);
            }
            if(heap[i].compareTo(heap[min])>0){
                swap(i, min);
            } else {
                break;
            }
            i = min;
        }
    }

    /**
     * We bewegen het element naar boven tot er aan de heapvoorwaarde voldaan wordt.
     * In het slechtste geval wordt een blad naar de wortel verplaatst, dus dit heeft complexiteit O(log(n)).
     * @param i Index van het te verplaatsen element.
     * @param delete We hebben de bedoeling om het element te verwijderen, we bewegen het element naar de wortel ongeacht de heapvoorwaarde.
     */
    private void moveUp(int i, boolean delete){
        while (hasParent(i)&& (heap[parent(i)].compareTo(heap[i])>0||delete)){
            swap(i, parent(i));
            i = parent(i);
        }
    }

    /**
     * We verwijderen het kleinste element van de heap en verplaatsen het laatste blad van de complete boom naar de wortel.
     * Nadien herstellen we de heapvoorwaarde.
     * @return De waarde van het verwijderde element.
     * @throws EmptyHeapException De heap is leeg.
     */
    @Override
    public T removeMin() throws EmptyHeapException {
        if(isEmpty()){
            throw new EmptyHeapException();
        }

        Element ret = findMin();

        heap[1] = heap[size];
        heap[1].setIndex(1);
        heap[size--] = null;
        fixHeap(1);

        return ret.value();
    }

    /**
     * Hulpmethode voor het omwisselen van 2 elementen en het fixen van de referenties naar het Element.
     * O(1)
     * @param pos1 De index van het eerste te wisselen element.
     * @param pos2 De index van het tweede te wisselen element.
     */
    private void swap(int pos1, int pos2){
        Element tmp = heap[pos1];

        heap[pos1] = heap[pos2];
        heap[pos1].setIndex(pos1);

        heap[pos2] = tmp;
        tmp.setIndex(pos2);
    }

    /**
     * Print de heap in een human readable vorm.
     */
    @Override
    public void printHeap(){
        String out = Arrays.stream(heap).filter(Objects::nonNull).map(i->i.value().toString()).collect(Collectors.joining(", "));
        System.out.println(out);
    }

    /**
     * @return Een human readable naam van de hoop.
     */
    @Override
    public String getName() {
        return "Binary Heap";
    }

    /**
     * @return Of de heap leeg is.
     */
    private boolean isEmpty(){
        return size<1;
    }

    private boolean _isValid(int i) {
        if (i * 2 > size) {
            return true;
        }
        if (i * 2 + 1 > size) {
            return _isValid(i * 2);
        }
        return heap[i * 2].compareTo(heap[i]) >= 0 && heap[i * 2 + 1].compareTo(heap[i]) >= 0 && _isValid(i * 2) && _isValid(i * 2 + 1);
    }

    /**
     * We controleren of elk kind minstens gelijk aan zijn ouder is.
     * @return Of de heap aan alle voorwaarden voldoet.
     */
    @Override
    public boolean isValid(){
        return _isValid(1);
    }

    public boolean contains(ComparableElement<T> comp) {
        for (ComparableElement<T> e : heap) {
            if(comp.equals(e)){
                return true;
            }
        }
        return false;
    }

    /**
     * Het Element dat een voorstelling is van een heapelement.
     * We houden telkens zijn locatie binnen het array bij zodat we aanpassingen eenvoudig kunnen doorvoeren.
     */
    private class Element extends ComparableElement<T>{
        private int index;
        private Element(T value, int index){
            super(value);
            this.index = index;
        }

        /**
         * We bewegen het element naar de wortel van de boom (ongeacht de heapeigenschap).
         * Nadien verwijderen we het "kleinste" element, wat in dit geval dit element is aangezien het zich in de wortel bevindt.
         * @param delete Geeft het element tijdelijk prioriteit -Infinity.
         */
        @Override
        public void remove() {
            moveUp(index, true);
            try {
                removeMin();
            } catch (EmptyHeapException e) {
                //Er is minstens 1 element in de heap (nl. dit element)
                e.printStackTrace();
            }
        }

        /**
         * Als de nieuwe waarde groter is dan de oude waarde kan het zijn dat een kind kleiner is da de nieuwe waarde. We bewegen het element naar beneden.
         * In het andere geval kan het zijn dat de waarde naar boven verplaatst moet worden.
         * @param value de nieuwe waarde voor het element.
         */
        @Override
        public void update(T value) {
            if(this.value.compareTo(value)<0){
                this.value = value;
                fixHeap(index);
            } else {
                this.value = value;
                moveUp(index, false);
            }
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
}
