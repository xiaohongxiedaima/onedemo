package com.xiaohongxiedaima.demo;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
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
                "红色 新品",
                "白色 复古",
                "红色 复古",
                "黑色 新品",
                "黑色 复古"
        };

        for (int i = 0 ; i < ids.length ;i ++) {
            Map<String, Object> record = new HashMap<String, Object>();
            record.put(GOODS_ID, ids[i]);
            record.put(GOODS_BASE_SCORE, baseScore[i]);
            record.put(GOODS_ATTRS, attrs[i]);
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
            Analyzer analyzer = new StandardAnalyzer();
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
                DoubleField goodsBaseScoreField = new DoubleField(GOODS_BASE_SCORE, goodsBaseScore, Field.Store.YES);
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

}
