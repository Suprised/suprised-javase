package com.suprised.spring.aop;

import java.util.Arrays;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 通知点(切点所做的操作)
 */
@Aspect
@Component
public class Advice {

	// 定义一个切点
	@Pointcut("execution(* com.suprised.spring.aop.AOPMain.execute(..))") 
	public void execute() {
	}

	@Before(value = "execute()")
	public void beforeOpt2() {
		System.out.println("方法执行之前操作2...");
	}
	
	// 使用上面的切点 相对于beforeOpt2() 先执行。
	@Before(value = "execute()")
	public void beforeOpt(JoinPoint joinpoint) {
		System.out.println("Joinpoint 属性begin------------------------");
		System.out.println(joinpoint.getKind());
		System.out.println(joinpoint.toLongString());
		System.out.println(joinpoint.toShortString());
		System.out.println(joinpoint.toString());
		System.out.println(ToStringBuilder.reflectionToString(joinpoint.getTarget()));
		System.out.println(ToStringBuilder.reflectionToString(joinpoint.getThis()));
		System.out.println(Arrays.toString(joinpoint.getArgs()));
		System.out.println(ToStringBuilder.reflectionToString(joinpoint.getSourceLocation()));
		System.out.println(joinpoint.getSignature().getName()); // 方法名
		System.out.println(joinpoint.getSignature().getDeclaringType().getSimpleName()); // 类名
		System.out.println(joinpoint.getSignature().getDeclaringTypeName()); // 包名+类名 
		System.out.println(ToStringBuilder.reflectionToString(joinpoint.getSignature()));
		System.out.println(ToStringBuilder.reflectionToString(joinpoint.getStaticPart()));
		System.out.println("Joinpoint 属性end------------------------");
		System.out.println("方法执行之前操作...");
	}
	
	// 在方法执行后调用，出现异常也调用
	@After(value = "execute()")
	public void afterOpt() {
		System.out.println("方法执行之后操作...");
	}
	
	// 在方法执行成功后才调用，出现异常则不调用该方法
	@AfterReturning(value = "execute()", returning="returnValue")
	public void afterReturingOpt(JoinPoint joinpoint, Object returnValue) {
		System.out.println("方法执行成功之后操作...返回值：" + returnValue);
	}
	
	@Around(value = "execute()", argNames="throwing")
	public String aroundOpt(ProceedingJoinPoint joinpoint) {
		System.out.println("方法执行前后各操作一次...");
		String result ;
		try {
			result = (String) joinpoint.proceed(joinpoint.getArgs()); // 必须显示调用该方法，以确保被代理的方法调用
			System.out.println("aroundOpt()方法的返回值：" + result);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		System.out.println("方法执行前后各操作一次...");
		return result;
	}
	
	// 出现异常时调用
	@AfterThrowing(value = "execute()", throwing = "e")
	public void exceptionOpt(Exception e) {
		System.out.println("方法出现异常时操作... 异常：" + e.getMessage());
	}
	
}
