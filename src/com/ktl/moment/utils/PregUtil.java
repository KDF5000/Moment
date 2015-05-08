package com.ktl.moment.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PregUtil {
	
	public static boolean pregImgUrl(String imgUrl) {
		Pattern pattern = Pattern.compile("/^http:\\/\\/*/");
		Matcher matcher = pattern.matcher(imgUrl);
		return matcher.matches();
	}
}
