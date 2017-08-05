package com.suprised.thread.connectionpool;

import java.util.LinkedList;

/**
 * 一个简单的数据库连接池的例子：具有获取超时特性
 */
public class ConnectionPool {

	private LinkedList<Connection> pool = new LinkedList<>();
	
	public ConnectionPool(int size) {
		init(size);
	}
	
	/**
	 * 初始化指定大小的连接
	 */
	public void init(int size) {
		if (size <= 0) {
			return;
		}
		for (int i=0; i<size; i++) {
			pool.add(Connection.getConnection());
		}
	}
	
	/**
	 * 归还一个连接对象
	 */
	public void releaseConnection(Connection conn) {
		synchronized (pool) {
			if (conn != null) {
				pool.addLast(conn);// 归还线程
				pool.notifyAll();// 通知等待获取连接的线程
			}
		}
	}
	
	/**
	 * 获取一个连接
	 * 
	 * @param mills 超时时间毫秒
	 */
	public Connection getConnection(long mills) throws InterruptedException {
		synchronized (pool) {
			if (mills <= 0) {// 没有超时时间，一直等到获取到连接
				while(pool.isEmpty()) {
					pool.wait();
				}
				return pool.removeFirst();
			} else {// 到达超时时间就返回
				long future = System.currentTimeMillis() + mills;// 超时时间
				long remaining = mills;// 等待时长
				while (pool.isEmpty() && remaining > 0) {
					pool.wait();
					remaining = future - System.currentTimeMillis();// 在等待时间内
				}
				Connection connection = null;
				if (!pool.isEmpty()) {
					connection = pool.removeFirst();
				}
				return connection;
			}
		}
	}
	
}
