package com.xiaohongxiedaima.demo;

import com.alibaba.fastjson.JSON;
import org.apache.commons.cli.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.StorageDescriptor;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
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

    // 如果需要导入指定 hive 分区表的数据需要传入这个参数
    // 参数格式为 d1:h1,d2:h2,d3:h3
    // 其中 h1 为 d1 的子分区
    private static final String HIVE_PARTITION = "hive.partition";

    private static final String HBASE_TABLENAME = "hbase.tablename";


    private static final Map<String,String> CONF = new HashMap<String,String>();
    public static final Hive2HbaseConf HIVE_2_HBASE_CONF = new Hive2HbaseConf();

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

            // 解析配置文件
            if (commandLine.hasOption("conf")) {
                String conf = commandLine.getOptionValue("conf");
                File file = new File(conf);
                if (file.exists() && file.isFile()) {
                    logger.info("加载配置...");

                    Properties props = new Properties();
                    props.load(new FileInputStream(file));

                    for (Map.Entry<Object, Object> entry : props.entrySet()) {
                        CONF.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
                    }

                    logger.info("CONF: {}", JSON.toJSONString(CONF, true));

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
        String hivePartition = CONF.get(HIVE_PARTITION);

        String hbaseTableName = CONF.get(HBASE_TABLENAME);

        HIVE_2_HBASE_CONF.setHiveDBName(hiveDBName);
        HIVE_2_HBASE_CONF.setHiveTableName(hiveTableName);
        HIVE_2_HBASE_CONF.setHivePartition(hivePartition);

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
//        HTableInterface hbaseTable = getHTableInterface(HIVE_2_HBASE_CONF.getHbaseTableName());

        logger.info(JSON.toJSONString(hiveTable, true));

        Path[] inputPathAndOutputPath = getHiveInputPath(hiveTable);
        logger.info(JSON.toJSONString(inputPathAndOutputPath, true));
    }


    public static Table getHiveTable(String dbname, String name) throws TException {
        HiveConf hiveConf = new HiveConf();
        HiveMetaStoreClient hiveMetaStoreClient = new HiveMetaStoreClient(hiveConf);
        Table hiveTable = hiveMetaStoreClient.getTable(dbname, name);
        return hiveTable;
    }

    /**
     * 获取需要导出的hive表在hdfs上的路径
     * @param table
     * @return
     */
    public static Path[] getHiveInputPath(Table table) {

        Path[] paths = null;

        StorageDescriptor sd = table.getSd();
        String location = sd.getLocation();

        Hive2HbaseConf hive2HbaseConf = HIVE_2_HBASE_CONF;

        String exportPartition = hive2HbaseConf.getHivePartition();
        if (exportPartition == null) { //直接导出整表
            Path path = new Path(location);
            paths = new Path[]{path};
        } else { //导出指定分区
            List<FieldSchema> partitionKeys = table.getPartitionKeys();

            String[] allPartitionValues = exportPartition.split(",");

            List<Path> pathList = new ArrayList<Path>();
            for (String partitionValue : allPartitionValues) {
                String[] values = partitionValue.split(":");
                StringBuffer path = new StringBuffer(location);
                for (int i = 0; i < values.length; i ++) {
                    path.append("/").append(partitionKeys.get(i).getName()).append("=").append(values[i].trim());
                }
                pathList.add(new Path(path.toString()));
            }

            paths = pathList.toArray(new Path[pathList.size()]);
        }

        for (Path path : paths) {
            logger.info("hive file paths: {}", path);
        }
        return paths;
    }

    public static HTableInterface getHTableInterface(String tableName) throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        HConnection connection = HConnectionManager.createConnection(configuration);
        HTableInterface table = connection.getTable(tableName);
        return table;
    }

}
