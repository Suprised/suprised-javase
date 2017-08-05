package com.suprised.cache.ehcache;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 从initital文件夹下读取数据字典数据并缓存
 * 
 * <p>此处的数据字典类型放入在内存缓存，且永不过期</p>
 * <p>用于系统中的固定的字典，且不能够更改.能够从页面维护的字典需要存在可更改的缓存中</p>
 * 
 * @author 刘金喜
 */
@SuppressWarnings("unchecked")
public class DictBeanImpl {
    
    private static final String DICT_NAME = "dictName";
    
    public void init() {
        EhCacheManage.Manage.clearCache(DICT_NAME);
        EhCacheManage.Manage.putCache(DICT_NAME, loadDictBean());
        for (int i=0; i<=10; i++) {
        	EhCacheManage.Manage.putCache(DICT_NAME + i, loadDictBean());	
        }
    }
    
    private Map<String, List<DictBean>> loadDictBean() {
        String url = DictBeanImpl.class.getClassLoader().getResource("lisence.xml").getPath();
        File[] files = new File(url.split("lisence.xml")[0] + "initital").listFiles();
        Map<String, List<DictBean>> dictBeans = Maps.newHashMap();
        for (File file : files) {
            loadDictBean(file, dictBeans);
        }
        return dictBeans;
    }
    
    private void loadDictBean(File file, Map<String, List<DictBean>> dictBeans) {
        SAXReader saxReader = new SAXReader();
        Document docuemnt;
        try {
            docuemnt = saxReader.read(file);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        Element element = docuemnt.getRootElement();
        List<Element> nodes = (List<Element>)element.selectNodes("dics");
        for (Element node : nodes) {
            dictBeans.put(node.attribute("key").getValue(),loadDictBean(node));
        }
    }
    
    private List<DictBean> loadDictBean(Element element) {
        List<DictBean> beans = Lists.newArrayList();
        List<Element> childs = (List<Element>)element.selectNodes("dic");
        DictBean bean;
        for (Element e : childs) {
            bean = new DictBean();
            bean.setCode(Integer.parseInt(e.attribute("code").getValue()));
            bean.setTitle(e.attribute("title").getValue());
            beans.add(bean);
            List<Element> child = e.selectNodes("dic");
            if (!child.isEmpty()) {
                loadDictBean(child, bean);
            }
        }
        return beans;
    }
    
    private void loadDictBean(List<Element> child, DictBean bean) {
        DictBean dictBean ;
        List<DictBean> beans = Lists.newArrayList();
        for (Element e : child) {
            dictBean = new DictBean();
            dictBean.setCode(Integer.parseInt(e.attribute("code").getValue()));
            dictBean.setTitle(e.attribute("title").getValue());
            beans.add(dictBean);
            List<Element> tmps = e.selectNodes("dic");
            if (!tmps.isEmpty()) {
                loadDictBean(tmps, dictBean);
            }
        }
        bean.setDicts(beans);
    }
    
    public String getDictTitle(String key, int code) {
        return getDictTitle(DICT_NAME, key, code);
    }
    
    public String getDictTitle(String cacheKey, String key, int code) {
        Map<String, List<DictBean>> beansMap = (Map<String, List<DictBean>>)EhCacheManage.Manage.getCache(DICT_NAME);
        List<DictBean> beans = beansMap.get(key);
        for (DictBean bean : beans) {
            if (bean.getCode() == code)
                return bean.getTitle();
        }
        return null;
    }
    
    public List<DictBean> getDicts(String key) {
        Map<String, List<DictBean>> beansMap = (Map<String, List<DictBean>>)EhCacheManage.Manage.getCache(DICT_NAME);
        List<DictBean> beans = beansMap.get(key);
        return beans;
    }
    
    public static void main(String[] args) {
        new DictBeanImpl().init();
        System.out.println(new DictBeanImpl().getDictTitle("robin.visitType", 2));
        Map<String, List<DictBean>> beansMap = (Map<String, List<DictBean>>)EhCacheManage.Manage.getCache(DICT_NAME);
        for (Entry<String, List<DictBean>> map : beansMap.entrySet()){
            System.out.println(map.getKey());
            for (DictBean bean : map.getValue()){
                System.out.println(bean);
            }
        }
        System.out.println(beansMap.get("robin.visitType"));
        
        //EhCacheManage.Manage.putUserCache("user_key", "liujinxi");
        //System.out.println(EhCacheManage.Manage.getUserCache("user_key"));
        //Threads.sleep(1016);
        //System.out.println(EhCacheManage.Manage.getUserCache("user_key"));
    }

}
