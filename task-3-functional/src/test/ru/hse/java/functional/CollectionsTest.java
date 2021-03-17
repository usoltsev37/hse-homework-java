package ru.hse.java.functional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CollectionsTest {

    private List<Integer> list;
    private final int listSizeOfElems = 100_000;
    private final int listSize = 10_000;
    private final int seed = 3781;
    private final int stressCnt = 10_000;
    Random rand;

    @BeforeEach
    void startTest() {
        rand = new Random(seed);
        list = new ArrayList<>(listSize);
        for(int i = 0; i < listSize; i++) {
            list.add(rand.nextInt(listSizeOfElems));
        }
    }

    @Test
    void testMap() {
        List<Integer> actual = Collections.map(x -> x + 3, list);
        List<Integer> expected = list.stream()
                .map(x -> x + 3)
                .collect(Collectors.toList());

        Assertions.assertEquals(expected, actual);

        List<Integer> actual1 = Collections.map(x -> x / 3, list);
        List<Integer> expected1= list.stream()
                .map(x -> x / 3)
                .collect(Collectors.toList());

        Assertions.assertEquals(expected1, actual1);
    }

    @Test
    void testFilter() {
        List<Integer> actual = Collections.filter(x -> x % 23 == 0, list);
        List<Integer> expected = list.stream()
                .filter(x -> x % 23 == 0)
                .collect(Collectors.toList());

        Assertions.assertEquals(expected, actual);

        List<Integer> actual1 = Collections.filter(x -> x >  listSizeOfElems / 3, list);
        List<Integer> expected1 = list.stream()
                .filter(x -> x >  listSizeOfElems / 3)
                .collect(Collectors.toList());

        Assertions.assertEquals(expected1, actual1);
    }

    @Test
    void testTakeWhile() {
        List<Integer> actual = Collections.takeWhile(x -> x < listSizeOfElems / 2, list);
        List<Integer> expected = list.stream()
                .takeWhile(x ->  x < listSizeOfElems / 2)
                .collect(Collectors.toList());

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testTakeUnless() {
        List<Integer> actual = Collections.takeUnless(x -> x > listSizeOfElems / 2, list);
        List<Integer> expected = list.stream()
                .takeWhile(x ->  x <= listSizeOfElems / 2)
                .collect(Collectors.toList());

        Assertions.assertEquals(expected, actual);

        actual = Collections.takeUnless(x -> x % 597 == 0, list);
        expected = list.stream()
                .takeWhile(x -> x % 597 != 0)
                .collect(Collectors.toList());

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testFoldr() {
        Integer actual1 = Collections.foldr((x, y) -> (x + y) % listSizeOfElems, 7264, list);
        Integer expected1 = list.stream()
                .reduce(7264, (x, y) -> (x + y) % listSizeOfElems);

        Assertions.assertEquals(expected1, actual1);

        Integer actual2 = Collections.foldr((x, y) -> (x * y) % 1000, 5, list);
        Integer expected2 = list.stream()
                .reduce(5, (x, y) -> (x * y) % 1000);

        Assertions.assertEquals(expected2, actual2);
    }

    @Test
    void testFoldl() {
        Integer actual1 = Collections.foldl((x, y) -> (x - y * x) % listSizeOfElems, 4321, list);
        Integer expected1 = list.stream()
                .reduce(4321, (x, y) -> (x - y * x) % listSizeOfElems);

        Assertions.assertEquals(expected1, actual1);

        Integer actual2 = Collections.foldl((x, y) -> (x - y) % 234124, 241, list);
        Integer expected2 = 241;
        for(var el : list) {
            expected2 = (expected2 - el) % 234124;
        }

        Assertions.assertEquals(expected2, actual2);
    }

    @Test
    void testStress() {
        for(int cnt = 0; cnt < stressCnt; cnt++) {
            startTest();
            int command = rand.nextInt(6);
            switch (command) {
                case 0: {
                    testMap();
                }
                case 1: {
                    testFilter();
                }
                case 2: {
                    testTakeWhile();
                }
                case 3: {
                    testTakeUnless();
                }
                case 4: {
                    testFoldl();
                }
                case 5: {
                    testFoldr();
                }
            }
        }
    }
}
