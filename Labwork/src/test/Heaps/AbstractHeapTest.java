package Heaps;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public abstract class AbstractHeapTest {

    private AbstractHeap<Integer, Integer> testHeap;
    private static Random random;

    public abstract AbstractHeap<Integer, Integer> getHeap();

    @BeforeClass
    public static void setUpClass() {
        random = new Random();
    }

    @Before
    public void setUp() {
        testHeap = getHeap();
    }

    @Test
    public void insertTest() {
        assertEquals(0, testHeap.size);

        for (int i = 0; i < 100; i++) {
            testHeap.insert(random.nextInt(), null);
        }

        assertEquals(100, testHeap.size);
    }

    @Test
    public void deleteTest() {
        for (int i = 0; i < 10000; i++) {
            testHeap.insert(random.nextInt(1000), null);
        }

        int size = 10000;
        assertEquals(size, testHeap.size);

        for (int i = 0; i < 5000; i++) {
            int key = random.nextInt(1000);

            if (testHeap.delete(key)) {
                assertEquals(--size, testHeap.size);
            }
        }
    }

    @Test
    public void deleteAllValuesInGrowingSequence() {
        for (int i = 0; i < 100; i++) {
            testHeap.insert(i, i);
        }

        for (int i = 0; i < 100; i++) {
            testHeap.delete(i);
            assertEquals(100 - i - 1, testHeap.size);
        }

        assertNull(testHeap.extractMin());
    }

    @Test
    public void deleteAllValuesInFallingSequence() {
        for (int i = 0; i < 100; i++) {
            testHeap.insert(i, i);
        }

        for (int i = 99; i >= 0; i--) {
            testHeap.delete(i);
            assertEquals(i, testHeap.size);
        }

        assertNull(testHeap.extractMin());
    }

    @Test
    public void extractMinTest() {
        for (int i = 99; i >= 0; i--) {
            testHeap.insert(i, null);
        }

        for (int i = 0; i < 100; i++) {
            assertEquals((Integer) i, testHeap.extractMin().getKey());
        }
    }

    @Test
    public void extractMinBigDataTest() {
        List<Integer> keys = new ArrayList<>();

        for (int i = 0; i < 10000; i++) {
            int key = random.nextInt(1000);
            keys.add(key);
            testHeap.insert(key, null);
        }

        keys.sort(Integer::compareTo);

        for (Integer key : keys) {
            assertEquals(key, testHeap.extractMin().getKey());
        }
    }

    @Test
    public void decreaseKeyTest() {
        for (int i = 0; i < 100; i++) {
            if (i == 50) {
                testHeap.insert(i, 100);
            } else {
                testHeap.insert(i, null);
            }
        }
        assertEquals((Integer) 0, testHeap.extractMin().getKey());

        testHeap.decreaseKey(50, -1);

        SimpleEntry<Integer, Integer> buffer = testHeap.extractMin();
        assertEquals(-1, (int) buffer.getKey());
        assertEquals(100, (int) buffer.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void decreaseKeyNewKeyBiggerThanOldTest() {
        for (int i = 0; i < 100; i++) {
            testHeap.insert(i, null);
        }
        assertEquals((Integer) 0, testHeap.extractMin().getKey());

        testHeap.decreaseKey(50, 51);
    }
}