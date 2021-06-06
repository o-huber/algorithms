package Heaps;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.Assert.*;

public class CircularDoublyLinkedListTest {

    private CircularDoublyLinkedList<Integer> testList;
    private static Random random;

    @BeforeClass
    public static void setUpUtils() {
        random = new Random();
    }

    @Before
    public void setUp() {
        testList = new CircularDoublyLinkedList<>();
    }

    @Test
    public void isEmptyTest() {
        assertTrue(testList.isEmpty());
        testList.add(1);
        assertFalse(testList.isEmpty());
    }

    @Test
    public void addTest() {
        int value;
        assertEquals(0, testList.getSize());

        for (int i = 1; i <= 10; i++) {
            testList.add(value = random.nextInt());
            assertEquals(i, testList.getSize());
            assertEquals(Integer.valueOf(value), testList.get(testList.getSize() - 1));
        }
    }

    @Test
    public void insertAtStartTest() {
        int value;
        assertEquals(0, testList.getSize());

        for (int i = 1; i <= 10; i++) {
            testList.insertAtStart(value = random.nextInt());
            assertEquals(i, testList.getSize());
            assertEquals(Integer.valueOf(value), testList.get(0));
        }
    }

    @Test
    public void deleteTest() {
        for (int i = 0; i < 10; i++) {
            testList.add(i);
        }
        assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]", testList.toString());

        testList.delete(4);

        assertEquals(9, testList.getSize());
        assertEquals("[0, 1, 2, 3, 5, 6, 7, 8, 9]", testList.toString());
    }

    @Test
    public void deleteAllElementsTest() {
        for (int i = 0; i < 10; i++) {
            testList.add(i);
        }
        assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]", testList.toString());

        for (int i = 9; i >= 0; i--) {
            testList.delete(i);
        }

        assertTrue(testList.isEmpty());
        assertEquals(0, testList.getSize());
        assertEquals("[]", testList.toString());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void deleteNotExistIndex() {
        testList.delete(0);
    }

    @Test
    public void indexOfTest() {
        for (int i = 0; i < 10; i++) {
            testList.add(i);
        }

        assertEquals(9, testList.indexOf(9));
        assertEquals(-1, testList.indexOf(10));
    }

    @Test
    public void indexOfByAddressTest() {
        class Test {
            public final int field;

            public Test(int field) {
                this.field = field;
            }
        }

        CircularDoublyLinkedList<Test> list = new CircularDoublyLinkedList<>();

        Test buffer1 = new Test(-1);
        Test buffer2 = new Test(-1);

        list.add(buffer1);
        for (int i = 0; i < 10; i++) {
            list.add(new Test(i));
        }
        list.add(buffer2);

        assertEquals(0, list.indexOf(buffer1));
        assertEquals(11, list.indexOf(buffer2));
    }

    @Test
    public void indexOfNullTest() {
        for (int i = 0; i < 10; i++) {
            testList.add(i);
        }

        testList.add(null);

        assertEquals(10, testList.indexOf(null));
    }

    @Test
    public void bigDataTest() {
        int value;
        assertEquals(0, testList.getSize());

        for (int i = 0; i < 10_000; i++) {
            testList.insertAtStart(value = random.nextInt(1000));
            assertEquals(i + 1, testList.getSize());
            assertEquals(Integer.valueOf(value), testList.get(0));
        }

        int size = testList.getSize();
        for (int i = 0; i < 10_000; i++) {
            value = random.nextInt(1000);
            int index = testList.indexOf(value);

            if (index != -1) {
                assertEquals(Integer.valueOf(value), testList.get(index));
                testList.delete(index);
                assertEquals(--size, testList.getSize());
            }
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void iteratorOutOfBoundTest() {
        testList.add(1);

        Iterator<Integer> iterator = testList.iterator();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
        iterator.next();
    }

    @Test
    public void iteratorForEachTest() {
        for (int i = 0; i < 10; i++) {
            testList.add(i);
        }

        StringBuilder list = new StringBuilder();
        for (Integer buffer : testList) {
            list.append(buffer).append(',').append(' ');
        }
        list.delete(list.length() - 2, list.length());

        assertEquals("0, 1, 2, 3, 4, 5, 6, 7, 8, 9", list.toString());
    }
}