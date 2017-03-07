package com.xiaohongxiedaima.demo.elasticsearch.utils;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.suggest.Suggest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

/**
 * Created by liusheng on 2017-03-07.
 */
public class SearchResponseUtils {

    private static final Logger LOG = LoggerFactory.getLogger(SearchResponseUtils.class);

    public static final void print(SearchResponse searchResponse) {

        SearchHits hits = searchResponse.getHits();

        if (hits.totalHits() > 0) {
            LOG.info("total hits: {}", hits.getTotalHits());
            for (SearchHit hit : hits.getHits()) {
                LOG.info("score: {}, source: {}", hit.score(), hit.getSourceAsString());
            }
        }

        Suggest suggest = searchResponse.getSuggest();

        Iterator<Suggest.Suggestion<? extends Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option>>> iterator = suggest.iterator();
        while (iterator.hasNext()) {
            Suggest.Suggestion<? extends Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option>> next = iterator.next();

            LOG.info("suggest name: {}", next.getName());

            for (Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option> entry : next.getEntries()) {
                LOG.info("text: {}", entry.getText());

                for (Suggest.Suggestion.Entry.Option option : entry.getOptions()) {
                    LOG.info("score: {}, text: {}", option.getScore(), option.getText());
                }

            }

        }

    }

}
