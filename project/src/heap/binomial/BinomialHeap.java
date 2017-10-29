package heap.binomial;

import heap.ComparableElement;
import heap.EmptyHeapException;
import heap.ExtendedHeap;

import java.util.HashMap;
import java.util.HashSet;

import static heap.Debug.DASHES;

/**
 * Binomiale prioriteitswachtlijn.
 * Deze voldoet aan de heapeigenschap.
 * Ook is er telkens ten hoogste 1 boom van hoogte i in de wachtlijn.
 * @param <T> Het type van de Heap, wat Comparable moet zijn.
 */
public class BinomialHeap<T extends Comparable<T>> implements ExtendedHeap<T>{

    private BinomialTreeNode queue;
    /**
     * Hier slaan we op welk Element gelinkt is aan welke node om zo snel de locatie in de wachtlijn te kunnen vinden, en toch de complexiteit van pointers wisselen te vermijden.
     */
    private HashMap<Element, BinomialTreeNode> stub;
    private int size;

    public BinomialHeap(){
        this.queue = null;
        this.stub = new HashMap<>();
        this.size = 0;
    }

    /**
     * Voeg een nieuw element toe aan de wachtlijn door het met de oude wachtlijn te mergen.
     * @param value De waarde van het toe te voegen element.
     * @return Referentie naar het element.
     */
    @Override
    public ComparableElement<T> insert(T value) {
        BinomialTreeNode node = new BinomialTreeNode(new Element(value));
        stub.put(node.element, node);

        merge(node);
        size++;

        return node.element;
    }

    /**
     * Voer de merge operatie uit van een nieuwe boom met de reeds bestaande wachtlijn.
     * @param newElements De te mergen boom.
     */
    private void merge(BinomialTreeNode newElements){
        //Wachtlijn is leeg
        if(queue==null){
            queue = newElements;
            return;
        }

        //De te mergen boom is de minst diepe en dus de nieuwe origine van de wachtlijn
        if(queue.getDepth() > newElements.getDepth()){
            newElements.setNeighbor(queue);
            queue = newElements;
            return;
        }

        //Zoek de plek waar de boom gemerged moet worden
        BinomialTreeNode current = queue;
        BinomialTreeNode previous = null;
        while (current!=null){
            //De toe te voegen boom is hoger dan de huidige
            if(current.getDepth() < newElements.getDepth()){
                //Plaats de boom op het einde van de wachtlijn
                if(current.getNeighbor()==null){
                    current.setNeighbor(newElements);
                    return;
                //Plaats de boom tussen de huidige en het volgende als de volgende hoger is dan de toe te voegen boom
                } else if(newElements.getDepth() < current.getNeighbor().getDepth()){
                    BinomialTreeNode tmp = current.getNeighbor();
                    current.setNeighbor(newElements);
                    newElements.setNeighbor(tmp);
                    return;
                }
            }

            //De huidige en de toe te voegen boom hebben dezelfde hoogte. Ze moeten gemerged worden om aan de voorwaarden te voldoen.
            if(current.getDepth() == newElements.getDepth()){
                //Bepaal de kleinste en de grootste van de twee (in de zin van de wortel)
                BinomialTreeNode min, max;
                if(current.compareTo(newElements) <= 0){
                    min = current;
                    max = newElements;
                } else {
                    min = newElements;
                    max = current;
                }

                //Verwijder de huidige boom tijdelijk uit de wachtlijn
                if(previous!=null) {
                    previous.setNeighbor(current.getNeighbor());
                } else {
                    queue = current.getNeighbor();
                }
                current.setNeighbor(null);

                //Maak de grootste het laatste kind van de kleinste
                if(min.getChild()!=null){
                    BinomialTreeNode currentChild = min.getChild();
                    while (currentChild.getNeighbor()!=null){
                        currentChild = currentChild.getNeighbor();
                    }
                    currentChild.setNeighbor(max);
                } else {
                    min.setChild(max);
                }

                //Pas de referenties aan
                min.increaseDepth();
                max.setParent(min);

                //Merge deze nieuwe boom met de wachtlijn
                merge(min);
                return;
            }

            previous = current;
            current = current.getNeighbor();
        }
    }

    /**
     * Zoek het kleinste element door de wortels van de bomen in de wachtlijn te bekijken.
     * @param remove Verwijder de boom van dat Element ook uit de wachtlijn.
     * @return De locatie van het gevonden element.
     * @throws EmptyHeapException De wachtlijn is leeg.
     */
    private BinomialTreeNode _findMin(boolean remove) throws EmptyHeapException {
        //Wachtlijn is leeg
        if(queue==null){
            throw new EmptyHeapException();
        }

        BinomialTreeNode minNeighbour = null;
        BinomialTreeNode min = queue;
        BinomialTreeNode current = queue;

        //Zoek het kleinste element en zijn buur
        while (current.getNeighbor()!=null){
            BinomialTreeNode prev = current;
            current = current.getNeighbor();
            if(current.compareTo(min) < 0){
                minNeighbour = prev;
                min = current;
            }
        }

        //Verwijder de boom met het kleinste element en pas de referenties aan.
        if(remove){
          size--;
          if(minNeighbour==null){
              queue = min.getNeighbor();
          } else {
              minNeighbour.setNeighbor(min.getNeighbor());
          }
          stub.remove(min.element);
        }
        return min;
    }

    /**
     * Zoek het kleinste element.
     * @return De referentie naar het kleinste element.
     * @throws EmptyHeapException De wachtlijn is leeg.
     */
    @Override
    public ComparableElement<T> findMin() throws EmptyHeapException {
        return _findMin(false).element;
    }

    /**
     * Verwijder het kleinste element uit de wachtlijn.
     * @return De waarde van het kleinste element.
     * @throws EmptyHeapException De wachtlijn is leeg.
     */
    @Override
    public T removeMin() throws EmptyHeapException {
        //Zoek het kleinste element en verwijder zijn boom uit de wachtlijn
        BinomialTreeNode min = _findMin(true);

        //Merge alle kinderen van deze boom met de wachtlijn
        BinomialTreeNode current = min.getChild();
        while (current!=null){
            BinomialTreeNode tmp = current.getNeighbor();
            current.setNeighbor(null);
            merge(current);
            current = tmp;
        }

        return min.element.value();
    }

    private void _printHeap(BinomialTreeNode root){
        if(root==null){
            return;
        }

        BinomialTreeNode current = root.getChild();
        while (current!=null){
            System.out.print(""+current+" ");
            current = current.getNeighbor();
        }
        current = root.getChild();
        while (current!=null){
            _printHeap(current);
            current = current.getNeighbor();
        }
    }

    /**
     * Beweeg het element naar boven om de heap eigenschap te herstellen.
     * @param node Het te bewegen element.
     */
    private void moveUp(Element node){
        moveUp(node, false);
    }

    /**
     * Beweeg het element naar boven.
     * @param node Het te bewegen element.
     * @param boostUp Of dit element de nieuwe wortel moet worden.
     */
    private void moveUp(Element node, boolean boostUp){
        //Zoek de locatie van het element
        BinomialTreeNode toMove = stub.get(node);

        //Stop als aan de heapvoorwaarde voldaan wordt en het niet naar de wortel geforceerd wordt
        if (toMove.getParent()==null||(toMove.compareTo(toMove.getParent()) >= 0&&!boostUp)) {
            return;
        }

        //Wissel het element met zijn ouder en herhaal
        swap(toMove, toMove.getParent());
        moveUp(node, boostUp);
    }

    /**
     * Beweeg het element naar beneden om de heap eigenschap te herstellen.
     * @param node Het naar beneden te bewegen element.
     */
    private void moveDown(Element node) {
        //Zoek de locatie van het element
        BinomialTreeNode toMove = stub.get(node);

        //Het element heeft geen kind
        if(toMove.getChild()==null){
            return;
        }

        //Zoek het kleinste kind van het element
        BinomialTreeNode current = toMove.getChild();
        BinomialTreeNode min = current;
        while (current.getNeighbor()!=null){
            current = current.getNeighbor();
            if(min.compareTo(current)>0){
                min = current;
            }
        }

        //Als het kleinste kind van het element groter is dan dat element zelf doe niets.
        if(min.compareTo(toMove)>=0){
            return;
        }

        //Wissel het element met het kleinste kind en herhaal
        swap(min, toMove);
        moveDown(node);
    }

    /**
     * Verwijder het gegeven element.
     * @param node Het te verwijderen element.
     */
    private void _remove(Element node){
        //Verplaats het element naar de wortel
        moveUp(node, true);

        //Zoek de locatie
        BinomialTreeNode toMove = stub.get(node);

        BinomialTreeNode minNeighbour = null;
        BinomialTreeNode min = queue;
        BinomialTreeNode current = queue;

        //Zoek de buur van het te verwijderen element
        while (current.getNeighbor()!=null){
            BinomialTreeNode prev = current;
            current = current.getNeighbor();
            if(current == toMove){
                minNeighbour = prev;
                min = current;
                break;
            }
        }


        //Verwijder de boom van dit element uit de wachtlijn en pas de referenties aan
        size--;
        if(minNeighbour==null){
            queue = min.getNeighbor();
        } else {
            minNeighbour.setNeighbor(min.getNeighbor());
        }
        stub.remove(min.element);

        //Merge alle kinderen van de boom met de wachtlijn
        current = min.getChild();
        while (current!=null){
            BinomialTreeNode tmp = current.getNeighbor();
            current.setNeighbor(null);
            current.setParent(null);
            merge(current);
            current = tmp;
        }
    }

    /**
     * Wissel de referenties van 2 nodes om
     * @param newParent Het eerste te wisselen element.
     * @param oldParent Het tweede te wisselen element.
     */
    private void swap(BinomialTreeNode newParent, BinomialTreeNode oldParent) {
        Element tmp = newParent.element;
        newParent.setElement(oldParent.element);
        oldParent.setElement(tmp);

        stub.put(newParent.element, newParent);
        stub.put(oldParent.element, oldParent);
    }

    /**
     * Print de heap in human readable formaat.
     */
    @Override
    public void printHeap(){
        BinomialTreeNode current = queue;
        System.out.println(DASHES);
        System.out.println("Printing Heap");
        while (current!=null){
            System.out.print("This heap has depth: "+current.getDepth()+" Elements: "+current+" ");
            _printHeap(current);
            System.out.println();
            current = current.getNeighbor();
        }
        System.out.println(DASHES);
    }

    private boolean checkChildren(BinomialTreeNode root){
        if(root.getChild()==null){
            return true;
        }
        BinomialTreeNode child = root.getChild();
        while (child!=null){
            if(root.compareTo(child)>0||!checkChildren(child)){
                return false;
            }
            child = child.getNeighbor();
        }
        return true;
    }

    /**
     * Kijk of deze wachtlijn aan beide eigenschappen voldoet.
     * @return Of deze boom aan de eigenschappen voldoet
     */
    @Override
    public boolean isValid() {
        HashSet<Integer> depths = new HashSet<>();
        BinomialTreeNode current = queue;
        while (current!=null){
            if (depths.contains(current.getDepth()) || !checkChildren(current)) {
                return false;
            }
            depths.add(current.getDepth());
            current = current.getNeighbor();
        }
        return true;
    }

    /**
     * @return Human readable naam van de boom.
     */
    @Override
    public String getName() {
        return "Binomial Heap";
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Element extends ComparableElement<T> {

        public Element(T value) {
            super(value);
        }

        /**
         * Verwijder dit element uit de wachtlijn.
         */
        @Override
        public void remove() {
            _remove(this);
        }

        /**
         * Pas het element aan en herstel de heap eigenschap.
         * @param value de nieuwe waarde.
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

    /**
     *
     */
    private class BinomialTreeNode implements Comparable<BinomialTreeNode>{
        /**
         * Gelinkte lijst van de andere kinderen van de ouder, of van de andere bomen als de ouder niet bestaat.
         */
        private BinomialTreeNode neighbor;

        /**
         * Het eerste kind.
         */
        private BinomialTreeNode child;

        /**
         * De ouder.
         */
        private BinomialTreeNode parent;

        /**
         * Hoogte van de node (enkel relevant is de node een boom in de wachtlijn voorstelt)
         */
        private int depth;

        /**
         * Het element dat zich in deze node bevindt.
         */
        private Element element;

        private BinomialTreeNode(Element element){
            this.depth = 0;
            this.element = element;
        }

        private int size(){
            int total = 1;
            if(this.neighbor!=null){
                total += this.neighbor.size();
            }
            if(this.child!=null){
                total += this.child.size();
            }
            return total;
        }

        private BinomialTreeNode getNeighbor() {
            return neighbor;
        }

        private void setNeighbor(BinomialTreeNode neighbor) {
            this.neighbor = neighbor;
        }

        private BinomialTreeNode getChild() {
            return child;
        }

        private void setChild(BinomialTreeNode child) {
            this.child = child;
        }

        private int getDepth() {
            return depth;
        }

        private void setDepth(int depth) {
            this.depth = depth;
        }

        private void increaseDepth(){
            this.depth++;
        }

        private BinomialTreeNode getParent() {
            return parent;
        }

        private void setParent(BinomialTreeNode parent) {
            this.parent = parent;
        }

        public Element getElement() {
            return element;
        }

        public void setElement(Element element) {
            this.element = element;
        }

        @Override
        public int compareTo(BinomialTreeNode o) {
            return element.compareTo(o.element);
        }
    }
}
