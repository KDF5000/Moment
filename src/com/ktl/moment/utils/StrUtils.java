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
	/**
	 * 截取指定长度的字符串
	 * @param str
	 * @param count
	 * @return
	 */
	public static String subString(String str,int count){
		if(str.length() <= count-2){
			return str;
		}
		return str.substring(0, count-2)+"...";
	}
}
