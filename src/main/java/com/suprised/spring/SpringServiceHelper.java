package com.suprised.spring;

import org.springframework.context.ApplicationContext;

public class SpringServiceHelper {

	private static ApplicationContext applicationContext ; 

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static void setApplicationContext(ApplicationContext applicationContext) {
		SpringServiceHelper.applicationContext = applicationContext;
	}
	
	public static final <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}
}
