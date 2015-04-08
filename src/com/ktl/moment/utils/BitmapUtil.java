package com.ktl.moment.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapUtil {

	/**
	 * 获取服务器上的图片
	 * @param imgUrl
	 * @return
	 */
	public static Bitmap getBitmapFromNet(String imgUrl){
		Bitmap bitmap = null;
		try {
			URL bitmapUrl = new URL(imgUrl);
			HttpURLConnection imgConn = (HttpURLConnection) bitmapUrl.openConnection();
			imgConn.setDoInput(true);
			imgConn.connect();
			InputStream inputStream = imgConn.getInputStream();
			bitmap = BitmapFactory.decodeStream(inputStream);
			inputStream.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}
}
