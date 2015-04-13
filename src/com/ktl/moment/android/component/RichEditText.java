package com.ktl.moment.android.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
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
	 * @param bitmap
	 * @param uri
	 */
	public void addImage(Bitmap bitmap,String filePath) {
		Log.i("imgpath", filePath);
		String pathTag = "<momentimg src=\"" + filePath + "\"/>";
		SpannableString spanString = new SpannableString(pathTag);
		ImageSpan imgSpan = new ImageSpan(mContext, bitmap);
		spanString.setSpan(imgSpan, 0, pathTag.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		Editable editable = this.getText();		//获取edittext内容
		int start = this.getSelectionStart();		//设置欲添加的位置
		editable.insert(start, spanString);		//设置spanString要添加的位置
		this.setText(editable);
		this.setSelection(start, spanString.length());
	}
	
	
}
