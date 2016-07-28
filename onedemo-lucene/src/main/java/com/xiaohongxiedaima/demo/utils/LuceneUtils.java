package com.xiaohongxiedaima.demo.utils;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.lionsoul.jcseg.analyzer.v5x.JcsegAnalyzer5X;
import org.lionsoul.jcseg.tokenizer.core.ADictionary;
import org.lionsoul.jcseg.tokenizer.core.DictionaryFactory;
import org.lionsoul.jcseg.tokenizer.core.JcsegTaskConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * @author xiaohongxiedaima
 * @version 16/7/28
 * @E-mail redfishinaction@yahoo.com
 */
public class LuceneUtils {

    public static final Logger logger = LoggerFactory.getLogger(LuceneUtils.class);

    private static Directory getDir(String indexPath) {
        Path path = FileSystems.getDefault().getPath(indexPath);
        Directory directory = null;
        try {
            directory = FSDirectory.open(path);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return directory;
    }

    public static IndexWriter getIndexWriter(String path, boolean recreate) {

        Directory directory = getDir(path);

        JcsegTaskConfig config = new JcsegTaskConfig();

        ADictionary dic = DictionaryFactory.createSingletonDictionary(config, false);

        try {
            dic.loadDirectory("/Users/redfish/code/IntelliJIDEAProjects/onedemo/onedemo-lucene/src/main/resources/lexicon");
            dic.loadClassPath();        //加载classpath路径下的全部词库文件的全部词条（默认路径/lexicon）
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        Analyzer analyzer = new JcsegAnalyzer5X(JcsegTaskConfig.COMPLEX_MODE,config);
        IndexWriterConfig conf = new IndexWriterConfig(analyzer);

        // 性能优化
        conf.setMaxBufferedDocs(1000);
        conf.setRAMBufferSizeMB(64);

        if (recreate == true) {
            conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        }

        IndexWriter indexWriter = null;
        try {
            indexWriter = new IndexWriter(directory, conf);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return indexWriter;
    }

    public static boolean close(IndexWriter indexWriter) {
        boolean result = true;
        try {
            indexWriter.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            result = false;
        }
        return result;
    }

}
