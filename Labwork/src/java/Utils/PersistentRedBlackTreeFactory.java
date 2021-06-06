package Utils;

import Trees.AbstractBinaryTree;
import Trees.PersistentRedBlackTree;

public class PersistentRedBlackTreeFactory implements Factory {
    @Override
    public AbstractBinaryTree<Integer, Integer> createBinaryTree() {
        return new PersistentRedBlackTree<>();
    }
}