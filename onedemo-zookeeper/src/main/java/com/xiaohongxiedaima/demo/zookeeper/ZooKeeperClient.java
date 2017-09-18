package com.xiaohongxiedaima.demo.zookeeper;

import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xiaohongxiedaima
 * @version 16/5/10
 * @E-mail redfishinaction@yahoo.com
 */
public class ZooKeeperClient{

    private static final Logger logger = LoggerFactory.getLogger(ZooKeeperClient.class);

    /**
     *
     * @param connectionString ip:port,ip:port/node/node
     * @param sessionTimeout
     * @return
     */
    public static ZooKeeper getInstance(String connectionString, int sessionTimeout) {
        ZooKeeper zooKeeper = null;
        try {
            SimpleWatcher watcher = new SimpleWatcher();
            zooKeeper = new ZooKeeper(connectionString, sessionTimeout, watcher);
            logger.info("zookeeper connection state: {}", zooKeeper.getState());
            watcher.await();
            logger.info("zookeeper connection state: {}", zooKeeper.getState());
            logger.info("zookeeper session established" );
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return zooKeeper;
    }

    public static void main(String[] args) throws InterruptedException {
        String connectionString = "127.0.0.1:2181/zookeeper";
        int sessionTimeout = 5000;
        ZooKeeper zooKeeper = ZooKeeperClient.getInstance(connectionString, sessionTimeout);
        logger.info(zooKeeper.toString());
        zooKeeper.close();
    }

}
