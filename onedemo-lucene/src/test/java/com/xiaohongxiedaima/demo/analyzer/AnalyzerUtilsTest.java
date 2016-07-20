package com.xiaohongxiedaima.demo.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.junit.Before;
import org.junit.Test;
import org.lionsoul.jcseg.analyzer.v5x.JcsegAnalyzer5X;
import org.lionsoul.jcseg.tokenizer.core.JcsegTaskConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xiaohongxiedaima
 * @version 16/7/20
 * @E-mail redfishinaction@yahoo.com
 */
public class AnalyzerUtilsTest {

    private static final Logger logger = LoggerFactory.getLogger(AnalyzerUtilsTest.class);


    @Before
    public void before() {

    }

    @Test
    public void testPrintToken() {
        Analyzer a1 = new StandardAnalyzer();
        Analyzer a2 = new StopAnalyzer();
        Analyzer a3 = new SimpleAnalyzer();
        Analyzer a4 = new WhitespaceAnalyzer();
        Analyzer a5 = new JcsegAnalyzer5X(JcsegTaskConfig.COMPLEX_MODE);

        String text = "I'm 小红啊,红色的红, whatis you name, my email is liusheng11@qq.com, tel: 111111";
        logger.info(a1.getClass().toString());
        AnalyzerUtils.printTokenInfo(text, a1);
        logger.info(a2.getClass().toString());
        AnalyzerUtils.printTokenInfo(text, a2);
        logger.info(a3.getClass().toString());
        AnalyzerUtils.printTokenInfo(text, a3);
        logger.info(a4.getClass().toString());
        AnalyzerUtils.printTokenInfo(text, a4);
        logger.info(a5.getClass().toString());
        AnalyzerUtils.printTokenInfo(text, a5);
    }

    @Test
    public void testPrintAllTokenInfo() {
        Analyzer a1 = new StandardAnalyzer();
        Analyzer a2 = new StopAnalyzer();
        Analyzer a3 = new SimpleAnalyzer();
        Analyzer a4 = new WhitespaceAnalyzer();
        Analyzer a5 = new JcsegAnalyzer5X(JcsegTaskConfig.COMPLEX_MODE);

        String text = "I'm 小红啊,红色的红, whatis you name, my email is liusheng11@qq.com, tel: 111111";
        logger.info(a1.getClass().toString());
        AnalyzerUtils.printlnAllTokenInfo(text, a1);
        logger.info(a2.getClass().toString());
        AnalyzerUtils.printlnAllTokenInfo(text, a2);
        logger.info(a3.getClass().toString());
        AnalyzerUtils.printlnAllTokenInfo(text, a3);
        logger.info(a4.getClass().toString());
        AnalyzerUtils.printlnAllTokenInfo(text, a4);
        logger.info(a5.getClass().toString());
        AnalyzerUtils.printlnAllTokenInfo(text, a5);
    }

}
