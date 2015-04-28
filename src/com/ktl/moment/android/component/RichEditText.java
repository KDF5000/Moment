package com.ktl.moment.android.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

public class RichEditText extends EditText {

	private Context mContext;

	public RichEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	public RichEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	public RichEditText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	/**
	 * 添加一个图片
	 * 
	 * @param bitmap
	 * @param uri
	 */
	public void addImage(Bitmap bitmap, String filePath) {
		Log.i("imgpath", filePath);
		// String pathTag = "<momentimg src=\"" + filePath + "\"/>";
		String pathTag = "<img src=\"" + filePath + "\"/>";
		SpannableString spanString = new SpannableString(pathTag);
		// 获取屏幕的宽高
		int paddingLeft = getPaddingLeft();
		int paddingRight = getPaddingRight();
		int bmWidth = bitmap.getWidth();//图片高度
		int bmHeight = bitmap.getHeight();//图片宽度
		int zoomWidth = getWidth() - (paddingLeft + paddingRight);
		int zoomHeight = (int) (((float)zoomWidth / (float)bmWidth) * bmHeight);
		Bitmap newBitmap = zoomImage(bitmap, zoomWidth,zoomHeight);
		ImageSpan imgSpan = new ImageSpan(mContext, newBitmap);
		spanString.setSpan(imgSpan, 0, pathTag.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		Editable editable = this.getText(); // 获取edittext内容
		int start = this.getSelectionStart(); // 设置欲添加的位置
		editable.insert(start, spanString); // 设置spanString要添加的位置
		this.setText(editable);
		this.setSelection(start, spanString.length());
	}

	private Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
		// 获取这个图片的宽和高
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
				(int) height, matrix, true);
		return bitmap;
	}

}
