package com.suprised.core.java7;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * java7之后支持在switch中支持字符串
 * 
 * 这种匹配方式多次使用时，可以使用枚举来替代。
 * 
 * @author AmyChen
 *
 */
public class SwitchTest {
	
	public static void main(String[] args) {
		System.out.println(getTitle("男","刘金喜"));
	}
	
	private static String getTitle(String gender, String name) {
		if (StringUtils.isBlank(gender))
			return name;
		String title = "";
		switch(gender) {
		case "男":
			title = name + "先生";
			break;
		case "女":
			title = name + "女士";
			break;
		}
		return title;
	}

}
