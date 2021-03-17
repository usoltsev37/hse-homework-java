package ru.hse.java.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

public class DictionaryImplTest {

    private Dictionary<Integer, String> hashMap;

    @BeforeEach
    void startTest() {
        hashMap = new DictionaryImpl<>();
    }

    @Test
    public void testPut() {
        Assertions.assertNull(hashMap.put(18, "First things first"));
        Assertions.assertNull(hashMap.put(23, "I'ma say all the words inside my head"));
        Assertions.assertNull(hashMap.put(65, "I'm fired up and tired of the way that things have been, oh-ooh"));
        Assertions.assertNull(hashMap.put(72, "The way that things have been, oh-ooh"));

        Assertions.assertNull(hashMap.put(642, "Second thing second"));
        Assertions.assertNull(hashMap.put(8364, "Don't you tell me"));
        Assertions.assertNull(hashMap.put(215, "what you think that I could be"));
        Assertions.assertNull(hashMap.put(827, "I'm the one at the sail"));
        Assertions.assertNull(hashMap.put(64209, "I'm the master of my sea, oh-ooh"));
        Assertions.assertNull(hashMap.put(836124, "The master of my sea, oh-ooh"));
        Assertions.assertNull(hashMap.put(164922, "I was broken from a young age"));
        Assertions.assertNull(hashMap.put(7, "Taking my sulking to the masses"));

        Assertions.assertEquals("I'ma say all the words inside my head",
                hashMap.put(23, "The way that things have been, oh-ooh"));
        Assertions.assertEquals("The way that things have been, oh-ooh",
                hashMap.put(23, "Second thing second"));
        Assertions.assertEquals("The way that things have been, oh-ooh",
                hashMap.put(72, "dasf"));
    }

    @Test
    public void testContains() {
        Assertions.assertNull(hashMap.put(1, "Wake to see"));
        Assertions.assertNull(hashMap.put(2, "a fantasy"));
        Assertions.assertNull(hashMap.put(3, "Greatness"));
        Assertions.assertNull(hashMap.put(0, "Unsung"));
        Assertions.assertNull(hashMap.put(4, "Embedded"));

        Assertions.assertTrue(hashMap.containsKey(1));
        Assertions.assertTrue(hashMap.containsKey(0));
        Assertions.assertTrue(hashMap.containsKey(4));

        hashMap.remove(4);
        Assertions.assertFalse(hashMap.containsKey(4));
        Assertions.assertFalse(hashMap.containsKey(-87782));
        Assertions.assertFalse(hashMap.containsKey(-92));
    }

    @Test
    public void testRemove() {
        Assertions.assertNull(hashMap.put(321, "Wake to see"));
        Assertions.assertNull(hashMap.put(92, "a fantasy"));
        Assertions.assertNull(hashMap.put(8, "Greatness"));
        Assertions.assertNull(hashMap.put(0, "Unsung"));
        Assertions.assertNull(hashMap.put(634, "Embedded"));
        Assertions.assertNull(hashMap.put(-837, "Brainwashing"));
        Assertions.assertNull(hashMap.put(-28, "children"));

        Assertions.assertEquals(7, hashMap.size());

        Assertions.assertNull(hashMap.remove(-99999));
        Assertions.assertNull(hashMap.remove(6721));
        Assertions.assertEquals("Wake to see", hashMap.remove(321));
        Assertions.assertNull(hashMap.remove(321));
        Assertions.assertEquals("Unsung", hashMap.remove(0));
        Assertions.assertEquals("children", hashMap.remove(-28));
        Assertions.assertEquals("Brainwashing", hashMap.remove(-837));
        Assertions.assertEquals("Greatness", hashMap.remove(8));
        Assertions.assertEquals("a fantasy", hashMap.remove(92));
        Assertions.assertNull(hashMap.remove(92));

        Assertions.assertEquals(1, hashMap.size());
    }

    @Test
    public void testIsEmpty() {
        Assertions.assertNull(hashMap.put(1, "Wake to see"));
        Assertions.assertNull(hashMap.put(2, "a fantasy"));
        Assertions.assertNull(hashMap.put(3, "Greatness"));
        Assertions.assertNull(hashMap.put(0, "Unsung"));
        Assertions.assertNull(hashMap.put(4, "Embedded"));
        Assertions.assertNull(hashMap.put(5, "Brainwashing"));
        Assertions.assertNull(hashMap.put(6, "children"));

        Assertions.assertEquals(7, hashMap.size());

        hashMap.remove(0);
        hashMap.remove(1);
        hashMap.remove(2);
        hashMap.remove(3);
        Assertions.assertEquals(3, hashMap.size());

        hashMap.remove(4);
        hashMap.remove(5);
        hashMap.remove(6);
        Assertions.assertTrue(hashMap.isEmpty());
    }

    @Test
    public void testGet() {
        Assertions.assertNull(hashMap.put(1, "Wake to see"));
        Assertions.assertNull(hashMap.put(2, "a fantasy"));

        Assertions.assertEquals("a fantasy", hashMap.get(2));

        Assertions.assertNull(hashMap.put(3, "Greatness"));
        Assertions.assertNull(hashMap.put(0, "Unsung"));
        Assertions.assertNull(hashMap.put(4, "Embedded"));
        Assertions.assertNull(hashMap.put(5, "Brainwashing"));
        Assertions.assertNull(hashMap.put(6, "children"));

        Assertions.assertEquals("children", hashMap.get(6));
        Assertions.assertEquals("Unsung", hashMap.get(0));
        Assertions.assertEquals("Greatness", hashMap.get(3));

        Assertions.assertNull(hashMap.get(68623));
        Assertions.assertNull(hashMap.get(723));
    }

    @Test
    public void testKeySet() {
        Assertions.assertNull(hashMap.put(-33, "Greatness"));
        Assertions.assertNull(hashMap.put(11, "Wake to see"));
        Assertions.assertNull(hashMap.put(22, "a fantasy"));
        Assertions.assertNull(hashMap.put(20, "Unsung"));
        Assertions.assertNull(hashMap.put(34, "Embedded"));
        Assertions.assertNull(hashMap.put(65, "Brainwashing"));
        Assertions.assertNull(hashMap.put(96, "children"));

        Set<Integer> expected = new HashSet<Integer>();
        Collections.addAll(expected, -33, 11, 22, 20, 34, 65, 96);
        Assertions.assertEquals(expected, hashMap.keySet());
    }

    @Test
    public void testValues() {
        Assertions.assertNull(hashMap.put(-33, "A"));
        Assertions.assertNull(hashMap.put(11, "B"));
        Assertions.assertNull(hashMap.put(22, "C"));
        Assertions.assertNull(hashMap.put(20, "D"));
        Assertions.assertNull(hashMap.put(34, "D"));
        Assertions.assertNull(hashMap.put(65, "D"));
        Assertions.assertNull(hashMap.put(96, "E"));

        Collection<String> result = hashMap.values();
        Assertions.assertTrue(result.contains("A"));
        Assertions.assertTrue(result.contains("B"));
        Assertions.assertTrue(result.contains("C"));
        Assertions.assertTrue(result.contains("E"));
        Assertions.assertTrue(result.contains("D"));

        Object[] expectedArr = {"A", "B", "C", "D", "D", "D", "E"};
        Object[] resultArr = new Object[7];
        result.toArray(resultArr);

        Arrays.sort(resultArr);
        Assertions.assertTrue(Arrays.equals(resultArr, expectedArr));
    }

    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, String>> expected = new HashSet<>();
        Assertions.assertEquals(expected, hashMap.entrySet());

        Collections.addAll(expected, Map.entry(1, "1"), Map.entry(2, "2"),
                Map.entry(3, "3"), Map.entry(4, "4"), Map.entry(5, "5"));

        Assertions.assertNull(hashMap.put(1, "1"));
        Assertions.assertNull(hashMap.put(2, "2"));
        Assertions.assertNull(hashMap.put(3, "3"));
        Assertions.assertNull(hashMap.put(4, "4"));
        Assertions.assertNull(hashMap.put(5, "5"));

        Assertions.assertEquals(expected, hashMap.entrySet());
    }

    private final int cntStress = 100_000;
    private HashMap<Integer, String> correctMap = new HashMap<>();
    Random rand;
    private final int seedStress = 38371;

    @BeforeEach
    void startStress() {
        correctMap = new HashMap<>();
        rand = new Random(seedStress);
    }

    @Test
    public void testStressFull() {
        for (int cnt = 0; cnt < cntStress; cnt++) {
            StringBuilder strBuilder = new StringBuilder();
            for (int i = cnt % 10; i < 10; i++) {
                strBuilder.append((char) rand.nextInt(128));
            }
            String value = strBuilder.toString();
            int key = rand.nextInt(cntStress);
            int command = rand.nextInt(7);
            switch (command) {
                case 0: {
                    Assertions.assertEquals(correctMap.size(), hashMap.size());
                }
                case 1: {
                    Assertions.assertEquals(correctMap.put(key, value), hashMap.put(key, value));
                }
                case 2: {
                    Assertions.assertEquals(correctMap.get(key), hashMap.get(key));
                }
                case 3: {
                    Assertions.assertEquals(correctMap.remove(key), hashMap.remove(key));
                }
                case 4: {
                    Assertions.assertEquals(correctMap.containsKey(key), hashMap.containsKey(key));
                }
                case 5: {
                    Assertions.assertEquals(correctMap.keySet(), hashMap.keySet());
                }
                case 6: {
                    Assertions.assertEquals(correctMap.entrySet(), hashMap.entrySet());
                }
            }
        }
    }

    @Test
    public void testStressPutRemove() {
        for (int cnt = 0; cnt < cntStress; cnt++) {
            StringBuilder strBuilder = new StringBuilder();
            for (int i = cnt % 10; i < 10; i++) {
                strBuilder.append((char) rand.nextInt(128));
            }
            String value = strBuilder.toString();
            int key = rand.nextInt(cntStress);
            Assertions.assertEquals(correctMap.put(key, value), hashMap.put(key, value));
        }

        Assertions.assertEquals(correctMap.size(), hashMap.size());

        for (int cnt = 0; cnt < cntStress; cnt++) {
            int key = rand.nextInt(cntStress);
            Assertions.assertEquals(correctMap.remove(key), hashMap.remove(key));
        }

        Assertions.assertEquals(correctMap.size(), hashMap.size());
    }

    @Test
    public void testStressPutGetClear() {
        for (int cnt = 0; cnt < cntStress; cnt++) {
            StringBuilder strBuilder = new StringBuilder();
            for (int i = cnt % 10; i < 10; i++) {
                strBuilder.append((char) rand.nextInt(128));
            }
            String value = strBuilder.toString();
            int key = rand.nextInt(cntStress);

            if (cnt % 1000 == 0) {
                correctMap.clear();
                hashMap.clear();
            } else if (cnt % 2 == 0) {
                Assertions.assertEquals(correctMap.containsKey(key), hashMap.containsKey(key));
            } else {
                Assertions.assertEquals(correctMap.put(key, value), hashMap.put(key, value));
            }

            Assertions.assertEquals(correctMap.put(key, value), hashMap.put(key, value));
        }

        for (int cnt = 0; cnt < cntStress; cnt++) {
            int key = rand.nextInt(cntStress);
            Assertions.assertEquals(correctMap.containsKey(key), hashMap.containsKey(key));
        }
    }

    @Test
    public void testStressValues() {
        for (int cnt = 0; cnt < cntStress; cnt++) {
            StringBuilder strBuilder = new StringBuilder();
            for (int i = cnt % 10; i < 10; i++) {
                strBuilder.append((char) rand.nextInt(128));
            }
            String value = strBuilder.toString();
            int key = rand.nextInt(cntStress);
            Assertions.assertEquals(correctMap.put(key, value), hashMap.put(key, value));
            if (cnt % 1000 == 0) {
                String[] expected = correctMap.values().toArray(new String[0]);
                String[] myCollec = hashMap.values().toArray(new String[0]);
                Arrays.sort(expected);
                Arrays.sort(myCollec);
                Assertions.assertTrue(Arrays.equals(expected, myCollec));
            }
        }
    }
}
