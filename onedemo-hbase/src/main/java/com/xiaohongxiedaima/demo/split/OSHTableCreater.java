package com.xiaohongxiedaima.demo.split;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableExistsException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.regionserver.BloomType;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;

/**
 * @author xiaohongxiedaima
 * @version 16-7-22
 * @E-mail redfishinaction@yahoo.com
 */
public class OSHTableCreater {
    /**
     * <pre>
     * 日志
     * </pre>
     */
    private static final Logger logger = LoggerFactory.getLogger(OSHTableCreater.class);

    /**
     * <pre>
     *
     * </pre>
     *
     * @param args
     */
    public static void main(String[] args) {
//        Configuration conf = HBaseConfiguration.create();
//
//        try {
//            HBaseAdmin hba = new HBaseAdmin(conf);
//
//            createTable(hba, "order_bak", new String[] { "order", "item" }, getHexSplits(1000, 21941700, 300));
//
//            createTable(hba, "fos_order_detail", new String[] { "order", "item" }, getHexSplits(1000, 21941700, 300));
//
//            createTable(hba, "fos_order", new String[] { "order" }, getHexSplits(100, 73859372, 700));
//        } catch (Exception e) {
//            logger.error("", e);
//        }

        getHexSplits(10000, 40000, 100);
//        getHexSplits("0", "50000000", 100);
    }

    public static boolean createTable(HBaseAdmin admin, String tableName, String[] fms, byte[][] splits) throws IOException {
        try {
            HTableDescriptor table = new HTableDescriptor(tableName);

            HColumnDescriptor column = null;
            for (String fm : fms) {
                column = new HColumnDescriptor(fm);
                column.setBloomFilterType(BloomType.ROWCOL);
                table.addFamily(column);
            }

            admin.createTable(table, splits);
            return true;
        } catch (TableExistsException e) {
            return false;
        }
    }

    public static byte[][] getHexSplits(String startKey, String endKey, int numRegions) {
        byte[][] splits = new byte[numRegions - 1][];
        BigInteger lowestKey = new BigInteger(startKey, 32);
        BigInteger highestKey = new BigInteger(endKey, 32);
        BigInteger range = highestKey.subtract(lowestKey);
        BigInteger regionIncrement = range.divide(BigInteger.valueOf(numRegions));
        lowestKey = lowestKey.add(regionIncrement);

        for (int i = 0; i < numRegions - 1; i++) {
            BigInteger key = lowestKey.add(regionIncrement.multiply(BigInteger.valueOf(i)));
            byte[] b = String.format("%032x", key).getBytes();
            splits[i] = b;
            System.out.println(key);
        }

        return splits;
    }

    public static byte[][] getHexSplits(long startKey, long endKey, int numRegions) {
        byte[][] splits = new byte[numRegions - 1][];
        long range = endKey - startKey;
        long regionIncrement = range / numRegions;

        for (int i = 0; i < numRegions - 1; i++) {
            long key = startKey + (regionIncrement * i);
            byte[] b = Bytes.toBytes(key);
            splits[i] = b;
            System.out.println(key);
        }

        return splits;
    }

}
