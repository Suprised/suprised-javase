package com.suprised.thread.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import com.suprised.utils.Threads;

/**
 * 信号量： 控制并发线程数（用来控制同时访问特定资源的线程数量）
 */
public class SemaphoreTest {
	
	private static final int THREAD_COUNT = 30;// 30个线程
	
	private static ExecutorService pool = Executors.newFixedThreadPool(THREAD_COUNT);

	private static Semaphore s = new Semaphore(10, false);// 支持公平性
	
	public static void main(String[] args) {
		for (int i=0; i<THREAD_COUNT; i++) {
			pool.execute(new Runnable() {
				
				@Override
				public void run() {
					try {
						s.acquire();// 获取许可
						Threads.sleep(1000);
						System.out.println("执行完毕。");
						s.release();// 释放许可
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		}
		pool.shutdown();
	}
	
}
