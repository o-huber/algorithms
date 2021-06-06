package Utils;

import Trees.AbstractBinaryTree;
import Trees.SplayTree;

public class SplayTreeFactory implements Factory {
    @Override
    public AbstractBinaryTree<Integer, Integer> createBinaryTree() {
        return new SplayTree<>();
    }
}