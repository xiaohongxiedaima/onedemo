package com.xiaohongxiedaima.demo.javacore.concurrent;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {

    public static void main(String[] args) {
        final int testThreadNum = 10;
        final ReentrantLockCount count = new ReentrantLockCount();

        final CyclicBarrier cyclicBarrier = new CyclicBarrier(testThreadNum, () -> System.out.println(count.get()));

        ExecutorService executorService = Executors.newFixedThreadPool(testThreadNum);

        for (int i = 0; i < testThreadNum; i++) {
            executorService.execute(() -> {
                for (int j = 0; j < 1000; j++) {
                    count.inc(1);
                }
                try {
                    cyclicBarrier.await();
                } catch (Exception e) {
                }
            });
        }
    }

}

class ReentrantLockCount {
    private Lock lock = new ReentrantLock();

    private Integer v = 0;

    public void inc(Integer v) {
        lock.lock();
        try {
            this.v = this.v + v;
        } finally {
            lock.unlock();
        }
    }

    public Integer get() {
        lock.lock();
        try {
            return this.v;
        } finally {
            lock.unlock();
        }
    }
}
