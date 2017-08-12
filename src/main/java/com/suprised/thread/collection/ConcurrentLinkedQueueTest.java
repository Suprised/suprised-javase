package com.suprised.thread.collection;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 非阻塞的线程安全队列，使用循环CAS的方式实现
 */
public class ConcurrentLinkedQueueTest {

	public static void main(String[] args) {
		ConcurrentLinkedQueue<String> linkedQueue = new ConcurrentLinkedQueue<>();
		
		linkedQueue.add("value1");// 将值插入队列尾部
		linkedQueue.offer("value2");// 将值插入队列尾部
		
		String value = linkedQueue.poll();// 获取并移除此队列的头
		String value2 = linkedQueue.peek();// 获取但不移除此队列的头
		System.out.println("value1".equals(value));
		System.out.println("value2".equals(value2));
		System.out.println(linkedQueue);
		
	}
	
}
