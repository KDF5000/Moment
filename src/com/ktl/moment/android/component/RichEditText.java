package com.ktl.moment.android.component;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.EditText;

import com.ktl.moment.manager.ImageManager;
import com.ktl.moment.manager.ImageManager.ImgLoadCallback;
import com.ktl.moment.utils.ImageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;

public class RichEditText extends EditText {

	private Context mContext;
	private String path = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/moment/";

	private String mRichText;
	
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
	/**
	 * 对图片进行缩放
	 * @param bgimage
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
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
	
	/**
	 * 
	 * @param bitmap
	 * @param fileName
	 * @throws IOException
	 */
	private void save2File(Bitmap bitmap, String fileName) throws IOException {
		File dirFile = new File(path);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		File myCaptureFile = new File(path + fileName);
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(myCaptureFile));
		bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		bos.flush();
		bos.close();
	}

	/**
	 * 
	 */
	final private Html.ImageGetter httpImgGetter = new Html.ImageGetter() {

		@Override
		public Drawable getDrawable(String source) {
			// TODO Auto-generated method stub
			Drawable drawable = null;
			// 判断SD卡里面是否存在图片文件
			if (new File(source).exists()) {
				// 获取本地文件返回Drawable
				drawable = Drawable.createFromPath(source);
				// 设置图片边界
				//获取控件的左右padding
				int paddingLeft = getPaddingLeft();
				int paddingRight = getPaddingRight();
				int drawableWidth = drawable.getIntrinsicWidth();
				int drawableHeight = drawable.getIntrinsicHeight();
				DisplayMetrics dm = getResources().getDisplayMetrics();
				int showWidth = dm.widthPixels -(paddingLeft+paddingRight);
				int showHeight = (int) (((float)showWidth/(float)drawableWidth) * drawableHeight);
				drawable.setBounds(0, 0,showWidth ,showHeight);
				return drawable;
			} else {
				// 启动新线程下载
				String filePath = path + String.valueOf(source.hashCode());
				if (new File(filePath).exists()) {
					// 获取本地文件返回Drawable
//					drawable = Drawable.createFromPath(filePath);
					drawable = ImageUtils.loadImage(filePath);
					//获取控件的左右padding
					int paddingLeft = getPaddingLeft();
					int paddingRight = getPaddingRight();
					int drawableWidth = drawable.getIntrinsicWidth();
					int drawableHeight = drawable.getIntrinsicHeight();
					DisplayMetrics dm = getResources().getDisplayMetrics();
					int showWidth = dm.widthPixels -(paddingLeft+paddingRight);
					int showHeight = (int) (((float)showWidth/(float)drawableWidth) * drawableHeight);
					drawable.setBounds(0, 0,showWidth ,showHeight);
					return drawable;
				} else {
					ImageManager manager = ImageManager.getInstance();
					manager.setmImgLoadCallback(new ImgLoadCallback() {

						@Override
						public void OnError(FailReason failReason) {
							// TODO Auto-generated method stub
						}

						@Override
						public void OnComplete(String imageUri,
								Bitmap loadedImage) {
							// TODO Auto-generated method stub
							try {
								save2File(loadedImage,String.valueOf(imageUri.hashCode()));
								setText(Html.fromHtml(mRichText, httpImgGetter, null));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
					manager.loadImg(source);
				}
				return drawable;
			}
		}
	};
	
	public void setRichText(String text){
		this.mRichText = text;
		this.mRichText = mRichText.replaceAll("[\\n\\r]", "<br>");
		this.setText(Html.fromHtml(mRichText, httpImgGetter, null));
	}
	
	public String getRichText(){
		String richText = Html.toHtml(getEditableText());
		//过滤掉换行
		richText = richText.replaceAll("(<p dir=\"ltr\">)|(</p>)", "");
		richText = richText.replaceAll("<br>", "\\n\\r");
		return richText;
	}
}
