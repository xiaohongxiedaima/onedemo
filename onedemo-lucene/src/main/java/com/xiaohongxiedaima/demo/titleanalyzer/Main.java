package com.xiaohongxiedaima.demo.titleanalyzer;

import com.xiaohongxiedaima.demo.utils.LuceneUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author xiaohongxiedaima
 * @version 16/7/28
 * @E-mail redfishinaction@yahoo.com
 */
public class Main {

    private static final String IDX_PATH = "/Users/redfish/code/IntelliJIDEAProjects/onedemo/onedemo-lucene/idx_dir/goods_title";

    private static final String GOODS_TITLE_DATA_PATH = "/Users/redfish/code/IntelliJIDEAProjects/onedemo/onedemo-lucene/data/js_sgoods_info/js_sgoods_info_1888_高跟凉鞋.txt";

    public static void main(String[] args) throws IOException {

        IndexWriter indexWriter = LuceneUtils.getIndexWriter(IDX_PATH, true);

        Scanner scanner = new Scanner(new File(GOODS_TITLE_DATA_PATH));

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] words = line.split("`");
            if (words.length > 0) {
                Integer goodsId = Integer.parseInt(words[0].trim());
                String title = words[1];

                Document doc = new Document();
                IntField goodsIdField = new IntField("goodsId", goodsId, Field.Store.YES);
                TextField titleField = new TextField("title", title, Field.Store.YES);

//                doc.add(goodsIdField);
                doc.add(titleField);

                indexWriter.addDocument(doc);
            }

        }

        LuceneUtils.close(indexWriter);

    }

}
