package Utils;

import Trees.AbstractBinaryTree;

public interface Factory {
    AbstractBinaryTree<Integer, Integer> createBinaryTree();
}