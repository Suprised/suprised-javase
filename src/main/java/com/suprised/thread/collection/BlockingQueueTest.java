package com.suprised.thread.collection;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 阻塞队列：7种阻塞队列
 * 
 * 每个队列插入和移除都有四种操作，分别是：
 * 抛出异常：add(e), element() 当队列满时，再插入就会抛出异常， 当队列空时，获取元素会抛出异常
 * 返回值： offer(e), poll()  当插入队列元素时，会返回是否插入成功(true/false)，如果是移除方法，则取出元素时，没有时，会返回null
 * 阻塞：    put(e), take() 当队列满时，再插入时，会一直阻塞，直到队列可用或者中断退出，当队列为空时，如果获取元素，同样会一直阻塞，直到队列不为空
 * 超时退出：offer(e, time ,unit), poll(time, unit) 当队列满时，再插入时，会阻塞指定的时间，如果没有插入成功就会退出，获取元素也是一样
 */
public class BlockingQueueTest {
	
	public static void main(String[] args) throws Exception {
		// ArrayBlockingQueue 一个由数组结构组成的有界阻塞队列
		// LinkedBlockingQueue 一个由链表结构组成的有界阻塞队列
		// PriorityBlockingQueue 一个支持优先级排序的无界阻塞队列
		// DelayQueue 一个使用优先级实现的无界阻塞队列：支持延时获取元素
		// SynchronousQueue 一个不存储元素的阻塞队列：插入一个元素后，必须等到一个线程获取
		// LinkedTransferQueue 一个由链表结构组成的无界阻塞队列
		// LinkedBlockingDeque 一个由链表结构组成的双向阻塞队列
		new ArrayBlockingQueueTest().test();
	}

}

// 一个由数组结构组成的有界阻塞队列： 支持阻塞公平性
class ArrayBlockingQueueTest {
	
	public void test() throws Exception {
		// 非公平
		ArrayBlockingQueue<String> blockQueue = new ArrayBlockingQueue<>(200, false);
		
		for (int i=0; i<=200; i++) {
			String value = "index:" + i;
			// blockQueue.add(value);// 队列满时，会抛出异常 i=200时，抛出异常
			// blockQueue.put(value);// 队列满时，阻塞. i=200时，会一直阻塞
			// boolean flag = blockQueue.offer(value);// 队列如果插入成功，则返回true，否则返回false。当i=200时，返回false
			boolean flag = blockQueue.offer(value, 10, TimeUnit.SECONDS);// i==200时，插入的超时时长为10s
			if (!flag) {
				System.out.println("i=" + i + ", result : " + flag);
			}
		}
	}
}
