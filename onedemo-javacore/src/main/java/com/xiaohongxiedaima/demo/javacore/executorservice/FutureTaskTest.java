package com.xiaohongxiedaima.demo.javacore.executorservice;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author liusheng
 * @date 2021年04月01日 2:41 下午
 */
@Slf4j
public class FutureTaskTest {
    private static ThreadPoolExecutor threadPoolExecutor;

    static {
        threadPoolExecutor = new ThreadPoolExecutor(10,
                10,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100));
        threadPoolExecutor.prestartAllCoreThreads();
    }


    public static void main(String[] args) throws InterruptedException, ExecutionException {
        final Obj obj1 = new Obj(1, 1);
        log.info("obj1: {}", obj1);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                obj1.setValue(2);
                log.info(Thread.currentThread().getName());
            }
        };

        Future<Obj> submit = threadPoolExecutor.submit(runnable, obj1);
        Obj obj2 = submit.get();

        log.info("obj2: {}", obj2);
    }

    @Data
    public static class Obj {
        private int key;

        private int value;

        public Obj(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }
}
