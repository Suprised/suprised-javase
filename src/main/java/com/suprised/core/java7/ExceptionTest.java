package com.suprised.core.java7;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * java7支持在catch中捕获多个异常：
 * 
 * try { } catch(Exception A | ExceptionB ab){}
 * 
 * @author AmyChen
 * 
 */
public class ExceptionTest {

	public static void main(String[] args) {
		String filePath = "D:\\git_clone\\pom.xml";
//		catchMuiltException(filePath);
		tryWithResources(filePath);
	}

	/**
	 * catch抓住多个异常
	 */
	private static void catchMuiltException(String filePath) {
		File file = new File(filePath);
		InputStream is = null;
		BufferedReader reader = null;
		try {
			is = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(is));
			String line = "";
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			Integer.parseInt("hello");
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 自动释放资源
	 */
	private static void tryWithResources(String filePath) {
		try (InputStream is = new FileInputStream(filePath);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is))) {
			String line = "";
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
