package Trees;

public class SplayTree<K extends Comparable<K>, V> extends AbstractBinaryTree<K, V> {

    public SplayTree() {
        super.setRoot(null);
        this.NIL = null;
    }

    @Override
    public V search(K key) {
        Node searchNode = searchNode(this.getRoot(), key);

        if (searchNode != null) {
            splay(searchNode);
            return searchNode.getValue();

        } else {
            return null;
        }
    }

    @Override
    public void insert(K key, V value) {
        Node currentNode = this.getRoot(), previous = null;

        while (currentNode != null) {
            previous = currentNode;

            if (currentNode.getKey().compareTo(key) < 0) {
                currentNode = currentNode.getRight();
            } else {
                currentNode = currentNode.getLeft();
            }
        }

        Node insertElement = new Node(key, value);
        insertElement.setParent(previous);

        if (previous == null) {
            this.setRoot(insertElement);
        } else if (previous.getKey().compareTo(insertElement.getKey()) < 0) {
            previous.setRight(insertElement);
        } else {
            previous.setLeft(insertElement);
        }

        splay(insertElement);
    }

    @Override
    public V delete(K key) {
        Node deletedNode = searchNode(this.getRoot(), key);

        if (deletedNode != null) {
            if (deletedNode.getRight() == null)
                transplant(deletedNode, deletedNode.getLeft());
            else if (deletedNode.getLeft() == null)
                transplant(deletedNode, deletedNode.getRight());
            else {
                Node newLocalRoot = treeMinimum(deletedNode.getRight());

                if (newLocalRoot.getParent() != deletedNode) {
                    transplant(newLocalRoot, newLocalRoot.getRight());
                    newLocalRoot.setRight(deletedNode.getRight());
                    newLocalRoot.getRight().setParent(newLocalRoot);
                }

                transplant(deletedNode, newLocalRoot);
                newLocalRoot.setLeft(deletedNode.getLeft());
                newLocalRoot.getLeft().setParent(newLocalRoot);

                splay(newLocalRoot);
            }

            return deletedNode.getValue();
        }

        return null;
    }

    private void splay(Node node) {
        while (node != this.getRoot()) {
            if (node.getParent() == this.getRoot()) {
                if (node == node.getParent().getLeft())
                    rightRotate(node.getParent());
                else if (node == node.getParent().getRight()) {
                    leftRotate(node.getParent());
                }
            } else {
                if (node == node.getParent().getLeft() &&
                        node.getParent() == node.getParent().getParent().getLeft()) {

                    rightRotate(node.getParent().getParent());
                    rightRotate(node.getParent());

                } else if (node == node.getParent().getRight() &&
                        node.getParent() == node.getParent().getParent().getRight()) {

                    leftRotate(node.getParent().getParent());
                    leftRotate(node.getParent());

                } else if (node == node.getParent().getRight() &&
                        node.getParent() == node.getParent().getParent().getLeft()) {

                    leftRotate(node.getParent());
                    rightRotate(node.getParent());

                } else if (node == node.getParent().getLeft() &&
                        node.getParent() == node.getParent().getParent().getRight()) {

                    rightRotate(node.getParent());
                    leftRotate(node.getParent());
                }
            }
        }
    }
}