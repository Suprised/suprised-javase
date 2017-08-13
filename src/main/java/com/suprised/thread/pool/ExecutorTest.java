package com.suprised.thread.pool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * Executor框架使用
 */
public class ExecutorTest {

	public static void main(String[] args) throws Exception {
		testThreadPool();
		testScheduledThreadPool();
	}
	
	public static void testThreadPool() throws Exception {
		// 可重用的固定线程数的线程池，使用的是LinkedBlockingQueue
		Executors.newFixedThreadPool(10, new ThreadFactoryBuilder().setNameFormat("pool-task-%d").build());
		// 创建单线程的池，使用的LinkedBlockingQueue
		Executors.newSingleThreadExecutor();
		// 一个任务会创建一个线程处理，队列不存放数据  60s会终止空闲的线程 使用SynchronousQueue，不存放任务
		// new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60l, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
		ExecutorService pools = Executors.newCachedThreadPool();
		pools.execute(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("没有返回值的任务。");
			}
		});
		
		Future<String> result = pools.submit(new Callable<String>() {

			@Override
			public String call() throws Exception {
				Thread.sleep(3000);
				return "返回值";
			}
		});
		String resultValue = result.get();// 获取返回值,阻塞等待返回值
		System.out.println("result: " + resultValue);
	}
	
	public static void testScheduledThreadPool() throws Exception {
		// 处理延时/定期类的任务 使用DelayQueue队列
		Executors.newScheduledThreadPool(10, new ThreadFactoryBuilder().setNameFormat("pool-task-%d").build());
		ScheduledExecutorService pools = Executors.newSingleThreadScheduledExecutor();// 单个线程
		// 一次性操作
		ScheduledFuture<String> futrue = pools.schedule(new Callable<String>() {

			@Override
			public String call() throws Exception {
				return "延迟5秒后执行,有返回值";
			}
			
		}, 5, TimeUnit.SECONDS);
		
		System.out.println(futrue.get());
		// 一次性操作
		pools.schedule(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("延迟3秒后执行，无返回值。");
			}
		}, 3, TimeUnit.SECONDS);
		
		// 周期性操作：三秒后执行第一次，然后每隔一秒执行一次
		ScheduledFuture<?> cancel = pools.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("周期性操作：按时间作为执行标识。");
			}
		}, 3, 1, TimeUnit.SECONDS);
		
		Thread.sleep(10000);// 暂停10s
		
		cancel.cancel(true);// 取消周期行操作。
		System.out.println("按时间周期性操作已取消。");
		// 周期性操作：三秒后执行第一次，然后再第一次任务执行完后五秒执行
		// 每一次执行终止和下一次执行开始之间都存在给定的延迟
		ScheduledFuture<?> cancel2 = pools.scheduleWithFixedDelay(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("周期性操作：按上次执行结束作为标识。");
			}
		}, 3, 5, TimeUnit.SECONDS);
		
		Thread.sleep(10000);// 暂停10s
		cancel2.cancel(false);// 取消周期行操作。
		System.out.println("按执行任务周期性操作任务已取消。");
	}
}
