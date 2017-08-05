package com.suprised.thread;

import com.suprised.utils.Threads;

/**
 * 安全地终止线程：两个方法演示终止线程
 */
public class SafeStopThread {

	public static void main(String[] args) {
		Runner runner1 = new Runner();
		Thread thread1 = new Thread(runner1, "CountThread1");
		thread1.start();
		Threads.sleep(1000);
		// 中线thread1，使线程感知被中断而结束
		thread1.interrupt();
		
		Runner runner2 = new Runner();
		Thread thread2 = new Thread(runner2, "CountThread2");
		thread2.start();
		Threads.sleep(1000);
		
		runner2.cancel();// 标识线程中断： 设置on=false，让线程感知而结束
	}
	
}

/**
 * 线程计数
 */
class Runner implements Runnable {
	
	private boolean on = true;// 线程是否中断的开关
	private long count;
	
	@Override
	public void run() {
		while(on && !Thread.currentThread().isInterrupted()) {
			count++;
		}
		System.out.println(Thread.currentThread().getName() + ": count = " + count);
	}
	
	/**
	 * 停止线程
	 */
	public void cancel() {
		on = false;
	}
}