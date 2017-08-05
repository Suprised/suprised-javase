package com.suprised.lucene.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.suprised.lucene.bean.TopicBaseBean;

public class LuceneCreateIndexer {
    
    public static IndexWriter getIndexWriter(File indexDir, boolean create)  throws Exception {
        Directory dir = FSDirectory.open(indexDir);
        Analyzer analyzer = new StandardAnalyzer(Costs.LUCENE_VERSION); // 标准分词
        IndexWriterConfig iwc = new IndexWriterConfig(Costs.LUCENE_VERSION, analyzer);
        // 创建所以模式或者OpenMode.CREATE_OR_APPEND
        iwc.setOpenMode(create ? OpenMode.CREATE : OpenMode.CREATE_OR_APPEND);
//        iwc.setOpenMode(OpenMode.CREATE); 
        IndexWriter writer = new IndexWriter(dir, iwc);
        return writer;
    }
    
    public static void createIndex(List<TopicBaseBean> objs, String[] indexFields, File indexDir, boolean create) throws Exception {
        Date start = new Date();
        IndexWriter writer = getIndexWriter(indexDir, create);
        int index = 0;
        for (TopicBaseBean obj : objs) {
            Document document = new Document();
            // 保存并索引
            Field field = new StringField("key", obj.getKey(), Store.YES);
            document.add(field);
            // 索引不保存(支持分词)
            Field fieldContent = new TextField("content", obj.getContent(), Store.NO);
            document.add(fieldContent);
            // 保存，索引
            Field fieldDate = new StoredField("lastReplyDate", obj.getLastReplyDate().toString());
            document.add(fieldDate);
            Field fieldTitle = new StringField("parentTitle", obj.getParentTitle(), Store.YES);
            document.add(fieldTitle);
            Field fieldCreator = new StoredField("creator", obj.getCreator());
            document.add(fieldCreator);
            
            if (index % 100 == 0) {
                fieldContent.setBoost(1.5f); // 设置优先级别
            }
            index++;
            
            if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
                System.out.println("adding " + obj); // 添加索引
                writer.addDocument(document);
            } else {
                System.out.println("updating " + obj);
//                writer.updateDocument(new Term("content"), document); // 更新文件路径索引
//                writer.updateDocument(new Term("creator"), document); // 更新文件路径索引
            }
        }
        writer.close();
        Date end = new Date();
        System.out.println(end.getTime() - start.getTime() + " total milliseconds");
    }
    
    /**
     * 将一个目录创建索引
     * @param indexDir 索引的目录
     * @param dataDir 数据的目录
     * @throws Exception
     */
    public static void createIndex(File indexDir, File dataDir, boolean create) throws Exception {
        Date start = new Date();
        IndexWriter writer = getIndexWriter(indexDir, create);
        indexDocs(writer, dataDir);
        writer.close();
        Date end = new Date();
        System.out.println(end.getTime() - start.getTime() + " total milliseconds");
    }

    /**
     * 创建索引
     * 
     * @param writer
     * @param file
     * @throws IOException
     */
    private static void indexDocs(IndexWriter writer, File file) throws IOException {
        if (!file.canRead()) return;
        if (file.isDirectory()) { // 目录递归
            String[] files = file.list();
            // an IO error could occur
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    indexDocs(writer, new File(file, files[i]));
                }
            }
        } else { //创建文件索引
            FileInputStream fis;
            try {
                fis = new FileInputStream(file);
            } catch (FileNotFoundException fnfe) {
                return;
            }

            try {
                Document doc = new Document(); // 每个文件，作为一个Document
                //每个文档有多个Field
                
                // Add the path of the file as a field named "path". Use a
                // field that is indexed (i.e. searchable), but don't tokenize
                // the field into separate words and don't index term frequency
                // or positional information:
                Field pathField = new StringField(Costs.PATH, file.getPath(), Field.Store.YES);
                doc.add(pathField); // 将路径作为一个Filed 
                
                Field titleField = new StringField(Costs.TITLE,file.getName(), Field.Store.YES);
                doc.add(titleField);// 将标题作为一个Field
                // Add the last modified date of the file a field named "modified".
                // Use a LongField that is indexed (i.e. efficiently filterable with
                // NumericRangeFilter). This indexes to milli-second resolution, which
                // is often too fine. You could instead create a number based on
                // year/month/day/hour/minutes/seconds, down the resolution you require.
                // For example the long value 2011021714 would mean
                // February 17, 2011, 2-3 PM.
                doc.add(new LongField(Costs.MODIFYED, file.lastModified(), Field.Store.NO)); //最后修改时间

                // Add the contents of the file to a field named "contents". Specify a Reader,
                // so that the text of the file is tokenized and indexed, but not stored.
                // Note that FileReader expects the file to be in UTF-8 encoding.
                // If that's not the case searching for special characters will fail.
                // 内容
                doc.add(new TextField(Costs.CONTENTS, new BufferedReader(new InputStreamReader(fis, "UTF-8"))));

                if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
                    // New index, so we just add the document (no old document can be there):
                    System.out.println("adding " + file); // 添加索引
                    writer.addDocument(doc);
                } else {
                    // Existing index (an old copy of this document may have been indexed) so
                    // we use updateDocument instead to replace the old one matching the exact
                    // path, if present:
                    System.out.println("updating " + file);
                    writer.updateDocument(new Term(Costs.PATH, file.getPath()), doc); // 更新文件路径索引
                }
            } finally {
                fis.close();
            }
        }
    }
    
}
