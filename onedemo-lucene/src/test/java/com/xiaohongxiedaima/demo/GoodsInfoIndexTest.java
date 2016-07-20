package com.xiaohongxiedaima.demo;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xiaohongxiedaima
 * @version 16-7-18
 * @E-mail redfishinaction@yahoo.com
 */
public class GoodsInfoIndexTest {

    private static final Logger logger = LoggerFactory.getLogger(GoodsInfoIndexTest.class);

    GoodsInfoIndex goodsInfoIndex;

    @Before
    public void before() {
        goodsInfoIndex = new GoodsInfoIndex();
    }

    @Test
    public void testIndex() {
        goodsInfoIndex.index();
    }

    @Test
    public void testSearch() {
        goodsInfoIndex.search();
    }

    @Test
    public void testSearchByStr() {


        // 默认 OR
        String str = "红色 水染皮";
        logger.info(str);
        goodsInfoIndex.searchByStr(str);


        // AND
        str = "红色 AND 水染皮";
        logger.info(str);
        goodsInfoIndex.searchByStr(str);

        // 创建 QueryParser 会指定默认搜索的 Field , 如果想同时搜索其他 Field 可以在查询字符串中指定
        str = "goodsAttrs:红色 AND goodsId:1";
        logger.info(str);
        goodsInfoIndex.searchByStr(str);

        // 使用 () 来组合关系 AND OR NOT
        str = "(goodsAttrs:红色 OR goodsAttrs:新品) AND (goodsId:2 OR goodsId:4)";
        logger.info(str);
        goodsInfoIndex.searchByStr(str);

        // 相连字符串匹配
        str = "\"黑色 水染皮\"";
        logger.info(str);
        goodsInfoIndex.searchByStr(str);

        // 范围匹配
        str = "goodsId: [1 TO 3}";
        logger.info(str);
        goodsInfoIndex.searchByStr(str);
    }
}
