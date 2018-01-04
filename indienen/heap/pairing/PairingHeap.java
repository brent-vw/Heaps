package heap.pairing;

import heap.*;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Pairing heap.
 * Voldoet aan de min-heap eigenschap.
 * Elk kind van de wortel van de pairing heap is opnieuw een pairing heap.
 * @param <T>
 */
public class PairingHeap<T extends Comparable<T>> implements ExtendedHeap<T> {
    private PairingHeapNode root;

    /**
     * De merge bewerking is de centrale bewerking van de pairing heap, elke andere bewerking is gebaseerd op deze merge bewerking.
     * De kleinste heap wordt de ouder van de grootste heap. Dit gebeurt in O(1) gearmortiseerd.
     * @param h1 De eerste te mergen pairing heap.
     * @param h2 De tweede te mergen pairing heap.
     * @return De pairing heap die het resultaat is van het mergen van twee pairing heaps.
     */
    private PairingHeapNode merge(PairingHeapNode h1, PairingHeapNode h2){
        //Nieuwe heap
        if(h1==null){
            return h2;
        }

        if(h2==null){
            return h1;
        }

        //Zoek de kleinste van de twee
        PairingHeapNode min;
        PairingHeapNode max;
        if(h1.compareTo(h2) < 0){
            min = h1;
            max = h2;
        } else {
            min = h2;
            max = h1;
        }

        //Maak de kleinste de ouder van de grootste
        max.parent = min;
        max.sibling = min.child;
        min.child = max;

        return min;
    }

    /**
     * Maak een nieuwe pairing heap met de gegeven waarde en merge deze met de wortel van de reeds bestaande heap. Maak het resultaat van de merge de nieuwe wortel.
     * O(1) gearmortiseerd.
     * @param value De waarde van de toe te voegen top.
     * @return De referentie naar het toegevoegde element.
     */
    @Override
    public ComparableElement<T> insert(T value) {
        PairingHeapNode e = new PairingHeapNode(value);

        root = merge(root, e);

        return e;
    }

    /**
     * Zoek het kleinste element van de pairing heap. Dit is altijd de wortel.
     * @return De waarde van de wortel van de pairing heap.
     * @throws EmptyHeapException De heap is leeg.
     */
    @Override
    public ComparableElement<T> findMin() throws EmptyHeapException {
        if(isEmpty()){
            throw new EmptyHeapException();
        }
        return root;
    }

    /**
     * Zoek het kleinste element van de pairing heap. Verwijder het nadien.
     * O(log n) gearmortiseerd.
     * @return De waarde van het kleinste element.
     * @throws EmptyHeapException De heap is leeg.
     */
    @Override
    public T removeMin() throws EmptyHeapException {
        if(isEmpty()){
            throw new EmptyHeapException();
        }

        PairingHeapNode min = root;

        //Maak de pairing heap zonder de wortel de nieuwe wortel van deze heap
        root = remove(root);

        return min.value();
    }

    /**
     * Bouw een nieuwe pairing heap zonder de wortel.
     * Doe dit door eerst de kinderen van de wortel in volgorde paarsgewijs te mergen. Nadien merge de overgebleven heaps in omgekeerde volgorde tot 1 heap.
     * O(log n) gearmortiseerd.
     * @param parent De te verwijderen wortel.
     * @return De nieuwe heap zonder de wortel.
     */
    private PairingHeapNode remove(PairingHeapNode parent){
        //Bouw een lijst van alle kinderen
        PairingHeapNode root = parent;
        PairingHeapNode child = root.child;
        ArrayList<PairingHeapNode> children = new ArrayList<>();
        while (child!=null){
            children.add(child);
            child.parent = null;
            PairingHeapNode oldChild = child;
            child = child.sibling;
            oldChild.sibling = null;
        }

        //Merge de kinderen paarsgewijs in volgorde
        Stack<PairingHeapNode> firstMerge = new Stack<>();
        for (int i=0; i<children.size(); i+=2) {
            if(i+1==children.size()){
                firstMerge.push(children.get(i));
            } else {
                firstMerge.push(
                        merge(children.get(i), children.get(i+1))
                );
            }
        }

        //Als deze heap niet de eigenlijke wortel is van de heap, verwijder alle referenties naar deze heap in de huidige boom
        if(root.parent!=null){
            removeChild(root);
        }

        //Bouw de nieuwe pairing heap door de bekomen heaps in omgekeerde volgorde tot 1 heap te mergen
        root = null;
        while (!firstMerge.isEmpty()){
            root = merge(root, firstMerge.pop());
        }
        return root;
    }

    /**
     * Verwijder een top die niet de wortel is uit de pairing heap.
     * @param node de te verwijderen top.
     */
    private void _remove(PairingHeapNode node){
        if(node==root){
            try {
                removeMin();
            } catch (EmptyHeapException e) {
                //
            }
            return;
        }

        root = merge(remove(node), root);
    }

    /**
     * Merge de top met zijn kinderen opnieuw met de heap, indien deze strikt groter is dan zijn ouder.
     * @param node De top waarvan de waarde gewijzigd is.
     */
    private void decreaseKey(PairingHeapNode node){
        //Het is reeds de wortel
        if(node==root){
           return;
        }

        //Merge de top opnieuw met de pairing heap indien er niet meer aan de min-heap eigenschap voldaan wordt
        if(node.parent.compareTo(node)>0) {
            //Verwijder de referentie naar het kind
            removeChild(node);
            root = merge(node, root);
        }
    }

    /**
     * Verplaats de top naar de wortel. Merge de kinderen van de top opnieuw met de heap en merge de top zelf opnieuw met de heap.
     * @param node De aan te passen heap.
     */
    private void increaseKey(PairingHeapNode node){
        if(node==root){
            try {
                removeMin();
                node.child = null;
                root = merge(node, root);
            } catch (EmptyHeapException e) {
                //
            }
            return;
        }
        PairingHeapNode newNode = remove(node);
        node.child = null;
        node = merge(newNode, node);
        root = merge(node, root);

    }

    /**
     * Verwijder de ouder referentie naar het kind.
     * @param child Het te verwijderen kind.
     */
    private void removeChild(PairingHeapNode child) {
        //Ouder
        PairingHeapNode parent = child.parent;
        if(parent==null){
            return;
        }

        //Directe referentie
        if(parent.child==child){
            parent.child = child.sibling;
            child.sibling = null;
            child.parent = null;
            return;
        }

        //Zoek in de gelinkte lijst
        PairingHeapNode previous = null;
        PairingHeapNode current = parent.child;
        while (current!=null){
            if(current==child){
                previous.sibling = current.sibling;
                current.parent = null;
                current.sibling=null;
                return;
            }
            previous = current;
            current = current.sibling;
        }
    }

    /**
     * Kijk of de heap leeg is.
     * @return true als de heap leeg is.
     */
    public boolean isEmpty(){
        return root == null;
    }

    /**
     * Kijk of er aan de min-heap eigenschap voldaan wordt.
     * @param node De wortel van de te checken pairing heap.
     * @return true als de heap een min-heap is.
     */
    private boolean isMinHeap(PairingHeapNode node){
        if(node == null){
            return true;
        }
        PairingHeapNode child = node.child;
        while (child!=null){
            if(!isMinHeap(child)||child.compareTo(node) < 0){
                return false;
            }
            child = child.sibling;
        }
        return true;
    }

    /**
     * Kijk of deze pairing heap geldig is.
     * @return true als deze heap een geldige pairing heap is.
     */
    @Override
    public boolean isValid(){
        return isMinHeap(root);
    }

    /**
     * Voorstelling van een element in de heap.
     */
    public class PairingHeapNode extends ComparableElement<T>{
        private PairingHeapNode parent;
        private PairingHeapNode sibling;
        private PairingHeapNode child;

        public PairingHeapNode(T value) {
            super(value);
        }

        /**
         * Verwijder deze top uit de boom.
         */
        @Override
        public void remove() {
            _remove(this);
        }

        /**
         * Kijk of de increase-key of decrease-key toegepast moet worden.
         * @param value De nieuwe waarde.
         */
        @Override
        public void update(T value) {
            if (value.compareTo(this.value) < 0) {
                this.value = value;
                decreaseKey(this);
            } else if (value.compareTo(this.value) > 0) {
                this.value = value;
                increaseKey(this);
            }
        }

        public PairingHeapNode getParent() {
            return parent;
        }
    }
}
