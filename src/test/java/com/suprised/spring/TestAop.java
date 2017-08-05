package com.suprised.spring;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.suprised.spring.aop.AOPMain;
import com.suprised.spring.aop.interfaceparent.DeclareInterface;
import com.suprised.spring.aop.interfaceparent.EnhanceInterface;

public class TestAop extends TestBase {

	@Autowired
	private AOPMain aopMain ;

	@Autowired
	private DeclareInterface declareInterface;
	
	@Test
	public void testExecuteAop() {
		System.out.println(aopMain.execute(false));
		System.out.println("--------------------------------");
		try {
			System.out.println(aopMain.execute(true)); // 抛出异常
		} catch(Exception e) {
		}
		
		aopMain.exception();
		aopMain.registerUser();
		aopMain.transaction();
	}
	
	@Test
	public void testInterfaceEnhance() { // 接口增强
		EnhanceInterface enhanceInterface = (EnhanceInterface) declareInterface ;
		if (enhanceInterface.register()) {
			declareInterface.execute();
		}
	}
}

