package com.suprised.thread.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 原子操作更新基本类型：AtomicInteger AtomicBoolean AtomicLong
 */
public class AtomicBase {

	static AtomicInteger ai = new AtomicInteger(1);
	
	public static void main(String[] args) {
		// AtomicInteger AtomicBoolean AtomicLong
		System.out.println(ai.getAndIncrement());// 以原子的方式增进1,返回自增前的值
		System.out.println(ai.get());// 获取自增后的值
		
		System.out.println(ai.addAndGet(10));// 增加两个值，并返回结果
		// 如果输入的值等于预期值，则以原子方式将该值设置为输入的值，否则不修改值
		System.out.println(ai.compareAndSet(11, 20));// 如果预期值为20，则ai.get()==20
		System.out.println(ai.get());
		
		System.out.println(ai.getAndSet(30));// 设置新值，返回就值
		System.out.println(ai.get());
	}
	
}
