package com.xiaohongxiedaima.demo.analyzer;

import com.xiaohongxiedaima.demo.attribute.TokenAttributeUtils;
import com.xiaohongxiedaima.demo.synonyms.SimpleSynonymsContext;
import com.xiaohongxiedaima.demo.synonyms.SynonymsContext;
import com.xiaohongxiedaima.demo.tokenfilter.SynonymsTokenFilter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.lionsoul.jcseg.analyzer.v5x.JcsegAnalyzer5X;
import org.lionsoul.jcseg.analyzer.v5x.JcsegFilter;
import org.lionsoul.jcseg.analyzer.v5x.JcsegTokenizer;
import org.lionsoul.jcseg.tokenizer.core.DictionaryFactory;
import org.lionsoul.jcseg.tokenizer.core.JcsegException;
import org.lionsoul.jcseg.tokenizer.core.JcsegTaskConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author xiaohongxiedaima
 * @version 16-7-20
 * @E-mail redfishinaction@yahoo.com
 */
public class SynonymsAnalyzer extends JcsegAnalyzer5X {

    private static final Logger logger = LoggerFactory.getLogger(SynonymsAnalyzer.class);

    private int mode;
    private JcsegTaskConfig config;

    private SynonymsContext synonymsContext;

    public SynonymsAnalyzer(int mode, JcsegTaskConfig config, SynonymsContext synonymsContext) {
        super(mode, config);
        this.mode = mode;
        this.config = config;

        this.synonymsContext = synonymsContext;

    }

    protected TokenStreamComponents createComponents(String fieldName) {

        try {
            Tokenizer tokenizer = new JcsegTokenizer(mode, config, DictionaryFactory.createSingletonDictionary(config));

            TokenFilter f1 = new SynonymsTokenFilter(tokenizer, synonymsContext);

            return new TokenStreamComponents(tokenizer, f1);

        } catch (JcsegException e) {
            logger.error(e.getMessage(), e);

        } catch (IOException e) {
            logger.error(e.getMessage(), e);

        }

        return null;

    }

    public static void main(String[] args) {

        SynonymsAnalyzer analyzer = new SynonymsAnalyzer(JcsegTaskConfig.COMPLEX_MODE,
                new JcsegTaskConfig(true), new SimpleSynonymsContext());

        String str = "i am中国 from china and i love it";
        TokenStream ts = analyzer.tokenStream("", str);
        TokenAttributeUtils.print(ts);

    }


}
