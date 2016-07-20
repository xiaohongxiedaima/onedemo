package com.xiaohongxiedaima.demo.analyzer;

import com.xiaohongxiedaima.demo.attribute.TokenAttributeUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseTokenizer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xiaohongxiedaima
 * @version 16-7-20
 * @E-mail redfishinaction@yahoo.com
 */
public class StopAnalyzerDemo {

    private static final Logger logger = LoggerFactory.getLogger(StopAnalyzerDemo.class);

    public static void main(String[] args) {

        Analyzer analyzer = new StandardAnalyzer();

        String str = "i am中国 from china and i love it";
        logger.info("source str: {}", str);

        TokenStream ts = analyzer.tokenStream("", str);

        TokenAttributeUtils.print(ts);


    }

}
