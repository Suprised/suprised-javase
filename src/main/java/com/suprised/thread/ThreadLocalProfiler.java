package com.suprised.thread;

import com.suprised.utils.Threads;

/**
 * ThreadLocal 线程变量
 */
public class ThreadLocalProfiler {

	public static void main(String[] args) {
		Profiler filer = new Profiler();
		filer.begin();
		Threads.sleep(1000);
		System.out.println(filer.end());
	}
	
	static class Profiler {
		
		private ThreadLocal<Long> time_local = new ThreadLocal<Long>(){
			protected Long initialValue() {// 初始化值，每个线程都会初始化
				return System.currentTimeMillis();
			}
		};
		
		public void begin() {
			time_local.set(System.currentTimeMillis());
		}
		
		public long end() {
			return System.currentTimeMillis() - time_local.get();
		}
	}
}
