package Trees;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.Random;

import static org.junit.Assert.*;

public class BTreeTest {

    private BTree<Integer, ?> testBTree;
    private static Random random;

    @BeforeClass
    public static void setUpBTreeTest() {
        random = new Random();
    }

    @Before
    public void setUp() {
        testBTree = new BTree<>(new Random().nextInt(10) + 2);
    }

    @Test
    public void insertTest() {
        for (int i = 0; i < 1000; i++) {
            testBTree.insert(random.nextInt(1000), null);

            try {
                assertTrue(checkTreeProperties(testBTree.getRoot()));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Test
    public void insertGrowingSequenceTest() {
        for (int i = 0; i < 1000; i++) {
            testBTree.insert(i, null);

            try {
                assertTrue(checkTreeProperties(testBTree.getRoot()));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Test
    public void insertFallingSequenceTest() {
        for (int i = 1000; i >= 0; i--) {
            testBTree.insert(i, null);

            try {
                assertTrue(checkTreeProperties(testBTree.getRoot()));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Test
    public void deleteTest() {
        for (int i = 0; i < 1000; i++) {
            testBTree.insert(random.nextInt(1000), null);
        }

        for (int i = 0; i < 500; i++) {
            Integer key = random.nextInt(1000);
            AbstractMap.SimpleEntry<Integer, ?> buffer = testBTree.delete(key);

            if (testBTree.search(key) != null) {
                assertEquals(key, buffer.getKey());
            }

            try {
                assertTrue(checkTreeProperties(testBTree.getRoot()));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Test
    public void deleteInGrowingOrderTest() {
        for (int i = 0; i < 1000; i++) {
            testBTree.insert(i, null);
        }

        for (int i = 0; i < 1000; i++) {
            Integer key = i;
            boolean isKeyInTree = testBTree.search(key) != null;
            AbstractMap.SimpleEntry<Integer, ?> buffer = testBTree.delete(key);

            if (isKeyInTree) {
                assertEquals(key, buffer.getKey());
            }

            try {
                assertTrue(checkTreeProperties(testBTree.getRoot()));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Test
    public void deleteInFallingOrderTest() {
        for (int i = 0; i < 1000; i++) {
            testBTree.insert(i, null);
        }

        for (int i = 1000; i >= 0; i--) {
            Integer key = i;
            boolean isKeyInTree = testBTree.search(key) != null;
            AbstractMap.SimpleEntry<Integer, ?> buffer = testBTree.delete(key);

            if (isKeyInTree) {
                assertEquals(key, buffer.getKey());
            }

            try {
                assertTrue(checkTreeProperties(testBTree.getRoot()));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Test
    public void bigDataTest() {
        int i;
        System.out.println("t: " + testBTree.getT());

        for (i = 0; i < 100000; i++) {
            testBTree.insert(random.nextInt(10000), null);

            try {
                assertTrue(checkTreeProperties(testBTree.getRoot()));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        for (int j = i; j >= 0; j--) {
            Integer key = random.nextInt(10000);
            testBTree.delete(key);

            try {
                assertTrue(checkTreeProperties(testBTree.getRoot()));
            } catch (Exception e) {
                System.out.println(e.getCause().getMessage());
            }
        }

        try {
            assertTrue(checkTreeProperties(testBTree.getRoot()));
        } catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    public void searchTest() {
        for (int i = 0; i < 100; i++) {
            testBTree.insert(i, null);
        }

        Integer key = random.nextInt(100);
        assertEquals(key, testBTree.search(key).getKey());
    }

    @Test
    public void searchNotExistKey() {
        for (int i = 0; i < 100; i++) {
            testBTree.insert(i, null);
        }

        Integer key = 101;
        assertNull(testBTree.search(key));
    }

    @Test
    public void toStringTest() {
        BTree<Integer, String> bTree = new BTree<>(3);

        for (int i = 0; i < 100; i++) {
            bTree.insert(i, null);
        }

        assertEquals("\n" +
                "                              0=null\n" +
                "                              1=null\n" +
                "                    2=null\n" +
                "                              3=null\n" +
                "                              4=null\n" +
                "                    5=null\n" +
                "                              6=null\n" +
                "                              7=null\n" +
                "          8=null\n" +
                "                              9=null\n" +
                "                              10=null\n" +
                "                    11=null\n" +
                "                              12=null\n" +
                "                              13=null\n" +
                "                    14=null\n" +
                "                              15=null\n" +
                "                              16=null\n" +
                "          17=null\n" +
                "                              18=null\n" +
                "                              19=null\n" +
                "                    20=null\n" +
                "                              21=null\n" +
                "                              22=null\n" +
                "                    23=null\n" +
                "                              24=null\n" +
                "                              25=null\n" +
                "26=null\n" +
                "                              27=null\n" +
                "                              28=null\n" +
                "                    29=null\n" +
                "                              30=null\n" +
                "                              31=null\n" +
                "                    32=null\n" +
                "                              33=null\n" +
                "                              34=null\n" +
                "          35=null\n" +
                "                              36=null\n" +
                "                              37=null\n" +
                "                    38=null\n" +
                "                              39=null\n" +
                "                              40=null\n" +
                "                    41=null\n" +
                "                              42=null\n" +
                "                              43=null\n" +
                "          44=null\n" +
                "                              45=null\n" +
                "                              46=null\n" +
                "                    47=null\n" +
                "                              48=null\n" +
                "                              49=null\n" +
                "                    50=null\n" +
                "                              51=null\n" +
                "                              52=null\n" +
                "53=null\n" +
                "                              54=null\n" +
                "                              55=null\n" +
                "                    56=null\n" +
                "                              57=null\n" +
                "                              58=null\n" +
                "                    59=null\n" +
                "                              60=null\n" +
                "                              61=null\n" +
                "          62=null\n" +
                "                              63=null\n" +
                "                              64=null\n" +
                "                    65=null\n" +
                "                              66=null\n" +
                "                              67=null\n" +
                "                    68=null\n" +
                "                              69=null\n" +
                "                              70=null\n" +
                "          71=null\n" +
                "                              72=null\n" +
                "                              73=null\n" +
                "                    74=null\n" +
                "                              75=null\n" +
                "                              76=null\n" +
                "                    77=null\n" +
                "                              78=null\n" +
                "                              79=null\n" +
                "          80=null\n" +
                "                              81=null\n" +
                "                              82=null\n" +
                "                    83=null\n" +
                "                              84=null\n" +
                "                              85=null\n" +
                "                    86=null\n" +
                "                              87=null\n" +
                "                              88=null\n" +
                "          89=null\n" +
                "                              90=null\n" +
                "                              91=null\n" +
                "                    92=null\n" +
                "                              93=null\n" +
                "                              94=null\n" +
                "                    95=null\n" +
                "                              96=null\n" +
                "                              97=null\n" +
                "                              98=null\n" +
                "                              99=null", bTree.toString());
    }

    private <K extends Comparable<K>, V> boolean checkTreeProperties(BTree<K, V>.Node node) throws Exception{
        boolean isCorrect = true;

        for (int i = 0; i < node.children.size(); i++) {
            isCorrect &= checkTreeProperties(node.children.get(i));
        }

        isCorrect &= checkNode(node);

        return isCorrect;
    }

    private <K extends Comparable<K>, V> boolean checkNode(BTree<K, V>.Node node) throws Exception {
        boolean isCorrect = true;

        if (node.keys.size() != node.amountOfKeys) {
            throw new Exception("node.keys.size() != node.amountOfKeys");
        } else if (node.keys.size() < testBTree.getT() - 1 && node != testBTree.getRoot()) {
            throw new Exception("node.keys.size() < testBTree.getT() - 1");
        } else if (node.keys.size() > testBTree.getT() * 2 - 1) {
            throw new Exception("node.keys.size() > 2t - 1");
        } else if (!node.isLeaf && node.children.size() < testBTree.getT() && node != testBTree.getRoot()) {
            throw new Exception("node.children.size() < t");
        } else if (node.children.size() > testBTree.getT() * 2) {
            throw new Exception("node.children.size() > 2t");
        } else if (!node.isLeaf && node.keys.size() + 1 != node.children.size()) {
            throw new Exception("node.keys.size() + 1 != node.children.size()");
        }

        for (int i = 0; i < node.keys.size() - 1; i++) {
            if (node.keys.get(i).getKey().compareTo(node.keys.get(i + 1).getKey()) > 0) {
                isCorrect = false;
                break;
            }
        }

        return isCorrect;
    }
}