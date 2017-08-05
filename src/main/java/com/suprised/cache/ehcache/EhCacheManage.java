package com.suprised.cache.ehcache;

import java.net.URL;
import java.util.Collection;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * 用枚举做单例实现缓存管理
 */
public enum EhCacheManage {
    
    Manage;
    
    private CacheManager cacheManager = null; // 缓存管理类
    
    // 以下是自定义缓存的名称，详情见ehcache.xml
    private static final String DEFAULT_CACHE = "DEFAULT_CACHE";
    private static final String SDKDATA_CACHE = "SDKDATA_CACHE";
    private static final String SDKUSER_CACHE = "SDKDATA_USER_CACHE";
    private static final String WEBDATA_CACHE = "WEBDATA_CACHE";

    private EhCacheManage() {
        URL url = EhCacheManage.class.getResource("ehcache.xml");
        cacheManager = new CacheManager(url);
    }

    private Cache getDefaultCache() {
        return getCacheByName(DEFAULT_CACHE);
    }

    private Cache getSDKCache() {
        return getCacheByName(SDKDATA_CACHE);
    }

    private Cache getUserCache() {
        return getCacheByName(SDKUSER_CACHE);
    }

    private Cache getWebCache() {
        return getCacheByName(WEBDATA_CACHE);
    }

    private Cache getCacheByName(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        return cache;
    }

    /**
     * 保存全局的cache，这个cache是永久的cache，不会定时清除
     * 
     * @param cacheKey
     * @param cacheObj
     */
    public  void putCache(String cacheKey, Object cacheObj) {
        Cache cache = getDefaultCache();
        if (cache != null) {
            cache.put(new Element(cacheKey, cacheObj));
        }
    }

    /**
     * 获得全局的cache
     * 
     * @param cacheKey
     * @return
     */
    public  Object getCache(String cacheKey) {
        Cache cache = getDefaultCache();
        if (cache != null) {
            Element element = cache.get(cacheKey);
            if (element != null) {
                return element.getObjectValue();
            }
        }
        return null;
    }

    /**
     * 清除全局的cache
     * 
     * @param cacheKey
     */
    public  void clearCache(String cacheKey) {
        Cache cache = getDefaultCache();
        if (cache != null) {
            cache.remove(cacheKey);
        }
    }

    public  void removeCacheValue(String cacheKey, String valueKey) {
        Object cacheValue = getCache(cacheKey);
        if (cacheValue != null && cacheValue instanceof Map) {
            Map cacheValueMap = (Map) cacheValue;
            cacheValueMap.remove(valueKey);
            putCache(cacheKey, cacheValueMap);
        } else if (cacheValue != null && cacheValue instanceof Collection) {
            Collection cacheValueCollection = (Collection) cacheValue;
            cacheValueCollection.remove(valueKey);
            putCache(cacheKey, cacheValueCollection);
        }
    }

    private  Cache getObjectCacheByType(String scopeType) {
        Cache targetCache = getSDKCache();
        return targetCache;
    }

    private  String getCacheKey(String cacheType, String cacheKey) {
        return cacheType + cacheKey;
    }

    public  void recordObjectCacheByType(String cacheType,
        String cacheKey, Object cacheValue) {
        Cache cache = getObjectCacheByType(cacheType);
        if (cache != null) {
            cache
                .put(new Element(getCacheKey(cacheType, cacheKey), cacheValue));
        }
    }

    public  Object loadObjectCacheByType(String cacheType, String cacheKey) {
        Cache cache = getObjectCacheByType(cacheType);
        if (cache == null) {
            return null;
        }

        Element element = cache.get(getCacheKey(cacheType, cacheKey));
        if (element != null) {
            return element.getObjectValue();
        }
        return null;
    }

    public  void removeObjectCacheByType(String cacheType, String cacheKey) {
        Cache cache = getObjectCacheByType(cacheType);
        if (cache != null) {
            cache.remove(getCacheKey(cacheType, cacheKey));
        }
    }

    /**
     * 保存User的cache，会定时清除
     * 
     * @param cacheKey
     * @param cacheObj
     */
    public  void putUserCache(String cacheKey, Object cacheObj) {
        Cache cache = getUserCache();
        if (cache != null) {
            cache.put(new Element(cacheKey, cacheObj));
        }
    }

    /**
     * 获得用户的cache
     * 
     * @param cacheKey
     * @return
     */
    public  Object getUserCache(String cacheKey) {
        Cache cache = getUserCache();
        if (cache != null) {
            Element element = cache.get(cacheKey);
            if (element != null) {
                return element.getObjectValue();
            }
        }
        return null;
    }

    /**
     * 清除全局的cache
     * 
     * @param cacheKey
     */
    public  void removeUserCache(String cacheKey) {
        Cache cache = getUserCache();
        if (cache != null) {
            cache.remove(cacheKey);
        }
    }

	public  void removeUserCacheValue(String cacheKey, String valueKey) {
        Object cacheValue = getUserCache(cacheKey);
        if (cacheValue != null && cacheValue instanceof Map) {
            Map cacheValueMap = (Map) cacheValue;
            cacheValueMap.remove(valueKey);
            putUserCache(cacheKey, cacheValueMap);
        } else if (cacheValue != null && cacheValue instanceof Collection) {
            Collection cacheValueCollection = (Collection) cacheValue;
            cacheValueCollection.remove(valueKey);
            putUserCache(cacheKey, cacheValueCollection);
        }
    }

    /**
     * 保存Web的cache，会定时清除
     * 
     * @param cacheKey
     * @param cacheObj
     */
    public  void putWebCache(String cacheKey, Object cacheObj) {
        Cache cache = getWebCache();
        if (cache != null) {
            cache.put(new Element(cacheKey, cacheObj));
        }
    }

    /**
     * 获得用户的cache
     * 
     * @param cacheKey
     * @return
     */
    public  Object getWebCache(String cacheKey) {
        Cache cache = getWebCache();
        if (cache != null) {
            Element element = cache.get(cacheKey);
            if (element != null) {
                return element.getObjectValue();
            }
        }
        return null;
    }

    /**
     * 清除全局的cache
     * 
     * @param cacheKey
     */
    public void removeWebCache(String cacheKey) {
        Cache cache = getWebCache();
        if (cache != null) {
            cache.remove(cacheKey);
        }
    }

    public void removeWebCacheValue(String cacheKey, String valueKey) {
        Object cacheValue = getWebCache(cacheKey);
        if (cacheValue != null && cacheValue instanceof Map) {
            Map cacheValueMap = (Map) cacheValue;
            cacheValueMap.remove(valueKey);
            putWebCache(cacheKey, cacheValueMap);
        } else if (cacheValue != null && cacheValue instanceof Collection) {
            Collection cacheValueCollection = (Collection) cacheValue;
            cacheValueCollection.remove(valueKey);
            putWebCache(cacheKey, cacheValueCollection);
        }
    }

}
