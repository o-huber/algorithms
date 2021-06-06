package Trees;

import java.util.AbstractMap.SimpleEntry;

public class OrderStatisticTree<K extends Comparable<K>, V> extends RedBlackTree<K, V> {

    class OSNode extends RBNode {
        private int size;
        private OSNode parent, left, right;

        public OSNode(K key, V value) {
            super(key, value);
            parent = left = right = (OSNode) NIL;
        }

        public int getSize() {
            return size;
        }

        public void incrementSize() {
            this.size++;
        }

        public void decrementSize() {
            this.size--;
        }

        @Override
        public OSNode getParent() {
            return parent;
        }

        @Override
        public void setParent(Node parent) {
            this.parent = (OSNode) parent;
        }

        @Override
        public OSNode getLeft() {
            return left;
        }

        @Override
        public void setLeft(Node left) {
            this.left = (OSNode) left;
        }

        @Override
        public OSNode getRight() {
            return right;
        }

        @Override
        public void setRight(Node right) {
            this.right = (OSNode) right;
        }

        @Override
        public String toString() {
            return this.getKey() + "=size:" + this.getSize();
        }
    }

    public OrderStatisticTree() {
        this.NIL = null;
        this.NIL = new OSNode(null, null);
        super.setRoot(NIL);
    }

    @Override
    public void insert(K key, V value) {
        OSNode searchNode = (OSNode) searchNode(this.getRoot(), key);

        if (searchNode == null) {
            OSNode node = new OSNode(key, value);
            node.incrementSize();
            increaseSizes(key);
            insertRBNode(node);
        } else {
            searchNode.setValue(value);
        }
    }

    @Override
    public V delete(K key) {
        OSNode deletedNode = (OSNode) searchNode(this.getRoot(), key);

        if (deletedNode != null) {
            decreaseSizes(key);
            deleteOSNode(deletedNode);
            return deletedNode.getValue();
        } else {
            return null;
        }
    }

    protected void deleteOSNode(OSNode deletedNode) {
        OSNode suspect, suspectsProxy;
        boolean suspectsOriginalColor;

        if (deletedNode != null) {
            suspect = deletedNode;
            suspectsOriginalColor = suspect.getColor();

            if (deletedNode.getLeft() == NIL) {
                suspectsProxy = deletedNode.getRight();
                transplantRBTree(deletedNode, deletedNode.getRight());

                if (suspectsProxy != NIL) {
                    suspectsProxy.size = suspectsProxy.left.size + suspectsProxy.right.size + 1;
                }
            } else if (deletedNode.getRight() == NIL) {
                suspectsProxy = deletedNode.getLeft();
                transplantRBTree(deletedNode, deletedNode.getLeft());

                if (suspectsProxy != NIL) {
                    suspectsProxy.size = suspectsProxy.left.size + suspectsProxy.right.size + 1;
                }
            } else {
                suspect = treeMinimumOS(deletedNode.getRight());
                suspectsOriginalColor = suspect.getColor();
                suspectsProxy = (OSNode) replaceDeletedNode(deletedNode, suspect);
                suspect.size = suspect.left.size + suspect.right.size + 1;

                if (deletedNode.getColor()) {
                    suspect.setColorBlack();
                } else {
                    suspect.setColorRed();
                }
            }

            if (suspectsOriginalColor) {
                deleteFixup(suspectsProxy);
            }
        }
    }

    public SimpleEntry<K, V> selectOST(int rank) {
        if (rank < 1) {
            throw new IllegalArgumentException("Illegal rank: " + rank);
        }

        OSNode node = recursiveSelect((OSNode) this.getRoot(), rank);

        if (node != null) {
            return new SimpleEntry<>(node.getKey(), node.getValue());
        } else {
            return null;
        }
    }

    private OSNode recursiveSelect(OSNode root, int rank) {
        if (root != null && root.getLeft() != null) {
            int temp = root.getLeft().getSize() + 1;

            if (rank == temp) {
                return root;
            } else if (rank < temp) {
                return recursiveSelect(root.getLeft(), rank);
            } else {
                return recursiveSelect(root.getRight(), rank - temp);
            }
        } else {
            return null;
        }
    }

    public int getRank(K key) {
        OSNode node = (OSNode) searchNode(this.getRoot(), key);

        if (node != null) {
            int rank = node.getLeft().getSize() + 1;

            while (node != this.getRoot()) {
                if (node == node.getParent().getRight()) {
                    rank += node.getParent().getLeft().getSize() + 1;
                }

                node = node.getParent();
            }

            return rank;
        } else {
            return -1;
        }
    }

    private void increaseSizes(K key) {
        OSNode temp = (OSNode) this.getRoot();

        while (temp != this.NIL) {
            temp.incrementSize();
            temp = temp.getKey().compareTo(key) <= 0 ? temp.getRight() : temp.getLeft();
        }
    }

    private void decreaseSizes(K key) {
        OSNode temp = (OSNode) this.getRoot();

        while (temp != this.NIL) {
            temp.decrementSize();

            if (temp.getKey().equals(key)) break;

            temp = temp.getKey().compareTo(key) <= 0 ? temp.getRight() : temp.getLeft();
        }
    }

    @Override
    protected void leftRotate(Node root) {
        OSNode temp = (OSNode) root;
        OSNode newRoot = temp.getRight();

        super.leftRotate(root);

        newRoot.size = temp.size;
        temp.size = temp.left.size + temp.right.size + 1;
    }

    @Override
    protected void rightRotate(Node root) {
        OSNode temp = (OSNode) root;
        OSNode newRoot = temp.getLeft();

        super.rightRotate(root);

        newRoot.size = temp.size;
        temp.size = temp.left.size + temp.right.size + 1;
    }

    private OSNode treeMinimumOS(OSNode root) {
        while (root.getLeft() != NIL) {
            root.decrementSize();
            root = root.getLeft();
        }
        return root;
    }

    @Override
    public String toString() {
        StringBuilder treeImagination = new StringBuilder();
        print(treeImagination, this.getRoot(), 0);
        return treeImagination.toString();
    }
}