package com.xiaohongxiedaima.demo.tars.server.ip.impl;

import com.xiaohongxiedaima.demo.tars.server.ip.IpServant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liusheng on 18-1-22.
 */
public class IpServantImpl implements IpServant {

    private static final Logger LOG = LoggerFactory.getLogger(IpServantImpl.class);
    @Override
    public String query(String ip) {
        try {
            Thread.sleep(10l);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
        }
        return "ip result";
    }
}
