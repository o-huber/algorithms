package Trees;

import java.util.ArrayList;
import java.util.List;

public class RedBlackTreeTestingUtils<K extends Comparable<K>, V> {

    private RedBlackTree<K, V> tree;

    public RedBlackTreeTestingUtils(RedBlackTree<K , V> tree) {
        this.tree = tree;
    }

    @SuppressWarnings("ConstantConditions")
    public boolean checkTreeProperties(RedBlackTree<K, V>.RBNode root) {
        boolean isCorrect = true;

        if (root != tree.NIL) {
            isCorrect &= checkTreeProperties(root.getLeft());

            isCorrect &= checkNode(root);

            isCorrect &= checkTreeProperties(root.getRight());
        }

        return isCorrect;
    }

    private boolean checkNode(RedBlackTree<K, V>.RBNode node) {
        boolean isCorrect = true;

        if (node == tree.getRoot() && !node.getColor()) {
            isCorrect = false;
        }

        if (node == tree.NIL && !node.getColor()) {
            isCorrect = false;
        }

        if (!node.getColor() && (!node.getLeft().getColor() || !node.getRight().getColor())) {
            isCorrect = false;
        }

        if (tree instanceof OrderStatisticTree) {
            OrderStatisticTree<K, V>.OSNode temp = (OrderStatisticTree<K, V>.OSNode) node;

            if (temp.getSize() != temp.getLeft().getSize() + temp.getRight().getSize() + 1) {
                isCorrect = false;
            }
        }

        try {
            checkBlackHeight(node);
        } catch (Exception e) {
            isCorrect = false;
        }

        return isCorrect;
    }

    public int checkBlackHeight(RedBlackTree<K, V>.RBNode node) throws Exception {
        if (node == tree.NIL) {
            return 1;
        } else {
            int leftBlackHeight = checkBlackHeight(node.getLeft()) + (node.getColor() ? 1 : 0);
            int rightBlackHeight = checkBlackHeight(node.getRight()) + (node.getColor() ? 1 : 0);

            if (leftBlackHeight != rightBlackHeight) {
                throw new Exception("Left black height is " + (leftBlackHeight < rightBlackHeight ? "less" : "more") +
                        "than right black height");
            }

            return leftBlackHeight;
        }
    }

    public boolean checkTreeCorrectness(RedBlackTree<K, V>.RBNode root) {
        List<K> keys = new ArrayList<>();
        collectingKeysTraverse(root, keys);
        boolean isCorrect = true;

        for (int i = 0; i < keys.size() - 1; i++) {
            if (keys.get(i).compareTo(keys.get(i + 1)) > 0) {
                isCorrect = false;
                break;
            }
        }

        return isCorrect;
    }

    private void collectingKeysTraverse(RedBlackTree<K, V>.RBNode root, List<K> keys) {
        if (root != tree.NIL) {
            collectingKeysTraverse(root.getLeft(), keys);

            keys.add(root.getKey());

            collectingKeysTraverse(root.getRight(), keys);
        }
    }
}
