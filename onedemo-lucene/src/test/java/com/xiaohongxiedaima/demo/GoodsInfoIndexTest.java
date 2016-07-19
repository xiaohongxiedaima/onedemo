package com.xiaohongxiedaima.demo;

import org.junit.Before;
import org.junit.Test;

/**
 * @author xiaohongxiedaima
 * @version 16-7-18
 * @E-mail redfishinaction@yahoo.com
 */
public class GoodsInfoIndexTest {

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

}
