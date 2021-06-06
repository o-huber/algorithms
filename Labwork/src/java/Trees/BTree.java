package Trees;

import java.util.ArrayList;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;

public class BTree<K extends Comparable<K>, V> {

    private Node root;
    private int t;

    class Node {
        public int amountOfKeys;
        public boolean isLeaf;
        public List<SimpleEntry<K, V>> keys;
        public List<Node> children;

        public Node() {
            this.amountOfKeys = 0;
            this.isLeaf = true;
            this.keys = new ArrayList<>();
            this.children = new ArrayList<>();
        }
    }

    public BTree(int t) {
        this.root = new Node();
        assert t > 1;
        this.t = t;
    }

    public SimpleEntry<K, V> search(K key) {
        SimpleEntry<Node, Integer> searchNode = searchBTree(root, key);

        if (searchNode != null) {
            List<SimpleEntry<K, V>> keys = searchNode.getKey().keys;
            Integer indexOfSearchKey = searchNode.getValue();

            return keys.get(indexOfSearchKey);
        } else {
            return null;
        }
    }

    public void insert(K key, V value) {
        SimpleEntry<K, V> insertEntry = new SimpleEntry<>(key, value);

        if (this.root.amountOfKeys == 2 * t - 1) {
            Node temp = new Node();
            temp.isLeaf = false;
            temp.amountOfKeys = 0;
            temp.children.add(this.root);
            this.root = temp;
            splitChildBTree(temp, 0);
        }

        insertNonFull(root, insertEntry);
    }

    public SimpleEntry<K, V> delete(K key) {
        return deleteBTree(this.root, key);
    }

    private SimpleEntry<Node, Integer> searchBTree(Node root, K key) {
        int i = 0;

        while (i < root.amountOfKeys && key.compareTo(root.keys.get(i).getKey()) > 0) {
            i++;
        }

        if (i < root.amountOfKeys && key.compareTo(root.keys.get(i).getKey()) == 0) {
            return new SimpleEntry<>(root, i);
        } else if (root.isLeaf) {
            return null;
        } else {
            return searchBTree(root.children.get(i), key);
        }
    }

    private void insertNonFull(Node root, SimpleEntry<K, V> insertEntry) {
        int i = root.amountOfKeys - 1;
        while (i >= 0 && insertEntry.getKey().compareTo(root.keys.get(i).getKey()) < 0) {
            i--;
        }

        if (root.isLeaf) {
            if (i >= 0 && insertEntry.getKey().compareTo(root.keys.get(i).getKey()) == 0) {
                root.keys.set(i, insertEntry);
            } else {
                root.keys.add(i + 1, insertEntry);
                root.amountOfKeys++;
            }
        } else {
            i++;

            if (root.children.get(i).amountOfKeys == 2 * t - 1) {
                splitChildBTree(root, i);

                if (insertEntry.getKey().compareTo(root.keys.get(i).getKey()) > 0) {
                    i++;
                }
            }

            insertNonFull(root.children.get(i), insertEntry);
        }
    }

    private void splitChildBTree(Node root, int index) {
        Node newLeftChild = root.children.get(index);
        Node newRightChild = new Node();
        newRightChild.isLeaf = newLeftChild.isLeaf;
        newRightChild.amountOfKeys = t - 1;
        SimpleEntry<K, V> median = newLeftChild.keys.get(t - 1);

        for (int i = 0; i < t; i++) {
            if (i < t - 1) {
                newRightChild.keys.add(newLeftChild.keys.get(t));
            }

            if (!newLeftChild.isLeaf) {
                newRightChild.children.add(newLeftChild.children.get(t));
                newLeftChild.children.remove(t);
            }

            newLeftChild.keys.remove(t - 1);
        }
        newLeftChild.amountOfKeys = newLeftChild.keys.size();

        root.keys.add(index, median);
        root.children.add(index + 1, newRightChild);

        root.amountOfKeys++;
    }

    private SimpleEntry<K, V> deleteBTree(Node root, K key) {
        if (root == null) {
            return null;
        }

        int i = 0;

        while (i < root.amountOfKeys && root.keys.get(i).getKey().compareTo(key) < 0) {
            i++;
        }

        if (i < root.amountOfKeys) {
            SimpleEntry<K, V> foundKey = root.keys.get(i);

            if (root.isLeaf && foundKey.getKey().compareTo(key) == 0) {
                root.keys.remove(i);
                root.amountOfKeys--;
                return foundKey;
            } else if (!root.isLeaf) {
                if (foundKey.getKey().compareTo(key) == 0) {
                    Node previousChild = root.children.get(i);
                    SimpleEntry<K, V> deletedEntry = root.keys.get(i);

                    if (previousChild.amountOfKeys >= t) {
                        root.keys.set(i, getProxyForDelete(root, i, true));
                        return deletedEntry;
                    } else {
                        Node nextChild = root.children.get(i + 1);

                        if (nextChild.amountOfKeys >= t) {
                            root.keys.set(i, getProxyForDelete(root, i + 1, false));
                            return deletedEntry;
                        } else {

                            previousChild.keys.add(root.keys.get(i));
                            previousChild.amountOfKeys++;
                            mergeNodes(previousChild, nextChild);

                            root.keys.remove(i);
                            root.children.remove(i + 1);
                            root.amountOfKeys--;

                            return deleteBTree(previousChild, key);
                        }
                    }
                } else {
                    return deleteBTree(descent(root, i), key);
                }
            }
        } else {
            return deleteBTree(descent(root, i), key);
        }

        return null;
    }

    private Node descent(Node node, int index) {
        if (node.isLeaf) {
            return null;
        }

        List<Node> children = node.children;
        Node childByIndex = children.get(index);

        if (childByIndex.amountOfKeys >= t) {
            return childByIndex;
        } else if (index > 0 && children.get(index - 1).amountOfKeys >= t) {
            Node previousChild = children.get(index - 1);

            if (!childByIndex.isLeaf) {
                childByIndex.children.add(0, previousChild.children.get(previousChild.amountOfKeys));
                previousChild.children.remove(previousChild.amountOfKeys);
            }

            childByIndex.keys.add(0, node.keys.get(index - 1));

            node.keys.set(index - 1, previousChild.keys.get(previousChild.amountOfKeys - 1));
            previousChild.keys.remove(previousChild.amountOfKeys - 1);

            childByIndex.amountOfKeys++;
            previousChild.amountOfKeys--;

            return childByIndex;

        } else if (index < node.amountOfKeys && children.get(index + 1).amountOfKeys >= t) {
            Node nextChild = children.get(index + 1);

            childByIndex.keys.add(node.keys.get(index));
            node.keys.set(index, nextChild.keys.get(0));

            if (!childByIndex.isLeaf) {
                childByIndex.children.add(nextChild.children.get(0));
                nextChild.children.remove(0);
            }

            nextChild.keys.remove(0);

            childByIndex.amountOfKeys++;
            nextChild.amountOfKeys--;

            return childByIndex;

        } else {
            Node nextChild;
            Node previousChild;

            if (this.root == node && node.amountOfKeys == 1) {
                if (index == 0) {
                    this.root = previousChild = children.get(index);
                    nextChild = children.get(index + 1);
                } else {
                    this.root = previousChild = children.get(index - 1);
                    nextChild = children.get(index);
                }
            } else if (index < node.amountOfKeys) {
                nextChild = children.get(index + 1);
                previousChild = children.get(index);
            } else {
                nextChild = children.get(index);
                previousChild = children.get(index - 1);
            }

            if (index < node.amountOfKeys) {
                previousChild.keys.add(node.keys.get(index));
            } else {
                previousChild.keys.add(node.keys.get(index - 1));
            }
            previousChild.amountOfKeys++;
            mergeNodes(previousChild, nextChild);

            if (index < node.amountOfKeys) {
                node.keys.remove(index);
                node.children.remove(index + 1);
            } else {
                node.keys.remove(index - 1);
                node.children.remove(index);
            }
            node.amountOfKeys--;

            return previousChild;
        }
    }

    private void mergeNodes(Node previousChild, Node nextChild) {
        for (int j = 0; j < nextChild.amountOfKeys; j++) {
            previousChild.keys.add(nextChild.keys.get(j));
            previousChild.amountOfKeys++;

            if (!previousChild.isLeaf) {
                previousChild.children.add(nextChild.children.get(j));
            }
        }

        if (!previousChild.isLeaf) {
            previousChild.children.add(nextChild.children.get(nextChild.amountOfKeys));
        }
    }

    @SuppressWarnings("ConstantConditions")
    private SimpleEntry<K, V> getProxyForDelete(Node node, int index, boolean inLeftSubTree) {
        Node buffer = node.children.get(index);
        assert buffer != null;

        if (inLeftSubTree) {
            while (!buffer.isLeaf) {
                buffer = descent(buffer, buffer.amountOfKeys);
            }

            return deleteBTree(buffer, buffer.keys.get(buffer.amountOfKeys - 1).getKey());
        } else {
            while (!buffer.isLeaf) {
                buffer = descent(buffer, 0);
            }

            return deleteBTree(buffer, buffer.keys.get(0).getKey());
        }
    }

    @Override
    public String toString() {
        StringBuilder treeImagination = new StringBuilder();
        print(treeImagination, this.root, 0);
        return treeImagination.toString();
    }

    private void print(StringBuilder treeImagination, Node node, int n) {
        if (node != null) {
            if (node.isLeaf) {
                for (int i = 0; i < node.keys.size(); i++) {
                    treeImagination.append('\n').append(" ".repeat(Math.max(0, n)));
                    treeImagination.append(node.keys.get(i));
                }
            }

            for (int i = 0; i < node.children.size(); i++) {
                print(treeImagination, node.children.get(i), n + 10);

                if (i < node.keys.size()) {
                    treeImagination.append('\n').append(" ".repeat(Math.max(0, n)));
                    treeImagination.append(node.keys.get(i));
                }
            }
        }
    }

    Node getRoot() {
        return root;
    }

    public int getT() {
        return t;
    }
}