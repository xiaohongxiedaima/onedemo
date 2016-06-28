package com.xiaohongxiedaima.demo;

import org.apache.commons.cli.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author xiaohongxiedaima
 * @version 16/6/25
 * @E-mail redfishinaction@yahoo.com
 */
public class Hive2Hbase {

    private static final Logger logger = LoggerFactory.getLogger(Hive2Hbase.class);

    private static final Options OPTIONS = new Options();

    private static final String HIVE_DBNAME = "hive.dbname";
    private static final String HIVE_TABLENAME = "hive.tablename";
    private static final String HBASE_TABLENAME = "hbase.tablename";

    private static final Map<String,String> CONF = new HashMap<String,String>();
    private static final Hive2HbaseConf HIVE_2_HBASE_CONF = new Hive2HbaseConf();

    static {
        initOptions();
    }

    private static final String USAGE = "\n用法: java class [-options]\n" +
            "其中的选项包括:\n" +
            "    -conf[√]              运行bulkload时配置文件的绝对路径";

    private static void initOptions() {
        OPTIONS.addOption("conf", true, "conf path");
    }

    private static void parseOptions(String[] args) {
        try {
            CommandLineParser parser = new BasicParser();
            CommandLine commandLine = parser.parse(OPTIONS, args);
            if (commandLine.hasOption("conf")) {
                String conf = commandLine.getOptionValue("conf");
                File file = new File(conf);
                if (file.exists() && file.isFile()) {
                    System.out.println("加载配置...");
                    Scanner scanner = new Scanner(file);
                    while (scanner.hasNext()) {
                        String line = scanner.next();
                        System.out.println(line);
                        if (line != null && !line.trim().equals("")) {
                            String[] keyValue = line.split("=");
                            CONF.put(keyValue[0].trim(), keyValue[1].trim());
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException("arg: conf require");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            logger.error(USAGE);
            System.exit(-1);
        }
    }

    private static void checkConf() {
        String hiveDBName = CONF.get(HIVE_DBNAME);
        String hiveTableName = CONF.get(HIVE_TABLENAME);
        String hbaseTableName = CONF.get(HBASE_TABLENAME);

        HIVE_2_HBASE_CONF.setHiveDBName(hiveDBName);
        HIVE_2_HBASE_CONF.setHiveTableName(hiveTableName);
        HIVE_2_HBASE_CONF.setHbaseTableName(hbaseTableName);
    }

    public static void main(String[] args) throws TException, IOException {

        args = new String[] {
                "-conf","/Users/redfish/code/IntelliJIDEAProjects/onedemo/onedemo-hbase/src/main/resources/bulkload.properties"
        };

        parseOptions(args);
        // 检查配置文件的参数是否正确
        checkConf();

        Table hiveTable = getHiveTable(HIVE_2_HBASE_CONF.getHiveDBName(), HIVE_2_HBASE_CONF.getHiveTableName());
        HTableInterface hbaseTable = getHTableInterface(HIVE_2_HBASE_CONF.getHbaseTableName());



    }


    public static Table getHiveTable(String dbname, String name) throws TException {
        HiveConf hiveConf = new HiveConf();
        HiveMetaStoreClient hiveMetaStoreClient = new HiveMetaStoreClient(hiveConf);
        Table hiveTable = hiveMetaStoreClient.getTable(dbname, name);
        return hiveTable;
    }

    public static HTableInterface getHTableInterface(String tableName) throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        HConnection connection = HConnectionManager.createConnection(configuration);
        HTableInterface table = connection.getTable(tableName);
        return table;
    }

}
