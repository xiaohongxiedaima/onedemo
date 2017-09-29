package com.xiaohongxiedaima.demo.algorithm.dnf;

import com.google.gson.Gson;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Filters.*;
import com.xiaohongxiedaima.demo.algorithm.dnf.model.AdGroup;
import com.xiaohongxiedaima.demo.algorithm.dnf.model.AdPlan;
import com.xiaohongxiedaima.demo.algorithm.dnf.model.AdUnit;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;

/**
 * Created by liusheng on 17-9-28.
 */
public class MetaData {

    private static final Logger LOG = LoggerFactory.getLogger(MetaData.class);

    private static Map<String, AdPlan> adPlanMap = new HashMap<String, AdPlan>();
    private static Map<String, AdGroup> adGroupMap = new HashMap<String, AdGroup>();
    private static Map<String, AdUnit> adUnitMap = new HashMap<String, AdUnit>();

    private static MongoClient mongoClient = new MongoClient( "192.168.1.69" , 27017 );
    private static MongoDatabase database = mongoClient.getDatabase("sndo");

    private static Gson gson = new Gson();

    // 加载所有广告
    public static void loadAd() {
        MongoCollection<Document> planCollection = database.getCollection("ad.plan");
        Bson planFilters = and(eq("status", 1), gt("budget", 0.0f), eq("today_status", 1));

        planCollection.find(planFilters).forEach(new Block<Document>() {
            public void apply(Document document) {
                String json = gson.toJson(document);
                AdPlan adPlan = gson.fromJson(json, AdPlan.class);
                adPlan.setId(document.getString("_id"));
                adPlanMap.put(adPlan.getId(), adPlan);
            }
        });

        MongoCollection<Document> groupCollection = database.getCollection("ad.group");
        Bson groupFilters = and(eq("plan_status", 1), eq("status", 1));
        groupCollection.find(groupFilters).forEach(new Block<Document>() {
            public void apply(Document document) {
                String json = gson.toJson(document);
                AdGroup adGroup = gson.fromJson(json, AdGroup.class);
                adGroup.setId(document.getString("_id"));
                adGroupMap.put(adGroup.getId(), adGroup);
            }
        });

        MongoCollection<Document> unitCollection = database.getCollection("ad.unit");
        Bson unitFilters = and(eq("status", 1));
        unitCollection.find(unitFilters).forEach(new Block<Document>() {
            public void apply(Document document) {
                String json = gson.toJson(document);
                AdUnit adUnit = gson.fromJson(json, AdUnit.class);
                adUnit.setId(document.getString("_id"));
                adUnitMap.put(adUnit.getId(), adUnit);
            }
        });
    }

    public static void main(String[] args) {
        MetaData.loadAd();
        LOG.info(gson.toJson(adPlanMap));
        LOG.info(gson.toJson(adGroupMap));
        LOG.info(gson.toJson(adUnitMap));
    }


}
