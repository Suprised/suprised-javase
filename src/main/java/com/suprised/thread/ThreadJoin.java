package com.suprised.thread;

/**
 * Thread.join()方法：
 * 如果一个线程A执行了thread.join()方法，含义是：当前线程A等待thread线程终止后才继续执行。
 */
public class ThreadJoin {

	public static void main(String[] args) {
		Thread previous = Thread.currentThread();
		for (int i=0; i<10; i++) {
			Thread next = new Thread(new Domino(previous), "Thread-" + i);
			next.start();
			previous = next;
		}
		System.out.println(Thread.currentThread().getName());
	}
	
	static class Domino implements Runnable {
		
		private Thread joinThread = null;
		
		public Domino(Thread thread) {
			joinThread = thread;
		}
		
		@Override
		public void run() {
			try {
				joinThread.join();// 等待当前线程执行完，再继续执行
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName());
		}
	}
}
