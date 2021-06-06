package PerfectHashing;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;

/**
 * Key must be unique positive integer.
*/
public class PerfectHashMap<K extends Integer, V> {

    private InnerHashMap<K, V>[] perfectHashMap;

    private final Set<Pair<K, V>> immutableSet;
    private final long firstHashingValue, secondHashingValue, primeDivider;
    private final int hashMapSize;

    public static class Pair<K, V> extends SimpleEntry<K, V> {

        public Pair(K key, V value) {
            super(key, value);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            } else {
                SimpleEntry<?, ?> e = (SimpleEntry<?, ?>) o;
                return this.getKey() == null ? e.getKey() == null : this.getKey().equals(e.getKey());
            }
        }

        @Override
        public int hashCode() {
            return this.getKey().hashCode();
        }
    }

    private static class InnerHashMap<K extends Integer, V> {
        private final long firstHashingValue, secondHashingValue;
        private final int hashMapSize;
        private final Pair<K, V>[] hashMap;

        @SuppressWarnings("unchecked")
        public InnerHashMap(long firstHashingValue, long secondHashingValue, int hashMapSize) {
            this.firstHashingValue = firstHashingValue;
            this.secondHashingValue = secondHashingValue;
            this.hashMapSize = hashMapSize;
            hashMap = new Pair[hashMapSize];
        }

        public Pair<K, V> get(int index) {
            return hashMap[index];
        }

        public void set(int index, Pair<K, V> value) {
            hashMap[index] = value;
        }

        public boolean hasIndex(int index) {
            return hashMap[index] != null;
        }

        int getHashForKey(K key, long primeDivider) {
            return ValuesGenerator.generateUniversalHash(key.hashCode(),
                    firstHashingValue, secondHashingValue, primeDivider, hashMapSize);
        }
    }

    public PerfectHashMap(Set<Pair<K, V>> set) {
        if (set.stream().anyMatch(pair -> pair.getKey().hashCode() < 0)) {
            throw new IllegalArgumentException("One of keys is negative!");
        }

        this.immutableSet = set;

        this.primeDivider = searchPrimeDivider();
        this.firstHashingValue = ValuesGenerator.generateFirstHashingValue(primeDivider);
        this.secondHashingValue = ValuesGenerator.generateSecondHashingValue(primeDivider);

        this.hashMapSize = (int) Math.pow(immutableSet.size(), 0.8);

        List<List<Pair<K, V>>> temp = new ArrayList<>();
        for (int i = 0; i < hashMapSize; i++) {
            temp.add(new ArrayList<>());
        }
        fillTempHashMap(temp);

        createInnerHashMaps(temp);
    }

    private long searchPrimeDivider() {
        int maxHashCode = 0;

        for (Pair<K, V> pair : immutableSet) {
            int buffer = pair.getKey().hashCode();

            if (buffer > maxHashCode) maxHashCode = buffer;
        }

        return ValuesGenerator.generatePrimeNumber(maxHashCode);
    }

    private void fillTempHashMap(List<List<Pair<K, V>>> temp) {
        for (Pair<K, V> pair : immutableSet) {
            int index = ValuesGenerator.generateUniversalHash(pair.getKey().hashCode(),
                    firstHashingValue, secondHashingValue, primeDivider, hashMapSize);

            temp.get(index).add(pair);
        }
    }

    @SuppressWarnings("unchecked")
    private void createInnerHashMaps(List<List<Pair<K, V>>> temp) {
        perfectHashMap = new InnerHashMap[hashMapSize];

        int index = 0;
        for (List<Pair<K, V>> chain : temp) {
            if (chain.size() > 0) {
                createInnerHashMap(chain, index);
            } else {
                perfectHashMap[index] = null;
            }

            index++;
        }
    }

    private void createInnerHashMap(List<Pair<K, V>> chain, int index) {
        int innerHashMapSize = (int) Math.pow(chain.size(), 2);
        boolean isCollision;

        do {
            long innerFirstHashingValue = ValuesGenerator.generateFirstHashingValue(primeDivider);
            long innerSecondHashingValue = ValuesGenerator.generateSecondHashingValue(primeDivider);

            perfectHashMap[index] = new InnerHashMap<>(
                    innerFirstHashingValue, innerSecondHashingValue, innerHashMapSize);
            isCollision = false;

            for (Pair<K, V> pair : chain) {
                int innerIndex = ValuesGenerator.generateUniversalHash(pair.getKey().hashCode(),
                        innerFirstHashingValue, innerSecondHashingValue, primeDivider, innerHashMapSize);

                if (perfectHashMap[index].hasIndex(innerIndex)) {
                    isCollision = true;
                    break;
                } else {
                    perfectHashMap[index].set(innerIndex, pair);
                }
            }
        } while (isCollision);
    }

    public V get(K key) {
        int rowIndex = ValuesGenerator.generateUniversalHash(key.hashCode(),
                firstHashingValue, secondHashingValue, primeDivider, hashMapSize);

        int columnIndex = perfectHashMap[rowIndex].getHashForKey(key, primeDivider);

        Pair<K, V> searchPair = perfectHashMap[rowIndex].get(columnIndex);

        if (searchPair != null && searchPair.getKey().equals(key)) {
            return searchPair.getValue();
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        StringBuilder hashMap = new StringBuilder("HashMap\n***\n");

        int i = 0;
        for (InnerHashMap<K, V> innerHashMap : this.perfectHashMap) {
            hashMap.append("Inner HashMap by index ").append(i).append(" : ");

            if (innerHashMap != null) {
                hashMap.append(Arrays.deepToString(innerHashMap.hashMap)).append('\n');
            } else {
                hashMap.append("null").append('\n');
            }

            i++;
        }
        hashMap.append("***").append('\n');

        return hashMap.toString();
    }
}