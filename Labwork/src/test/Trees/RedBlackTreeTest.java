package Trees;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class RedBlackTreeTest {

    protected RedBlackTree<Integer, Integer> testRedBlackTree;
    protected RedBlackTreeTestingUtils<Integer, Integer> testingUtils;
    protected static Random random;

    protected RedBlackTree<Integer, Integer> getRedBlackTree() {
        return new RedBlackTree<>();
    }

    @BeforeClass
    public static void setUpRedBlackTreeTest() {
        random = new Random();
    }

    @Before
    public void setUp() {
        testRedBlackTree = getRedBlackTree();
        testingUtils = new RedBlackTreeTestingUtils<>(testRedBlackTree);
    }

    @Test
    public void insertTest() {
        for (int i = 0; i < 1000; i++) {
            testRedBlackTree.insert(random.nextInt(), random.nextInt());
            assertTrue(testingUtils.checkTreeProperties((RedBlackTree<Integer, Integer>.RBNode) testRedBlackTree.getRoot()));
            assertTrue(testingUtils.checkTreeCorrectness((RedBlackTree<Integer, Integer>.RBNode) testRedBlackTree.getRoot()));
        }
    }

    @Test
    public void insertGrowingSequenceTest() {
        for (int i = 0; i < 1000; i++) {
            testRedBlackTree.insert(i, null);
            assertTrue(testingUtils.checkTreeProperties((RedBlackTree<Integer, Integer>.RBNode) testRedBlackTree.getRoot()));
            assertTrue(testingUtils.checkTreeCorrectness((RedBlackTree<Integer, Integer>.RBNode) testRedBlackTree.getRoot()));
        }
    }

    @Test
    public void insertFallingSequenceTest() {
        for (int i = 1000; i >= 0; i--) {
            testRedBlackTree.insert(i, null);
            assertTrue(testingUtils.checkTreeProperties((RedBlackTree<Integer, Integer>.RBNode) testRedBlackTree.getRoot()));
            assertTrue(testingUtils.checkTreeCorrectness((RedBlackTree<Integer, Integer>.RBNode) testRedBlackTree.getRoot()));
        }
    }

    @Test
    public void deleteTest() {
        for (int i = 0; i < 1000; i++) {
            testRedBlackTree.insert(random.nextInt(1000), null);
        }

        for (int i = 0; i < 500; i++) {
            Integer key = random.nextInt(1000);
            Integer valueByKey = testRedBlackTree.search(key);
            Integer buffer = testRedBlackTree.delete(key);

            assertEquals(valueByKey, buffer);

            assertTrue(testingUtils.checkTreeProperties((RedBlackTree<Integer, Integer>.RBNode) testRedBlackTree.getRoot()));
            assertTrue(testingUtils.checkTreeCorrectness((RedBlackTree<Integer, Integer>.RBNode) testRedBlackTree.getRoot()));
        }
    }

    @Test
    public void deleteInGrowingOrderTest() {
        for (int i = 0; i < 1000; i++) {
            testRedBlackTree.insert(i, null);
        }

        for (int i = 0; i < 1000; i++) {
            Integer key = i;
            Integer valueByKey = testRedBlackTree.search(key);
            Integer buffer = testRedBlackTree.delete(key);

            assertEquals(valueByKey, buffer);

            assertTrue(testingUtils.checkTreeProperties((RedBlackTree<Integer, Integer>.RBNode) testRedBlackTree.getRoot()));
            assertTrue(testingUtils.checkTreeCorrectness((RedBlackTree<Integer, Integer>.RBNode) testRedBlackTree.getRoot()));
        }
    }

    @Test
    public void deleteInFallingOrderTest() {
        for (int i = 0; i < 1000; i++) {
            testRedBlackTree.insert(i, null);
        }

        for (int i = 1000; i >= 0; i--) {
            Integer key = i;
            Integer valueByKey = testRedBlackTree.search(key);
            Integer buffer = testRedBlackTree.delete(key);

            assertEquals(valueByKey, buffer);

            assertTrue(testingUtils.checkTreeProperties((RedBlackTree<Integer, Integer>.RBNode) testRedBlackTree.getRoot()));
            assertTrue(testingUtils.checkTreeCorrectness((RedBlackTree<Integer, Integer>.RBNode) testRedBlackTree.getRoot()));
        }
    }

    @Test
    public void bigDataTest() {
        int i;

        for (i = 0; i < 100000; i++) {
            testRedBlackTree.insert(random.nextInt(10000), null);
        }

        for (int j = i; j >= 0; j--) {
            Integer key = random.nextInt(10000);
            Integer valueByKey = testRedBlackTree.search(key);
            Integer buffer = testRedBlackTree.delete(key);

            assertEquals(valueByKey, buffer);
        }

        try {
            assertTrue(testingUtils.checkTreeProperties((RedBlackTree<Integer, Integer>.RBNode) testRedBlackTree.getRoot()));
            assertTrue(testingUtils.checkTreeCorrectness((RedBlackTree<Integer, Integer>.RBNode) testRedBlackTree.getRoot()));
        } catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    public void searchTest() {
        int key = random.nextInt(100);
        int valueBuffer = 0;

        for (int i = 0; i < 100; i++) {
            int value = random.nextInt();
            testRedBlackTree.insert(i, value);

            if (i == key) {
                valueBuffer = value;
            }
        }

        assertEquals((Integer) valueBuffer, testRedBlackTree.search(key));
    }

    @Test
    public void searchNotExistKey() {
        for (int i = 0; i < 100; i++) {
            testRedBlackTree.insert(i, random.nextInt());
        }

        Integer key = 101;
        assertNull(testRedBlackTree.search(key));
    }

    @Test
    public void toStringTest() {
        RedBlackTree<Integer, ?> redBlackTree = new RedBlackTree<>();

        for (int i = 0; i < 50; i++) {
            redBlackTree.insert(i, null);
        }

        assertEquals("\n" +
                "                                                                                49=false\n" +
                "                                                                      48=true\n" +
                "                                                            47=false\n" +
                "                                                                      46=true\n" +
                "                                                  45=true\n" +
                "                                                            44=true\n" +
                "                                        43=false\n" +
                "                                                            42=true\n" +
                "                                                  41=true\n" +
                "                                                            40=true\n" +
                "                              39=true\n" +
                "                                                            38=true\n" +
                "                                                  37=true\n" +
                "                                                            36=true\n" +
                "                                        35=false\n" +
                "                                                            34=true\n" +
                "                                                  33=true\n" +
                "                                                            32=true\n" +
                "                    31=false\n" +
                "                                                  30=true\n" +
                "                                        29=true\n" +
                "                                                  28=true\n" +
                "                              27=true\n" +
                "                                                  26=true\n" +
                "                                        25=true\n" +
                "                                                  24=true\n" +
                "          23=true\n" +
                "                                        22=true\n" +
                "                              21=true\n" +
                "                                        20=true\n" +
                "                    19=true\n" +
                "                                        18=true\n" +
                "                              17=true\n" +
                "                                        16=true\n" +
                "15=true\n" +
                "                                        14=true\n" +
                "                              13=true\n" +
                "                                        12=true\n" +
                "                    11=true\n" +
                "                                        10=true\n" +
                "                              9=true\n" +
                "                                        8=true\n" +
                "          7=true\n" +
                "                                        6=true\n" +
                "                              5=true\n" +
                "                                        4=true\n" +
                "                    3=true\n" +
                "                                        2=true\n" +
                "                              1=true\n" +
                "                                        0=true", redBlackTree.toString());
    }
}