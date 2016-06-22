package com.xiaohongxiedaima.demo.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @author xiaohongxiedaima
 * @version 16/5/10
 * @E-mail redfishinaction@yahoo.com
 */
public class SimpleWatcher implements Watcher {

    private static final Logger logger = LoggerFactory.getLogger(SimpleWatcher.class);

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public boolean await() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
        return true;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        logger.info("watchedEvent: {}", watchedEvent);
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            countDownLatch.countDown();
        }
    }
}
