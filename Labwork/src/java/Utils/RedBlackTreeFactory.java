package Utils;

import Trees.AbstractBinaryTree;
import Trees.RedBlackTree;

public class RedBlackTreeFactory implements Factory {
    @Override
    public AbstractBinaryTree<Integer, Integer> createBinaryTree() {
        return new RedBlackTree<>();
    }
}