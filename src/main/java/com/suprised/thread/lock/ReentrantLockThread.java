package com.suprised.thread.lock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import com.suprised.utils.Threads;

/**
 * ReentrantLock: 公平锁和非公平锁； 非公平锁效率高（默认）
 * 公平锁： 获取锁的顺序按照FIFO(先进先出)进行获取。
 * 非公平锁：在等待线程中随机获取 
 */
public class ReentrantLockThread {
	
	private static ReentrantLock2 fair = new ReentrantLock2(true); // 公平锁
	private static ReentrantLock2 unFair = new ReentrantLock2(false);// 非公平锁

	public static void main(String[] args) {
		 testLock(fair);// 公平锁测试 ： 获取锁是按照FIFO的顺序
		 Threads.sleep(3000);
		 System.out.println("-----------------");
		 testLock(unFair);// 非公平锁测试：获取锁的顺序随机
	}
	
	private static void testLock(ReentrantLock2 lock) {
		// 启动5个job
		for (int i=0; i<5; i++) {
			Thread thread = new Thread(new Job(lock), String.valueOf(i));
			thread.start();
		}
	}
	
	private static class ReentrantLock2 extends ReentrantLock {
		
		private static final long serialVersionUID = -914763689563207391L;

		public ReentrantLock2(boolean fair) {
			super(fair);
		}
		
		@Override
		protected Collection<Thread> getQueuedThreads() {
			List<Thread> arrayList = new ArrayList<>(super.getQueuedThreads());
			Collections.reverse(arrayList);
			return arrayList;
		}
	}
	
	private static class Job extends Thread {
		private ReentrantLock2 lock = null;
		
		public Job(ReentrantLock2 lock) {
			this.lock = lock;
		}
		
		@Override
		public void run() {
			lock.lock();
			try {
				// 连续打印两次当前Thread和等待中的Thread
				System.out.println(String.format("获取锁的线程:[%s], 等待线程：%s", Thread
						.currentThread().getName(), Arrays.toString(lock
						.getQueuedThreads().toArray())));
			} finally {
				lock.unlock();
			}
		}
	}
}
