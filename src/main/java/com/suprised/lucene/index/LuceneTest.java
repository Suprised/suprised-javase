package com.suprised.lucene.index;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.suprised.lucene.bean.TopicBaseBean;

public class LuceneTest {

    private static File fileIndexDir = new File("F:\\indexs\\fileIndexs"); // 文件索引的位置
    private static File dataIndexDir = new File("F:\\indexs\\dbIndexs"); //数据库索引的位置
    private static File dataDir = new File("F:\\datas");
    
    public static void main(String[] args) throws Exception {
//        LuceneCreateIndexer.createIndex(fileIndexDir, dataDir, true);
//        createCommiIndexs(true);
        // 创建索引
        
        LuceneSearchIndexer.searchByFields(dataIndexDir, "abc论坛信息", "content");
        // File indexDir = new File("F:\\indexs");
         
        // File queryFile = new File("F:\\query.txt");
//       
         //LuceneSearchIndexer.search(fileIndexDir, "教");
//        LuceneSearchIndexer.search(indexDir, queryFile);
        
//        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(queryFile), "GBK"));
//        String line ;
//        while((line = in.readLine()) != null) {
//            System.out.println(line);1
//        }
//        in.close();
    }
    
    /**
     * 在线答疑、论坛、常见问题创建索引
     */
    public static void createCommiIndexs(boolean create) {
//        // 对所有的静态文件创建索引
//        File dataDir = new File("E:\\workspace\\dasSuite_manage\\webapp\\files-management\\static-file-temp");
        try {
            LuceneCreateIndexer.createIndex(getBeans(),getIndexFields(),dataIndexDir , create);
            System.out.println("创建所以完成。");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("创建索引失败：" + e.getMessage());
        }
    }
    
    public static String[] getIndexFields() {
        return new String[]{"key","creator", "parentTitle", "content", "lastReplyDate"};
    }
    
    public static List<TopicBaseBean> getBeans() {
        List<TopicBaseBean> beans = new ArrayList<TopicBaseBean>();
        for (int i=0; i<100000; i++) {
            beans.add(new TopicBaseBean("key_" + i, "author_" + i, "标题_title_" + i, "abc论坛信息，问题解答。常见问题。正文内容信息，这是正文信息。---- ____" + i, new Date()));
        }
        beans.add(new TopicBaseBean("1001001001", "111111_11111111", "aaaaSSSSSSVVVVVVVVaaa_aaaaaaaaaaa", "ccccccc_ccccccccccc", new Date()));
        beans.add(new TopicBaseBean("2002002002", "2222222_11111111", "aaaaSSSSSSaaaa_aaaaaaaaaaa", "ddddddddddd_ccccccccccc", new Date()));
        beans.add(new TopicBaseBean("1001003003", "33333333_11111111", "aaaSSSSSSaaa_aaaaaaaaaaa", "deeeeeeeeee_ccccccccccc", new Date()));
        beans.add(new TopicBaseBean("1001004004", "4444444444444_11111111", "aaaGGGGGa_aaaaaaaaaaa", "fffffffffffffffffff_ccccccccccc", new Date()));
        beans.add(new TopicBaseBean("1001005005", "5555555555_11111111", "RRRRRRRRaa_aaaaaaaaaaa", "cffffcGGGGGGGGGGGGGGGGG_ccccccBBBBBBBBBBcc", new Date()));
        return beans;
    }
    
    /**
     * 微博索引
     */
    public static void createWeiboIndexs() {
    }
}
