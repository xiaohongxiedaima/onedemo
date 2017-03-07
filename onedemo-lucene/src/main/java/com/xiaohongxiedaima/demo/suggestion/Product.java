package com.xiaohongxiedaima.demo.suggestion;

/**
 * Created by liusheng on 2017-03-07.
 */
public class Product implements java.io.Serializable {
    String name;
    String image;
    String[] regions;
    int numberSold;

    public Product(String name, String image, String[] regions,
                   int numberSold) {
        this.name = name;
        this.image = image;
        this.regions = regions;
        this.numberSold = numberSold;
    }
}