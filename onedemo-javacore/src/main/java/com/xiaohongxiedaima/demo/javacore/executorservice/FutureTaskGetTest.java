package com.xiaohongxiedaima.demo.javacore.executorservice;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @author liusheng
 * @date 2021年04月30日 1:47 下午
 */
@Slf4j
public class FutureTaskGetTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(100,
                100,
                100l,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue(10000));

        List<Future<?>> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Future<?> future = threadPoolExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    Random random = new Random();
                    int i = random.nextInt(100000);
                    try {
                        Thread.sleep(i);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage());
                    }
                    log.info("end: {}", Thread.currentThread().getName());
                }
            });
            list.add(future);
        }

        for (int i = 0; i < 100; i++) {
            Object o = list.get(i).get(100, TimeUnit.SECONDS);
            log.info("future task: {}", i);
        }
    }
}
