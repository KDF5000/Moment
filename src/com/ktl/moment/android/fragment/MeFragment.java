package com.ktl.moment.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktl.moment.R;
import com.ktl.moment.android.activity.EditInfoActivity;
import com.ktl.moment.android.activity.MyFocusActivty;
import com.ktl.moment.android.activity.MsgRemindActivity;
import com.ktl.moment.android.activity.SettingActivity;
import com.ktl.moment.android.activity.WPActivity;
import com.ktl.moment.android.base.BaseFragment;
import com.ktl.moment.android.component.BadgeView;
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

	@ViewInject(R.id.me_my_focus)
	private TextView myFocus;

	@ViewInject(R.id.me_my_fans)
	private TextView myFans;

	@ViewInject(R.id.me_edit_info_layout)
	private LinearLayout meEditInfoLayout;

	@ViewInject(R.id.me_notification_layout)
	private LinearLayout meNotificationLayout;

	@ViewInject(R.id.me_watched_layout)
	private LinearLayout meWatchedLayout;

	@ViewInject(R.id.me_praise_layout)
	private LinearLayout mePraiseLayout;

	private BadgeView msgNotifyBadge;//小红点

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_me, container, false);
		ViewUtils.inject(this, view);
		initView();
		addBadge();

		return view;
	}

	private void initView() {
		User user = (User) SharedPreferencesUtil.getInstance().getObject(
				C.SPKey.SPK_LOGIN_INFO);
		if (user == null) {
			return;
		}
		ImageLoader.getInstance().displayImage(user.getUserAvatar(),
				userAvatar, getDisplayImageOptions());
		nickname.setText(user.getNickName());
		if (user.getSex() == 0) {
			sex.setImageResource(R.drawable.female);
		} else {
			sex.setImageResource(R.drawable.male);
		}
		if (user.getSignature().equals("")) {
			signature.setText("这个童鞋TA很懒，还没有发表个性签名");
		} else {
			signature.setText(user.getSignature());
		}
	}

	@OnClick({ R.id.me_setting_layout, R.id.me_my_focus, R.id.me_my_fans,
			R.id.me_edit_info_layout, R.id.me_notification_layout,
			R.id.me_watched_layout, R.id.me_praise_layout })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.me_setting_layout:
			Intent settingIntent = new Intent(getActivity(),
					SettingActivity.class);
			startActivity(settingIntent);
			break;
		case R.id.me_my_focus:
			Intent focusIntent = new Intent(getActivity(), MyFocusActivty.class);
			focusIntent.putExtra("intentFlag", "focus");
			startActivity(focusIntent);
			break;
		case R.id.me_my_fans:
			Intent fansIntent = new Intent(getActivity(), MyFocusActivty.class);
			fansIntent.putExtra("intentFlag", "fans");
			startActivity(fansIntent);
			break;
		case R.id.me_edit_info_layout:
			Intent editInfoIntent = new Intent(getActivity(),
					EditInfoActivity.class);
			startActivity(editInfoIntent);
			break;
		case R.id.me_notification_layout:
			Intent notificationIntent = new Intent(getActivity(),
					MsgRemindActivity.class);
			startActivity(notificationIntent);
			break;
		case R.id.me_watched_layout:
			Intent watchedIntent = new Intent(getActivity(), WPActivity.class);
			watchedIntent.putExtra("wp", "watch");
			startActivity(watchedIntent);
			break;
		case R.id.me_praise_layout:
			Intent praisedIntent = new Intent(getActivity(), WPActivity.class);
			praisedIntent.putExtra("wp", "praise");
			startActivity(praisedIntent);
			break;
		default:
			break;
		}
	}

	private void addBadge() {
		msgNotifyBadge = new BadgeView(getActivity());
		msgNotifyBadge.setBadgeCount(8);
		msgNotifyBadge.setTargetView(meNotificationLayout);
		msgNotifyBadge.setBadgeMargin(0, 0, 20, 0);
		msgNotifyBadge.setBadgeGravity(Gravity.CENTER|Gravity.RIGHT);
	}
}
