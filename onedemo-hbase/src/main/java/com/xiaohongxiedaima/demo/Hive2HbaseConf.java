package com.xiaohongxiedaima.demo;

/**
 * @author xiaohongxiedaima
 * @version 16/6/28
 * @E-mail redfishinaction@yahoo.com
 */

class Hive2HbaseConf {

    private String hiveDBName;
    private String hiveTableName;
    private String hivePartition;

    private String hbaseTableName;


    public Hive2HbaseConf() {

    }

    public String getHiveDBName() {
        return hiveDBName;
    }

    public void setHiveDBName(String hiveDBName) {
        this.hiveDBName = hiveDBName;
    }

    public String getHiveTableName() {
        return hiveTableName;
    }

    public void setHiveTableName(String hiveTableName) {
        this.hiveTableName = hiveTableName;
    }

    public String getHivePartition() {
        return hivePartition;
    }

    public void setHivePartition(String hivePartition) {
        this.hivePartition = hivePartition;
    }

    public String getHbaseTableName() {
        return hbaseTableName;
    }

    public void setHbaseTableName(String hbaseTableName) {
        this.hbaseTableName = hbaseTableName;
    }
}

