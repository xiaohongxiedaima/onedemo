package com.xiaohongxiedaima.demo;

import org.apache.commons.cli.*;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.MetaException;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.hadoop.hive.metastore.tools.HiveMetaTool;
import org.apache.thrift.TException;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

/**
 * @author xiaohongxiedaima
 * @version 16/6/25
 * @E-mail redfishinaction@yahoo.com
 */
public class BulkLoad {

    private static final Logger logger = LoggerFactory.getLogger(BulkLoad.class);

    private static final Options OPTIONS = new Options();

    private static final Map<String,String> CONF = new HashMap<String,String>();

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
                            CONF.put(keyValue[0], keyValue[1]);
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

    public static void main(String[] args) {

        args = new String[] {
                "-conf","/Users/redfish/code/IntelliJIDEAProjects/onedemo/onedemo-hbase/src/main/resources/bulkload.properties"
        };

        parseOptions(args);

    }

    public static void getTableMetaStore(String dbname, String name) throws TException {
        HiveConf hiveConf = new HiveConf();
        HiveMetaStoreClient hiveMetaStoreClient = new HiveMetaStoreClient(hiveConf);
        Table table = hiveMetaStoreClient.getTable(dbname, name);
    }

}
