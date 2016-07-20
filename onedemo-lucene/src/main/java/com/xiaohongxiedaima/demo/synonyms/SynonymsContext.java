package com.xiaohongxiedaima.demo.synonyms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaohongxiedaima
 * @version 16-7-20
 * @E-mail redfishinaction@yahoo.com
 */
public interface SynonymsContext {
    public List<String> getSynonyms(String string);

    public boolean containSynonyms(String string);
}
