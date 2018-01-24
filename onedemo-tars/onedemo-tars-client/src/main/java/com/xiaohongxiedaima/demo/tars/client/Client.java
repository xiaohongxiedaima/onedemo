package com.xiaohongxiedaima.demo.tars.client;

import com.qq.tars.client.Communicator;
import com.qq.tars.client.CommunicatorConfig;
import com.qq.tars.client.CommunicatorFactory;
import com.qq.tars.client.ServantProxyConfig;
import com.qq.tars.common.util.Config;
import com.xiaohongxiedaima.demo.tars.client.index.IndexPrx;
import com.xiaohongxiedaima.demo.tars.client.ip.IpPrx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by liusheng on 18-1-19.
 */
public class Client {

    private static final Logger log = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws IOException {

        String path = null;
        Communicator communicator;

        try {
            path = new ClassPathResource("tars.conf").getFile().getCanonicalPath();

            CommunicatorConfig cfg = new CommunicatorConfig();
            cfg.load(Config.parseFile(path, Charset.forName("UTF-8")));

            communicator = CommunicatorFactory.getInstance().getCommunicator(cfg);
        } catch (Exception e) {
            log.error("init error, path={}", path, e);
            throw new RuntimeException(e);
        }

        ServantProxyConfig ipPrxConfig = new ServantProxyConfig("dmp.ip.ipObj");
        ipPrxConfig.setSyncTimeout(50);
        IpPrx ipPrx = communicator.stringToProxy(IpPrx.class, ipPrxConfig);
        String ipResult = ipPrx.query("ip");

        log.info("ip: {}", ipResult);

        ServantProxyConfig indexPrxConfig = new ServantProxyConfig("dmp.index.indexObj");
        indexPrxConfig.setSyncTimeout(30);
        IndexPrx indexPrx = communicator.stringToProxy(IndexPrx.class, indexPrxConfig);
        String indexResult = indexPrx.query("adid");
        log.info("adid: {}", indexResult);


    }

}
