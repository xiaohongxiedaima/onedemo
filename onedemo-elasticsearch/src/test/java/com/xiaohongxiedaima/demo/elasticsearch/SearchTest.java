package com.xiaohongxiedaima.demo.elasticsearch;

import com.xiaohongxiedaima.demo.elasticsearch.utils.ESClient;
import com.xiaohongxiedaima.demo.elasticsearch.utils.SearchResponseUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liusheng on 2017-03-05.
 */
public class SearchTest {

    private static final Logger LOG = LoggerFactory.getLogger(SearchTest.class);

    @Test
    public void testSearch() {
        TransportClient client = ESClient.getClient();

        QueryBuilder queryBuilder = QueryBuilders.matchQuery("title", "室");

        SearchResponse searchResponse = client.prepareSearch("demo").setTypes("demo").setQuery(queryBuilder).get();
        SearchResponseUtils.print(searchResponse);

    }

}
