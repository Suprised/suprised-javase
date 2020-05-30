package com.suprised.lucene.demo;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.google.common.collect.Lists;
import com.suprised.lucene.index.Costs;

/**
 * 全文检索帮助类
 * 
 */
public class LuceneInstance {

    public static final Version LUCENE_VERSION = Costs.LUCENE_VERSION;
    private static final String PATTERN = Costs.DATE_FULL_PATTERN;
    
    private static IndexReader reader; // Reader 是单例的
    
    protected File indexDirs = new File("E:\\lucene\\indexs\\webTestIndexs"); //默认索引的位置
    
    private LuceneInstance(){
    }
    
    public static final LuceneInstance getInstance() {
        return new LuceneInstance() ;
    }
    
    /**
     * DirectoryReader在程序中只有一个实例
     */
    public IndexSearcher getIndexSearcher(Directory directory) throws IOException{
        if (reader == null) {
            reader = DirectoryReader.open(directory);
        } else {
        	// 如果IndexReader里面的所有进行了增删改，则返回一个新的Reader，并把修改部分同步到索引中
            IndexReader newReader = DirectoryReader.openIfChanged((DirectoryReader)reader);
            if (newReader != null) {
                reader.close();
                reader = newReader;
            }
        }
        IndexSearcher searcher = new IndexSearcher(reader);
        return searcher;
    }
    
    private IndexWriter getIndexWriter(File indexDir) throws IOException {
        Directory dir = FSDirectory.open(indexDir);
        Analyzer analyzer = new StandardAnalyzer(LUCENE_VERSION); // 标准分词
        IndexWriterConfig iwc = new IndexWriterConfig(LUCENE_VERSION, analyzer);
        // iwc.setOpenMode(OpenMode.CREATE); // 创建一个新的索引，覆盖原有的 清空之前的索引，然后重新创建新的索引
        // iwc.setOpenMode(OpenMode.CREATE_OR_APPEND); // 如果存在索引，则打开，不存在则创建一个新的 在之前的索引基础上增加索引， 数据会重复
        // iwc.setOpenMode(OpenMode.APPEND); //打开一个存在的索引 在之前的索引基础上增加索引， 数据会重复
        
        IndexWriter writer = new IndexWriter(dir, iwc);
        return writer;
    }
    
    /**
     * 根据所以bean创建索引
     * @param objs
     * @param isCreate 判断是否创建 true：create false：update
     * @throws IOException
     */
    public void createOrUpdateIndexs(List<LuceneIndexBean> objs , boolean isCreate) throws IOException {
        IndexWriter writer = getIndexWriter(indexDirs);
        try {
            for (LuceneIndexBean obj : objs) {
                createIndex(obj, isCreate, writer);
            }
        } finally {
        	closeWriter(writer);
        }
    }
    
    public void createOrUpdateIndex(LuceneIndexBean obj , boolean isCreate) throws IOException {
        IndexWriter writer = getIndexWriter(indexDirs);
        try {
            createIndex(obj, isCreate, writer);
        } finally {
        	closeWriter(writer);
        }
    }
    
    /**
     * 关闭IndexWriter
     */
    private void closeWriter(IndexWriter writer) {
    	if (writer != null) {
        	try {
        		writer.close();
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        }
    }
    
    /**
     * 创建或者更新索引
     * 
     * @param isCreate true:创建 false:更新
     */
    private void createIndex(LuceneIndexBean obj , boolean isCreate, IndexWriter writer) throws IOException {
        Date start = new Date();
        Document document = new Document();
        // 保存并索引 是一个不需要分词，而直接用于索引的字符串 
        Field fieldKey = new StringField("key", obj.getKey(), Store.YES);
        document.add(fieldKey);
        // 索引并保存(支持分词) 是一大块需要经过分词的文本  
        Field fieldContent = new TextField("content", obj.getContent(), Store.YES);
        document.add(fieldContent);
        // 保存，索引
        Field fieldIndexDate = new StoredField("indexDate", new SimpleDateFormat(PATTERN).format(obj.getDate()));
        document.add(fieldIndexDate);
        // 
        Field dateField = new LongField("longDate", new Date().getTime(), Store.YES);
        document.add(dateField);
        
        if (obj.getDate() != null) {
            Field fieldDate = new StoredField("date", new SimpleDateFormat(PATTERN).format(obj.getDate()));
            document.add(fieldDate);
        }
        
        if (obj.getTitle() != null){
            Field fieldTitle = new StoredField("title", obj.getTitle());
            document.add(fieldTitle);
        }
        
        Field fieldUrl = new StoredField("url", obj.getUrl());
        document.add(fieldUrl);

        if (isCreate) { // 创建索引
            System.out.println("adding " + obj); // 添加索引
            writer.addDocument(document);
        } else {
            System.out.println("updating " + obj);
            // 根据key修改
            writer.updateDocument(new Term("key", obj.getKey()), document);
        }
        System.out.println(new Date().getTime() - start.getTime() + " total milliseconds");
    }
    
    public List<LuceneIndexBean> query(String queryString, int currPage, int pageSize) throws IOException {
        Directory directory = FSDirectory.open(indexDirs);
        IndexSearcher searcher = getIndexSearcher(directory);
        List<LuceneIndexBean> results = doSearch(searcher, "content", queryString, currPage, pageSize);
//        return wrapIndexBeans(results);
        return results;
    }
    
    public Pagination queryPage(String queryString, int currPage, int pageSize) throws IOException {
        Directory directory = FSDirectory.open(indexDirs);
        IndexSearcher searcher = getIndexSearcher(directory);
        // 进行搜索，默认从content域中搜索
        Pagination page = doPageSearch(searcher, "content", queryString, currPage, pageSize);
        return page;
    }
    
    private LuceneIndexBean wrapIndexBean(Document doc) {
        LuceneIndexBean bean = new LuceneIndexBean();
        bean.setContent(doc.get("content"));
        bean.setUrl(doc.get("url"));
        String date = doc.get("date");
        if (date != null) {
            try {
                bean.setDate(new SimpleDateFormat(PATTERN).parse(date));
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
        String title = doc.get("title");
        if (title != null && title.length() > 100) {
            title = title.substring(0, 99) + "......";
        }
        bean.setTitle(title);
        bean.setKey(doc.get("key"));
        return bean;
    }
    
//    private List<LuceneIndexBean> wrapIndexBeans(List<Document> results) {
//        List<LuceneIndexBean> indexsBeans = new ArrayList<LuceneIndexBean>();
//        LuceneIndexBean bean = null;
//        for (Document doc : results) {
//            bean = new LuceneIndexBean();
//            bean.setContent(doc.get("content"));
//            bean.setUrl(doc.get("url"));
//            String date = doc.get("date");
//            if (date != null) {
//                try {
//                    bean.setDate(new SimpleDateFormat(PATTERN).parse(date));
//                } catch (java.text.ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//            bean.setTitle(doc.get("title"));
//            bean.setKey(doc.get("key"));
//            indexsBeans.add(bean);
//        }
//        return indexsBeans;
//    }

    private Pagination doPageSearch(IndexSearcher searcher, String field, String queryString, int currPage, int pageSize) throws IOException {
        QueryParser parser = new QueryParser(LUCENE_VERSION, field, getAnalyzer());
        Query query = null;
        try {
            query = parser.parse(queryString);
            System.out.println("分词后：" + query.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        TopDocs docs = searcher.search(query, currPage * pageSize);
        System.out.println("总条数：" + docs.totalHits);
        ScoreDoc[] hits = docs.scoreDocs;
        List<LuceneIndexBean> results = Lists.newArrayList();
        int start = (currPage - 1) * pageSize;
        Document doc ;
        LuceneIndexBean bean;
        for (int i = start; i < hits.length; i++) {
            doc = searcher.doc(hits[i].doc);
            String contents = doc.get("content");
            bean = wrapIndexBean(doc);
            // 自定义标注高亮文本的标签
            // SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<span class=\"hightlight\">", "</span>");
            SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<b>", "</b>");
            Highlighter highlighter = new Highlighter(formatter, new QueryScorer(query));
            highlighter.setTextFragmenter(new SimpleFragmenter(300)); // 减少高亮的默认大小，默认是100
            TokenStream tokenStream = getAnalyzer().tokenStream("content", contents);
            String fragment;
            try {
                fragment = highlighter.getBestFragment(tokenStream, contents);
                if (StringUtils.isNotBlank(fragment) && fragment.length() > 200) {
                    fragment = fragment.substring(0, 199) + "......";
                }
                bean.setContent(fragment);
//                System.out.println(fragment);
            } catch (InvalidTokenOffsetsException e) {
                e.printStackTrace();
            }
            results.add(bean);
        }
        Pagination page = new Pagination(docs.totalHits, currPage, pageSize);
        page.setRow(results);
        return page;
    }
    
    /**
     * 分页查找
     * 
     * @param searcher
     * @param field
     * @param queryString
     * @return
     */
    private List<LuceneIndexBean> doSearch(IndexSearcher searcher, String field, String queryString, int currPage, int pageSize) throws IOException {
        QueryParser parser = new QueryParser(LUCENE_VERSION, field, getAnalyzer());
        Query query = null;
        try {
            query = parser.parse(queryString);
            System.out.println("分词后：" + query.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        TopDocs docs = searcher.search(query, currPage * pageSize);
        System.out.println("总条数：" + docs.totalHits);
        ScoreDoc[] hits = docs.scoreDocs;
        List<LuceneIndexBean> results = Lists.newArrayList();
        int start = (currPage - 1) * pageSize;
        Document doc ;
        LuceneIndexBean bean;
        for (int i = start; i < hits.length; i++) {
            doc = searcher.doc(hits[i].doc);
            String contents = doc.get("content");
            bean = wrapIndexBean(doc);
            // 自定义标注高亮文本的标签
            // SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<span class=\"hightlight\">", "</span>");
            SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<b>", "</b>");
            Highlighter highlighter = new Highlighter(formatter, new QueryScorer(query));
            highlighter.setTextFragmenter(new SimpleFragmenter(300)); // 减少高亮的默认大小，默认是100
            TokenStream tokenStream = getAnalyzer().tokenStream("content", contents);
            String fragment = "";
            try {
                fragment = highlighter.getBestFragment(tokenStream, contents);
                if (fragment.length() > 300) {
                    fragment = fragment.substring(0, 299) + "......";
                }
                bean.setContent(fragment);
                System.out.println(fragment);
            } catch (InvalidTokenOffsetsException e) {
                e.printStackTrace();
            }
            results.add(bean);
        }
        return results;
    }
    
    /**
     * 获得分词器
     */
    private Analyzer getAnalyzer() {
        return new StandardAnalyzer(LUCENE_VERSION);
//        return new CJKAnalyzer(LUCENE_VERSION); // 支持中日韩分词
    }
    
    public static void main(String[] args) throws IOException {
//        LuceneIndexBean indexBean = new LuceneIndexBean();
//        indexBean.setUrl("http://www.baidu.com?key=123456789");
//        indexBean.setContent("这条微博是要被Lucene索引的数据。");
//        indexBean.setDate(new Date());
//        indexBean.setKey("123456789");
        LuceneInstance helper = LuceneInstance.getInstance();
//        try {
//            helper.createIndex(indexBean, false);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        
        List<LuceneIndexBean> beans = helper.query("Lucene", 1, 10);
        for (LuceneIndexBean bean : beans) 
            System.out.println(bean);
    }
}
