package com.suprised.core.java7;

/**
 * 可变长的参数，使用泛型时消除警告的注解
 * @SafeVarargs 注解只能在static or final 的方法上声明
 * 
 * @author AmyChen
 */
public class SafeVarargsAnnotation {

	public static void main(String[] args) {
		System.out.println(calcParamLength("a", "b", "c"));
	}
	
	@SafeVarargs
	private static <T> int calcParamLength(T ... args) {
		return args == null ? 0 : args.length;
	}
	
	@SafeVarargs
	public final <T> int calc(T ... args) {
		return 0;
	}
}
