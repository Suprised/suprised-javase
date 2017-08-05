package com.suprised.thread;

import java.util.concurrent.CountDownLatch;

import com.suprised.utils.Threads;

/**
 * 使用synchronized线程来处理并发
 * 
 * synchronized是获取对象锁：
 * 1,当一个线程访问object中的一个synchronized方法时，其它线程仍可以访问这个object中是其它非synchronized。
 * 2,当一个线程访问object的一个synchronized方法时，其它线程对这个object中其它synchronized方法的访问将被阻塞。
 */
public class SynchronizedThread {

	public static void main(String[] args) {
		// 启动10线程SynRuner中的各个方法
		// 用来保证线程同时运行
		final CountDownLatch begin = new CountDownLatch(1);
		final SynRunner synRunner = new SynRunner();
		for (int i=0; i<10; i++) {
			Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						begin.await();// 线程计数等待
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					synRunner.sleep(1000);
				}
			}, "Thread1-" + i);
			thread.start();
		}
		
		for (int i=0; i<10; i++) {
			Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						begin.await();// 线程计数等待
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					synRunner.sleep2(2000);
				}
			}, "Thread2-" + i);
			thread.start();
		}
		
		for (int i=0; i<10; i++) {
			Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						begin.await();// 线程计数等待
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					synRunner.calcAdd(1,2);
				}
			}, "Thread2-" + i);
			thread.start();
		}
		begin.countDown();// 唤醒30个线程
	}
}

class SynRunner {
	
	public synchronized void sleep(long durationMillis) {
		System.out.println(Thread.currentThread().getName() + "sleep() begin");
		Threads.sleep(durationMillis);
		System.out.println(Thread.currentThread().getName() + "sleep() end");
	}
	
	public synchronized void sleep2(long durationMillis) {
		System.out.println(Thread.currentThread().getName() + "sleep2() begin");
		Threads.sleep(durationMillis);
		System.out.println(Thread.currentThread().getName() + "sleep2() end");
	}
	
	
	public void calcAdd(int i, int j) {
		System.out.println("i+j = " + (i + j));
	}
}
