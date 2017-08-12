package com.suprised.thread.util;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * java7 有Fork/Join
 * CountDownLatch 的典型的用法是将一个程序分为N个互相独立的可解决的任务。
 * CountDownLatch 被设计为只触发一次，计数值不能被重置，如果需要能够重置计数值的版本
 * 则可以使用 CyclicBarrier
 */
public class CountDownLatchTest {

	static final int SIZE = 100; 
	
	public static void main(String[] args) {
		CountDownLatch latch = new CountDownLatch(SIZE);
		ExecutorService exec = Executors.newCachedThreadPool();
		
		for (int i=0; i<10; i++) {
			// 等啊，等到CountDownLatch的size为0 后开始执行
			exec.execute(new WaitingTask(latch));
		}
		
		for (int i=0; i<SIZE; i++) {
			// 执行一个countDown-1
			exec.execute(new TaskPortion(latch));
		}
		System.out.println("完成所有的任务。");
		exec.shutdown();
	}
	
}

class TaskPortion implements Runnable {
	
	private static int counter = 0;
	private final int id = counter++;
	private static Random rand = new Random(47);
	private final CountDownLatch latch;
	
	public TaskPortion(CountDownLatch latch) {
		this.latch = latch;
	}
	
	@Override
	public void run() {
		try {
			doWork();
			latch.countDown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void doWork() throws InterruptedException {
		TimeUnit.MICROSECONDS.sleep(rand.nextInt(2000));
		System.out.println(this + " 任务完成");
	}
	
	@Override
	public String toString() {
		return String.format("%1$-3d", id);
	}
}

class WaitingTask implements Runnable {

	private static int counter = 0;
	private final int id = counter++;
	private final CountDownLatch latch;
	
	public WaitingTask(CountDownLatch latch) {
		this.latch = latch;
	}
	
	@Override
	public void run() {
		try {
			// 等到latch的数量为0时，开始执行
			latch.await();
			System.out.println("latch barrier passed for " + this);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		return String.format("waitingTask %1$-3d", id);
	}
}
