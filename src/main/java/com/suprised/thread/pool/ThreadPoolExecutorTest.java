package com.suprised.thread.pool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.suprised.utils.Threads;

/**
 * 线程池
 */
public class ThreadPoolExecutorTest {

	public static void main(String[] args) {
		// 线程池执行任务时： 
		// 如果当前使用的线程小于初始化的值，则直接创建/使用初始化线程(此时不进入队列)
		// 初始线程已经用完，则新加的任务会放入队列总等待调用。(此时也只是用初始化线程大小处理)
		// 如果队列已经放满，则会创建按最大线程数的创建线程执行任务。(此时按最大线程数创建线程)
		// 最大线程已经用完，则进入饱和策略。
		ThreadPoolExecutor pool = new ThreadPoolExecutor(
				10, // 初始化线程大小
				20, // 最大线程数
				1,// 线程活动保持时间
				TimeUnit.SECONDS, // 单位
				new LinkedBlockingQueue<Runnable>(10),// 添加到线程池的任务队列
				new ThreadFactoryBuilder().setNameFormat("pool-thread-%d").build(),// ThreadFactory 设置线程名称 
				new RejectedExecutionHandler() {// 饱和策略，池大小和队列大小都满时，处理。

					@Override
					public void rejectedExecution(Runnable run,
							ThreadPoolExecutor pool) {
						System.out.println("线程池已满，拒绝再加任务，活动线程数：" + pool.getActiveCount() + ",队列中等待的数：" + pool.getQueue().size());
					}
				});
		
		System.out.println("启动线程个数：" + pool.prestartAllCoreThreads());// 提前创建初始化个线程
		
		for (int i=0; i<=30; i++) {
			pool.execute(new Runnable() {
				
				@Override
				public void run() {
					Threads.sleep(3000);// 暂停3秒
					System.out.println(Thread.currentThread().getName());
				}
			});
		}
		
		System.out.println("cpu个数：" + Runtime.getRuntime().availableProcessors());
		
		pool.shutdown();
		// List<Runnable> runs = pool.shutdownNow();
		// System.out.println("未完成的任务大小：" + runs.size());
	}

}
