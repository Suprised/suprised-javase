package com.suprised.thread.custpool;

/**
 * 自定义一个线程池对象 
 */
public interface ThreadPool<Job extends Runnable> {

	/**
	 * 执行一个job 
	 */
	void execute(Job job);
	
	/**
	 * 关闭线程池
	 */
	void shutdown();
	
	/**
	 * 增进工作线程
	 */
	void addWorkers(int num);
	
	/**
	 * 减少工作线程
	 * @param num
	 */
	void removeWorker(int num);
	
	/**
	 * 获取工作线程数
	 */
	int getSize();
	
	/**
	 * 获取正在等待执行的任务数量
	 */
	int getJobSize();
}
