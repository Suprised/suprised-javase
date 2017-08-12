package com.suprised.thread.atomic;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 原子更新引用类型： AtomicReference 引用类型 AtomicReferenceFieldUpdater 引用类型的字段
 * AtomicMarkableReference 带有标记位的引用类型
 */
public class AtomicRef {

	static AtomicReference<User> atomicUserRef = new AtomicReference<>();
	
	public static void main(String[] args) {
		// AtomicReference AtomicReferenceFieldUpdater AtomicMarkableReference
		User user = new User("suprised", 27);
		atomicUserRef.set(user);
		User update = new User("amy", 28);
		System.out.println(atomicUserRef.compareAndSet(user, update));// 比较更新
		
		System.out.println(atomicUserRef.get().getName());
		System.out.println(atomicUserRef.get().getAge());
		
	}

	static class User {

		public User(String name, int age) {
			this.name = name;
			this.age = age;
		}
		
		private String name;
		private int age;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

	}
}
