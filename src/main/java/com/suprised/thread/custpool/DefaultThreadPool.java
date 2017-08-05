package com.suprised.thread.custpool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 一个默认的线程池实现类
 */
public class DefaultThreadPool<Job extends Runnable> implements ThreadPool<Job> {

	// 最大数量
	private static final int MAX_SIZE = 10;
	// 默认值
	private static final int DEF_SIZE = 5;
	// 最小数量
	private static final int MIN_SIZE = 1;
	// 等待执行的集合
	private final LinkedList<Job> jobs = new LinkedList<>();
	// 线程池中的线程
	private final List<Worker> workers = Collections.synchronizedList(new ArrayList<Worker>());
	// 工作线程的数量
	private int workerNum = DEF_SIZE;
	// 生成线程编号
	private AtomicLong threadNum = new AtomicLong();
	
	public DefaultThreadPool() {
		init(workerNum);
	}
	
	public DefaultThreadPool(int num) {
		workerNum = num > MAX_SIZE ? MAX_SIZE : num < MIN_SIZE ? MIN_SIZE : num;
		init(workerNum);
	}
	
	// 初始化线程池
	private void init(int num) {
		for (int i=0; i<num; i++) {
			Worker worker = new Worker();
			workers.add(worker);
			Thread thread = new Thread(worker, "ThreadPool-worker-" + threadNum.incrementAndGet());
			thread.start();
		}
	}
	
	@Override
	public void execute(Job job) {
		if (job == null) 
			return;
		synchronized (jobs) {
			// 添加一个job，并通知工作池
			jobs.addLast(job);
			jobs.notifyAll();
		}
	}

	@Override
	public void shutdown() {
		for (Worker worker : workers) {
			worker.shutdown();
		}
	}

	@Override
	public void addWorkers(int num) {
		synchronized (jobs) {
			if (num + workerNum > MAX_SIZE) {
				num = MAX_SIZE - this.workerNum;
			}
			init(num);
			this.workerNum += num;
		}
	}

	@Override
	public void removeWorker(int num) {
		synchronized (jobs) {
			if (num > workerNum) {
				num = workerNum;
			}
			int count = 0;
			while(count < num) {
				Worker worker = workers.get(count);
				if (workers.remove(worker)) {
					worker.shutdown();
					count++;
				}
			}
			this.workerNum -= count;
		}
	}

	@Override
	public int getSize() {
		return workers.size();
	}
	
	@Override
	public int getJobSize() {
		return jobs.size();
	}
	
	/**
	 * 工作线程 
	 */
	class Worker implements Runnable {

		public volatile boolean running = true;
		
		@Override
		public void run() {
			while (running && !Thread.currentThread().isInterrupted()) {
				Job job = null;
				synchronized (jobs) {
					while(jobs.isEmpty()) {
						try {
							jobs.wait();// 释放锁
						} catch (InterruptedException e) {
							e.printStackTrace();
							return;
						}
					}
					job = jobs.removeFirst();// 取出一个job
					System.out.println("剩下的job：" + jobs.size());
					// 释放锁
				}
				if (job != null) {
					try {
						job.run();// 执行操作
					} catch(Exception e) {
						// 忽略异常
					}
				}
			}
		}

		public void shutdown() {
			running = false;
		}
	}

}
