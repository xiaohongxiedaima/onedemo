package com.xiaohongxiedaima.demo.tars.server.index.impl;

import com.xiaohongxiedaima.demo.tars.server.index.IndexServant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liusheng on 18-1-22.
 */
public class IndexServantImpl implements IndexServant {
    private static final Logger LOG = LoggerFactory.getLogger(IndexServantImpl.class);

    public String query(String adid) {
        try {
            Thread.sleep(20l);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
        }

        return adid;
    }
}
