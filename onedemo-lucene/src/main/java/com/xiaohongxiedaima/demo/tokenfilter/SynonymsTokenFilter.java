package com.xiaohongxiedaima.demo.tokenfilter;

import com.xiaohongxiedaima.demo.synonyms.SynonymsContext;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

import java.io.IOException;

/**
 * @author xiaohongxiedaima
 * @version 16-7-20
 * @E-mail redfishinaction@yahoo.com
 */
public class SynonymsTokenFilter extends TokenFilter {

    private CharTermAttribute cta;
    private PositionIncrementAttribute pis;

    private SynonymsContext synonymsContext;

    public SynonymsTokenFilter(TokenStream input, SynonymsContext synonymsContext) {
        super(input);
        this.synonymsContext = synonymsContext;

        cta = addAttribute(CharTermAttribute.class);
        pis = addAttribute(PositionIncrementAttribute.class);
    }

    public boolean incrementToken() throws IOException {

        if (synonymsContext.containSynonyms(cta.toString())) {

            for (String synonyms : synonymsContext.getSynonyms(cta.toString())) {
                cta.setEmpty();
                cta.append(synonyms);
            }

        }

        return true;
    }


}
