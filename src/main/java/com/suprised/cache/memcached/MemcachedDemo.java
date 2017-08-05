package com.suprised.cache.memcached;

import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.google.common.collect.Lists;
import com.suprised.spring.SpringContextTestCase;

@ContextConfiguration(locations = { "classpath:spring/applicationContext-memcached.xml", "classpath:spring/applicationContext.xml" })
public class MemcachedDemo extends SpringContextTestCase {

	@Autowired
	private SpyMemcachedClient spyMemcachedClient;

	@Test
	public void normal() {

		String key = "consumer:1";
		String value = "admin";

		spyMemcachedClient.set(key, 60 * 60 * 1, value);

		String result = spyMemcachedClient.get(key);
		System.out.println(result);
		//assertThat(result).isEqualTo(value);

		spyMemcachedClient.delete(key);
		result = spyMemcachedClient.get(key);
		System.out.println(result);
		//assertThat(result).isNull();
	}

	@Test
	public void safeDelete() {
		String key = "consumer:1";
		spyMemcachedClient.set(key, 60, "admin");
		System.out.println(spyMemcachedClient.safeDelete(key));
		System.out.println(spyMemcachedClient.safeDelete("consumer:1"));
		//assertThat(spyMemcachedClient.safeDelete(key)).isTrue();
		//assertThat(spyMemcachedClient.safeDelete("consumer:1")).isFalse();
	}

	@Test
	public void getBulk() {

		String key1 = "consumer:1";
		String value1 = "admin";

		String key2 = "consumer:2";
		String value2 = "calvin";

		String key3 = "invalidKey";

		spyMemcachedClient.set(key1, 60 * 60 * 1, value1);
		spyMemcachedClient.set(key2, 60 * 60 * 1, value2);

		Map<String, String> result = spyMemcachedClient.getBulk(Lists.newArrayList(key1, key2));
		System.out.println(result.get(key1).equals(value1));
		System.out.println(result.get(key2).equals(value2));
		System.out.println(result.get(key3) == null);
		//assertThat(result.get(key1)).isEqualTo(value1);
		//assertThat(result.get(key2)).isEqualTo(value2);
		//assertThat(result.get(key3)).isNull();
	}

	@Test
	public void incr() {
		String key = "incr_key";

		// 注意,incr返回的数值使用long表达
		long result = spyMemcachedClient.incr(key, 2, 1);
		System.out.println(result == 1);
		//assertThat(result).isEqualTo(1);
		// 注意,get返回的数值使用字符串表达
		System.out.println(spyMemcachedClient.get(key).equals("1"));
		//assertThat(spyMemcachedClient.get(key)).isEqualTo("1");

		System.out.println(spyMemcachedClient.incr(key, 2, 1) == 3);
		System.out.println(spyMemcachedClient.get(key).equals("3"));
		//assertThat(spyMemcachedClient.incr(key, 2, 1)).isEqualTo(3);
		//assertThat(spyMemcachedClient.get(key)).isEqualTo("3");

		key = "set_and_incr_key";
		// 注意,set中的数值必须使用字符串,后面的incr操作结果才会正确.
		spyMemcachedClient.set(key, 60 * 60 * 1, "1");
		System.out.println(spyMemcachedClient.incr(key, 2, 1) == 3);
		//assertThat(spyMemcachedClient.incr(key, 2, 1)).isEqualTo(3);

	}
}
