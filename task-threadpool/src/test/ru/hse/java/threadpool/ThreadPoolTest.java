package ru.hse.java.threadpool;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import ru.hse.java.threadpool.exceptions.LightExecutionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class ThreadPoolTest {

    private ThreadPool myPool;
    private final int cntOfThreads = 16;
    private final int cntOfStress = 10_000;
    private final int cntOfRepeatedTest = 10;
    private Random rand = new Random(12);

    @BeforeEach
    void startTest() {
        myPool = ThreadPool.create(cntOfThreads);
    }


    @RepeatedTest(cntOfRepeatedTest)
    void testCntThreads() throws InterruptedException {
        var value = new AtomicInteger(0);
        for (int i = 0; i < 10000; i++) {
            myPool.submit(() -> {
                value.incrementAndGet();
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ignored) {}
                return null;
            });
        }
        Thread.sleep(500);
        assertEquals(cntOfThreads, value.get());
        assertEquals(cntOfThreads, myPool.getNumberOfThreads());
    }

    @Test
    void testSomeTasks() {
        LightFuture<Integer> task1 = myPool.submit(() -> 32);
        LightFuture<Integer> task2 = myPool.submit(() -> 5432);
        LightFuture<Integer> task3 = myPool.submit(() -> 2);
        LightFuture<Integer> task4 = myPool.submit(() -> 93);
        LightFuture<Integer> task5 = myPool.submit(() -> -129);
        try {
            assertEquals(32, task1.get());
            assertEquals(5432, task2.get());
            assertEquals(2, task3.get());
            assertEquals(93, task4.get());
            assertEquals(-129, task5.get());
        } catch (LightExecutionException e) {
            throw new RuntimeException("Не должно кидаться", e);
        }
    }

    @RepeatedTest(cntOfRepeatedTest)
    void testManyTasks() {
        List<Integer> ints = new ArrayList<>();
        List<LightFuture<Integer>> tasks = new ArrayList<>();
        for(int i = 0; i < cntOfStress; i++) {
            int newInt = rand.nextInt();
            ints.add(newInt);
            tasks.add(myPool.submit(() -> newInt));
        }

        for(int i = 0; i < cntOfStress; i++) {
            try {
                assertEquals(ints.get(i), tasks.get(i).get());
            } catch (LightExecutionException e) {
                throw new RuntimeException("Не должно кидаться", e);
            }
        }
    }

    @Test
    void testWait() {
        LightFuture<Integer> task = myPool.submit(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException("Не должно кидаться", e);
            }
            return 0;
        });

        assertFalse(task.isReady());
        try {
            assertEquals(0, task.get());
        } catch (LightExecutionException e) {
            throw new RuntimeException("Не должно кидаться", e);
        }
        assertTrue(task.isReady());
    }

    @Test
    void testShutdown() {
        myPool.shutdown();
        assertThrows(RejectedExecutionException.class, () -> myPool.submit(() -> 1000));
    }

    @Test
    void testThenApply() {
        LightFuture<Integer> task = myPool.submit(() -> 1);
        for (int i = 0; i < 11; i++) {
            task = task.thenApply(el -> 2 * el);
        }
        try {
            assertEquals(2048, task.get());
        } catch (LightExecutionException e) {
            throw new RuntimeException("Не должно кидаться", e);
        }
    }

    @Test
    void testTasksThrowsException() {
        LightFuture<Integer> task = myPool.submit(() -> {
            throw new RuntimeException("test");
        });

        assertThrows(LightExecutionException.class, task::get);
    }

    @Test
    void chained() {
        ThreadPool pool = ThreadPool.create(5);
        var task1 = pool.submit(() -> 2 / 0);
        var future = task1.thenApply(x -> x + 1).thenApply(y -> y + 1);
        try {
            future.get();
        } catch (LightExecutionException e) {
            assertEquals(ArithmeticException.class, e.getCause().getClass());
            assertEquals("/ by zero", e.getCause().getMessage());
        }
    }

}
