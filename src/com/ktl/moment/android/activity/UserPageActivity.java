package com.ktl.moment.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ktl.moment.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class UserPageActivity extends Activity {

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

		long userId = getIntent().getLongExtra("userId", 0);
		Toast.makeText(UserPageActivity.this, userId + "", Toast.LENGTH_SHORT)
				.show();

	}

	@OnClick({ R.id.userpage_back_iv, R.id.userpage_more_iv,
			R.id.userpage_cancel_ly, R.id.userpage_sendmsg_ly })
	public void OnClick(View v) {
		switch (v.getId()) {
		case R.id.userpage_back_iv:
			finish();
			break;
		case R.id.userpage_more_iv:
			
			break;
		case R.id.userpage_cancel_ly:
			
			break;
		case R.id.userpage_sendmsg_ly:
			
			break;
		}
	}
}
