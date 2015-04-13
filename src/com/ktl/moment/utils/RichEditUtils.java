package com.ktl.moment.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RichEditUtils {
	
	public static Map<String,String> extractImg(String content){
		//通过七牛sdk将文本里含有的图片上传，获取七牛的图片外链
		//<momentimg\s+src="([\w/\\]+)"\s*/>
		String str ="<momentimg src=\"(/[\\w/\\/.]+)\"\\s*/>";
		Pattern pattern = Pattern.compile(str);
		Matcher matcher = pattern.matcher(content);
//	    String res = "";
//	    int start = 0;
	    Map<String, String> fileMap = new HashMap<String, String>();
		while(matcher.find()){
			String localFilePath = matcher.group(1);
			String matchString = matcher.group();
			fileMap.put(matchString, localFilePath);
			
			//将该文件上传到图片存储服务器
			//获得外链
			//替换本地地址
//			res += content.substring(start,matcher.start()) + "<img src=\"" + localFilePath +  " \"/>";
//			start = matcher.end();
		}
//		res += content.substring(start);
		return fileMap;
	}
	
	
}
