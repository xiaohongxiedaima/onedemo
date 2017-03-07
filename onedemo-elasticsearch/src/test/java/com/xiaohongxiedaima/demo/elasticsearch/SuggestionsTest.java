package com.xiaohongxiedaima.demo.elasticsearch;

import com.xiaohongxiedaima.demo.elasticsearch.utils.ESClient;
import com.xiaohongxiedaima.demo.elasticsearch.utils.SearchResponseUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.elasticsearch.search.suggest.term.TermSuggestionBuilder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liusheng on 2017-03-07.
 */
public class SuggestionsTest {

    private static final Logger LOG = LoggerFactory.getLogger(SuggestionsTest.class);

    private static final TransportClient esClient = ESClient.getClient();

    @Test
    public void testTermSuggester() {

        SuggestBuilder suggestBuilder = new SuggestBuilder();

        TermSuggestionBuilder termSuggestionBuilder = SuggestBuilders.termSuggestion("title1").text("hello e");

        suggestBuilder.addSuggestion("title1Term", termSuggestionBuilder);
        LOG.info("suggest: {}", suggestBuilder.toString());

        SearchResponse searchResponse = esClient.prepareSearch("demo").setTypes("demo").suggest(suggestBuilder).get();

        SearchResponseUtils.print(searchResponse);

    }


    @Test
    public void testCompletionSuggester() {

        SuggestBuilder suggestBuilder = new SuggestBuilder();
        CompletionSuggestionBuilder completionSuggestionBuilder = SuggestBuilders.completionSuggestion("title1").text("hi e");

        suggestBuilder.addSuggestion("title1Completiong", completionSuggestionBuilder);
        LOG.info("suggest: {}", suggestBuilder.toString());

        SearchResponse searchResponse = esClient.prepareSearch("demo").setTypes("demo").suggest(suggestBuilder).get();

        SearchResponseUtils.print(searchResponse);

    }

}
