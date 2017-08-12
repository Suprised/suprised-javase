package com.suprised.thread.atomic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * 原子更新字段:AtomicIntegerFieldUpdater(整数字段更新) AtomicLongFieldUpdater(长整型字段的更新)
 * AtomicStampedReference(带有版本号的引用类型)
 */
public class AtmoicField {

	// 原子更新User类的age字段
	static AtomicIntegerFieldUpdater<UserAge> aif = AtomicIntegerFieldUpdater
			.newUpdater(UserAge.class, "age");

	public static void main(String[] args) {
		// AtomicIntegerFieldUpdater AtomicLongFieldUpdater
		// AtomicStampedReference
		UserAge user = new UserAge(27);
		System.out.println(aif.getAndIncrement(user));// 原子+1，返回原值
		System.out.println(aif.get(user));// +1后的值
	}

	static class UserAge {
		public volatile int age;
		
		public UserAge(int age) {
			this.age = age;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

	}
}
