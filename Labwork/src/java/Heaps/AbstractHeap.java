package Heaps;

import java.util.AbstractMap.SimpleEntry;

public abstract class AbstractHeap<K extends Comparable<K>, V> {

    protected CircularDoublyLinkedList<HeapNode> roots = new CircularDoublyLinkedList<>();
    protected int size = 0;
    protected HeapNode min = null;
    protected K minusInfinity;

    protected class HeapNode {

        private K key;
        private V value;
        private HeapNode parent = null;
        private final CircularDoublyLinkedList<HeapNode> children = new CircularDoublyLinkedList<>();
        protected int degree = 0;

        public HeapNode(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public HeapNode getParent() {
            return parent;
        }

        public void setParent(HeapNode parent) {
            this.parent = parent;
        }

        public CircularDoublyLinkedList<HeapNode> getChildren() {
            return children;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof AbstractHeap.HeapNode)) {
                return false;
            } else {
                //noinspection unchecked
                HeapNode heapNode = (HeapNode) o;
                return this.getKey() == null ? heapNode.getKey() == null : this.getKey().equals(heapNode.getKey());
            }
        }

        @Override
        public String toString() {
            return String.valueOf(this.key);
        }
    }

    public abstract void insert(K key, V value);

    public abstract boolean delete(K key);

    public abstract SimpleEntry<K, V> extractMin();

    public abstract boolean decreaseKey(K oldKey, K newKey);

    protected HeapNode searchNode(K key) {
        if (roots.getSize() == 0) {
            return null;
        } else {
            return searchNode(roots, key);
        }
    }

    private HeapNode searchNode(CircularDoublyLinkedList<HeapNode> siblings, K key) {
        HeapNode searchNode = null;

        for (HeapNode temp : siblings) {
            if (temp.getKey().equals(key)) {
                return temp;
            } else if (!temp.children.isEmpty()) {
                searchNode = searchNode(temp.children, key);

                if (searchNode != null) {
                    break;
                }
            }
        }

        return searchNode;
    }

    protected HeapNode link(HeapNode newRoot, HeapNode newChild) {
        if (newRoot.getKey().compareTo(newChild.getKey()) > 0) {
            HeapNode buffer = newRoot;
            newRoot = newChild;
            newChild = buffer;
        }

        roots.delete(roots.indexOfByAddress(newChild));
        newRoot.getChildren().add(newChild);
        newChild.setParent(newRoot);
        newRoot.degree++;

        return newRoot;
    }

    @Override
    public String toString() {
        StringBuilder heap = new StringBuilder();

        for (HeapNode tree : roots) {
            print(heap, tree);
        }

        return heap.toString();
    }

    private void print(StringBuilder heap, HeapNode root) {
        heap.append(root).append(' ');

        if (!root.children.isEmpty()) {
            for (HeapNode tree : root.children) {
                print(heap, tree);
            }
        }
    }
}