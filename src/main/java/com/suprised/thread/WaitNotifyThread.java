package com.suprised.thread;

import com.suprised.utils.Threads;

/**
 * 线程等待和唤醒通知
 */
public class WaitNotifyThread {
	
	private static boolean flag = true;// 标识状态  线程是否等待
	private static Object lock = new Object();

	public static void main(String[] args) {
		// 对对象进行加锁
		Thread waitThread = new Thread(new WaitRunner(), "waitThread");
		waitThread.start();
		
		Threads.sleep(1000);
		
		Thread notifyThread = new Thread(new NotifyRunner(), "notifyThread");
		notifyThread.start();
	}
	
	static class WaitRunner implements Runnable {
		
		@Override
		public void run() {
			synchronized (lock) {// 获取lock的锁
				while (flag) {
					try {
						lock.wait();// 如果flag=true时，当前线程进行等待，并且释放lock锁
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("well done!!!");
			}
		}
	}
	
	static class NotifyRunner implements Runnable {

		@Override
		public void run() {
			synchronized (lock) { // 获取lock锁
				flag = false;
				lock.notifyAll();// flag变化了，唤醒所有在lock上等待的线程
				Threads.sleep(1000);
				System.out.println("释放lock锁。");
			}
		}
	}
}
