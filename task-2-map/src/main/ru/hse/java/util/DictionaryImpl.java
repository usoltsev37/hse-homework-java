package ru.hse.java.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static java.lang.Math.sqrt;

public class DictionaryImpl<K, V> extends AbstractMap<K, V> implements Dictionary<K, V> {
    private ArrayList<ArrayList<Entry<K, V>>> hashTable;

    private int size;
    private int capacity;
    private int hashPrimeCapacity;

    private final double LOAD_FACTOR = 0.75;
    private final int REHASH_SIZE = 2;
    private final int MIN_CAPACITY = 4;

    public DictionaryImpl() {
        clear();
    }

    private int getBucket(int hashCode) {
        return ((hashCode) % hashPrimeCapacity + hashPrimeCapacity) % hashPrimeCapacity;
    }

    private boolean isPrime(int x) {
        if (x == 1) return false;
        if (x == 2 || x == 3) return true;
        for (int d = 2; d < sqrt(x) + 1; d++) {
            if (x % d == 0)
                return false;
        }
        return true;
    }

    private int getNearestLowerPrime(int x) {
        while (!isPrime(x)) {
            x--;
        }
        return x;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(@NotNull Object key) {
        int ind = getBucket(key.hashCode());
        for (Entry<K, V> entry : hashTable.get(ind)) {
            if (key.equals(entry.getKey())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public @Nullable V get(@NotNull Object key) {
        int ind = getBucket(key.hashCode());
        for (Entry<K, V> entry : hashTable.get(ind)) {
            if (key.equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    private void reHash(int capacity) throws IllegalArgumentException {
        if (capacity < MIN_CAPACITY) {
            throw new IllegalArgumentException("Capacity should be >= MIN_CAPACITY");
        }
        this.capacity = capacity;
        this.hashPrimeCapacity = getNearestLowerPrime(capacity);
        ArrayList<ArrayList<Entry<K, V>>> newHashTable = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            newHashTable.add(new ArrayList<>());
        }

        for (var bucket : hashTable) {
            for (Entry<K, V> entry : bucket) {
                K key = entry.getKey();
                V value = entry.getValue();
                if (value == null) {
                    continue;
                }
                int ind = getBucket(key.hashCode());
                newHashTable.get(ind).add(new AbstractMap.SimpleEntry<>(key, value));
            }
        }

        hashTable = newHashTable;
    }

    private boolean isFilled() {
        return size > capacity * LOAD_FACTOR;
    }

    @Override
    public @Nullable V put(@NotNull K key, V value) {
        int ind = getBucket(key.hashCode());
        for (Entry<K, V> entry : hashTable.get(ind)) {
            if (key.equals(entry.getKey())) {
                V lastValue = entry.getValue();
                entry.setValue(value);
                return lastValue;
            }
        }
        size++;
        hashTable.get(ind).add(new AbstractMap.SimpleEntry<>(key, value));
        if (isFilled()) {
            reHash(capacity * REHASH_SIZE);
        }
        return null;
    }

    private boolean isFree() {
        return capacity > MIN_CAPACITY && size * REHASH_SIZE <= capacity * LOAD_FACTOR;
    }

    @Override
    public @Nullable V remove(@NotNull Object key) {
        int ind = getBucket(key.hashCode());
        for (Entry<K, V> entry : hashTable.get(ind)) {
            if (key.equals(entry.getKey())) {
                size--;
                V lastValue = entry.getValue();
                hashTable.get(ind).remove(entry);
                if (isFree()) {
                    reHash(capacity / REHASH_SIZE);
                }
                return lastValue;
            }
        }
        return null;
    }

    @Override
    public void clear() {
        size = 0;
        capacity = MIN_CAPACITY;
        hashPrimeCapacity = 3;
        if (hashTable != null) hashTable.clear();
        hashTable = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            hashTable.add(new ArrayList<>());
        }

    }

    enum iteratorType {
        KEY, VALUE, ENTRY
    }

    private class MyIterator<I> implements Iterator<I> {

        private final Iterator<ArrayList<Entry<K, V>>> itTable;
        private Iterator<Entry<K, V>> itCurr;
        private final iteratorType type;

        MyIterator(iteratorType type, Iterator<ArrayList<Entry<K, V>>> itTable) {
            this.type = type;
            this.itTable = itTable;
            if (itTable.hasNext()) {
                this.itCurr = itTable.next().iterator();
            }
        }

        @Override
        public boolean hasNext() {
            if (itCurr == null) {
                return false;
            }
            if (itCurr.hasNext()) {
                return true;
            }
            while (itTable.hasNext()) {
                itCurr = itTable.next().iterator();
                if (itCurr.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public @Nullable I next() {
            if (!hasNext()) {
                return null;
            }
            if (type == iteratorType.KEY) {
                return (I) itCurr.next().getKey();
            } else if (type == iteratorType.VALUE) {
                return (I) itCurr.next().getValue();
            } else if (type == iteratorType.ENTRY) {
                return (I) itCurr.next();
            }
            return null;
        }

        @Override
        public void remove() {
            itCurr.remove();
            size--;
        }
    }

    private class KeySet extends AbstractSet<K> {

        @Override
        public @NotNull Iterator<K> iterator() {
            return new MyIterator<K>(iteratorType.KEY, hashTable.iterator());
        }

        @Override
        public int size() {
            return size;
        }
    }

    @Override
    public @NotNull Set<K> keySet() {
        return new KeySet();
    }

    private class ValuesCollection extends AbstractCollection<V> {

        @Override
        public @NotNull Iterator<V> iterator() {
            return new MyIterator<V>(iteratorType.VALUE, hashTable.iterator());
        }

        @Override
        public int size() {
            return size;
        }
    }

    @Override
    public @NotNull Collection<V> values() {
        return new ValuesCollection();
    }

    private class EntrySet extends AbstractSet<Entry<K, V>> {

        @Override
        public @NotNull Iterator<Entry<K, V>> iterator() {
            return new MyIterator<Entry<K, V>>(iteratorType.ENTRY, hashTable.iterator());
        }

        @Override
        public int size() {
            return size;
        }
    }

    @Override
    public @NotNull Set<Entry<K, V>> entrySet() {
        return new EntrySet();
    }
}
