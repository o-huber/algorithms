package PerfectHashing;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import PerfectHashing.PerfectHashMap.Pair;
import java.security.SecureRandom;
import java.util.*;

import static org.junit.Assert.*;

public class PerfectHashMapTest {

    private PerfectHashMap<Integer, Integer> perfectHashMap;
    private static SecureRandom random;

    @BeforeClass
    public static void setUpClass() {
        random = new SecureRandom();
    }

    @Before
    public void setUp() {
        Set<Pair<Integer, Integer>> set = new HashSet<>();

        for (int i = 0; i < 50; i++) {
            set.add(new Pair<>(i, i));
        }

        perfectHashMap = new PerfectHashMap<>(set);
    }

    @Test
    public void searchNotExistKey() {
        assertNull(perfectHashMap.get(51));
    }

    @Test
    public void getTest() {
        assertEquals(Integer.valueOf(1), perfectHashMap.get(1));
    }

    @Test
    public void bigDataTest() {
        Set<Pair<Integer, Integer>> set = new HashSet<>();
        List<Pair<Integer, Integer>> allValues = new ArrayList<>();

        for (int i = 0; i < 1_000_000; i++) {
            Integer key = Math.abs(random.nextInt());

            Pair<Integer, Integer> pair = new Pair<>(key, key);
            set.add(pair);
            allValues.add(new Pair<>(key, key));
        }

        PerfectHashMap<Integer, Integer> perfectHashMap = new PerfectHashMap<>(set);

        for (Pair<Integer, Integer> pair : allValues) {
            assertEquals(pair.getValue(), perfectHashMap.get(pair.getKey()));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void putNegativeKeyTest() {
        Set<Pair<Integer, Integer>> set = new HashSet<>();

        for (int i = 0; i < 10; i++) {
            set.add(new Pair<>(i, i));
        }
        set.add(new Pair<>(-1, -1));

        perfectHashMap = new PerfectHashMap<>(set);
    }

    @Test
    public void toStringTest() {
        assertEquals(25, perfectHashMap.toString().lines().count());
    }
}