package Trees;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PersistentRedBlackTreeTest extends RedBlackTreeTest {

    private PersistentRedBlackTree<Integer, Integer> testPersistentRedBlackTree;

    @Override
    protected RedBlackTree<Integer, Integer> getRedBlackTree() {
        return new PersistentRedBlackTree<>();
    }

    @Before
    @Override
    public void setUp() {
        super.setUp();
        testPersistentRedBlackTree = (PersistentRedBlackTree<Integer, Integer>) testRedBlackTree;
    }

    @Test
    public void deleteFromEmptySet() {
        assertNull(testPersistentRedBlackTree.delete(1));
    }

    @Test
    public void undoTest() {
        for (int i = 0; i < 10; i++) {
            testPersistentRedBlackTree.insert(i, i);
        }
        assertEquals("-----New version-----\n" +
                "0=true\n" +
                "-----New version-----\n" +
                "          1=false\n" +
                "0=true\n" +
                "-----New version-----\n" +
                "          2=false\n" +
                "1=true\n" +
                "          0=false\n" +
                "-----New version-----\n" +
                "                    3=false\n" +
                "          2=true\n" +
                "1=true\n" +
                "          0=true\n" +
                "-----New version-----\n" +
                "                    4=false\n" +
                "          3=true\n" +
                "                    2=false\n" +
                "1=true\n" +
                "          0=true\n" +
                "-----New version-----\n" +
                "                              5=false\n" +
                "                    4=true\n" +
                "          3=false\n" +
                "                    2=true\n" +
                "1=true\n" +
                "          0=true\n" +
                "-----New version-----\n" +
                "                              6=false\n" +
                "                    5=true\n" +
                "                              4=false\n" +
                "          3=false\n" +
                "                    2=true\n" +
                "1=true\n" +
                "          0=true\n" +
                "-----New version-----\n" +
                "                              7=false\n" +
                "                    6=true\n" +
                "          5=false\n" +
                "                    4=true\n" +
                "3=true\n" +
                "                    2=true\n" +
                "          1=false\n" +
                "                    0=true\n" +
                "-----New version-----\n" +
                "                              8=false\n" +
                "                    7=true\n" +
                "                              6=false\n" +
                "          5=false\n" +
                "                    4=true\n" +
                "3=true\n" +
                "                    2=true\n" +
                "          1=false\n" +
                "                    0=true\n" +
                "-----New version-----\n" +
                "                                        9=false\n" +
                "                              8=true\n" +
                "                    7=false\n" +
                "                              6=true\n" +
                "          5=true\n" +
                "                    4=true\n" +
                "3=true\n" +
                "                    2=true\n" +
                "          1=true\n" +
                "                    0=true\n", testPersistentRedBlackTree.toString());

        testPersistentRedBlackTree.undo();

        assertEquals("-----New version-----\n" +
                "0=true\n" +
                "-----New version-----\n" +
                "          1=false\n" +
                "0=true\n" +
                "-----New version-----\n" +
                "          2=false\n" +
                "1=true\n" +
                "          0=false\n" +
                "-----New version-----\n" +
                "                    3=false\n" +
                "          2=true\n" +
                "1=true\n" +
                "          0=true\n" +
                "-----New version-----\n" +
                "                    4=false\n" +
                "          3=true\n" +
                "                    2=false\n" +
                "1=true\n" +
                "          0=true\n" +
                "-----New version-----\n" +
                "                              5=false\n" +
                "                    4=true\n" +
                "          3=false\n" +
                "                    2=true\n" +
                "1=true\n" +
                "          0=true\n" +
                "-----New version-----\n" +
                "                              6=false\n" +
                "                    5=true\n" +
                "                              4=false\n" +
                "          3=false\n" +
                "                    2=true\n" +
                "1=true\n" +
                "          0=true\n" +
                "-----New version-----\n" +
                "                              7=false\n" +
                "                    6=true\n" +
                "          5=false\n" +
                "                    4=true\n" +
                "3=true\n" +
                "                    2=true\n" +
                "          1=false\n" +
                "                    0=true\n" +
                "-----New version-----\n" +
                "                              8=false\n" +
                "                    7=true\n" +
                "                              6=false\n" +
                "          5=false\n" +
                "                    4=true\n" +
                "3=true\n" +
                "                    2=true\n" +
                "          1=false\n" +
                "                    0=true\n", testPersistentRedBlackTree.toString());
    }

    @Test
    public void undoFirstOperationTest() {
        testPersistentRedBlackTree.insert(1, 1);

        assertEquals("-----New version-----\n" +
                "1=true\n", testPersistentRedBlackTree.toString());

        testPersistentRedBlackTree.undo();

        assertEquals("", testPersistentRedBlackTree.toString());
    }

    @Test(expected = IllegalCallerException.class)
    public void undoOnEmptySetTest() {
        testPersistentRedBlackTree.undo();
    }

    @Test
    public void clearTest() {
        for (int i = 0; i < 5; i++) {
            testPersistentRedBlackTree.insert(i, i);
        }
        assertEquals("-----New version-----\n" +
                "0=true\n" +
                "-----New version-----\n" +
                "          1=false\n" +
                "0=true\n" +
                "-----New version-----\n" +
                "          2=false\n" +
                "1=true\n" +
                "          0=false\n" +
                "-----New version-----\n" +
                "                    3=false\n" +
                "          2=true\n" +
                "1=true\n" +
                "          0=true\n" +
                "-----New version-----\n" +
                "                    4=false\n" +
                "          3=true\n" +
                "                    2=false\n" +
                "1=true\n" +
                "          0=true\n", testPersistentRedBlackTree.toString());

        testPersistentRedBlackTree.clear();

        assertEquals("", testPersistentRedBlackTree.toString());
    }

    @Test
    public void clearOnEmptySetTest() {
        assertEquals("", testPersistentRedBlackTree.toString());
        testPersistentRedBlackTree.clear();
        assertEquals("", testPersistentRedBlackTree.toString());
    }
}