package com.xiaohongxiedaima.demo.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author xiaohongxiedaima
 * @version 16/7/20
 * @E-mail redfishinaction@yahoo.com
 */
public class AnalyzerUtils {

    private static final Logger logger = LoggerFactory.getLogger(AnalyzerUtils.class);

    public static void printTokenInfo(String str, Analyzer analyzer) {
        TokenStream tokenStream = analyzer.tokenStream("empty", str);
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

        try {
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                logger.info("[ {} ]", charTermAttribute);
            }
            tokenStream.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void printlnAllTokenInfo(String str, Analyzer analyzer) {
        TokenStream tokenStream = analyzer.tokenStream("empty", str);
        TypeAttribute ta = tokenStream.addAttribute(TypeAttribute.class);
        PositionIncrementAttribute pia = tokenStream.addAttribute(PositionIncrementAttribute.class);
        PositionLengthAttribute pla = tokenStream.addAttribute(PositionLengthAttribute.class);
        OffsetAttribute oa = tokenStream.addAttribute(OffsetAttribute.class);
        CharTermAttribute ca = tokenStream.addAttribute(CharTermAttribute.class);
        TermToBytesRefAttribute ttbra = tokenStream.addAttribute(TermToBytesRefAttribute.class);

        try {
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                logger.info("TypeAttribute: {}", ta.type());
                logger.info("PositionIncrementAttribute: {}", pia.getPositionIncrement());
                logger.info("PositionLengthAttribute: {}", pla.getPositionLength());
                logger.info("OffsetAttribute: start: {}, end: {}", oa.startOffset(), oa.endOffset());
                logger.info("CharTermAttribute: {}", ca);
                logger.info("TermToBytesRefAttribute: {}", ttbra.getBytesRef().toString());
                logger.info("");
            }
            tokenStream.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
