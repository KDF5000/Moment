package com.ktl.moment.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktl.moment.R;
import com.ktl.moment.entity.User;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		ViewUtils.inject(this);

		Intent intent = this.getIntent();
		Bundle bundle = intent.getBundleExtra("user");
		user = (User) bundle.getSerializable("user");
		initUser();

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
		if (user.getSignature().equals("") || user.getSignature() == null) {
			signature.setText("这个同学他很懒，还没有发表个性签名~");
		} else {
			signature.setText(user.getSignature());
		}
		place.setText(user.getUserArea());
		birthday.setText(user.getBirthday());
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
