//import com.mongodb.Block;
//import com.mongodb.MongoClient;
//import com.mongodb.MongoClientURI;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoCursor;
//import com.mongodb.client.MongoDatabase;
//import com.mongodb.client.result.DeleteResult;
//import com.mongodb.client.result.UpdateResult;
//import org.bson.Document;
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import static com.mongodb.client.model.Filters.*;
//import static com.mongodb.client.model.Updates.inc;
//
///**
// * Created by liusheng on 17-7-4.
// */
//public class MongoTest {
//
//    private static final Logger LOG = LoggerFactory.getLogger(MongoTest.class);
//
//    MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");
//    // MongoClient 自带连接池
//    MongoClient mongoClient = null;
//    MongoCollection<Document> myTestCollection = null;
//    @Before
//    public void before() {
//        mongoClient = new MongoClient(connectionString);
//        MongoDatabase testDb = mongoClient.getDatabase("test");
//        myTestCollection = testDb.getCollection("myTest");
//    }
//
//    /**
//     * insertOne
//     * insertMany
//     */
//    @Test
//    public void testInsert() {
//
//
//        Document doc = new Document("name", "MongoDB")
//                .append("type", "database")
//                .append("count", 1)
//                .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
//                .append("info", new Document("x", 203).append("y", 102));
//
//        myTestCollection.insertOne(doc);
//
//        List<Document> documents = new ArrayList<Document>();
//        for (int i = 0; i < 100; i++) {
//            documents.add(new Document("i", i));
//        }
//
//        myTestCollection.insertMany(documents);
//
//        Assert.assertEquals(101, myTestCollection.count());
//
//    }
//
//    /**
//     * find().first()
//     * find().iterator()
//     */
//    @Test
//    public void testFind() {
//        // find
//        Document doc = myTestCollection.find().first();
//        LOG.info(doc.toJson());
//
//        // iterator
//        MongoCursor<Document> cursor = myTestCollection.find().iterator();
//        try {
//            while (cursor.hasNext()) {
//                System.out.println(cursor.next().toJson());
//            }
//        } finally {
//            cursor.close();
//        }
//    }
//
//    /**
//     * com.mongodb.client.model.Filters.*
//     */
//    @Test
//    public void testFilter() {
//        // filter
//        Block<Document> printBlock = new Block<Document>() {
//            @Override
//            public void apply(final Document document) {
//                System.out.println(document.toJson());
//            }
//        };
//
//        myTestCollection.find(and(gt("i", 50), lte("i", 100))).forEach(printBlock);
//    }
//
//    /**
//     * updateOne
//     * updateMany
//     */
//    @Test
//    public void testUpdate() {
//        myTestCollection.updateOne(eq("i", 10), new Document("$set", new Document("i", 110)));
//
//        UpdateResult updateResult = myTestCollection.updateMany(lt("i", 100), inc("i", 100));
//        System.out.println(updateResult.getModifiedCount());
//    }
//
//    @Test
//    public void testDelete() {
//        myTestCollection.deleteOne(eq("i", 110));
//
//        DeleteResult deleteResult = myTestCollection.deleteMany(gte("i", 100));
//        System.out.println(deleteResult.getDeletedCount());
//    }
//
//    @Test
//    public void createIndex() {
//        myTestCollection.createIndex(new Document("i", 1));
//    }
//
//    @After
//    public void after() {
//        mongoClient.close();
//    }
//
//
//}
