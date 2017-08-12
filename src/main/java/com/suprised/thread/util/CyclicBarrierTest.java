package com.suprised.thread.util;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 线程屏障：让一组线程达到一个屏障（同步点）时阻塞，
 * 	直到最后一个线程达到屏障时，所有被拦截的屏障才会继续运行
 */
public class CyclicBarrierTest {

	public static void main(String[] args) {
		testCyclicBarrier();
	}
	
	
	public static void testCyclicBarrier() {
		final CyclicBarrier cyclicBarrier = new CyclicBarrier(2, new Runnable() {
			
			@Override
			public void run() {
				System.out.println("所有线程都到达同步点时，优先执行该线程。");
			}
		});
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					cyclicBarrier.await();
				} catch (InterruptedException | BrokenBarrierException e) {
					e.printStackTrace();
				}
				System.out.println("1");
			}
		}).start();
		
		try {
			cyclicBarrier.await();
			System.out.println("2");
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
	}
}
