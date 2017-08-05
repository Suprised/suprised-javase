package com.suprised.thread.connectionpool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionTest {

	// 连接池中初始化10个连接
	static ConnectionPool pool = new ConnectionPool(10);
	// 保证10个线程同时执行
	static CountDownLatch begin = new CountDownLatch(1);
	// 等到所有线程执行完了才继续
	static CountDownLatch end = null;
	
	public static void main(String[] args) throws Exception {
		// 开启threadCount个线程，每个线程获取20次连接
		int threadCount = 20;
		int count = 20;
		end = new CountDownLatch(threadCount);
		// 获取到连接次数
		AtomicInteger getCount = new AtomicInteger();
		// 没有获取到连接次数
		AtomicInteger notGetCount = new AtomicInteger();
		
		for (int i=0; i<threadCount; i++) {
			Thread thread = new Thread(new ConnectionRunner(count, getCount, notGetCount), "Thread-" + i);
			thread.start();
		}
		// 开始执行
		begin.countDown();
		end.await();// 等所有的线程执行完成才继续
		
		System.out.println("总共获取线程次数： " + (count * threadCount));
		System.out.println("获取到连接的次数：" + getCount);
		System.out.println("没有获取到连接的次数：" + notGetCount);
	}
	
	static class ConnectionRunner implements Runnable {
		
		AtomicInteger getCount = null;
		AtomicInteger notGetCount = null;
		int count;
		
		public ConnectionRunner(int count, AtomicInteger getCount, AtomicInteger notGetCount) {
			this.count = count;
			this.getCount = getCount;
			this.notGetCount = notGetCount;
		}
		
		@Override
		public void run() {
			try {
				begin.await();// 等所有线程初始化完成后，才开始同时获取。
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			while(count > 0) {
				// 从连接池中获取线程，1000ms后还没有获取到则返回空
				try {
					Connection conn = pool.getConnection(1000);
					if (conn != null) {
						try {
							conn.execute();// 执行操作
							conn.commit();// 提交操作
						} finally {
							// 归还连接
							pool.releaseConnection(conn);
							getCount.incrementAndGet();
						}
					} else {
						notGetCount.incrementAndGet();
					}
				} catch (Exception e) {
					// 忽略异常
				} finally {
					count--;
				}
			}
			end.countDown();// 执行完一个线程后，线程计数器-1
		}
	}
}
