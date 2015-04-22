package com.ktl.moment.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ktl.moment.R;
import com.ktl.moment.android.activity.SettingActivity;
import com.ktl.moment.android.base.BaseFragment;
import com.ktl.moment.android.component.CircleImageView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MeFragment extends BaseFragment {
	private static final String TAG = "MeFragment";

	@ViewInject(R.id.me_user_avatar)
	private CircleImageView userAvatar;
	
	@ViewInject(R.id.me_setting_layout)
	private LinearLayout meSttingLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_me, container, false);
		ViewUtils.inject(this, view);

		ImageLoader.getInstance().displayImage(
				"http://7sbpmg.com1.z0.glb.clouddn.com/1.jpg", userAvatar,
				getDisplayImageOptions());
		return view;
	}
	
	@OnClick({R.id.me_setting_layout})
	public void onClick(View v){
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
