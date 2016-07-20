package com.xiaohongxiedaima.demo.attribute;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.CharTermAttributeImpl;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaohongxiedaima
 * @version 16-7-20
 * @E-mail redfishinaction@yahoo.com
 */
public class TokenAttributeUtils {

    private static final Logger logger = LoggerFactory.getLogger(TokenAttributeUtils.class);

    public static void print(TokenStream ts) {

        try {

            CharTermAttribute cta = ts.addAttribute(CharTermAttribute.class);
            OffsetAttribute oa = ts.addAttribute(OffsetAttribute.class);
            PositionIncrementAttribute pia = ts.addAttribute(PositionIncrementAttribute.class);

            ts.reset();

            while (ts.incrementToken()) {

                logger.info("[{}] {} [{},{}]", pia.getPositionIncrement(), cta.toString(), oa.startOffset(), oa.endOffset());

            }

            ts.end();

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

    }

}
