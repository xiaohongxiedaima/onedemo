package com.xiaohongxiedaima.demo.numeric;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.NumericUtils;
import org.lionsoul.jcseg.analyzer.v5x.JcsegAnalyzer5X;
import org.lionsoul.jcseg.tokenizer.core.JcsegTaskConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaohongxiedaima
 * @version 16-7-21
 * @E-mail redfishinaction@yahoo.com
 */
public class NumericDemo {


    private static final String IDX_DIR = "/home/hadoop/code/onedemo/onedemo-lucene/idx_dir/numeric_demo";

    private static final Logger logger = LoggerFactory.getLogger(NumericDemo.class);

    private static final FieldType sorted_double_store_field_type = new FieldType();

    static {
        sorted_double_store_field_type.setStored(true);
        sorted_double_store_field_type.setOmitNorms(true);
        sorted_double_store_field_type.setIndexOptions(IndexOptions.DOCS);
        sorted_double_store_field_type.setNumericType(FieldType.LegacyNumericType.DOUBLE);
        sorted_double_store_field_type.setDocValuesType(DocValuesType.SORTED_NUMERIC);
        sorted_double_store_field_type.freeze();
    }

    private static final List<Map<String, Object>> SRC_DATA = new ArrayList<Map<String, Object>>();

    private static final Long[] ids = new Long[]{1l, 2l, 3l, 4l, 5l, 6l};
    private static final Integer[] statuses = new Integer[]{1, 0, 2, 0, 0, 3};
    private static final Double[] prices = new Double[]{
            2.2, 3.1, 4.0, 0.8, 4.5, 2.5
    };
    private static final Float[] lengths = new Float[]{
            1.0f, 2.3f, 2.1f , 0.8f, 1.1f, 1.9f
    };
    private static final String[] names = new String[] {
            "我是小红 红色",
            "我是小流",
            "我是红色 蓝色",
            "我是小白",
            "我小黑 白色",
            "你小蓝色 红色"
    };
    static {

        for (int i = 0 ;i  < ids.length;i ++) {
            Map<String, Object> map = new HashMap<String, Object>();
            Long id = ids[i];
            Integer status = statuses[i];
            Double price = prices[i];
            Float length = lengths[i];
            String name = names[i];

            map.put("id", id);
            map.put("status", status);
            map.put("price", price);
            map.put("length", length);
            map.put("name", name);

            SRC_DATA.add(map);
        }

    }

    private Directory getDir() {
        Path path = FileSystems.getDefault().getPath(IDX_DIR);
        Directory directory = null;
        try {
            directory = FSDirectory.open(path);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return directory;
    }

    private IndexWriter getIndexWriter(boolean reIndex) {

        IndexWriter indexWriter = null;
        try {
            Directory directory = getDir();
            Analyzer analyzer = getAnalyzer();

            IndexWriterConfig conf = new IndexWriterConfig(analyzer);

            // 性能优化
            conf.setMaxBufferedDocs(1000);
            conf.setRAMBufferSizeMB(64);

            if (reIndex) {
                conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            }

            indexWriter = new IndexWriter(directory, conf);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return indexWriter;
    }

    private Analyzer getAnalyzer() {
        Analyzer analyzer = new JcsegAnalyzer5X(JcsegTaskConfig.COMPLEX_MODE);
//            Analyzer analyzer = new StandardAnalyzer();
        return analyzer;
    }
    public IndexReader getIndexReader() {
        Directory dir = getDir();
        IndexReader indexReader = null;
        try {
            indexReader = DirectoryReader.open(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return indexReader;

    }

    public void index(boolean reIndex) {

        IndexWriter indexWriter = getIndexWriter(reIndex);
        for (Map<String, Object> record : SRC_DATA) {
            Long id = (Long) record.get("id");
            Integer status = (Integer) record.get("status");
            Double price = (Double) record.get("price");
            Float length = (Float) record.get("length");
            String name = (String) record.get("name");

            StoredField idField = new StoredField("id", id);
//            IntField statusField = new IntField("status", status, Field.Store.YES);

            SortedNumericDocValuesField statusField = new SortedNumericDocValuesField("status", status);

//            DoubleField priceField = new DoubleField("price", price, Field.Store.YES);
//            DoubleField priceField = new DoubleField("price", price, sorted_double_store_field_type);
            SortedNumericDocValuesField priceField = new SortedNumericDocValuesField("price", NumericUtils.doubleToSortableLong(price));

            StoredField lengthField = new StoredField("length", length);
            TextField nameField = new TextField("name", name, Field.Store.YES);

            Document document = new Document();
            document.add(idField);
            document.add(statusField);
            document.add(priceField);
            document.add(lengthField);
            document.add(nameField);

            try {
                indexWriter.addDocument(document);
                indexWriter.commit();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }

        }

    }


    /**
     private static final Long[] ids = new Long[]{1l, 2l, 3l, 4l, 5l, 6l};
     private static final Integer[] statuses = new Integer[]{1, 0, 1, 0, 0, 1};
     private static final Double[] prices = new Double[]{
     2.2, 3.1, 1.0, 0.8, 4.5, 2.3
     };


     */
    public static void main(String[] args) throws IOException, ParseException {
        NumericDemo demo = new NumericDemo();
        demo.index(true);

        IndexReader indexReader = demo.getIndexReader();

        IndexSearcher searcher = new IndexSearcher(indexReader);

        Analyzer analyzer = demo.getAnalyzer();

        logger.info("int range");
        Query query = IntPoint.newRangeQuery("status", 0, 1);
        TopDocs topDocs = searcher.search(query, 10);
        print(topDocs, searcher);

        logger.info("long range");
        query = LongPoint.newRangeQuery("id", 4l , 6l);
        topDocs = searcher.search(query, 10);
        print(topDocs, searcher);

        logger.info("double range");
        query = DoublePoint.newRangeQuery("price", 1.0 , 2.3);
        topDocs = searcher.search(query, 10);
        print(topDocs, searcher);

        logger.info("float range");
        query = FloatPoint.newRangeQuery("length", 0.1f , 2.3f);
        topDocs = searcher.search(query, 10);
        print(topDocs, searcher);

        logger.info("string analyzer");
        String str = "红色";
        QueryParser queryParser = new QueryParser("name", analyzer);
        query = queryParser.parse(str);
        topDocs = searcher.search(query, 10);
        print(topDocs, searcher);

        logger.info("sort by double");
        str = "红色";
        queryParser = new QueryParser("name", analyzer);
        query = queryParser.parse(str);
        SortField sortField = new SortedNumericSortField("price", SortField.Type.DOUBLE, true);
        Sort sort = new Sort(sortField);
        topDocs = searcher.search(query, 10, sort);
        print(topDocs, searcher);

        logger.info("sort by int");
        str = "红色";
        queryParser = new QueryParser("name", analyzer);
        query = queryParser.parse(str);
        sortField = new SortedNumericSortField("status", SortField.Type.INT, true);
        sort = new Sort(sortField);
        topDocs = searcher.search(query, 10, sort);
        print(topDocs, searcher);

        indexReader.close();

    }


    public static void print(TopDocs topDocs, IndexSearcher searcher) throws IOException {
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            logger.info("doc: {}, id: {}, status: {} price: {}, length:{}, name: {}, score: {}", scoreDoc.doc, doc.get("id"), doc.get("status"), doc.get("price"),doc.get("length"), doc.get("name"), scoreDoc.score);
        }
    }
}
