package com.ktl.moment.utils;

public class StrUtils {

	/**
	 * 判断内容是否为null
	 * @param content
	 * @return
	 */
	public static boolean isEmpty(String content){
		if(content == "" || content == null || content.equals("")){
			return true;
		}
		return false;
	}
	
}
