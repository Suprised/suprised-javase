package com.suprised.spring.aop.interfaceparent;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareParents;
import org.springframework.stereotype.Component;

/**
 * 接口增强的切面通知  为实现DeclareInterface接口的类，增加EnhanceInterface接口的方法
 * 
 * 使用时需要强转：EnhanceInterface enhanceInterface = (EnhanceInterface)declareInterface;
 */
@Component
@Aspect
public class EnhanceAdvice {

	@DeclareParents(value="com.suprised.spring.aop.interfaceparent.DeclareInterface+",
					defaultImpl = EnhanceInterfaceImpl.class)
	public EnhanceInterface enhanceInterface;
	
}
