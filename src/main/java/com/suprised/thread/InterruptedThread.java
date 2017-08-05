package com.suprised.thread;

import com.suprised.utils.Threads;

/**
 * 线程中断
 * 
 * 中断线程只是一个线程标识位属性，表示线程是否被其它线程中断；线程通过检查自身是否被中断响应。
 */
public class InterruptedThread {
	
	public static void main(String[] args) {
		// 不停的尝试睡眠
		Thread sleepThread = new Thread(new SleepRunner(), "SleepThread");
		sleepThread.setDaemon(true);
		
		// 不停的运行
		Thread busyThread = new Thread(new BusyRunner(), "BusyThread");
		busyThread.setDaemon(true);
		
		sleepThread.start();
		busyThread.start();
		
		// 休眠5秒，让线程充分运行
		Threads.sleep(5000);
		// 中断线程
		sleepThread.interrupt();
		busyThread.interrupt();
		System.out.println("SleepThread interrupted is " + sleepThread.isInterrupted());
		System.out.println("BusyThread interrupted is " + busyThread.isInterrupted());
		
		Threads.sleep(2000);
	}
	
}

/**
 * 一直随眠状态的线程 
 */
class SleepRunner implements Runnable {

	@Override
	public void run() {
		while(true) {
			// 抛出InterruptedException的方法：
			// 抛出异常之前，java虚拟机会将该线程的中断标识位清除，然后抛出异常，
			// 调用isInterrupted()方法会返回false
			Threads.sleep(10000);
		}
	}
}

/**
 * 一直运行中的线程
 */
class BusyRunner implements Runnable {

	@Override
	public void run() {
		while(true){
		}
	}
}