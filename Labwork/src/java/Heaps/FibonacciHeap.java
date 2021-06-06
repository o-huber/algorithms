package Heaps;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FibonacciHeap<K extends Comparable<K>, V> extends AbstractHeap<K, V> {

    private class FHeapNode extends HeapNode {
        private boolean mark = false;

        public FHeapNode(K key, V value) {
            super(key, value);
        }
    }

    public FibonacciHeap(K minusInfinity) {
        this.minusInfinity = minusInfinity;
    }

    public void insert(K key, V value) {
        FHeapNode node = new FHeapNode(key, value);

        if (this.min == null) {
            this.min = node;
        } else {
            if (node.getKey().compareTo(this.min.getKey()) < 0) {
                this.min = node;
            }
        }

        this.roots.add(node);
        this.size++;
    }

    public boolean delete(K key) {
        if (decreaseKey(key, this.minusInfinity)) {
            return extractMin() != null;
        } else {
            return false;
        }
    }

    public boolean decreaseKey(K oldKey, K newKey) {
        if (newKey.compareTo(oldKey) > 0){
            throw new IllegalArgumentException(
                    String.format("New key %s bigger than old key %s!", newKey, oldKey));
        }

        FHeapNode oldKeyNode = (FHeapNode) searchNode(oldKey);

        if (oldKeyNode == null) {
            return false;
        }

        oldKeyNode.setKey(newKey);
        FHeapNode keyNodeParent = (FHeapNode) oldKeyNode.getParent();

        if (keyNodeParent != null && newKey.compareTo(keyNodeParent.getKey()) < 0) {
            cut(oldKeyNode, keyNodeParent);
            cascadingCut(keyNodeParent);
        }

        if (newKey.compareTo(this.min.getKey()) < 0) {
            this.min = oldKeyNode;
        }

        return true;
    }

    private void cut(FHeapNode child, FHeapNode parent) {
        assert child.getParent() == parent;

        CircularDoublyLinkedList<HeapNode> children = parent.getChildren();
        children.delete(children.indexOfByAddress(child));
        parent.degree--;
        roots.add(child);
        child.setParent(null);
        child.mark = false;
    }

    private void cascadingCut(FHeapNode node) {
        FHeapNode nodeParent = (FHeapNode) node.getParent();

        if (nodeParent != null) {
            if (!node.mark) {
                node.mark = true;
            } else {
                cut(node, nodeParent);
                cascadingCut(nodeParent);
            }
        }
    }

    public SimpleEntry<K, V> extractMin() {
        HeapNode min = this.min;

        if (min != null) {
            roots.delete(roots.indexOfByAddress(min));
            CircularDoublyLinkedList<HeapNode> children = min.getChildren();

            while (0 < children.getSize()) {
                HeapNode child = children.get(0);
                children.delete(0);
                roots.add(child);
                child.setParent(null);
            }

            this.size--;

            if (roots.getSize() == 0) {
                this.min = null;
            } else {
                this.min = roots.get(0);
                consolidate();
            }
        }

        if (min != null) {
            return new SimpleEntry<>(min.getKey(), min.getValue());
        } else {
            return null;
        }
    }

    private void consolidate() {
        List<FHeapNode> degreesBuffer = Stream
                .generate(() -> (FHeapNode) null)
                .limit(log2(this.size) + 2)
                .collect(Collectors.toList());

        for (int i = 0; i < roots.getSize(); i++) {
            HeapNode temp = roots.get(i);
            int degree = temp.degree;

            while (degreesBuffer.get(degree) != null) {
                FHeapNode sameDegreeNode = degreesBuffer.get(degree);

                temp = link(temp, sameDegreeNode);

                degreesBuffer.set(degree, null);
                degree++;
                i--;
            }

            degreesBuffer.set(degree, (FHeapNode) temp);
        }

        this.min = null;

        for (int i = 0; i < log2(this.size) + 2; i++) {
            HeapNode heapNode = degreesBuffer.get(i);

            if (heapNode != null) {
                if (this.min == null) {
                    this.roots = new CircularDoublyLinkedList<>();
                    roots.add(heapNode);
                    this.min = heapNode;

                } else {
                    roots.add(heapNode);

                    if (heapNode.getKey().compareTo(this.min.getKey()) < 0) {
                        this.min = heapNode;
                    }
                }
            }
        }
    }

    private static int log2(int number) {
        return (int) (Math.log(number) / Math.log(2));
    }
}