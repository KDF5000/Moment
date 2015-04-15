/*** 
 * activity�Ļ��࣬����ʵ��activity�Ĺ������֣����繫��UI���㲥...
 * @author KDF5000
 * @date 2015-3-29
 */
package com.ktl.moment.android.base;

import java.util.Map;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ktl.moment.R;
import com.ktl.moment.manager.AppManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class BaseActivity extends FragmentActivity {

	protected TextView titleNameTv;
	protected RelativeLayout baseTitleReLayout;
	protected LinearLayout baseActivityLayout;
	protected FrameLayout contentLayout;
	protected TextView middleTitleTv;
	protected ImageView titleBackImg;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		// getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		setContentView(R.layout.activity_base);

		findViews();
		hideAllNavigationInfo();
		AppManager.getInstance().addActivity(this);
	}

	private void findViews() {
		titleNameTv = (TextView) findViewById(R.id.title_left_view_name_tv);
		baseTitleReLayout = (RelativeLayout) findViewById(R.id.activity_base_title_container_layout);
		baseActivityLayout = (LinearLayout) findViewById(R.id.activity_base_layout);
		contentLayout = (FrameLayout) findViewById(R.id.activity_base_content_container);

		middleTitleTv = (TextView) findViewById(R.id.middle_title_tv);
		titleBackImg = (ImageView) findViewById(R.id.title_back_img);
	}

	private void hideAllNavigationInfo() {
		setHomeTitleVisible(false);
		setMiddleTitleVisible(false);
		setTitleBackImgVisible(false);
	}

	protected void setHomeTitleVisible(boolean isVisible) {
		if (isVisible) {
			titleNameTv.setVisibility(View.VISIBLE);
		} else {
			titleNameTv.setVisibility(View.GONE);
		}
	}

	protected void setMiddleTitleVisible(boolean isVisible) {
		if (isVisible) {
			middleTitleTv.setVisibility(View.VISIBLE);
		} else {
			middleTitleTv.setVisibility(View.GONE);
		}
	}

	protected void setTitleBackImgVisible(boolean isVisible) {
		if (isVisible) {
			titleBackImg.setVisibility(View.VISIBLE);
		} else {
			titleBackImg.setVisibility(View.GONE);
		}
	}

	protected void setTitleTvName(int resStringId) {
		titleNameTv.setText(resStringId);
	}

	protected void setMiddleTitleName(int resStringId) {
		middleTitleTv.setText(resStringId);
	}

	protected void setBaseActivityBgColor(int resColorId) {
		baseTitleReLayout.setBackgroundColor(resColorId);
	}

	protected void setBaseContainerBgColor(int resId) {
		contentLayout.setBackgroundColor(resId);
	}

	/**
	 * 销毁当前界面跳到指定界面 参数为map --- map里类型暂定 根据实际过程修改
	 * 
	 * @param classObj
	 * @param params
	 */
	protected void actionStart(Class<?> classObj, Map<String, String> params) {
		Intent intent = new Intent(this, classObj);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Bundle bundle = new Bundle();
		for (String key : params.keySet()) {
			bundle.putString(key, params.get(key));
		}
		intent.putExtra("data", bundle);
		this.startActivity(intent);
		this.finish();
	}

	/**
	 * 销毁当前界面跳到指定界面
	 * 
	 * @param classObj
	 */
	protected void actionStart(Class<?> classObj) {
		Intent intent = new Intent(this, classObj);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
		this.finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		AppManager.getInstance().removeActivity(this);
	}

	/**
	 * 获取指定uri的本地绝对路径
	 * 
	 * @param uri
	 * @return
	 */
	@SuppressWarnings("deprecation")
	protected String getAbsoluteImagePath(Uri uri) {
		// can post image
		String[] proj = { MediaColumns.DATA };
		Cursor cursor = managedQuery(uri, proj, // Which columns to return
				null, // WHERE clause; which rows to return (all rows)
				null, // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)

		int column_index = cursor
				.getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();

		return cursor.getString(column_index);
	}

	public DisplayImageOptions getDisplayImageOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.default_img)
				.showImageForEmptyUri(R.drawable.default_img)
				.showImageOnFail(R.drawable.default_img).cacheOnDisk(true)
				.cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565)
				.considerExifParams(true).build();
		return options;
	}
}
