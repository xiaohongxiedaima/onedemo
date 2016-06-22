package com.xiaohongxiedaima.demo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author xiaohongxiedaima
 * @version 16/6/13
 * @E-mail redfishinaction@yahoo.com
 */
public class GetTest {

    private static final Logger logger = LoggerFactory.getLogger(GetTest.class);

    private static final String TABLE_NAME = "test";

    public static void main(String[] args) throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        HTable hTable = new HTable(configuration, TABLE_NAME);

        String rowkey = "rowkey1";
        Get get = new Get(Bytes.toBytes(rowkey));
        Result result = hTable.get(get);
    }
}
