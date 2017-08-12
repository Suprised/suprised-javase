package com.suprised.thread.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;


/**
 * 原子的方式更新数组：AtomicIntegerArray AtomicLongArray AtomicReferenceArray(引用类型的数组)
 */
public class AtomicArray {

	static int [] value = new int[] {1, 2};
	// 复制value值，操作不影响原来的value数组
	static AtomicIntegerArray ai = new AtomicIntegerArray(value);
	
	public static void main(String[] args) {
		// AtomicIntegerArray AtomicLongArray AtomicReferenceArray
		System.out.println(ai.getAndSet(0, 3));// 设置指定下标的数组值，返回旧值
		System.out.println(ai.get(0) + "," + value[0]);// 设置后，不影响原来的数组
		
		// 给指定的下标的值加上指定的值，并返回新值
		System.out.println(ai.addAndGet(1, 2));
		System.out.println(ai.get(1));
		
		// 比较下标数组中的值与期望值，如果一致，则修改，否则不变
		System.out.println(ai.compareAndSet(1, 4, 10));
		System.out.println(ai.get(1));// 10
	}
	
}
