package com.ktl.moment.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ktl.moment.R;
import com.ktl.moment.android.component.pullzoomview.PullToZoomListViewEx;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserPageActivity extends Activity {
	@ViewInject(R.id.user_page_list_view)
	private PullToZoomListViewEx userPageListView;

	@ViewInject(R.id.userpage_back_iv)
	private ImageView backIv;// 后退

	@ViewInject(R.id.userpage_more_iv)
	private ImageView moreIv;// 更多

	@ViewInject(R.id.userpage_avatar_iv)
	private ImageView userAvatar;// 头像

	@ViewInject(R.id.userpage_nickname_tv)
	private ImageView userNickName;// 昵称

	@ViewInject(R.id.userpage_set_iv)
	private ImageView userSex;// 性别

	@ViewInject(R.id.userpage_signature_tv)
	private ImageView userSignature;// 个人签名

	@ViewInject(R.id.userpage_cancel_ly)
	private LinearLayout cancelLy;// 取消关注

	@ViewInject(R.id.userpage_sendmsg_ly)
	private LinearLayout sendMsgLy;// 私信

	@Override
	protected void onCreate(Bundle saveInstanceBundle) {
		// TODO Auto-generated method stub
		super.onCreate(saveInstanceBundle);
		setContentView(R.layout.activity_user_page);
		ViewUtils.inject(this);

		String[] adapterData = new String[] {};

		userPageListView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, adapterData));

		userPageListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Log.e("zhuwenwu", "position = " + position);
					}
				});
		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
		int mScreenHeight = localDisplayMetrics.heightPixels;
		int mScreenWidth = localDisplayMetrics.widthPixels;
		AbsListView.LayoutParams localObject = new AbsListView.LayoutParams(
				mScreenWidth, (int) (16.0F * (mScreenWidth / 20.0F)));
		userPageListView.setHeaderLayoutParams(localObject);

		// 设置头像
		ImageLoader.getInstance().displayImage(
				"http://7sbpmg.com1.z0.glb.clouddn.com/1.jpg", userAvatar,
				getDisplayImageOptions());
	}

	@OnClick({ R.id.userpage_back_iv, R.id.userpage_cancel_ly,
			R.id.userpage_sendmsg_ly })
	public void OnClick(View v) {
		switch (v.getId()) {
		case R.id.userpage_back_iv:
			finish();
			break;
		case R.id.userpage_cancel_ly:
			Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
			break;
		case R.id.userpage_sendmsg_ly:
			Intent intent = new Intent(this, MsgActivity.class);
			intent.putExtra("userName", "TEST");
			intent.putExtra("userId", 0);
			startActivity(intent);
			break;
		}
	}

	private DisplayImageOptions getDisplayImageOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.default_img)
				.showImageForEmptyUri(R.drawable.default_img)
				.showImageOnFail(R.drawable.default_img).cacheOnDisk(true)
				.cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565)
				.considerExifParams(true).build();
		return options;
	}
}
