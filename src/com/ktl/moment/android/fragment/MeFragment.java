package com.ktl.moment.android.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktl.moment.R;
import com.ktl.moment.android.activity.SettingActivity;
import com.ktl.moment.android.base.BaseFragment;
import com.ktl.moment.android.component.CircleImageView;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.User;
import com.ktl.moment.utils.SharedPreferencesUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MeFragment extends BaseFragment {
	private static final String TAG = "MeFragment";

	@ViewInject(R.id.me_user_avatar)
	private CircleImageView userAvatar;
	
	@ViewInject(R.id.me_sex)
	private ImageView sex;
	
	@ViewInject(R.id.me_user_nickname)
	private TextView nickname;
	
	@ViewInject(R.id.me_signature)
	private TextView signature;

	@ViewInject(R.id.me_setting_layout)
	private LinearLayout meSttingLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_me, container, false);
		ViewUtils.inject(this, view);
		initView();

		return view;
	}

	private void initView() {
		List<User> user = SharedPreferencesUtil.getInstance().getList(
				C.SPKey.SPK_QQ_LOGIN_INFO);
		ImageLoader.getInstance().displayImage(user.get(0).getUserAvatar(),
				userAvatar, getDisplayImageOptions());
		nickname.setText(user.get(0).getNickName());
		if(user.get(0).getSex() == 0){
			sex.setImageResource(R.drawable.female);
		}else{
			sex.setImageResource(R.drawable.male);
		}
		signature.setText(user.get(0).getSignature());
	}

	@OnClick({ R.id.me_setting_layout })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.me_setting_layout:
			Intent intent = new Intent(getActivity(), SettingActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}
