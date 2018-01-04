package heap.skew;

import heap.*;

import java.util.HashMap;

/**
 * Skew heap.
 * Voldoet aan de min-heap eigenschap.
 * Elke top met een rechterkind heeft ook een linkerkind.
 * @param <T>
 */
public class SkewHeap<T extends Comparable<T>> implements ExtendedHeap<T> {
    private SkewNode root;
    /**
     * Map die elementen linkt aan hun locatie in de heap.
     */
    private HashMap<Element, SkewNode> refs;

    public SkewHeap(){
        refs = new HashMap<>();
    }

    /**
     * Maak een nieuwe node aan en merge deze met de bestaande heap.
     * O(log n) gearmortiseerd.
     * @param value De waarde van het toe te voegen element.
     * @return Referentie naar het toegevoegde element.
     */
    @Override
    public ComparableElement<T> insert(T value) {
        //Maak een nieuwe element aan
        Element element = new Element(value);
        SkewNode newNode = new SkewNode(element);
        refs.put(element, newNode);

        //Merge met de wortel
        root = merge(newNode, root);

        return newNode.element;
    }

    /**
     * Zoek het kleinste element. Dit is altijd de wortel.
     * @return Referentie naar het kleinste element.
     * @throws EmptyHeapException De heap is leeg.
     */
    @Override
    public ComparableElement<T> findMin() throws EmptyHeapException {
        if(isEmpty()){
            throw new EmptyHeapException();
        }

        return root.element;
    }

    /**
     * Zoek het minimum van de heap, merge nadien de kinderen van het minimum. Dit is de nieuwe heap.
     * @return Waarde van het minimum.
     * @throws EmptyHeapException De heap is leeg.
     */
    @Override
    public T removeMin() throws EmptyHeapException {
        if(isEmpty()){
            throw new EmptyHeapException();
        }

        //Wortel is het minimum
        SkewNode min = root;

        //Verwijder referenties naar het minimum
        if(root.left!=null) {
            root.left.parent = null;
        }
        if(root.right!=null) {
            root.right.parent = null;
        }
        refs.remove(root.element);

        //Merge de kinderen van de wortel
        root = merge(root.left, root.right);

        return min.element.value();
    }

    /**
     * Kijk of de heap leeg is.
     * @return true als de heap leeg is.
     */
    public boolean isEmpty(){
        return root == null;
    }

    /**
     * Verwijder een arbitrair element uit de heap.
     * Verplaats het element naar de wortel. Verwijder nadien de wortel.
     * @param e Het te verwijderen element.
     */
    private void _remove(Element e){
        SkewNode node = refs.get(e);

        while (node.parent!=null){
            swap(node, node.parent);
            node = refs.get(e);
        }

        try {
            removeMin();
        } catch (EmptyHeapException e1) {
            //surpress
        }
    }


    /**
     * Verplaats het element naar boven tot er opnieuw aan de min-heap eigenschap voldaan wordt.
     * @param e Het te verplaatsen element.
     */
    private void moveUp(Element e){
        SkewNode node = refs.get(e);

        while (node.parent!=null && node.parent.compareTo(node) > 0){
            //Wissel referenties
            swap(node, node.parent);
            node = refs.get(e);
        }

    }

    /**
     * Verplaats het element naar beneden tot er opnieuw aan de min-heap eigenschap voldaan wordt.
     * @param e Het te verplaatsen element.
     */
    private void moveDown(Element e){
        SkewNode node = refs.get(e);

        //Geen kinderen
        if(node.left==null&&node.right==null){
            return;
        }

        //1 Kind swap dat kind indien niet aan min-heap voorwaarde voldaan wordt
        if(node.left==null){
            if(node.right.compareTo(node) < 0){
                swap(node, node.right);
                moveDown(e);
            }
            return;
        }
        if(node.right==null){
            if(node.left.compareTo(node) < 0){
                swap(node, node.left);
                moveDown(e);
            }
            return;
        }

        //Zoek het kleinste kind en kijk of er nog aan de min-heap voorwaarde voldaan wordt. Swap indien niet.
        SkewNode smallest = node.left.compareTo(node.right) < 0 ? node.left : node.right;
        if(smallest.compareTo(node) < 0){
            swap(node, smallest);
            moveDown(e);
        }

    }

    /**
     * Wissel de 'node' referenties en de referenties in de map om.
     * @param node1 De eerste te wisselen node.
     * @param node2 De tweede te wisselen node.
     */
    private void swap(SkewNode node1, SkewNode node2){
        Element tmp = node1.element;
        node1.element = node2.element;
        node2.element = tmp;

        refs.put(node1.element, node1);
        refs.put(node2.element, node2);
    }

    /**
     * Kijk of deze skew heap aan beide eigenschappen voldoet.
     * @return true als aan de voorwaarden voldaan wordt.
     */
    @Override
    public boolean isValid() {
        return isHeap(root) && isSkew(root);
    }

    /**
     * Kijk de min-heap eigenschap na voor deze top en zijn kinderen.
     * @param node De te bekijken top.
     * @return true als aan de min-heap eigenschap voldaan wordt.
     */
    private boolean isHeap(SkewNode node){
        if(node==null){
            return true;
        }

        if(node.parent==null){
            return isHeap(node.left) && isHeap(node.right);
        } else {
            return node.parent.compareTo(node) <= 0 && isHeap(node.left) && isHeap(node.right);
        }
    }

    /**
     * Kijk de skew eigenschap na voor deze top en zijn kinderen.
     * @param node De te bekijken top.
     * @return true als aan de skew eigenschap voldaan wordt.
     */
    private boolean isSkew(SkewNode node) {
        return node==null || (!(node.left == null && node.right != null) && isSkew(node.left) && isSkew(node.right));
    }

    /**
     * De centrale bewerking voor deze heap. We gebruiken hier de recursive skew merge bewerking.
     * O(log n) gearmortiseerd.
     * @param node1 Eerste te mergen node.
     * @param node2 Tweede te mergen node.
     * @return Het resultaat van het mergen van de twee nodes.
     */
    private SkewNode merge(SkewNode node1, SkewNode node2){
        //Nieuwe wortel
        if(node1==null){
            return node2;
        }
        if(node2==null){
            return node1;
        }

        //Zoek de kleinste
        SkewNode min, max;
        if(node1.compareTo(node2) < 0){
            min = node1;
            max = node2;
        } else {
            min = node2;
            max = node1;
        }

        //Wissel kinderen van de kleinste om en merge linkerkind vd. kleinste met de grootste
        SkewNode tmp = min.right;
        min.right = min.left;
        max.parent = min;
        min.left = merge(max, tmp);
        return min;
    }

    /**
     * Voorstelling van de waarde van een top.
     */
    private class Element extends ComparableElement<T> {
        public Element(T value) {
            super(value);
        }

        @Override
        public void remove() {
            _remove(this);
        }

        @Override
        public void update(T value) {
            if(this.value.compareTo(value)>0){
                this.value = value;
                moveUp(this);
            } else if(this.value.compareTo(value)<0){
                this.value = value;
                moveDown(this);
            }
        }
    }

    /**
     * Voorstelling van een top.
     */
    private class SkewNode implements Comparable<SkewNode> {
        private Element element;
        private SkewNode parent;
        private SkewNode left;
        private SkewNode right;

        private SkewNode(Element e){
            this.element = e;
        }

        @Override
        public int compareTo(SkewNode o) {
            return element.compareTo(o.getElement());
        }

        public void setElement(Element element) {
            this.element = element;
        }

        public Element getElement(){
            return element;
        }
    }
}
