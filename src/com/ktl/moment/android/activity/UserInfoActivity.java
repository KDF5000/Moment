package com.ktl.moment.android.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktl.moment.R;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.User;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.utils.net.ApiManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserInfoActivity extends Activity {

	@ViewInject(R.id.user_info_back)
	private ImageView back;

	@ViewInject(R.id.user_info_avatar)
	private ImageView avatar;

	@ViewInject(R.id.user_info_nickname)
	private TextView nickname;

	@ViewInject(R.id.user_info_sex)
	private ImageView sex;

	@ViewInject(R.id.user_info_signature)
	private TextView signature;

	@ViewInject(R.id.user_info_place)
	private TextView place;

	@ViewInject(R.id.user_info_birthday)
	private TextView birthday;

	private User user;
	private long userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		ViewUtils.inject(this);

		Intent intent = this.getIntent();
		userId = intent.getLongExtra("userId", -1);

		user = new User();
		getData();

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void initUser() {
		if (user == null) {
			return;
		}
		ImageLoader.getInstance().displayImage(user.getUserAvatar(), avatar,
				getDisplayImageOptions());
		nickname.setText(user.getNickName());
		if (user.getSex() == 1) {
			sex.setImageResource(R.drawable.male);
		} else {
			sex.setImageResource(R.drawable.female);
		}
		signature.setText(user.getSignature());
		place.setText(user.getUserArea());
		birthday.setText(user.getBirthday());
	}

	private void getData() {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		ApiManager.getInstance().post(this, C.API.GET_USER_INFO, params,
				new HttpCallBack() {

					@Override
					public void onSuccess(Object res) {
						// TODO Auto-generated method stub
						if (user == null) {
							user = new User();
						}
						@SuppressWarnings("unchecked")
						List<User> list = (List<User>) res;
						user = list.get(0);
						initUser();
					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub

					}
				}, "User");
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
