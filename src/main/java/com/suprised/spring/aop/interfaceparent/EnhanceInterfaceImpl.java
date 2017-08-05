package com.suprised.spring.aop.interfaceparent;

import org.springframework.stereotype.Component;

@Component
public class EnhanceInterfaceImpl implements EnhanceInterface {

	@Override
	public boolean register() {
		System.out.println("增强方法：register()...");
		return true;
	}

}
