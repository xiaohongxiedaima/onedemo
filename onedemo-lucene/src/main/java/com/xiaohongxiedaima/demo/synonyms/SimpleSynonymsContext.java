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
public class SimpleSynonymsContext implements SynonymsContext {

    private static Map<String, List<String>> SYNONYMS = new HashMap<String, List<String>>();

    static {
        String key = "中国";
        List<String> values = new ArrayList<String>();
        values.add("zg");
        values.add("大陆");
        SYNONYMS.put(key, values);

        key = "我";
        values = new ArrayList<String>();
        values.add("咱");
        SYNONYMS.put(key, values);

    }

    public List<String> getSynonyms(String string) {
        return SYNONYMS.get(string);
    }

    public boolean containSynonyms(String string) {
        return SYNONYMS.containsKey(string);
    }
}
