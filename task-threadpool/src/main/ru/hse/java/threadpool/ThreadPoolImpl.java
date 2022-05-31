package ru.hse.java.threadpool;

import org.jetbrains.annotations.NotNull;
import ru.hse.java.threadpool.exceptions.LightExecutionException;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.function.Supplier;

public class ThreadPoolImpl implements ThreadPool {
    private final Thread[] threads;
    private final int nThreads;
    private final ConcurrentQueue<LightFutureImpl<?>> tasks = new ConcurrentQueue<>();
    private volatile boolean isShutdown = false;

    public ThreadPoolImpl(int nThreads) {
        threads = new Thread[nThreads];

        for (int i = 0; i < nThreads; i++) {
            threads[i] = new Thread(new Worker());
            threads[i].start();
        }

        this.nThreads = nThreads;
    }

    @Override
    public @NotNull <R> LightFuture<R> submit(Supplier<@NotNull R> supplier) {
        if (isShutdown) {
            throw new RejectedExecutionException();
        }

        LightFutureImpl<R> future = new LightFutureImpl<>(supplier);
        tasks.push(future);
        return future;
    }

    public void shutdown() {
        isShutdown = true;
        for (int i = 0; i < nThreads; i++) {
            threads[i].interrupt();
        }
        for (int i = 0; i < nThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException ignored) { }
        }
    }

    @Override
    public int getNumberOfThreads() {
        return nThreads;
    }

    private class Worker implements Runnable {
        @Override
        public void run() {
            while (!Thread.interrupted() && !isShutdown) {
                try {
                    LightFutureImpl<?> task = tasks.pop();
                    task.run();
                } catch (InterruptedException ignored) {}
            }
        }
    }

    private static class ConcurrentQueue<T> {
        private final Deque<T> queue = new ArrayDeque<>();
        private final Lock lockQueue = new ReentrantLock();
        private final Condition notEmpty = lockQueue.newCondition();

        private void push(T value) {
            lockQueue.lock();
            try {
                queue.addLast(value);
                notEmpty.signal();
            } finally {
                lockQueue.unlock();
            }
        }

        private T pop() throws InterruptedException {
            T result;
            lockQueue.lock();
            try {
                while (queue.isEmpty()) {
                    notEmpty.await();
                }
                result = queue.pollFirst();
            } finally {
                lockQueue.unlock();
            }
            return result;
        }
    }

    private class LightFutureImpl<R> implements LightFuture<R>, Runnable {
        private final Supplier<@NotNull R> supplier;
        private R result;
        private boolean isReady = false;

        private final Lock lockTask = new ReentrantLock();
        private final Condition isReadyCondition = lockTask.newCondition();

        private Exception supplierException = null;
        private final List<LightFutureImpl<?>> thenApplyList = new LinkedList<>();

        public LightFutureImpl(Supplier<@NotNull R> supplier) {
            this.supplier = supplier;
        }

        @Override
        public void run() {
            lockTask.lock();
            try {
                try {
                    result = supplier.get();
                } catch (Exception e) {
                    supplierException = e;
                }
                isReady = true;
                isReadyCondition.signalAll();
                for(var future : thenApplyList) {
                    tasks.push(future);
                }
            } finally {
                lockTask.unlock();
            }
        }

        @Override
        public boolean isReady() {
            lockTask.lock();
            try {
                return isReady;
            } finally {
                lockTask.unlock();
            }
        }

        @NotNull
        @Override
        public R get() throws  LightExecutionException {
            lockTask.lock();
            try {
                while (!isReady) {
                    try {
                        isReadyCondition.await();
                    } catch (InterruptedException e) {
                        throw new LightExecutionException(e);
                    }
                }
            } finally {
                lockTask.unlock();
            }

            if (supplierException != null) {
                throw new LightExecutionException(supplierException);
            }
            return result;
        }

        @Override
        public @NotNull <R1> LightFuture<R1> thenApply(Function<R, @NotNull R1> function) {
            LightFutureImpl<R1> future = new LightFutureImpl<>(() -> function.apply(result));
            lockTask.lock();
            try {
                if(supplierException != null) {
                    future = new LightFutureImpl<>(() -> (R1)new Object());
                    future.supplierException = supplierException;
                }
                if (isReady) {
                    tasks.push(future);
                } else {
                    thenApplyList.add(future);
                }
            } finally {
                lockTask.unlock();
            }
            return future;
        }
    }
}