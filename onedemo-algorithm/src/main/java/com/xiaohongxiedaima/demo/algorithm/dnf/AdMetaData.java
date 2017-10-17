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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

/**
 * Created by liusheng on 17-9-28.
 */
public class AdMetaData {

    private static final Logger LOG = LoggerFactory.getLogger(AdMetaData.class);

    public static Map<String, AdPlan> AD_PLAN_MAP = new HashMap<String, AdPlan>();
    public static Map<String, AdGroup> AD_GROUP_MAP = new HashMap<String, AdGroup>();
    public static Map<String, AdUnit> AD_UNIT_MAP = new HashMap<String, AdUnit>();

    public static Map<String, Set<String>> AD_PLAN_GROUP_MAPPING = new HashMap<String, Set<String>>();
    public static Map<String, Set<String>> AD_GROUP_UNIT_MAPPING = new HashMap<String, Set<String>>();

    // 人群 四级标签到三级标签的映射
    public static Map<String, String> CATE4_3_MAPPING = new HashMap<String, String>();

    // 内容 二级一级映射
    public static Map<String, String> TOPIC_2_1_MAPPING = new HashMap<String, String>();

    private static Gson gson = new Gson();

    private static MongoClient mongoClient = new MongoClient("localhost");

    public static void loadAd() {
        MongoDatabase sndoDatabase = mongoClient.getDatabase("sndo");

        MongoCollection<Document> planCollection = sndoDatabase.getCollection("ad.plan");
//        Bson planFilters = and(eq("status", 1), gt("budget", 0.0f), eq("today_status", 1));
        planCollection.find().forEach(new Block<Document>() {
            public void apply(Document document) {
                String json = gson.toJson(document);
                AdPlan adPlan = gson.fromJson(json, AdPlan.class);
                adPlan.setId(document.getString("_id"));
                AD_PLAN_MAP.put(adPlan.getId(), adPlan);
            }
        });

        MongoCollection<Document> groupCollection = sndoDatabase.getCollection("ad.group");
//        Bson groupFilters = and(eq("plan_status", 1), eq("status", 1));
        groupCollection.find().forEach(new Block<Document>() {
            public void apply(Document document) {
                String json = gson.toJson(document);
                AdGroup adGroup = gson.fromJson(json, AdGroup.class);
                adGroup.setId(document.getString("_id"));
                AD_GROUP_MAP.put(adGroup.getId(), adGroup);

                Set<String> groupIds = AD_PLAN_GROUP_MAPPING.get(adGroup.getPlanId());
                if (groupIds == null) {
                    groupIds = new HashSet<String>();
                }
                groupIds.add(adGroup.getId());
            }
        });

        MongoCollection<Document> unitCollection = sndoDatabase.getCollection("ad.unit");
//        Bson unitFilters = and(eq("status", 1));
        unitCollection.find().forEach(new Block<Document>() {
            public void apply(Document document) {
                String json = gson.toJson(document);
                AdUnit adUnit = gson.fromJson(json, AdUnit.class);
                adUnit.setId(document.getString("_id"));
                AD_UNIT_MAP.put(adUnit.getId(), adUnit);

                Set<String> unitIds = AD_GROUP_UNIT_MAPPING.get(adUnit.getGroupId());
                if (unitIds == null) {
                    unitIds = new HashSet<String>();
                }
                unitIds.add(adUnit.getId());
            }
        });
    }

    public static void loadUserLabelMapping() {
        MongoDatabase dmpDwDatabase = mongoClient.getDatabase("dmp_dw");
        MongoCollection<Document> cate4Collection = dmpDwDatabase.getCollection("LabelFourthCategory");
        Bson cate4Filters = and(eq("status", 1));
        cate4Collection.find(cate4Filters).forEach(new Block<Document>() {
            public void apply(Document document) {
                String cate4 = String.valueOf(document.getInteger("id"));
                String cate3 = String.valueOf(document.getInteger("parentid"));
                CATE4_3_MAPPING.put("cate4_" + cate4, "cate3_" + cate3);
            }
        });
    }

    public static void loadTopicMapping() {
        MongoDatabase dmpDwDatabase = mongoClient.getDatabase("dmp_dw");
        MongoCollection<Document> collection = dmpDwDatabase.getCollection("SecondaryTopicKeywords");
        collection.find().forEach(new Block<Document>() {
            public void apply(Document document) {
                String cate2 = String.valueOf(document.getInteger("id"));
                String cate1 = String.valueOf(document.getInteger("parentid"));
                TOPIC_2_1_MAPPING.put("cate2_" + cate2, "cate1_" + cate1);
            }
        });
    }

    public static void main(String[] args) {
        LOG.info(gson.toJson(AD_PLAN_MAP));
        LOG.info(gson.toJson(AD_GROUP_MAP));
        LOG.info(gson.toJson(AD_UNIT_MAP));
        LOG.info(gson.toJson(CATE4_3_MAPPING));
        LOG.info(gson.toJson(TOPIC_2_1_MAPPING));
    }


}
