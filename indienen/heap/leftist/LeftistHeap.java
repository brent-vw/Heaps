package heap.leftist;

import heap.*;

import java.util.HashMap;

/**
 * Leftist heap.
 * Voldoet aan de min-heap eigenschap.
 * Er geldt ook: de npl van het linker kind >= het rechterkind van elke top.
 * @param <T>
 */
public class LeftistHeap<T extends Comparable<T>> implements ExtendedHeap<T> {

    private LeftistHeapNode root;

    /**
     * Link de elementen met de leftistHeapNodes
     */
    private HashMap<ComparableElement<T>, LeftistHeapNode> stub;

    public LeftistHeap() {
        root = null;
        stub = new HashMap<>();
    }

    /**
     * Merge 2 LeftistHeapNodes
     * @param h1 Eerste node die gemerged moet worden.
     * @param h2 Tweede node.
     * @return Het gemergede resultaat van beide nodes.
     */
    private LeftistHeapNode _merge(LeftistHeapNode h1, LeftistHeapNode h2) {
        //Niets om te mergen
        if (h1 == null) {
            return h2;
        }
        if (h2 == null) {
            return h1;
        }

        //Zoek het kleinste en het grootste van de twee
        LeftistHeapNode min, max;
        if (h1.getValue().compareTo(h2.getValue()) > 0) {
            max = h1;
            min = h2;
        } else {
            min = h1;
            max = h2;
        }

        //Maak het rechterkind van de kleinste het gemergde resultaat van het rechterkind met het maximum
        min.setRight(_merge(min.getRight(), max));

        //Wissel de kinderen als er niet aan de NPL voorwaarde voldaan wordt
        if (min.getLeft() == null) {
            min.getRight().setParent(min);
            min.setLeft(min.getRight());
            min.setRight(null);
        } else {
            min.getLeft().setParent(min);
            min.getRight().setParent(min);
            if (min.getLeft().getNpl() < min.getRight().getNpl()) {
                LeftistHeapNode tmp = min.getLeft();
                min.setLeft(min.getRight());
                min.setRight(tmp);
            }
        }
        return min;
    }

    /**
     * Merge 2 nodes en maak de wortel van het resultaat de nieuwe wortel.
     * @param heap Eerste node die gemerged moet worden.
     * @param newNode Tweede node.
     */
    private void merge(LeftistHeapNode heap, LeftistHeapNode newNode) {
        root = _merge(heap, newNode);
    }

    private void merge(LeftistHeapNode newNode) {
        merge(root, newNode);
    }

    /**
     * Maak een nieuwe node en merge deze met de huidige boom.
     * @param value De waarde van de nieuwe node.
     * @return De referentie naar de waarde van de node.
     */
    @Override
    public ComparableElement<T> insert(T value) {
        LeftistHeapNode node = new LeftistHeapNode(value);
        stub.put(node.value, node);
        merge(node);
        return node.getValue();
    }

    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Zoek het minimum van de LeftistHeap, dit is altijd de wortel.
     * @return De referentie naar het Element.
     * @throws EmptyHeapException De heap is leeg.
     */
    @Override
    public ComparableElement<T> findMin() throws EmptyHeapException {
        if (isEmpty()) {
            throw new EmptyHeapException();
        }
        return root.getValue();
    }

    /**
     * Verwijder de wortel en merge het linker- en rechterkind van de wortel tot de nieuwe heap.
     * @return Waarde van het minimum.
     * @throws EmptyHeapException De heap is leeg.
     */
    @Override
    public T removeMin() throws EmptyHeapException {
        ComparableElement<T> min =  findMin();
        stub.remove(min);

        if(root.getLeft()!=null){
            root.getLeft().parent = null;
        }

        if(root.getRight()!=null){
            root.getRight().parent = null;
        }

        merge(root.getLeft(), root.getRight());
        return min.value();
    }

    /**
     * Herstel de heap eigenschap door het element naar beneden te bewegen.
     * @param e Het te verplaatsen element.
     */
    private void moveDown(Element e){
        LeftistHeapNode node = stub.get(e);
        //Geen kindren
        if(node.getLeft()==null&&node.getRight()==null){
            return;
        //Geen linkerkind, wissel met rechterkind als e groter is
        } else if(node.getLeft()==null){
            if(node.getRight().getValue().compareTo(e)>=0){
                return;
            }
            swap(node.getRight(), node);
            moveDown(e);
        //Analoog voor linkerkind
        } else if(node.getRight()==null){
            if(node.getLeft().getValue().compareTo(e)>=0){
                return;
            }
            swap(node.getLeft(), node);
            moveDown(e);
        //Wissel met de kleinste als e groter is dan die kleinste
        } else {
            LeftistHeapNode smallest = node.getRight().value.compareTo(node.getLeft().value) < 0 ? node.getRight() : node.getLeft();
            if(smallest.value.compareTo(e)>=0){
                return;
            }
            swap(smallest, node);
            moveDown(e);
        }
    }

    /**
     * Herstel de heap eigenschap door het element naar boven te bewegen.
     * @param e Het te verplaatsen element.
     */
    private void moveUp(Element e){
        LeftistHeapNode node = stub.get(e);

        //Verplaats e naar boven tot het groter is dan zijn ouder, of het de wortel is.
        while (node.getParent()!=null && node.getParent().value.compareTo(e) > 0){
            swap(node.parent, node);
            node = stub.get(e);
        }
    }

    /**
     * Verwijder het element uit de heap.
     * @param e Het te verwijderen element.
     */
    private void remove(Element e){
        LeftistHeapNode node = stub.get(e);

        //Verplaats het element naar boven tot het de wortel is
        while (node.getParent()!=null){
            swap(node, node.getParent());
            node = stub.get(e);
        }


        //Verwijder de wortel
        try {
            removeMin();
        } catch (EmptyHeapException e1) {
            //surpress
        }
    }

    /**
     * Wissel de 2 nodes en hun referenties.
     * @param n1 Eerste node om te wisselen.
     * @param n2 Tweede node om te wisselen.
     */
    private void swap(LeftistHeapNode n1, LeftistHeapNode n2){
        Element tmp = n1.value;
        n1.setValue(n2.value);
        n2.setValue(tmp);

        stub.put(n1.value, n1);
        stub.put(n2.value, n2);
    }

    private boolean isSmaller(LeftistHeapNode check, LeftistHeapNode compare){
        if(compare==null){
            return true;
        }
        return check.value.compareTo(compare.value) <= 0;
    }

    private boolean heapCheck(LeftistHeapNode node){
        if(node==null){
            return true;
        }
        return heapCheck(node.getLeft()) && heapCheck(node.getRight()) && isSmaller(node, node.left) && isSmaller(node, node.right);
    }

    private boolean leftistCheck(LeftistHeapNode node){
        if(node==null){
            return true;
        }
        return node._getNpl(node.getLeft()) >= node._getNpl(node.getRight()) && leftistCheck(node.getLeft()) && leftistCheck(node.getRight());
    }

    /**
     * @return Of de boom voldoet aan heap eigenschap en de leftistheap eigenschap.
     */
    @Override
    public boolean isValid() {
        return heapCheck(root) && leftistCheck(root);
    }

    @Override
    public String getName() {
        return "Leftist Heap";
    }

    @Override
    public void printHeap() {
    }

    private class Element extends ComparableElement<T> {
        public Element(T value) {
            super(value);
        }

        /**
         *
         */
        @Override
        public void remove() {
            LeftistHeap.this.remove(this);
        }

        /**
         * @param value
         */
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

    private class LeftistHeapNode {
        private Element value;

        private int npl;

        private LeftistHeapNode parent;
        private LeftistHeapNode left;
        private LeftistHeapNode right;

        public LeftistHeapNode(T value) {
            this(value, null, null);
        }

        public LeftistHeapNode(T value, LeftistHeapNode left, LeftistHeapNode right) {
            this.value = new Element(value);
            this.left = left;
            this.right = right;
            this.npl = calcNpl();
        }

        public LeftistHeapNode getLeft() {
            return left;
        }

        public void setLeft(LeftistHeapNode left) {
            this.left = left;
            this.npl = calcNpl();
        }

        public LeftistHeapNode getRight() {
            return right;
        }

        public void setRight(LeftistHeapNode right) {
            this.right = right;
            this.npl = calcNpl();
        }

        /**
         * Bereken effectief de nulpadlengte.
         * @return
         */
        public int calcNpl() {
            return Math.min(_getNpl(left), _getNpl(right)) + 1;
        }

        /**
         * Vraag de nulpadlengte van het kind op een veilige manier.
         * @param node
         * @return
         */
        public int _getNpl(LeftistHeapNode node) {
            return node == null ? -1 : node.getNpl();
        }

        /**
         * Vraag de opgeslagen npl.
         * @return
         */
        public int getNpl() {
            return npl;
        }

        public ComparableElement<T> getValue() {
            return value;
        }

        public void setValue(Element value) {
            this.value = value;
        }

        public LeftistHeapNode getParent() {
            return parent;
        }

        public void setParent(LeftistHeapNode parent) {
            this.parent = parent;
        }
    }
}
