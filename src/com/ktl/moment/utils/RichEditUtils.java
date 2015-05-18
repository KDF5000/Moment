package com.ktl.moment.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RichEditUtils {
	
	public static Map<String,String> extractImg(String content){
		String str ="<img src=\"(/[\\w\\W/\\/.]*)\"\\s*/>";
		Pattern pattern = Pattern.compile(str);
		Matcher matcher = pattern.matcher(content);
	    Map<String, String> fileMap = new HashMap<String, String>();
		while(matcher.find()){
			String localFilePath = matcher.group(1);
			String matchString = matcher.group();
			fileMap.put(matchString, localFilePath);
			
		}
		return fileMap;
	}
	/**
	 * 提取内容摘要
	 * @param content
	 * @return
	 */
	public static String extactAbstract(String content,int count){
//		String str ="<img src = \"(/[\\w/\\/.]+)\"\\s*/>";
//		String abstractContent = content.replaceAll(str, "");
		String abstractContent = content.replaceAll("<img [^>]*[/]{0,1}>","");
		if(abstractContent.length() < count){
			return abstractContent;
		}
		return abstractContent.substring(0, count);
	}
}
