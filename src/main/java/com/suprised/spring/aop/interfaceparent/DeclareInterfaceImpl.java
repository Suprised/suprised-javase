package com.suprised.spring.aop.interfaceparent;

import org.springframework.stereotype.Component;

/**
 * 接口实现
 */
@Component
public class DeclareInterfaceImpl implements DeclareInterface {

	@Override
	public void execute() {
		System.out.println("execute()...");
	}

}
