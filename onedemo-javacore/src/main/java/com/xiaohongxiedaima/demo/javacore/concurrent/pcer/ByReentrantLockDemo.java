package com.xiaohongxiedaima.demo.javacore.concurrent.pcer;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用ReentrantLock实现生产者消费者模式
 * 2个生产者，一个消费者
 * 通过 线程中断 的方式停止生产消费逻辑
 */
@Slf4j
public class ByReentrantLockDemo {
    public static void main(String[] args) throws InterruptedException {
        Lock lock = new ReentrantLock(false);
        Condition condition = lock.newCondition();
        List<Product> productList = new ArrayList<>();
        Thread producer1 = new Thread(new Producer(lock, condition, productList), "producer-1");
        Thread producer2 = new Thread(new Producer(lock, condition, productList), "producer-2");
        Thread consumer = new Thread(new Consumer(lock, condition, productList), "consumer-1");
        producer1.start();
        producer2.start();
        consumer.start();

        Thread.sleep(10);

        producer1.interrupt();
        producer2.interrupt();
        consumer.interrupt();

        log.info("isInterrupted: {},{},{}", producer1.isInterrupted(), producer1.isInterrupted(), consumer.isInterrupted());
    }

    static class Producer implements Runnable {
        private static final AtomicInteger COUNT = new AtomicInteger(0);
        private static final Integer PRODUCT_MAX_SIZE = 10;
        private final Lock lock;

        private final Condition condition;

        private final List<Product> productList;

        public Producer(Lock lock, Condition condition, List<Product> productList) {
            this.lock = lock;
            this.condition = condition;
            this.productList = productList;
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    lock.lock();
                    if (this.productList.size() >= PRODUCT_MAX_SIZE) {
                        condition.await();
                    } else {
                        Product product = new Product(COUNT.incrementAndGet());
                        this.productList.add(product);
                        log.info("producer : {}", product);
                    }
                } catch (InterruptedException e) {
//                    log.info(e.getMessage(), e);
                    // 跑出中断异常后会重置中断标记为false，在这里将中断标记设置为true
                    Thread.currentThread().interrupt();
                } finally {
                    lock.unlock();
                }
            }
            log.info("producer stop");
        }
    }

    static class Consumer implements Runnable {
        private final Lock lock;

        private final Condition condition;

        List<Product> productList;

        public Consumer(Lock lock, Condition condition, List<Product> productList) {
            this.lock = lock;
            this.condition = condition;
            this.productList = productList;
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    lock.lock();
                    if (productList.size() == 0) {
                        condition.signalAll();
                    } else {
                        Product product = this.productList.remove(0);
                        log.info("consumer : {}", product);
                    }
                } finally {
                    lock.unlock();
                }
            }

            // 清理未消费的对象
            Iterator<Product> iterator = this.productList.iterator();
            while (iterator.hasNext()) {
                Product product = iterator.next();
                log.info("consume: {}", product);
                iterator.remove();
            }
            log.info("consumer stop");
        }
    }

    @Data
    static class Product {
        private Integer id;

        public Product(Integer id) {
            this.id = id;
        }

        public String toString() {
            return Thread.currentThread().getName() + "-----" + this.id;
        }
    }
}
