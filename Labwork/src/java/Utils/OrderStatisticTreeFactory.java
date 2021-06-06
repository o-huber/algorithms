package Utils;

import Trees.AbstractBinaryTree;
import Trees.OrderStatisticTree;

public class OrderStatisticTreeFactory implements Factory {
    @Override
    public AbstractBinaryTree<Integer, Integer> createBinaryTree() {
        return new OrderStatisticTree<>();
    }
}