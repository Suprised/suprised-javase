package com.suprised.thread.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.suprised.utils.Threads;

/**
 * Condition接口依赖Lock对象，和Lock对象配合使用可以实现等待/通知模式
 * 
 * Condition方法被调用前需要获取锁
 */
public class ConditionThread {
	
	// 下面的例子：
	// 有一个队列，如果队列满了，则添加队列的操作进入等待状态等待移除，
	// 如果队列为空，则移除的操作进入等待状态等待添加。
	public static void main(String[] args) throws Exception {
		// 20个值的队列
		final BoundeQueue<Integer> queue = new BoundeQueue<>(20);
		
		// 添加10个值，然后删除20个，再加10个
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				for (int i=0; i<10; i++) {
					try {
						queue.addItem(i);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}, "1").start();
		Threads.sleep(200);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				for (int i=0; i<20; i++) {
					try {
						System.out.println(queue.removeItem());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}, "2").start();
		Threads.sleep(200);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				for (int i=10; i<20; i++) {
					try {
						queue.addItem(i);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}, "3").start();
		
		Threads.sleep(2000);
	}
}

/**
 * 演示队列
 */
class BoundeQueue<T> {
	
	private Object[] items; // 队列中用数组
	// 添加的下标，删除的下标和数组中当前数量
	private int addIndex, removeIndex, count;
	// 声明锁和条件
	private Lock lock = new ReentrantLock();
	private Condition notEmpty = lock.newCondition();
	private Condition notFull = lock.newCondition();
	
	public BoundeQueue(int size) {
		items = new Object[size];
	}
	
	// 添加一个元素，如果数组满了，则添加线程进入等待，直到数组有位置可以加。
	// 添加成功，则通知删除的线程
	public void addItem(T t) throws InterruptedException {
		lock.lock();
		try {
			while (count == items.length) {// 数组满了
				notFull.await();// 释放锁，进入等待
			}
			// System.out.println("addIndex=" + addIndex);
			items[addIndex++] = t;// 设置值
			if (addIndex == items.length) {
				addIndex = 0; // 如果加满，则重置索引
			}
			count++;
			notEmpty.signal();// 通知等待删除的线程
		} finally {
			lock.unlock();
		}
	}
	
	// 从头部删除一个元素，如果数组为空，则删除线程进入等待，直到数组中有值。
	public T removeItem() throws InterruptedException {
		lock.lock();
		try {
			while (count == 0) {
				notEmpty.await();// 为空，则进入等待
			}
			// System.out.println("removeIndex = " + removeIndex);
			Object x = items[removeIndex++];
			if (removeIndex == items.length) {
				removeIndex = 0;// 删完重置
			}
			count--;
			notFull.signal();// 删除成功，通知添加的线程。
			return (T) x ;
		} finally {
			lock.unlock();
		}
	}
}

// condition的简单使用
class ConditionTest {
	
	private Lock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();// 需要通过Lock对象调用
	
	public void conditionWait() throws InterruptedException {
		lock.lock();
		try {
			condition.await();// 线程等待，并释放锁：类似Object.wait();
		} finally {
			lock.unlock();
		}
	}
	
	public void conditionSignal() throws InterruptedException {
		lock.lock();
		try {
			condition.signal();// 唤醒等待的线程
		} finally {
			lock.unlock();
		}
	}
}
