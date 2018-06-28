package com.xiaohongxiedaima.demo.javacore.thread;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockDemo {
    public static void main(String[] args) {
        final int testThreadNum = 10;
        final ReentrantReadWriteLockCount count = new ReentrantReadWriteLockCount();

        final CountDownLatch latch = new CountDownLatch(testThreadNum);

        ExecutorService executorService = Executors.newFixedThreadPool(testThreadNum);

        for (int i = 0; i < testThreadNum; i++) {
            executorService.execute(() -> {
                for (int j = 0; j < 1000; j++) {
                    count.inc(1);
                }
                latch.countDown();
            });
        }

        try {
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(count.get());
    }
}

class ReentrantReadWriteLockCount {
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    private Integer v = 0;

    public void inc(Integer v) {
        lock.writeLock().lock();
        try {
            this.v = this.v + v;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Integer get() {
        lock.readLock().lock();
        try {
            return this.v;
        } finally {
            lock.readLock().unlock();
        }
    }
}
