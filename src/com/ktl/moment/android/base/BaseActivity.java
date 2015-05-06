/*** 
 * activity�Ļ��࣬����ʵ��activity�Ĺ������֣����繫��UI���㲥...
 * @author KDF5000
 * @date 2015-3-29
 */
package com.ktl.moment.android.base;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
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
import com.ktl.moment.utils.db.DBManager;
import com.ktl.moment.utils.db.DbTaskHandler;
import com.ktl.moment.utils.db.DbTaskManager;
import com.ktl.moment.utils.db.DbTaskType;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public abstract class BaseActivity extends FragmentActivity {

	protected TextView titleNameTv;
	protected RelativeLayout baseTitleReLayout;
	protected LinearLayout baseActivityLayout;
	protected FrameLayout contentLayout;
	protected TextView middleTitleTv;
	protected ImageView titleBackImg;
	protected ImageView titleRightImg;
	protected ImageView titleRightImgLeft;
	protected TextView titleRightTv;

	public LinearLayout titleFindTab;
	public TextView titleMiddleRecommend;
	public TextView titleMiddleChannel;

	private DbTaskManager taskManager;

	public ImageView getTitleRightImg() {
		return titleRightImg;
	}

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		// getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		setContentView(R.layout.activity_base);

		findViews();
		hideAllNavigationInfo();
		AppManager.getInstance().addActivity(this);

		// 数据库操作管理类
		taskManager = new DbTaskManager();
		// init db
		DBManager.getInstance().init(this);
	}

	private void findViews() {
		titleNameTv = (TextView) findViewById(R.id.title_left_view_name_tv);
		baseTitleReLayout = (RelativeLayout) findViewById(R.id.activity_base_title_container_layout);
		baseActivityLayout = (LinearLayout) findViewById(R.id.activity_base_layout);
		contentLayout = (FrameLayout) findViewById(R.id.activity_base_content_container);

		middleTitleTv = (TextView) findViewById(R.id.middle_title_tv);
		titleBackImg = (ImageView) findViewById(R.id.title_back_img);

		titleRightImg = (ImageView) findViewById(R.id.title_right_img);
		titleRightTv = (TextView) findViewById(R.id.title_right_tv);
		titleRightImgLeft = (ImageView) findViewById(R.id.title_right_img_left);

		titleFindTab = (LinearLayout) findViewById(R.id.title_find_tab);
		titleMiddleChannel = (TextView) findViewById(R.id.title_middle_channel);
		titleMiddleRecommend = (TextView) findViewById(R.id.title_middle_recommend);

	}

	private void hideAllNavigationInfo() {
		setHomeTitleVisible(false);
		setMiddleTitleVisible(false);
		setTitleBackImgVisible(false);
		setTitleRightImgVisible(false);
		setTitleRightImgLeftVisible(false);
		setMiddleFindTabVisible(false);
		setTitleRightTvVisible(false);
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

	protected void setTitleRightImgVisible(boolean isVisible) {
		if (isVisible) {
			titleRightImg.setVisibility(View.VISIBLE);
		} else {
			titleRightImg.setVisibility(View.GONE);
		}
	}

	protected void setTitleRightImgLeftVisible(boolean isVisible) {
		if (isVisible) {
			titleRightImgLeft.setVisibility(View.VISIBLE);
		} else {
			titleRightImgLeft.setVisibility(View.GONE);
		}
	}

	protected void setTitleRightTvVisible(boolean isVisible) {
		if (isVisible) {
			titleRightTv.setVisibility(View.VISIBLE);
		} else {
			titleRightTv.setVisibility(View.GONE);
		}
	}

	protected void setMiddleFindTabVisible(boolean isVisible) {
		if (isVisible) {
			titleFindTab.setVisibility(View.VISIBLE);
		} else {
			titleFindTab.setVisibility(View.GONE);
		}
	}

	protected void setTitleTvName(int resStringId) {
		titleNameTv.setText(resStringId);
	}

	protected void setTitleTvNameEmpty() {
		titleNameTv.setText("");
	}

	protected void setMiddleTitleName(int resStringId) {
		middleTitleTv.setText(resStringId);
	}

	protected void setMiddleTitleName(CharSequence str) {
		middleTitleTv.setText(str);
	}

	protected void setBaseActivityBgColor(int resColorId) {
		baseTitleReLayout.setBackgroundColor(resColorId);
	}

	protected void setBaseContainerBgColor(int resId) {
		contentLayout.setBackgroundColor(resId);
	}

	protected void setTitleBackImg(int resId) {
		titleBackImg.setImageResource(resId);
	}

	protected void setTitleRightImg(int resId) {
		titleRightImg.setImageResource(resId);
	}

	protected void setTitleRightTv(int resId) {
		titleRightTv.setText(resId);
	}

	protected void setTitleRightTv(CharSequence str) {
		titleRightTv.setText(str);
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

		int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
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

	/**
	 * 数据库任务操作的回调 子类重写
	 * 
	 * @param res
	 * @return
	 */
	public abstract void OnDbTaskComplete(Message res);

	/**
	 * 异步保存数据到数据库
	 * 
	 * @param taskType
	 * @param entityType
	 * @param entities
	 */
	protected void saveDbDataAsync(int taskId, DbTaskType taskType,
			Class<?> entityType, List<?> entities, DbTaskHandler handler) {
		taskManager.addTask(taskId, taskType, entities, entityType, handler);
	}

	/**
	 * 从本地数据库获取数据
	 * 
	 * @param taskType
	 * @param entityType
	 * @param handler
	 */
	protected void getDbDataAsync(int taskId, DbTaskType taskType,
			Class<?> entityType, DbTaskHandler handler) {
		taskManager.addTask(taskId, taskType, null, entityType, handler);
	}

	/**
	 * 从本地数据库获取数据
	 * 
	 * @param taskId
	 * @param taskType
	 * @param entityType
	 * @param builder
	 * @param handler
	 */
	protected void getDbDataAsync(int taskId, DbTaskType taskType,
			Class<?> entityType, WhereBuilder builder, DbTaskHandler handler) {
		taskManager.addTask(taskId, taskType, null, entityType, builder,
				handler);
	}

}
