package com.xiaohongxiedaima.demo.javacore.concurrent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadInterruptDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // false
                        log.info("interrupt flag: {}", Thread.currentThread().isInterrupted());
                        Thread.currentThread().interrupt();
                        // true
                        log.info("interrupt flag: {}", Thread.currentThread().isInterrupted());
                    }
                }
                log.info("exit");
            }
        });
        t.start();
        Thread.sleep(50);
        t.interrupt();
        // true
        log.info("interrupt flag: {}", t.isInterrupted());
        Thread.sleep(200);
        // false 线程结束返回false
        log.info("interrupt flag: {}", t.isInterrupted());
    }
}
