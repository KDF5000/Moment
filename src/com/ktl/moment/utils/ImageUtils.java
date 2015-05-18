package com.ktl.moment.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.ktl.moment.android.MomentApplication;

public class ImageUtils {
	/**
	 * 
	 * @param res
	 * @param bmpId
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeBitmapFromRes(Resources res, int bmpId,
			int reqWidth, int reqHeight) {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;// 设置这个，只得到能解析的出来图片的大小，并不进行真正的解析
		BitmapFactory.decodeResource(res, bmpId, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight); // 跟指定大小比较，计算缩放比例
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, bmpId, options);
	}

	/**
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
		return inSampleSize;
	}

	/**
	 * 
	 * @param imgDir
	 * @return
	 */
	public static Drawable loadImage(String imgDir) {
		File imageFile = new File(imgDir);
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(imageFile), null, o);

			// The new size we want to scale to
			final int REQUIRED_SIZE = 400;

			// Find the correct scale value. It should be the power of 2.
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_SIZE
					&& o.outHeight / scale / 2 >= REQUIRED_SIZE)
				scale *= 2;

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(imageFile),
					null, o2);
			ImageView iv = new ImageView(MomentApplication.getApplication());
			iv.setImageBitmap(bitmap);
			return iv.getDrawable();
		} catch (FileNotFoundException e) {

		}
		return null;
	}

	// -------------------压缩图片的方法
	// 参考网址：http://www.cnblogs.com/leizhenzi/archive/2011/05/14/2046431.html
	// public Drawable createThumbnails(String url) {
	// BitmapFactory.Options options = new BitmapFactory.Options();
	// options.inJustDecodeBounds = true;
	// Bitmap bitmap = BitmapFactory.decodeFile(url, options); // 此时返回bm为空
	//
	// options.inJustDecodeBounds = false;
	//
	// // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
	//
	// int be = (int) (options.outHeight / (float) 2);
	// if (be <= 0)
	// be = 1;
	//
	// options.inSampleSize = be;
	//
	// // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
	//
	// bitmap = BitmapFactory.decodeFile(url, options);
	// int w = bitmap.getWidth();
	// int h = bitmap.getHeight();
	// ImageView iv = new ImageView(context);
	// iv.setImageBitmap(bitmap);
	//
	// return iv.getDrawable();
	// }
	
	/**
	 * 
	 * @param inputstream
	 * @return
	 */
	public static Bitmap loadImage(InputStream inputstream) {
		if(inputstream==null){
			return null;
		}
		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(inputstream, null, o);

		// The new size we want to scale to
		final int REQUIRED_SIZE = 400;

		// Find the correct scale value. It should be the power of 2.
		int scale = 1;
		while (o.outWidth / scale / 2 >= REQUIRED_SIZE
				&& o.outHeight / scale / 2 >= REQUIRED_SIZE)
			scale *= 2;

		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		Bitmap bitmap = BitmapFactory.decodeStream(inputstream,
				null, o2);
		return bitmap;
	}

}