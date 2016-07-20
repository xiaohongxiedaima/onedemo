package com.xiaohongxiedaima.demo;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaohongxiedaima
 * @version 16-7-18
 * @E-mail redfishinaction@yahoo.com
 */
public class GoodsInfoIndex {

    private static final String IDX_DIR = "/home/hadoop/code/onedemo/onedemo-lucene/idx_dir/goods_info";

    private static final Logger logger = LoggerFactory.getLogger(GoodsInfoIndex.class);

    private static final List<Map<String, Object>> SRC_DATA = new ArrayList<Map<String, Object>>();

    private static final String GOODS_ID = "goodsId"; // 商品id
//    private static final String GOODS_CATE_1 = "goodsCate1"; // 商品一级类目ID
//    private static final String GOODS_CATE_2 = "goodsCate2"; // 商品二级类目ID
//    private static final String GOODS_CATE_3 = "goodsCate3"; // 商品三级类目ID

    private static final String GOODS_BASE_SCORE = "goodsBaseScore"; // 基础得分

    private static final String GOODS_ATTRS = "goodsAttrs";

    // 测试基础数据
    static {
        Integer[] ids = new Integer[]{1, 2, 3, 4, 5};
        Double[] baseScore = new Double[]{0.3, 0.5, 0.1, 0.9, 0.6};
        String[] attrs = new String[] {
                "红色 新品 新品 珂卡芙",
                "白色 复古 中空 水",
                "红色 水染皮 复古 纯色 水染皮",
                "黑色 水染皮 新品 低帮",
                "黑色 复古 胶粘鞋"
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
            Analyzer analyzer = new JcsegAnalyzer5X(JcsegTaskConfig.COMPLEX_MODE);;
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
                IntField goodsIdField = new IntField(GOODS_ID, goodsId, Field.Store.YES);
//                DoubleField goodsBaseScoreField = new DoubleField(GOODS_BASE_SCORE, goodsBaseScore, Field.Store.YES);
                TextField goodsAttrsField = new TextField(GOODS_ATTRS, goodsAttrs, Field.Store.YES);

                Document document = new Document();
                document.add(goodsIdField);
//                document.add(goodsBaseScoreField);
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

    public void search() {
        Directory dir = getDir();
        IndexReader reader = null;
        try {
            reader = DirectoryReader.open(dir);

            IndexSearcher searcher = new IndexSearcher(reader);
//            Term term = new Term(GOODS_ATTRS, "水染皮");
//            Query query = new TermQuery(term);

            Analyzer analyzer = new JcsegAnalyzer5X(JcsegTaskConfig.COMPLEX_MODE);;
            QueryParser queryParser = new QueryParser(GOODS_ATTRS, analyzer);
            Query query = queryParser.parse("水染皮");

//            SortField sortField = new SortField(GOODS_BASE_SCORE, SortField.Type.DOUBLE);
//            Sort sort = new Sort(sortField);

            TopDocs topDocs = searcher.search(query, 10);

            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                logger.info("document: {}, score: {}", searcher.doc(scoreDoc.doc), scoreDoc.score);
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

}
