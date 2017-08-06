package com.suprised.thread.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.suprised.utils.Threads;

/**
 * 读写锁： 支持公平锁和非公平锁
 * 写锁会阻塞其它线程
 * 读锁支持多个线程同时获取
 */
public class ReadWriteLockThread {

	public static void main(String[] args) {
		
		final CustCache cache = new CustCache();
		cache.putObject("key", "value");
		for (int i=0; i<10; i++) {
			Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					cache.getObject("key");
				}
			}, String.valueOf(i));
			thread.start();
		}
		
	}
	
}

/**
 * 使用读写锁实现简单的缓存
 */
class CustCache {
	
	private Map<String, Object> cacheMap = new HashMap<>();
	
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private Lock readLock = lock.readLock();// 获取读锁
	private Lock writeLock = lock.writeLock();// 获取写锁
	
	// 获取缓存值，获得读锁
	public Object getObject(String key) {
		readLock.lock();
		try {
			Threads.sleep(200);
			return cacheMap.get(key);
		} finally {
			readLock.unlock();
		}
	}
	
	// 存入缓存，获取写锁
	public void putObject(String key, Object value) {
		writeLock.lock();// 获取写锁，其它锁堵塞
		try {
			Threads.sleep(300);
			cacheMap.put(key, value);
		} finally {
			writeLock.unlock();
		}
	}
	
	// 清除缓存，获取写锁
	public void clear() {
		writeLock.lock();
		try {
			cacheMap.clear();
		} finally {
			writeLock.unlock();
		}
	}
}