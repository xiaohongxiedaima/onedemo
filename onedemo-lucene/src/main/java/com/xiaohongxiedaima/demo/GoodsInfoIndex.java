package com.xiaohongxiedaima.demo;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.lionsoul.jcseg.analyzer.v5x.JcsegAnalyzer5X;
import org.lionsoul.jcseg.tokenizer.core.JcsegTaskConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xiaohongxiedaima
 * @version 16-7-18
 * @E-mail redfishinaction@yahoo.com
 */
public class GoodsInfoIndex {

//    private static final String IDX_DIR = "/home/hadoop/code/onedemo/onedemo-lucene/idx_dir/goods_info";

    private static final String IDX_DIR = "/home/hadoop/下载/userprofile-se/history/20160726160019/all";

    private static final Logger logger = LoggerFactory.getLogger(GoodsInfoIndex.class);

    private static final FieldType DOUBLE_FIELD_TYPE_STORED_SORTED = new FieldType();
    static {
        DOUBLE_FIELD_TYPE_STORED_SORTED.setTokenized(true);
        DOUBLE_FIELD_TYPE_STORED_SORTED.setOmitNorms(true);
        DOUBLE_FIELD_TYPE_STORED_SORTED.setIndexOptions(IndexOptions.DOCS);
        DOUBLE_FIELD_TYPE_STORED_SORTED.setNumericType(FieldType.LegacyNumericType.DOUBLE);
        DOUBLE_FIELD_TYPE_STORED_SORTED.setStored(true);
        DOUBLE_FIELD_TYPE_STORED_SORTED.setDocValuesType(DocValuesType.NUMERIC);
        DOUBLE_FIELD_TYPE_STORED_SORTED.freeze();
    }



    private static final List<Map<String, Object>> SRC_DATA = new ArrayList<Map<String, Object>>();

    private static final String GOODS_ID = "goodsId"; // 商品id
//    private static final String GOODS_CATE_1 = "goodsCate1"; // 商品一级类目ID
//    private static final String GOODS_CATE_2 = "goodsCate2"; // 商品二级类目ID
//    private static final String GOODS_CATE_3 = "goodsCate3"; // 商品三级类目ID

    private static final String GOODS_BASE_SCORE = "goodsBaseScore"; // 基础得分

    private static final String GOODS_ATTRS = "goodsAttrs";

    // 测试基础数据
    static {
        Integer[] ids = new Integer[]{1,2,3,4,5};
        Double[] baseScore = new Double[]{0.2d, 0.2d, 0.3d, 0.3d, 0.1d};
        String[] attrs = new String[] {
                "红色 新品 新品 珂卡芙",
                "白色 复古 中空 水",
                "红色 红色 红色 水染皮 复古 纯色 水染皮",
                "黑色 水染皮 新品 低帮",
                "红色 黑色 复古 胶粘鞋"
        };


        for (int i = 0 ; i < ids.length ;i ++) {
            Map<String, Object> record = new HashMap<String, Object>();
            record.put(GOODS_ID, ids[i]);
            record.put(GOODS_BASE_SCORE, baseScore[i]);
            record.put(GOODS_ATTRS, attrs[i]);
            SRC_DATA.add(record);
        }

    }

    public Directory getDir() {
        Path path = FileSystems.getDefault().getPath(IDX_DIR);
        Directory directory = null;
        try {
             directory = FSDirectory.open(path);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return directory;
    }

    public void index() {
        IndexWriter indexWriter = null;

        try {
            Directory directory = getDir();
            Analyzer analyzer = new JcsegAnalyzer5X(JcsegTaskConfig.COMPLEX_MODE);
//            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig conf = new IndexWriterConfig(analyzer);

            // 性能优化
            conf.setMaxBufferedDocs(1000);
            conf.setRAMBufferSizeMB(64);

            indexWriter = new IndexWriter(directory, conf);

            for (Map<String, Object> record : SRC_DATA) {
                Integer goodsId = (Integer) record.get(GOODS_ID);
                Double goodsBaseScore = (Double) record.get(GOODS_BASE_SCORE);
                String goodsAttrs = (String) record.get(GOODS_ATTRS);

                // 添加索引
                StoredField goodsIdField = new StoredField(GOODS_ID, goodsId);

//                SortedNumericDocValuesField goodsBaseScoreField = new SortedNumericDocValuesField(GOODS_BASE_SCORE, NumericUtils.doubleToSortableLong(goodsBaseScore));

//                DoubleDocValuesField goodsBaseScoreField = new DoubleDocValuesField(GOODS_BASE_SCORE, goodsBaseScore.doubleValue());
//                DoubleField goodsBaseScoreField = new DoubleField(GOODS_BASE_SCORE, goodsBaseScore.doubleValue(), DOUBLE_FIELD_TYPE_STORED_SORTED);

                SortedDocValuesField goodsBaseScoreField = new SortedDocValuesField(GOODS_BASE_SCORE, new BytesRef(String.valueOf(goodsBaseScore).getBytes()));

//                NumericDocValuesField goodsBaseScoreField = new NumericDocValuesField(GOODS_BASE_SCORE, Double.doubleToRawLongBits(goodsBaseScore));
                TextField goodsAttrsField = new TextField(GOODS_ATTRS, goodsAttrs, Field.Store.YES);

                Document document = new Document();
                document.add(goodsIdField);
                document.add(goodsBaseScoreField);
                document.add(goodsAttrsField);

                indexWriter.addDocument(document);
            }
            indexWriter.commit();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (indexWriter != null) {
                try {
                    indexWriter.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }

    }

    public void searchByNumeric() {
        Directory dir = getDir();
        IndexReader reader = null;
        try {
            reader = DirectoryReader.open(dir);

            IndexSearcher searcher = new IndexSearcher(reader);

            Analyzer analyzer = new JcsegAnalyzer5X(JcsegTaskConfig.COMPLEX_MODE);

//            Query query = NumericRangeQuery.newDoubleRange(GOODS_BASE_SCORE, 0d, 1d, true, true);


            Query query = IntPoint.newRangeQuery(GOODS_BASE_SCORE, 1, 10);

            TopDocs topDocs = searcher.search(query, 10);

            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                logger.info("doc: {}, goodsBaseScore: {} goodsAttrs: {}, score: {}", scoreDoc.doc, doc.get(GOODS_BASE_SCORE), doc.get(GOODS_ATTRS), scoreDoc.score);
            }


        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    public void search(String str) {
        Directory dir = getDir();
        IndexReader reader = null;
        try {
            reader = DirectoryReader.open(dir);

            IndexSearcher searcher = new IndexSearcher(reader);

            Analyzer analyzer = new JcsegAnalyzer5X(JcsegTaskConfig.COMPLEX_MODE);
//            Analyzer analyzer = new StandardAnalyzer();
            QueryParser queryParser = new QueryParser(GOODS_ATTRS, analyzer);
            logger.info(str);
            Query query = queryParser.parse(str);

//            SortField sortField1 = SortField.FIELD_SCORE;
            SortField sortField2 = new SortField(GOODS_BASE_SCORE, SortField.Type.STRING, true);
            SortField[] sortFeilds = new SortField[]{
                    sortField2
            };
            Sort sort = new Sort(sortFeilds);

            TopDocs topDocs = searcher.search(query, 10, sort, true, true);

            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                logger.info("doc: {}, goodsBaseScore: {} goodsAttrs: {}, score: {}", scoreDoc.doc, doc.get(GOODS_BASE_SCORE), doc.get(GOODS_ATTRS), scoreDoc.score);
            }


        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    public void searchStoreInHdfs(String str) {
        Directory dir = getDir();
        IndexReader reader = null;
        try {
            reader = INDEX_READER;

            IndexSearcher searcher = new IndexSearcher(reader);

            Analyzer analyzer = new JcsegAnalyzer5X(JcsegTaskConfig.COMPLEX_MODE);
            QueryParser queryParser = new QueryParser(GOODS_ATTRS, analyzer);
            Query query = queryParser.parse(str);

            SortedNumericSortField cate3Score = new SortedNumericSortField("cate3_score", SortField.Type.DOUBLE);
            Sort sort = new Sort(cate3Score);
//            TopDocs topDocs = searcher.search(query, 10);

            TopDocs topDocs = searcher.search(query, 10, sort);

            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                logger.info("doc:{}, doc:{}", scoreDoc.doc, doc.toString());
            }


        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static IndexReader INDEX_READER = null;
    static {
        Path path = FileSystems.getDefault().getPath(IDX_DIR);
        Directory directory = null;
        try {
            directory = FSDirectory.open(path);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        Directory dir = directory;
        try {
            INDEX_READER = DirectoryReader.open(dir);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }



    public static void main(String[] args) {
//        GoodsInfoIndex idx = new GoodsInfoIndex();
//        idx.searchStoreInHdfs("attr: 短袖");

        ExecutorService es = Executors.newFixedThreadPool(1);

        for (int i = 0 ;i < 1000; i ++) {
            es.execute(new Runnable() {
                public void run() {

                    String[] ss = new String[] {
                            "中长裙  圆领",
                            "短裙 高腰 圆领 百褶裙",
                            "中长裙 高腰"
                    };

                    Random random = new Random();
                    Integer idx = random.nextInt(3);

                    String s = ss[idx];

                    GoodsInfoIndex goodsInfoIndex = new GoodsInfoIndex();

                    long start = System.currentTimeMillis();

                    goodsInfoIndex.searchStoreInHdfs(s);

                    long end = System.currentTimeMillis();

                    logger.info("*****************************************s: {}, cost: {}", s, end-start);
                }
            });

        }
        es.shutdown();
    }

}
