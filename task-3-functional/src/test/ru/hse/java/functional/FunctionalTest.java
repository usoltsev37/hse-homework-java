package ru.hse.java.functional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Random;
import java.util.function.Function;


public class FunctionalTest {

    private final int STRESS_MAX_INT = 10_000;
    private final int stressCnt = 100_000;
    private final int seed = 32151;
    Random rand;

    @Test
    void testFunction1use() {
        Function1<Integer, Integer> f = x -> x * 10;
        Assertions.assertEquals(100, f.use(10));
    }

    @Test
    void testFunction1compose() {
        Function1<Integer, Integer> fMult10 = x -> x * 10;
        Function1<Integer, Integer> fSucc10 = x -> x + 10;
        Assertions.assertEquals(60, fMult10.compose(fSucc10).use(5));
        Assertions.assertEquals(150, fSucc10.compose(fMult10).use(5));
    }

    @Test
    void testFunction2use() {
        Function2<Integer, Integer, Integer> f = (x, y) -> x * 10 + y;
        Assertions.assertEquals(102, f.use(10, 2));
        Assertions.assertEquals(30, f.use(2, 10));
    }

    @Test
    void testFunction2compose() {
        Function2<Integer, Integer, Integer> f = (x, y) -> x * 10 + y;
        Function1<Integer, Integer> g = z -> z + 1;
        Assertions.assertEquals(57 + 1, f.compose(g).use(5, 7));
    }

    @Test
    void testFunction2binds() {
        Function2<Integer, Integer, Integer> f = (x, y) -> x * 10 + y;
        Assertions.assertEquals(103, f.bind1(10).use(3));
        Assertions.assertEquals(40, f.bind2(10).use(3));
    }

    @Test
    void testFunction2curry() {
        Function2<Integer, String, String> f = (x, y) -> x.toString() + " " + y;
        Assertions.assertEquals("5 6", f.curry().use(5).use("6"));
        Assertions.assertEquals("7 hello", f.curry().use(7).use("hello"));
    }

    @Test
    void testPredicateOrAnd() {
        Predicate<Integer> pEven = x -> (x % 2 == 0);
        Predicate<Integer> pOdd = x -> (x % 2 == 1);
        Assertions.assertTrue(pEven.or(pOdd).use(0));
        Assertions.assertFalse(pEven.and(pOdd).use(0));

        Predicate<Integer> p3 = x -> (x % 3 == 0);
        Assertions.assertTrue(pEven.and(p3).use(66));
        Assertions.assertFalse(pEven.and(p3).use(3));
    }

    @BeforeEach
    void startTest() {
        rand = new Random(seed);
    }


    @Test
    void testPredicateNot() {
        Predicate<Integer> pEven = x -> (x % 2 == 0);
        Predicate<Integer> pOdd = x -> (x % 2 == 1);

        for(int i = 0; i < 100; i++) {
            int x = rand.nextInt(STRESS_MAX_INT);
            Assertions.assertEquals(pEven.use(x), pOdd.not().use(x));
        }
    }

    @Test
    void testStressPredicate() {
        for (int i = 0; i < stressCnt; i++) {
            final int mod = rand.nextInt(1000) + 1;
            final int num = rand.nextInt(STRESS_MAX_INT);
            Function<Integer, Boolean> expected1 = x -> (x % mod == 0);
            Function<Integer, Boolean> expected2 = x -> (x < num);
            Predicate<Integer> p1 = x -> (x % mod == 0);
            Predicate<Integer> p2 = x -> (x < num);
            int command = rand.nextInt(3);
            int x = rand.nextInt(STRESS_MAX_INT);
            switch (command) {
                case 0: {
                    Assertions.assertEquals(expected1.apply(x), p1.use(x));
                    Assertions.assertEquals(expected2.apply(x), p2.use(x));
                }
                case 1: {
                    p1.and(p2).use(x);
                    boolean expect = expected1.apply(x) && expected2.apply(x);
                    Assertions.assertEquals(expect, p1.and(p2).use(x));
                }
                case 2: {
                    p1.or(p2).use(x);
                    boolean expect = expected1.apply(x) || expected2.apply(x);
                    Assertions.assertEquals(expect, p1.or(p2).use(x));
                }
            }
        }
    }

    @Test
    void testStressFunction1compose() {
        for (int i = 0; i < stressCnt; i++) {
            final int div = rand.nextInt(10_000) + 1;
            final int num = rand.nextInt(10_000) + 1;

            Function1<Integer, Integer> p1 = x -> x / div;
            Function1<Integer, Integer> p2 = x -> x - num;

            int x = rand.nextInt(STRESS_MAX_INT);
            int expected1 = (x / div) - num;
            int expected2 = (x - num) / div;
            Assertions.assertEquals(expected1, p1.compose(p2).use(x));
            Assertions.assertEquals(expected2, p2.compose(p1).use(x));
        }
    }

    @Test
    void testStressFunction2compose() {
        for (int i = 0; i < stressCnt; i++) {
            final int div = rand.nextInt(10_000) + 1;
            final int num = rand.nextInt(10_000) + 1;

            Function2<Integer, Integer, Integer> p1 = (x, y) -> x / div + y;
            Function2<Integer, Integer, Integer> p2 = (x, y) -> (x - num) / y;

            Function1<Integer, Integer> g = x -> x * num;

            int x = rand.nextInt(STRESS_MAX_INT) + 1;
            int y = rand.nextInt(STRESS_MAX_INT) + 1;

            int expected1 = (x / div + y) * num;
            int expected2 = ((x - num) / y) * num;
            Assertions.assertEquals(expected1, p1.compose(g).use(x, y));
            Assertions.assertEquals(expected2, p2.compose(g).use(x, y));
        }
    }
}
