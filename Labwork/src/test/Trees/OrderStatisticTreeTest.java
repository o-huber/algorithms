package Trees;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OrderStatisticTreeTest extends RedBlackTreeTest {

    private OrderStatisticTree<Integer, Integer> testOrderStatisticTree;

    @Override
    protected RedBlackTree<Integer, Integer> getRedBlackTree() {
        return new OrderStatisticTree<>();
    }

    @Before
    @Override
    public void setUp() {
        super.setUp();
        testOrderStatisticTree = (OrderStatisticTree<Integer, Integer>) testRedBlackTree;
    }

    @Test
    public void getRankTest() {
        for (int i = 1; i <= 100; i++) {
            testOrderStatisticTree.insert(i, null);
        }

        assertEquals(25, testOrderStatisticTree.getRank(25));
    }

    @Test
    public void getRankIncorrectKeyTest() {
        for (int i = 1; i <= 100; i++) {
            testOrderStatisticTree.insert(i, null);
        }

        assertEquals(-1, testOrderStatisticTree.getRank(1000));
    }

    @Test
    public void selectOSTTest() {
        for (int i = 1; i <= 100; i++) {
            testOrderStatisticTree.insert(i, null);
        }

        assertEquals(100, (int) testOrderStatisticTree.selectOST(100).getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void selectOSTNegativeRankTest() {
        for (int i = 1; i <= 100; i++) {
            testOrderStatisticTree.insert(i, null);
        }

        testOrderStatisticTree.selectOST(-1);
    }

    @Test
    public void selectOSTBiggerRankTest() {
        for (int i = 1; i <= 100; i++) {
            testOrderStatisticTree.insert(i, null);
        }

        assertNull(testOrderStatisticTree.selectOST(101));
    }

    @Test
    public void equalityOfGetRankAndSelectOSTMethods() {
        for (int i = 1; i <= 100; i++) {
            testOrderStatisticTree.insert(i, null);
        }

        assertEquals(50,
                (int) testOrderStatisticTree.selectOST(testOrderStatisticTree.getRank(50)).getKey());
    }

    @Test
    @Override
    public void toStringTest() {
        OrderStatisticTree<Integer, ?> orderStatisticTree = new OrderStatisticTree<>();

        for (int i = 0; i < 50; i++) {
            orderStatisticTree.insert(i, null);
        }

        assertEquals("\n" +
                "                                                                                49=size:1\n" +
                "                                                                      48=size:2\n" +
                "                                                            47=size:4\n" +
                "                                                                      46=size:1\n" +
                "                                                  45=size:6\n" +
                "                                                            44=size:1\n" +
                "                                        43=size:10\n" +
                "                                                            42=size:1\n" +
                "                                                  41=size:3\n" +
                "                                                            40=size:1\n" +
                "                              39=size:18\n" +
                "                                                            38=size:1\n" +
                "                                                  37=size:3\n" +
                "                                                            36=size:1\n" +
                "                                        35=size:7\n" +
                "                                                            34=size:1\n" +
                "                                                  33=size:3\n" +
                "                                                            32=size:1\n" +
                "                    31=size:26\n" +
                "                                                  30=size:1\n" +
                "                                        29=size:3\n" +
                "                                                  28=size:1\n" +
                "                              27=size:7\n" +
                "                                                  26=size:1\n" +
                "                                        25=size:3\n" +
                "                                                  24=size:1\n" +
                "          23=size:34\n" +
                "                                        22=size:1\n" +
                "                              21=size:3\n" +
                "                                        20=size:1\n" +
                "                    19=size:7\n" +
                "                                        18=size:1\n" +
                "                              17=size:3\n" +
                "                                        16=size:1\n" +
                "15=size:50\n" +
                "                                        14=size:1\n" +
                "                              13=size:3\n" +
                "                                        12=size:1\n" +
                "                    11=size:7\n" +
                "                                        10=size:1\n" +
                "                              9=size:3\n" +
                "                                        8=size:1\n" +
                "          7=size:15\n" +
                "                                        6=size:1\n" +
                "                              5=size:3\n" +
                "                                        4=size:1\n" +
                "                    3=size:7\n" +
                "                                        2=size:1\n" +
                "                              1=size:3\n" +
                "                                        0=size:1", orderStatisticTree.toString());
    }
}