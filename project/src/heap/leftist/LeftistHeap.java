package heap.leftist;

import heap.*;

import java.util.HashMap;

public class LeftistHeap<T extends Comparable<T>> implements ExtendedHeap<T> {

    private LeftistHeapNode root;
    private HashMap<ComparableElement<T>, LeftistHeapNode> stub;

    public LeftistHeap() {
        root = null;
        stub = new HashMap<>();
    }

    private LeftistHeapNode _merge(LeftistHeapNode h1, LeftistHeapNode h2) {
        if (h1 == null) {
            return h2;
        }
        if (h2 == null) {
            return h1;
        }

        LeftistHeapNode min, max;
        if (h1.getValue().compareTo(h2.getValue()) > 0) {
            max = h1;
            min = h2;
        } else {
            min = h2;
            max = h1;
        }

        min.setRight(_merge(min.getRight(), max));

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

    private void merge(LeftistHeapNode heap, LeftistHeapNode newNode) {
        root = _merge(heap, newNode);
    }

    private void merge(LeftistHeapNode newNode) {
        merge(root, newNode);
    }

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

    @Override
    public ComparableElement<T> findMin() throws EmptyHeapException {
        if (isEmpty()) {
            throw new EmptyHeapException();
        }
        return root.getValue();
    }

    @Override
    public T removeMin() throws EmptyHeapException {
        ComparableElement<T> min =  findMin();
        stub.remove(min);
        merge(root.getLeft(), root.getRight());
        return min.value();
    }

    private void moveDown(Element e){
        LeftistHeapNode node = stub.get(e);
        if(node.getLeft()==null&&node.getRight()==null){
            return;
        } else if(node.getLeft()==null){
            if(node.getRight().getValue().compareTo(e)>=0){
                return;
            }
            swap(node.getRight(), node);
            moveDown(e);
        } else if(node.getRight()==null){
            if(node.getLeft().getValue().compareTo(e)>=0){
                return;
            }
            swap(node.getLeft(), node);
            moveDown(e);
        } else {
            LeftistHeapNode smallest = node.getRight().value.compareTo(node.getLeft().value) < 0 ? node.getRight() : node.getLeft();
            if(smallest.value.compareTo(e)>=0){
                return;
            }
            swap(smallest, node);
            moveDown(e);
        }
    }

    private void moveUp(Element e){
        LeftistHeapNode node = stub.get(e);
        while (node.getParent()!=null){
            if(node.getParent().value.compareTo(e) > 0){
                swap(node.parent, node);
                node = stub.get(e);
            } else {
                break;
            }
        }
    }

    private void remove(Element e){
        LeftistHeapNode node = stub.get(e);
        while (node.getParent()!=null){
            swap(node, node.getParent());
            node = stub.get(e);
        }
        merge(node.getLeft(), node.getRight());
    }

    private void swap(LeftistHeapNode n1, LeftistHeapNode n2){
        Element tmp = n1.value;
        n1.setValue(n2.value);
        n2.setValue(tmp);

        stub.put(n1.value, n1);
        stub.put(n2.value, n2);
    }

    @Override
    public boolean isValid() {
        return true;
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

        @Override
        public void remove() {
            LeftistHeap.this.remove(this);
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

        public int calcNpl() {
            return Math.min(_getNpl(left), _getNpl(right)) + 1;
        }

        private int _getNpl(LeftistHeapNode node) {
            return node == null ? -1 : node.getNpl();
        }

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
