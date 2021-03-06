package com.ktl.moment.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;

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
	
	/**
	 * 字符串转换成图片
	 * 
	 * @param str
	 * @return
	 */
	public static Bitmap createBitmap(String str) {
		int len = str.length();
		Bitmap bp = Bitmap.createBitmap(40*len+60, 60, Config.ARGB_8888); // 画布大小
		Canvas c = new Canvas(bp);
		Paint paint1 = new Paint();
		paint1.setColor(Color.BLACK);
		c.drawColor(Color.parseColor("#41aaf2"));// 画布颜色 

		Paint paint2 = new Paint();// 画姓名前边的间隔
		paint2.setColor(Color.WHITE);
		paint2.setStrokeWidth(1f);
		c.drawLine(0, 0, 0, 60, paint2);

		Paint paint = new Paint();
		paint.setTextSize(36);// 字体大小
		paint.setColor(Color.WHITE);// 字体大小
		paint.setFakeBoldText(true); // 粗体
		paint.setTextSkewX(0);// 斜度
		paint.setTextAlign(Paint.Align.CENTER);
		c.drawText(str, 20*len+5, 42, paint);// 文字位置
		c.save(Canvas.ALL_SAVE_FLAG);// 保存
		c.restore();//
		
		//画取消按钮
		Paint paint3 = new Paint();// 画姓名前边的间隔
		paint3.setColor(Color.parseColor("#dcdcdc"));
		paint3.setStrokeWidth(3f);
		c.drawLine(len*40 + 15, 15, len*40 + 40, 40, paint3);
		c.drawLine(len*40 + 40, 15,len*40 + 15, 40, paint3);
		return bp;
	}

	/**
	 * 生成圆角图片
	 * @param bitmap
	 * @return
	 */
	public static Bitmap GetRoundedCornerBitmap(Bitmap bitmap) {
		try {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
					bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());
			final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight()));
			final float roundPx = 20;
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(Color.BLACK);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

			final Rect src = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());

			canvas.drawBitmap(bitmap, src, rect, paint);
			return output;
		} catch (Exception e) {
			return bitmap;
		}
	}
	
	/**
	 * 根据一定的模式找到字符串W
	 * @param pattern
	 * @param str
	 * @return
	 */
	public static List<String> extractString(String pattern,String str){
		Pattern patternReg = Pattern.compile(pattern);
		Matcher matcher = patternReg.matcher(str);
	    List<String> strList = new ArrayList<String>();
		while(matcher.find()){
			String name = matcher.group(1);
			strList.add(name);
		}
		return strList;
	}
	/**
	 * 知道对象在list中的位置
	 * @param list
	 * @param obj
	 * @return
	 */
	public static <T> int findPositionInList(List<T> list,Object obj){
		if(list==null){
			return -1;
		}
		for(int i=0;i<list.size();i++){
			if(list.get(i) == obj || list.get(i).equals(obj)){
				return i;
			}
		}
		return -1;
	}
}
