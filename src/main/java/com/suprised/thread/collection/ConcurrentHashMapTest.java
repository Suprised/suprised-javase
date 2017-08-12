package com.suprised.thread.collection;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 是线程安全且高效的HashMap效率比HashTable好 
 */
public class ConcurrentHashMapTest {

	public static void main(String[] args) {
		ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>(16);
		map.put("key1", "value1");
		/**
		 * 如果指定键已经不再与某个值相关联，则将它与给定值关联。这等价于： 
		   if (!map.containsKey(key)) 
		      return map.put(key, value);
		  else
		       return map.get(key);
		 */
		System.out.println(map.putIfAbsent("key1", "value2"));
		System.out.println(map.putIfAbsent("key2", "value2"));// 不存在key值，返回空
		
		System.out.println(map);
		
		System.out.println(map.containsKey("key1"));
		System.out.println(map.containsValue("value1"));
		
		/**
		 * if (map.containsKey(key)) {
		       return map.put(key, value);
		   } else return null;
		 */
		System.out.println(map.replace("key1", "value1-replace"));// 只有存在值才替换,并返回旧值
		System.out.println(map);
	}
	
}
