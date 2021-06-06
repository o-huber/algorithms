package Trees;

import java.util.function.Consumer;

public class RedBlackTree<K extends Comparable<K>, V> extends AbstractBinaryTree<K, V> {

    protected class RBNode extends Node {
        private boolean color;
        private RBNode parent, left, right;

        public RBNode(K key, V value) {
            super(key, value);
            this.color = true;
            parent = left = right = (RBNode) NIL;
        }

        public boolean getColor() {
            return this.color;
        }

        public void setColorBlack() {
            this.color = true;
        }

        public void setColorRed() {
            this.color = false;
        }

        @Override
        public RBNode getParent() {
            return parent;
        }

        @Override
        public void setParent(Node parent) {
            this.parent = (RBNode) parent;
        }

        @Override
        public RBNode getLeft() {
            return left;
        }

        @Override
        public void setLeft(Node left) {
            this.left = (RBNode) left;
        }

        @Override
        public RBNode getRight() {
            return right;
        }

        @Override
        public void setRight(Node right) {
            this.right = (RBNode) right;
        }

        @Override
        public String toString() {
            return this.getKey() + "=" + this.getColor();
        }
    }

    public RedBlackTree() {
        this.NIL = new RBNode(null, null);
        super.setRoot(this.NIL);
    }

    @Override
    public void insert(K key, V value) {
        RBNode searchNode = (RBNode) searchNode(this.getRoot(), key);

        if (searchNode == null) {
            insertRBNode(new RBNode(key, value));
        } else {
            searchNode.setValue(value);
        }
    }

    protected void insertRBNode(RBNode insertedNode) {
        RBNode currentNode = (RBNode) this.getRoot(), previousNode = (RBNode) NIL;

        while (currentNode != NIL) {
            previousNode = currentNode;

            if (insertedNode.getKey().compareTo(currentNode.getKey()) < 0) {
                currentNode = currentNode.getLeft();
            } else {
                currentNode = currentNode.getRight();
            }
        }

        insertedNode.setParent(previousNode);

        if (previousNode == NIL) {
            this.setRoot(insertedNode);
        } else if (insertedNode.getKey().compareTo(previousNode.getKey()) < 0) {
            previousNode.setLeft(insertedNode);
        } else {
            previousNode.setRight(insertedNode);
        }

        insertedNode.setLeft(NIL);
        insertedNode.setRight(NIL);
        insertedNode.setColorRed();

        insertFixup(insertedNode);
    }

    protected void insertFixup(RBNode currentNode) {
        while (!currentNode.getParent().getColor()) {
            if (currentNode.getParent() == currentNode.getParent().getParent().getLeft()) {
                RBNode rightUncle = currentNode.getParent().getParent().getRight();
                currentNode = fixPropertiesInsert(currentNode, rightUncle, true);
            } else {
                RBNode leftUncle = currentNode.getParent().getParent().getLeft();
                currentNode = fixPropertiesInsert(currentNode, leftUncle, false);
            }
        }

        ((RBNode) this.getRoot()).setColorBlack();
    }

    protected RBNode fixPropertiesInsert(RBNode currentNode, RBNode uncle, boolean isParentsLeftChild) {
        if (isParentsLeftChild) {
            if (!uncle.getColor()) {
                currentNode.getParent().setColorBlack();
                uncle.setColorBlack();
                currentNode.getParent().getParent().setColorRed();
                currentNode = currentNode.getParent().getParent();

            } else {
                if (currentNode == currentNode.getParent().getRight()) {
                    currentNode = currentNode.getParent();
                    leftRotate(currentNode);
                }

                currentNode.getParent().setColorBlack();
                currentNode.getParent().getParent().setColorRed();
                rightRotate(currentNode.getParent().getParent());
            }
        } else {
            if (!uncle.getColor()) {
                currentNode.getParent().setColorBlack();
                uncle.setColorBlack();
                currentNode.getParent().getParent().setColorRed();
                currentNode = currentNode.getParent().getParent();

            } else {
                if (currentNode == currentNode.getParent().getLeft()) {
                    currentNode = currentNode.getParent();
                    rightRotate(currentNode);
                }

                currentNode.getParent().setColorBlack();
                currentNode.getParent().getParent().setColorRed();
                leftRotate(currentNode.getParent().getParent());
            }
        }

        return currentNode;
    }

    @Override
    public V delete(K key) {
        RBNode deletedNode = (RBNode) searchNode(this.getRoot(), key);

        if (deletedNode != null) {
            deleteRBNode(deletedNode);
            return deletedNode.getValue();
        } else {
            return null;
        }
    }

    protected void deleteRBNode(RBNode deletedNode) {
        RBNode suspect, suspectsProxy;
        boolean suspectsOriginalColor;

        if (deletedNode != null) {
            suspect = deletedNode;
            suspectsOriginalColor = suspect.getColor();

            if (deletedNode.getLeft() == NIL) {
                suspectsProxy = deletedNode.getRight();
                transplantRBTree(deletedNode, deletedNode.getRight());
            } else if (deletedNode.getRight() == NIL) {
                suspectsProxy = deletedNode.getLeft();
                transplantRBTree(deletedNode, deletedNode.getLeft());
            } else {
                suspect = (RBNode) treeMinimum(deletedNode.getRight());
                suspectsOriginalColor = suspect.getColor();
                suspectsProxy = replaceDeletedNode(deletedNode, suspect);
            }

            if (suspectsOriginalColor) {
                deleteFixup(suspectsProxy);
            }
        }
    }

    protected final RBNode replaceDeletedNode(RBNode deletedNode, RBNode suspect) {
        return replaceDeletedNode(deletedNode, suspect, null);
    }

    protected RBNode replaceDeletedNode(RBNode deletedNode, RBNode suspect, Consumer<RBNode> processAfterTransplant) {
        RBNode suspectsProxy = suspect.getRight();

        if (suspect.getParent() == deletedNode) {
            suspectsProxy.setParent(suspect);
        } else {
            transplantRBTree(suspect, suspect.getRight());
            suspect.setRight(deletedNode.getRight());
            suspect.getRight().setParent(suspect);
        }

        transplantRBTree(deletedNode, suspect);

        if (processAfterTransplant != null) {
            processAfterTransplant.accept(deletedNode);
        }

        suspect.setLeft(deletedNode.getLeft());
        suspect.getLeft().setParent(suspect);

        if (deletedNode.getColor()) {
            suspect.setColorBlack();
        } else {
            suspect.setColorRed();
        }

        return suspectsProxy;
    }

    protected void deleteFixup(RBNode currentNode) {
        while (currentNode != this.getRoot() && currentNode.getColor()) {
            if (currentNode == currentNode.getParent().getLeft()) {
                RBNode rightSibling = handleSiblingRed(currentNode, currentNode.getParent().getRight(), true);

                if (rightSibling.getLeft().getColor() && rightSibling.getRight().getColor()) {
                    rightSibling.setColorRed();
                    currentNode = currentNode.getParent();
                } else {
                    currentNode = handleSiblingsChildRed(currentNode, rightSibling, true);
                }
            } else {
                RBNode leftSibling = handleSiblingRed(currentNode, currentNode.getParent().getLeft(), false);

                if (leftSibling.getRight().getColor() && leftSibling.getLeft().getColor()) {
                    leftSibling.setColorRed();
                    currentNode = currentNode.getParent();
                } else {
                    currentNode = handleSiblingsChildRed(currentNode, leftSibling, false);
                }
            }
        }

        currentNode.setColorBlack();
    }

    protected RBNode handleSiblingRed(RBNode currentNode, RBNode sibling, boolean isSiblingRight) {
        if (!sibling.getColor()) {
            if (isSiblingRight) {
                sibling.setColorBlack();
                currentNode.getParent().setColorRed();
                leftRotate(currentNode.getParent());
                sibling = currentNode.getParent().getRight();

            } else {
                sibling.setColorBlack();
                currentNode.getParent().setColorRed();
                rightRotate(currentNode.getParent());
                sibling = currentNode.getParent().getLeft();
            }
        }

        return sibling;
    }

    protected final RBNode handleSiblingsChildRed(RBNode currentNode, RBNode sibling, boolean isSiblingRight) {
        return handleSiblingsChildRed(currentNode, sibling, isSiblingRight,
                null, null);
    }

    protected RBNode handleSiblingsChildRed(RBNode currentNode, RBNode sibling, boolean isSiblingRight,
                                            Consumer<RBNode> beforeFirstChildChangingColor,
                                            Consumer<RBNode> beforeSecondChildChangingColor) {

        if (isSiblingRight) {
            if (sibling.getRight().getColor()) {
                if (beforeFirstChildChangingColor != null) {
                    beforeFirstChildChangingColor.accept(sibling);
                }

                sibling.getLeft().setColorBlack();
                sibling.setColorRed();
                rightRotate(sibling);
                sibling = currentNode.getParent().getRight();
            }

            if (currentNode.getParent().getColor()) {
                sibling.setColorBlack();
            } else {
                sibling.setColorRed();
            }

            currentNode.getParent().setColorBlack();

            if (beforeSecondChildChangingColor != null) {
                beforeSecondChildChangingColor.accept(sibling);
            }

            sibling.getRight().setColorBlack();
            leftRotate(currentNode.getParent());
        } else {
            if (sibling.getLeft().getColor()) {
                if (beforeFirstChildChangingColor != null) {
                    beforeFirstChildChangingColor.accept(sibling);
                }

                sibling.getRight().setColorBlack();
                sibling.setColorRed();
                leftRotate(sibling);
                sibling = currentNode.getParent().getLeft();
            }

            if (currentNode.getParent().getColor()) {
                sibling.setColorBlack();
            } else {
                sibling.setColorRed();
            }

            currentNode.getParent().setColorBlack();

            if (beforeSecondChildChangingColor != null) {
                beforeSecondChildChangingColor.accept(sibling);
            }

            sibling.getLeft().setColorBlack();
            rightRotate(currentNode.getParent());
        }

        currentNode = (RBNode) this.getRoot();

        return currentNode;
    }

    protected void transplantRBTree(RBNode replacedNode, RBNode proxy) {
        if (replacedNode.getParent() == NIL) {
            this.setRoot(proxy);
        } else if (replacedNode == replacedNode.getParent().getLeft()) {
            replacedNode.getParent().setLeft(proxy);
        } else {
            replacedNode.getParent().setRight(proxy);
        }

        proxy.setParent(replacedNode.getParent());
    }

    @Override
    public String toString() {
        StringBuilder treeImagination = new StringBuilder();
        print(treeImagination, this.getRoot(), 0);
        return treeImagination.toString();
    }
}