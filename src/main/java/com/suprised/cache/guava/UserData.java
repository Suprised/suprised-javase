package com.suprised.cache.guava;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.collect.Lists;

public class UserData {

	private int id;
	private String name;
	private String password;
	private int age;

	public UserData() {
	}
	
	public UserData(int id ,String name, String password, int age) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	private static List<UserData> users;
	
	static {
		users = Lists.newArrayList();
		for (int i=1; i<=100; i++) {
			users.add(new UserData(i, i + "*userName*" + i, "***passw0rd***", i * 10));
		}
	}
	
	public static UserData get(int id) {
		System.out.println("UserData.get();");
		return users.get(id - 1);
	}
}
