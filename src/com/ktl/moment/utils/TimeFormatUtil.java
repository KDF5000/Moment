package com.ktl.moment.utils;

import android.annotation.SuppressLint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class TimeFormatUtil {
	private static final int THOUSAND = 1000;
	private static final int MINUTE_TIME_UNIT = 60 * THOUSAND;
	private static final int HOUR_TIME_UNIT = 3600 * THOUSAND;
	private static final int DAY_TIME_UNIT = 86400 * THOUSAND;

	/**
	 * 获取当前日期时间 yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getCurrentDateTime() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.format(new Date());
	}

	/**
	 * 将字符串时间转化为毫秒时间
	 * 
	 * @param date
	 * @return
	 */
	private static long string2TimeMillis(String date) {
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c.getTimeInMillis();
	}

	/**
	 * 获取当天00:00的毫秒时间
	 * 
	 * @return
	 */
	private static long getMidnightTimeMillis() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0); // 0表示获取当天开始的时间，24表示获取当天结束时的时间
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}

	/**
	 * 格式化成我们需要的时间格式
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(String date) {
		String formatDate = "";
		long currentTime = System.currentTimeMillis();
		long time = string2TimeMillis(date);
		long timeDiff = (currentTime - time);
		long midnightDiff = currentTime - getMidnightTimeMillis();

		long day = timeDiff / DAY_TIME_UNIT;
		long hour = (timeDiff % DAY_TIME_UNIT) / HOUR_TIME_UNIT;
		long min = ((timeDiff % DAY_TIME_UNIT) % HOUR_TIME_UNIT)
				/ MINUTE_TIME_UNIT;

		if (day == 0 && hour == 0 && min == 0) {
			formatDate = "不到1分钟";
		}
		if (day == 0 && hour == 0 && min > 0 && min <= 10) {
			formatDate = min + "分钟前";
		}
		if (day == 0 && hour == 0 && min > 10) {
			formatDate = "不到1小时";
		}
		if (day == 0 && hour > 0 && timeDiff < midnightDiff) {
			formatDate = hour + "小时前";
		}
		if (timeDiff > midnightDiff && timeDiff < midnightDiff + DAY_TIME_UNIT) {
			formatDate = "昨天";
		}
		if (day > 1 && day <= 7 && timeDiff > midnightDiff + DAY_TIME_UNIT) {
			formatDate = day + "天前";
		}
		if (day > 7) {
			formatDate = new SimpleDateFormat("MM月dd日")
					.format(string2TimeMillis(date));
		}
		return formatDate;
	}

}
