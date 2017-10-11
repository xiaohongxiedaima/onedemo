package com.xiaohongxiedaima.demo.algorithm.dnf;

import com.google.gson.Gson;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.xiaohongxiedaima.demo.algorithm.dnf.model.AdGroup;
import com.xiaohongxiedaima.demo.algorithm.dnf.model.AdPlan;
import com.xiaohongxiedaima.demo.algorithm.dnf.model.AdUnit;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;

/**
 * Created by liusheng on 17-9-28.
 */
public class MetaData {

    private static final Logger LOG = LoggerFactory.getLogger(MetaData.class);

    public static Map<String, AdPlan> AD_PLAN_MAP = new HashMap<String, AdPlan>();
    public static Map<String, AdGroup> AD_GROUP_MAP = new HashMap<String, AdGroup>();
    public static Map<String, AdUnit> AD_UNIT_MAP = new HashMap<String, AdUnit>();

    public static Map<String, String> CATE_4_MAP = new HashMap<String, String>();

    private static Gson gson = new Gson();

    static {
        loadAd();
    }
    // 加载所有广告
    public static void loadAd() {
        MongoClient mongoClient = new MongoClient( "192.168.1.69" , 27017 );
        MongoDatabase sndoDatabase = mongoClient.getDatabase("sndo");
        MongoCollection<Document> planCollection = sndoDatabase.getCollection("ad.plan");
        MongoDatabase dmpDwDatabase = mongoClient.getDatabase("dmp_dw");

        Bson planFilters = and(eq("status", 1), gt("budget", 0.0f), eq("today_status", 1));

        planCollection.find(planFilters).forEach(new Block<Document>() {
            public void apply(Document document) {
                String json = gson.toJson(document);
                AdPlan adPlan = gson.fromJson(json, AdPlan.class);
                adPlan.setId(document.getString("_id"));
                AD_PLAN_MAP.put(adPlan.getId(), adPlan);
            }
        });

        MongoCollection<Document> groupCollection = sndoDatabase.getCollection("ad.group");
        Bson groupFilters = and(eq("plan_status", 1), eq("status", 1));
        groupCollection.find(groupFilters).forEach(new Block<Document>() {
            public void apply(Document document) {
                String json = gson.toJson(document);
                AdGroup adGroup = gson.fromJson(json, AdGroup.class);
                adGroup.setId(document.getString("_id"));
                AD_GROUP_MAP.put(adGroup.getId(), adGroup);
            }
        });

        MongoCollection<Document> unitCollection = sndoDatabase.getCollection("ad.unit");
        Bson unitFilters = and(eq("status", 1));
        unitCollection.find(unitFilters).forEach(new Block<Document>() {
            public void apply(Document document) {
                String json = gson.toJson(document);
                AdUnit adUnit = gson.fromJson(json, AdUnit.class);
                adUnit.setId(document.getString("_id"));
                AD_UNIT_MAP.put(adUnit.getId(), adUnit);
            }
        });

        MongoCollection<Document> cate4Collection = dmpDwDatabase.getCollection("LabelFourthCategory");
        Bson cate4Filters = and(eq("status", 1));
        cate4Collection.find(cate4Filters).forEach(new Block<Document>() {
            public void apply(Document document) {
                String cate4 = String.valueOf(document.getInteger("id"));
                String cate3 = String.valueOf(document.getInteger("parentid"));
                CATE_4_MAP.put("cate4_" + cate4, "cate3_" + cate3);
            }
        });

        mongoClient.close();
    }

    public static void main(String[] args) {
        LOG.info(gson.toJson(AD_PLAN_MAP));
        LOG.info(gson.toJson(AD_GROUP_MAP));
        LOG.info(gson.toJson(AD_UNIT_MAP));
        LOG.info(gson.toJson(CATE_4_MAP));
    }


}
