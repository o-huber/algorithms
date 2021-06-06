package Trees;

import java.util.AbstractMap.SimpleEntry;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public abstract class AbstractBinaryTree<K extends Comparable<K>, V> implements Iterable<SimpleEntry<K, V>> {

    private Node root;
    protected Node NIL;

    protected class Node {
        private final K key;
        private V value;
        private Node parent, left, right;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return this.key;
        }

        public V getValue() {
            return this.value;
        }

        public void setValue(V newValue) {
            this.value = newValue;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        @Override
        public String toString() {
            return String.valueOf(this.getKey());
        }
    }

    public V search(K key) {
        Node searchNode = searchNode(this.getRoot(), key);

        if (searchNode != null) {
            return searchNode.getValue();
        } else {
            return null;
        }
    }

    public abstract void insert(K key, V value);

    public abstract V delete(K key);

    public int size() {
        return size(getRoot());
    }

    private int size(Node node) {
        if (node == NIL) {
            return 0;
        } else {
            return size(node.getLeft()) + size(node.getRight()) + 1;
        }
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public Iterator<SimpleEntry<K, V>> iterator() {
        return new BinaryTreeIterator(this.getRoot());
    }

    protected Node searchNode(Node root, K key)  {
        Node temp = root;

        while (temp != NIL) {
            if (temp.getKey().equals(key)) {
                return temp;
            } else if (temp.getKey().compareTo(key) < 0) {
                temp = temp.getRight();
            } else {
                temp = temp.getLeft();
            }
        }

        return null;
    }

    protected void leftRotate(Node root) {
        Node newRoot = root.getRight();
        root.setRight(newRoot.getLeft());

        if (newRoot.getLeft() != NIL) {
            newRoot.getLeft().setParent(root);
        }

        newRoot.setParent(root.getParent());

        if (root.getParent() == NIL) {
            setRoot(newRoot);
        } else if (root == root.getParent().getLeft()) {
            root.getParent().setLeft(newRoot);
        } else {
            root.getParent().setRight(newRoot);
        }

        newRoot.setLeft(root);
        root.setParent(newRoot);
    }

    protected void rightRotate(Node root) {
        Node newRoot = root.getLeft();
        root.setLeft(newRoot.getRight());

        if (newRoot.getRight() != NIL) {
            newRoot.getRight().setParent(root);
        }

        newRoot.setParent(root.getParent());

        if (root.getParent() == NIL) {
            setRoot(newRoot);
        } else if (root == root.getParent().getRight()) {
            root.getParent().setRight(newRoot);
        } else {
            root.getParent().setLeft(newRoot);
        }

        newRoot.setRight(root);
        root.setParent(newRoot);
    }

    protected Node treeMinimum(Node root) {
        while (root.getLeft() != NIL) {
            root = root.getLeft();
        }

        return root;
    }

    protected void transplant(Node replacedNode, Node proxy) {
        if (replacedNode.getParent() == null) {
            setRoot(proxy);
        } else if (replacedNode == replacedNode.getParent().getLeft()) {
            replacedNode.getParent().setLeft(proxy);
        } else {
            replacedNode.getParent().setRight(proxy);
        }

        if (proxy != null) {
            proxy.setParent(replacedNode.getParent());
        }
    }

    protected Node getRoot() {
        return this.root;
    }

    protected void setRoot(Node newRoot) {
        this.root = newRoot;
    }

    private class BinaryTreeIterator implements Iterator<SimpleEntry<K, V>> {

        private final Stack<Node> stack;

        public BinaryTreeIterator(Node root) {
            stack = new Stack<>();

            while (root != NIL) {
                stack.push(root);
                root = root.getLeft();
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public SimpleEntry<K, V> next() {
            if (hasNext()) {
                Node temp = stack.pop();
                SimpleEntry<K, V> buffer = new SimpleEntry<>(temp.getKey(), temp.getValue());

                if (temp.getRight() != NIL) {
                    temp = temp.getRight();
                    while (temp != NIL) {
                        stack.push(temp);
                        temp = temp.getLeft();
                    }
                }
                return buffer;

            } else {
                throw new NoSuchElementException();
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder treeImagination = new StringBuilder();
        print(treeImagination, this.getRoot(), 0);
        return treeImagination.toString();
    }

    public void print(StringBuilder treeImagination, Node node, int amountOfSpaces) {
        if (node != this.NIL) {
            print(treeImagination, node.getRight(), amountOfSpaces + 10);

            treeImagination.append('\n').append(" ".repeat(Math.max(0, amountOfSpaces)));
            treeImagination.append(node);

            print(treeImagination, node.getLeft(), amountOfSpaces + 10);
        }
    }
}