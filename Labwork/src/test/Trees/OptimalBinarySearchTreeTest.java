package Trees;

import org.junit.Test;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OptimalBinarySearchTreeTest {

    private final OptimalBinarySearchTree<Integer, Integer> testTree;

    public OptimalBinarySearchTreeTest() {
        List<SimpleEntry<Integer, Integer>> values = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            values.add(new SimpleEntry<>(i, null));
        }
        double[] nodeProbabilities = {0.15, 0.1, 0.05, 0.1, 0.2};
        double[] fictiveProbabilities = {0.05, 0.1, 0.05, 0.05, 0.05, 0.1};
        testTree = new OptimalBinarySearchTree<>(values, nodeProbabilities, fictiveProbabilities);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void insertTest() {
        testTree.insert(1, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void deleteTest() {
        testTree.delete(1);
    }

    @Test
    public void toStringTest() {
        assertEquals("\n" +
                "          5\n" +
                "                    4\n" +
                "                              3\n" +
                "2\n" +
                "          1", testTree.toString());
    }
}