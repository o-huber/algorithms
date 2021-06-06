package Trees;

import java.util.ArrayList;
import java.util.List;

public class PersistentRedBlackTree<K extends Comparable<K>, V> extends RedBlackTree<K, V> {

    private final List<RBNode> roots;

    public PersistentRedBlackTree() {
        this.roots = new ArrayList<>();
    }

    @Override
    public void insert(K key, V value) {
        RBNode searchNode = null;

        if (roots.size() > 0) {
            searchNode = (RBNode) searchNode(getRoot(), key);
        }

        if (searchNode == null) {
            insertRBNode(new RBNode(key, value));
        } else {
            searchNode = (RBNode) searchNodeWithCopying(getRoot(), key);
            assert searchNode != null;
            searchNode.setValue(value);
        }
    }

    @Override
    public void insertRBNode(RBNode insertedNode) {
        RBNode currentNode = (RBNode) NIL, copyBuffer = (RBNode) NIL, latestRoot = (RBNode) NIL;

        if (roots.size() > 0 && getRoot() != NIL) {
            currentNode = (RBNode) getRoot();
            copyBuffer = deepCopy(currentNode);
            latestRoot = copyBuffer;
        }

        while (currentNode != NIL) {
            if (insertedNode.getKey().compareTo(currentNode.getKey()) < 0) {
                if (currentNode.getLeft() != NIL) {
                    RBNode left = deepCopy(currentNode.getLeft());
                    left.setParent(copyBuffer);
                    copyBuffer.setLeft(left);
                    copyBuffer = copyBuffer.getLeft();
                }

                currentNode = currentNode.getLeft();
            } else {
                if (currentNode.getRight() != NIL) {
                    RBNode right = deepCopy(currentNode.getRight());
                    right.setParent(copyBuffer);
                    copyBuffer.setRight(right);
                    copyBuffer = copyBuffer.getRight();
                }

                currentNode = currentNode.getRight();
            }
        }

        insertedNode.setParent(copyBuffer);

        if (roots.size() > 0 && getRoot() != NIL) {
            roots.add(latestRoot);

            if (insertedNode.getKey().compareTo(copyBuffer.getKey()) < 0) {
                copyBuffer.setLeft(insertedNode);
            } else {
                copyBuffer.setRight(insertedNode);
            }
        } else {
            roots.add(insertedNode);
        }

        insertedNode.setLeft(NIL);
        insertedNode.setRight(NIL);
        insertedNode.setColorRed();

        insertFixup(insertedNode);
    }

    @Override
    protected void insertFixup(RBNode currentNode) {
        while (!currentNode.getParent().getColor()) {
            if (currentNode.getParent() == currentNode.getParent().getParent().getLeft()) {
                RBNode rightUncle = currentNode.getParent().getParent().getRight();

                if (rightUncle != NIL) {
                    currentNode.getParent().getParent().setRight(deepCopy(rightUncle));
                    rightUncle = currentNode.getParent().getParent().getRight();
                }

                currentNode = fixPropertiesInsert(currentNode, rightUncle, true);
            } else {
                RBNode leftUncle = currentNode.getParent().getParent().getLeft();

                if (leftUncle != NIL) {
                    currentNode.getParent().getParent().setLeft(deepCopy(leftUncle));
                    leftUncle = currentNode.getParent().getParent().getLeft();
                }

                currentNode = fixPropertiesInsert(currentNode, leftUncle, false);
            }
        }

        ((RBNode) getRoot()).setColorBlack();
    }

    @Override
    public V delete(K key) {
        RBNode deletedNode = (RBNode) searchNodeWithCopying(getRoot(), key);

        if (deletedNode != null) {
            deleteRBNode(deletedNode);
            return deletedNode.getValue();
        } else {
            this.undo();
            return null;
        }
    }

    @Override
    protected void deleteRBNode(RBNode deletedNode) {
        RBNode suspect, suspectsProxy;
        boolean suspectsOriginalColor;

        if (deletedNode != null) {
            suspect = deletedNode;
            suspectsOriginalColor = suspect.getColor();

            if (deletedNode.getLeft() == NIL) {
                if (deletedNode.getRight() != NIL) {
                    deletedNode.setRight(deepCopy(deletedNode.getRight()));
                    deletedNode.getRight().setParent(deletedNode);
                }
                suspectsProxy = deletedNode.getRight();
                transplantRBTree(deletedNode, deletedNode.getRight());

            } else if (deletedNode.getRight() == NIL) {
                if (deletedNode.getLeft() != NIL) {
                    deletedNode.setLeft(deepCopy(deletedNode.getLeft()));
                    deletedNode.getLeft().setParent(deletedNode);
                }
                suspectsProxy = deletedNode.getLeft();
                transplantRBTree(deletedNode, deletedNode.getLeft());

            } else {
                deletedNode.setRight(deepCopy(deletedNode.getRight()));
                deletedNode.getRight().setParent(deletedNode);

                suspect = (RBNode) treeMinimum(deletedNode.getRight());
                suspectsOriginalColor = suspect.getColor();

                if (suspect.getRight() != NIL) {
                    suspect.setRight(deepCopy(suspect.getRight()));
                }

                suspectsProxy = replaceDeletedNode(deletedNode, suspect, (node) -> {
                    if (node.getLeft() != NIL) {
                        node.setLeft(deepCopy(node.getLeft()));
                        node.getLeft().setParent(node);
                    }
                });
            }

            if (suspectsOriginalColor) {
                deleteFixup(suspectsProxy);
            }
        }
    }

    @Override
    protected void deleteFixup(RBNode currentNode) {
        while (currentNode != getRoot() && currentNode.getColor()) {
            if (currentNode == currentNode.getParent().getLeft()) {
                currentNode.getParent().setRight(deepCopy(currentNode.getParent().getRight()));
                currentNode.getParent().getRight().setParent(currentNode.getParent());
                RBNode rightSibling = handleSiblingRed(currentNode, currentNode.getParent().getRight(), true);

                if (rightSibling.getLeft().getColor() && rightSibling.getRight().getColor()) {
                    rightSibling.setColorRed();
                    currentNode = currentNode.getParent();
                } else {
                    currentNode = handleSiblingsChildRed(currentNode, rightSibling, true, sibling -> {
                        sibling.setLeft(deepCopy(sibling.getLeft()));
                        sibling.getLeft().setParent(sibling);
                    }, sibling -> {
                        sibling.setRight(deepCopy(sibling.getRight()));
                        sibling.getRight().setParent(sibling);
                    });
                }
            } else {
                currentNode.getParent().setLeft(deepCopy(currentNode.getParent().getLeft()));
                currentNode.getParent().getLeft().setParent(currentNode.getParent());
                RBNode leftSibling = handleSiblingRed(currentNode, currentNode.getParent().getLeft(), false);

                if (leftSibling.getRight().getColor() && leftSibling.getLeft().getColor()) {
                    leftSibling.setColorRed();
                    currentNode = currentNode.getParent();
                } else {
                    currentNode = handleSiblingsChildRed(currentNode, leftSibling, false, sibling -> {
                        sibling.setRight(deepCopy(sibling.getRight()));
                        sibling.getRight().setParent(sibling);
                    }, sibling -> {
                        sibling.setLeft(deepCopy(sibling.getLeft()));
                        sibling.getLeft().setParent(sibling);
                    });
                }
            }
        }

        currentNode.setColorBlack();
    }

    @Override
    protected Node treeMinimum(Node root) {
        while (root.getLeft() != NIL) {
            RBNode left = deepCopy((RBNode) root.getLeft());
            left.setParent(root);
            root.setLeft(left);
            root = root.getLeft();
        }

        return root;
    }

    private Node searchNodeWithCopying(Node root, K key)  {
        Node temp = root, copyBuffer = deepCopy((RBNode) root);
        this.roots.add((RBNode) copyBuffer);

        while (temp != NIL) {
            if (temp.getKey().compareTo(key) > 0) {
                if (temp.getLeft() != NIL) {
                    RBNode left = deepCopy((RBNode) temp.getLeft());
                    left.setParent(copyBuffer);
                    copyBuffer.setLeft(left);
                    copyBuffer = copyBuffer.getLeft();
                }

                temp = temp.getLeft();
            } else if (temp.getKey().compareTo(key) < 0) {
                if (temp.getRight() != NIL) {
                    RBNode right = deepCopy((RBNode) temp.getRight());
                    right.setParent(copyBuffer);
                    copyBuffer.setRight(right);
                    copyBuffer = copyBuffer.getRight();
                }

                temp = temp.getRight();
            } else {
                return copyBuffer;
            }
        }

        return null;
    }

    private RBNode deepCopy(RBNode nodeToCopy) {
        RBNode buffer = new RBNode(nodeToCopy.getKey(), nodeToCopy.getValue());
        buffer.setLeft(nodeToCopy.getLeft());
        buffer.setRight(nodeToCopy.getRight());
        buffer.setParent(nodeToCopy.getParent());

        if (nodeToCopy.getColor()) {
            buffer.setColorBlack();
        } else {
            buffer.setColorRed();
        }

        return buffer;
    }

    public void undo() {
        if (roots.size() > 0) {
            roots.remove(roots.size() - 1);
        } else {
            throw new IllegalCallerException("Do operation before call undo()!");
        }
    }

    public void clear() {
        roots.clear();
    }

    @Override
    protected Node getRoot() {
        if (roots.size() > 0) {
            return this.roots.get(this.roots.size() - 1);
        } else {
            return this.NIL;
        }
    }

    @Override
    protected void setRoot(Node newRoot) {
        roots.set(roots.size() - 1, (RBNode) newRoot);
    }

    @Override
    public String toString() {
        StringBuilder treeImagination = new StringBuilder();

        for (RBNode root : roots) {
            treeImagination.append("-----New version-----");
            print(treeImagination, root, 0);
            treeImagination.append('\n');
        }

        return treeImagination.toString();
    }
}