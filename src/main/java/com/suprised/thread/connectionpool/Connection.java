package com.suprised.thread.connectionpool;

import com.suprised.utils.Threads;

/**
 * 连接bean
 */
public class Connection {

	public void execute() {
		Threads.sleep(100);
	}

	public void commit() {
		Threads.sleep(20);
	}
	
	public static Connection getConnection() {
		return new Connection();
	}
}
