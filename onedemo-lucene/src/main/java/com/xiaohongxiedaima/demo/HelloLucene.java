package com.xiaohongxiedaima.demo;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.FilterDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * @author xiaohongxiedaima
 * @version 16/7/17
 * @E-mail redfishinaction@yahoo.com
 */
public class HelloLucene {

    private static final Logger logger = LoggerFactory.getLogger(HelloLucene.class);

    private static final String SRC_DIR_PATH = "/Users/redfish/code/IntelliJIDEAProjects/onedemo/onedemo-lucene/src/main/java/com/xiaohongxiedaima/demo";

    private static final String INDEX_DIR = "/Users/redfish/code/IntelliJIDEAProjects/onedemo/onedemo-lucene/src/main/resources";

    public void index() {

        // 1. 创建 Directory
        Path path = FileSystems.getDefault().getPath(INDEX_DIR);
        Directory directory = null;
        try {
            directory = FSDirectory.open(path);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            System.exit(-1);
        }

        // 2. 创建IndexWriter
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig conf = new IndexWriterConfig(analyzer);
        IndexWriter writer = null;
        try {
            writer = new IndexWriter(directory, conf);

            // 3. 创建Document
            Document doc = null;

            // 4. 给 Document 添加Field
            File dir = new File(SRC_DIR_PATH);
            for (File file : dir.listFiles()) {
                doc = new Document();
                StringField name = new StringField("name", file.getName(), Store.YES);
                TextField content = new TextField("content", new FileReader(file));

                doc.add(name);
                doc.add(content);
                // 5. 通过 IndexWriter 添加文档到索引中
                writer.addDocument(doc);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }

    }

    public void search() {

        IndexReader reader = null;

        try {
            // 1. 获取 Directory
            Path path = FileSystems.getDefault().getPath(INDEX_DIR);
            Directory directory = null;
            directory = FSDirectory.open(path);

            // 2. 创建 IndexReader
            reader = DirectoryReader.open(directory);

            // 3. 根据 IndexReader 创建 IndexSearcher
            IndexSearcher searcher = new IndexSearcher(reader);

            // 4. 创建搜索的 Query
            Analyzer analyzer = new StandardAnalyzer();
            QueryParser parser = new QueryParser("content", analyzer);
            Query query = parser.parse("final");

            // 5. 根据 Searcher 搜索并返回TopDocs
            TopDocs topDocs = searcher.search(query, 10);

            // 6. 根据 TopDocs 获取 ScoreDoc
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;

            // 7. 根据 Searcher 和 ScoreDoc 获取具体的 Document
            for (ScoreDoc scoreDoc: scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                String fileName = doc.get("name");
                logger.info("filename: {}", fileName);
            }

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            System.exit(-1);
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
    public static void main(String[] args) {
        HelloLucene helloLucene = new HelloLucene();
//        helloLucene.index();
        helloLucene.search();
    }
}
