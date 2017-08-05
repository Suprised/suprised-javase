package com.suprised.thread.custpool;

import com.suprised.utils.Threads;

public class ThreadPoolTest {

	public static void main(String[] args) {
		// 初始化五个线程
		ThreadPool<Runnable> pools = new DefaultThreadPool<>(5);
		System.out.println("pools size : " + pools.getSize());
		// 加三个
		pools.addWorkers(3);
		System.out.println("pools size : " + pools.getSize());
		// 减4个
		pools.removeWorker(4);
		System.out.println("pools size : " + pools.getSize());
		
		// 执行100个job
		for (int i=0; i<100; i++) {
			pools.execute(new Runnable() {
				
				@Override
				public void run() {
					Threads.sleep(20);
				}
			});
		}
		while (pools.getJobSize() > 0) {
			Threads.sleep(1000);
		}
		System.out.println("well done.");
	}
	
}
