package Trees;

import Utils.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class AbstractBinaryTreeTest {

    private AbstractBinaryTree<Integer, Integer> testTree;
    private static Factory treeFactory;
    private static Random random;

    @BeforeClass
    public static void setUpUtils() {
        treeFactory = new SplayTreeFactory();
        random = new Random();
    }

    @Before
    public void setUp() {
        testTree = treeFactory.createBinaryTree();
    }

    @Test
    public void searchOnEmptyTree() {
        assertNull(testTree.search(1));
    }

    @Test
    public void searchTest() {
        assertEquals(0, testTree.size());
        int i;
        for (i = 0; i < 100; i++) {
            testTree.insert(i, 100 - i);
        }
        i -= 25;
        assertEquals(100, testTree.size());

        assertEquals(Integer.valueOf(100 - i), testTree.search(i));
    }

    @Test
    public void searchInEmptyTree() {
        assertTrue(testTree.isEmpty());
        assertNull(testTree.search(1));
        assertTrue(testTree.isEmpty());
    }

    @Test
    public void searchNotExistKey() {
        int i;
        for (i = 0; i < 100; i++) {
            testTree.insert(i, 100 - i);
        }

        assertNull(testTree.search(101));
    }

    @Test
    public void insertTest() {
        assertTrue(testTree.isEmpty());
        assertEquals(0, testTree.size());
        testTree.insert(1, 1);
        assertFalse(testTree.isEmpty());
        assertEquals(1, testTree.size());
    }

    @Test
    public void deleteTest() {
        List<Integer> keys = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Integer key = random.nextInt();
            keys.add(key);
            testTree.insert(key, i);

            assertEquals(i + 1, testTree.size());
        }

        assertFalse(testTree.isEmpty());

        IntStream.range(0, keys.size()).map(i -> keys.size() - 1 - i).mapToObj(keys::get).forEach(testTree::delete);

        assertTrue(testTree.isEmpty());
        assertEquals(0, testTree.size());
    }

    @Test
    public void deleteNonExistKey() {
        testTree.insert(1, 1);
        assertFalse(testTree.isEmpty());
        assertNull(testTree.delete(2));
        assertFalse(testTree.isEmpty());
    }

    @Test
    public void deleteFromEmptyTree() {
        assertTrue(testTree.isEmpty());
        assertNull(testTree.delete(1));
        assertTrue(testTree.isEmpty());
    }

    @Test
    public void deleteOneElementMultipleTimes() {
        for (int i = 0; i < 5; i++) {
            testTree.insert(i, i);
        }
        assertEquals(5, testTree.size());
        assertFalse(testTree.isEmpty());
        assertNotNull(testTree.delete(1));
        assertEquals(4, testTree.size());
        assertFalse(testTree.isEmpty());
        assertNull(testTree.delete(1));
        assertEquals(4, testTree.size());
        assertNull(testTree.delete(1));
        assertEquals(4, testTree.size());
        assertNull(testTree.delete(1));
        assertEquals(4, testTree.size());
        assertNull(testTree.delete(1));
        assertEquals(4, testTree.size());
        assertNull(testTree.delete(1));
        assertFalse(testTree.isEmpty());
        assertEquals(4, testTree.size());
    }

    @Test
    public void bigDataTest() {
        assertEquals(0, testTree.size());

        int i;
        for (i = 0; i < 1000; i++) {
            testTree.insert(i, random.nextInt());
            assertEquals(i + 1, testTree.size());
        }
        int sizeBuffer = i - 1;

        for (i = 0; i < 100; i++) {
            assertNotNull(testTree.search(random.nextInt(1000)));
        }

        for (i = 0; i < 1000; i++) {
            Integer buffer = testTree.delete(random.nextInt(1000));

            if (buffer != null) {
                assertEquals(sizeBuffer--, testTree.size());
            }
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void iteratorNextOnEmptyTree() {
        Iterator<SimpleEntry<Integer, Integer>> iterator = testTree.iterator();
        assertFalse(iterator.hasNext());
        iterator.next();
    }

    @Test
    public void iteratorHasNextTest() {
        testTree.insert(1, 1);

        Iterator<SimpleEntry<Integer, Integer>> iterator = testTree.iterator();
        assertTrue(iterator.hasNext());

        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void iteratorBeyondOutTest() {
        testTree.insert(1, 1);

        Iterator<SimpleEntry<Integer, Integer>> iterator = testTree.iterator();
        iterator.next();
        iterator.next();
    }

    @Test
    public void iteratorForEachSequenceTest() {
        for (int i = 0; i < 20; i++) {
            testTree.insert(i, null);
        }

        StringBuilder tree = new StringBuilder();
        for (SimpleEntry<Integer, Integer> buffer : testTree) {
            tree.append(buffer.getKey()).append(',').append(' ');
        }
        tree.delete(tree.length() - 2, tree.length());

        assertEquals("0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19",
                tree.toString());
    }

    @Test
    public void iteratorForEachTest() {
        testTree.insert(50, null);
        testTree.insert(2, null);
        testTree.insert(123, null);
        testTree.insert(121, null);
        testTree.insert(75, null);
        testTree.insert(-12, null);
        testTree.insert(154, null);
        testTree.insert(1920, null);
        testTree.insert(1, null);
        testTree.insert(-43, null);
        testTree.insert(21, null);
        testTree.insert(892, null);
        testTree.insert(55, null);
        testTree.insert(10, null);
        testTree.insert(15, null);

        StringBuilder tree = new StringBuilder();
        for (SimpleEntry<Integer, Integer> buffer : testTree) {
            tree.append(buffer.getKey()).append(',').append(' ');
        }
        tree.delete(tree.length() - 2, tree.length());

        assertEquals("-43, -12, 1, 2, 10, 15, 21, 50, 55, 75, 121, 123, 154, 892, 1920",
                tree.toString());
    }
}