package com.suprised.core.java7;

/**
 * try语句管理自动关闭资源
 * 
 * 能够被try语句管理的资源需要实现AutoCloseable接口
 * 
 * @author AmyChen
 *
 */
public class AutoCloseResource implements AutoCloseable {

	@Override
	public void close() throws Exception {
		System.out.println("资源关闭操作！！！");
	}
	
	public void process() {
		System.out.println("使用资源");
	}
	
	public static void main(String[] args) {
		try (AutoCloseResource  resouce = new AutoCloseResource()) {
			resouce.process();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
