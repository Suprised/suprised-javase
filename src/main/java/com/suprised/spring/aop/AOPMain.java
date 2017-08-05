package com.suprised.spring.aop;

import org.springframework.stereotype.Component;

/**
 * 主体类
 */
@Component
public class AOPMain {

	
	public String execute(boolean throwing) {
		if (throwing) {
			throw new RuntimeException("throwing Exception");
		}
		return "executeAOP....";
	}
	
	public void transaction() {
		System.out.println("transaction()...");
	}
	
	public void registerUser() {
		System.out.println("registerUser().....");
	}
	
	public void exception() {
		System.out.println("exception()....");
	}
	
}
