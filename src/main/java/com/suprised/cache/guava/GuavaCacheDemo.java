package com.suprised.cache.guava;

import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import org.junit.Test;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.suprised.utils.Threads;

/**
 * 本地缓存演示，使用GuavaCache.
 */
public class GuavaCacheDemo extends TestCase {

	@Test
	public void testDemo() throws Exception {
		// 设置缓存最大个数为100，缓存过期时间为5秒
		LoadingCache<Integer, UserData> cache = CacheBuilder.newBuilder().maximumSize(100)
				.expireAfterAccess(5, TimeUnit.SECONDS).build(new CacheLoader<Integer, UserData>() {
					@Override
					public UserData load(Integer key) throws Exception {
						return UserData.get(key);
					}
				});
		
		// 第一次加载会查数据库
		UserData user = cache.get(1);
		System.out.println(user);
		
		// 第二次加载时直接从缓存里取
		UserData user2 = cache.get(1);
		System.out.println(user2);

		// 第三次加载时，因为缓存已经过期所以会查数据库
		Threads.sleep(10, TimeUnit.SECONDS);
		UserData user3 = cache.get(1);
		System.out.println(user3);
	}
}
