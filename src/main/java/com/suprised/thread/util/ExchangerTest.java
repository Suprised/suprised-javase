package com.suprised.thread.util;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 用于线程间交换数据
 */
public class ExchangerTest {

	private static final Exchanger<String> exchanger = new Exchanger<>();
	private static ExecutorService pool = Executors.newFixedThreadPool(2);
	
	public static void main(String[] args) {
		pool.execute(new Runnable() {
			
			@Override
			public void run() {
				String a = "thread-1的数据";
				try {
					String result = exchanger.exchange(a);// 等待数据交换
					System.out.println("thread1:" + result);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		pool.execute(new Runnable() {
			
			@Override
			public void run() {
				String b = "thread-2的数据";
				try {
					String result = exchanger.exchange(b);
					System.out.println("thread2:" + result);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		pool.shutdown();
	}
	
}
