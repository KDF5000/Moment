package com.ktl.moment.android.fragment;

import java.util.List;

import android.app.Activity;
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
import com.ktl.moment.android.activity.FocusActivty;
import com.ktl.moment.android.activity.HomeActivity;
import com.ktl.moment.android.activity.MsgRemindActivity;
import com.ktl.moment.android.activity.SettingActivity;
import com.ktl.moment.android.activity.WPActivity;
import com.ktl.moment.android.base.BaseFragment;
import com.ktl.moment.android.component.BadgeView;
import com.ktl.moment.android.component.CircleImageView;
import com.ktl.moment.common.Account;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.User;
import com.ktl.moment.im.entity.XgMessage;
import com.ktl.moment.im.xg.receiver.XgMessageReceiver;
import com.ktl.moment.im.xg.receiver.XgMessageReceiver.OnCustomMessageListener;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.utils.SharedPreferencesUtil;
import com.ktl.moment.utils.net.ApiManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.android.tpush.XGPushManager;

public class MeFragment extends BaseFragment implements OnCustomMessageListener{
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

	@ViewInject(R.id.me_my_focus_num)
	private TextView focusNum;

	@ViewInject(R.id.me_my_fans_num)
	private TextView fansNum;

	@ViewInject(R.id.me_my_watch_num)
	private TextView watchNum;

	@ViewInject(R.id.me_my_praise_num)
	private TextView praiseNum;

	private BadgeView msgNotifyBadge;

	private User user;
	private User spUser;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_me, container, false);
		ViewUtils.inject(this, view);
//		addBadge(8);
		getDataFromServer();
		return view;
	}

	private void initData() {
		if (user == null) {
			return;
		}
		spUser = Account.getUserInfo();// 一些数据从本地获取
		ImageLoader.getInstance().displayImage(spUser.getUserAvatar(),
				userAvatar, getDisplayImageOptions());
		nickname.setText(spUser.getNickName());
		if (spUser.getSex() == 0) {
			sex.setImageResource(R.drawable.female);
		} else {
			sex.setImageResource(R.drawable.male);
		}
		if (spUser.getSignature().equals("")) {
			signature.setText("这个童鞋TA很懒，还没有发表个性签名");
		} else {
			signature.setText(user.getSignature());
		}
		focusNum.setText(user.getAttentionNum() + "");
		fansNum.setText(user.getFansNum() + "");
		watchNum.setText(user.getWatchNum() + "");
		praiseNum.setText(user.getPraiseNum() + "");
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
			Intent focusIntent = new Intent(getActivity(), FocusActivty.class);
			focusIntent.putExtra("intentFlag", "focus");
			startActivity(focusIntent);
			break;
		case R.id.me_my_fans:
			Intent fansIntent = new Intent(getActivity(), FocusActivty.class);
			fansIntent.putExtra("intentFlag", "fans");
			startActivity(fansIntent);
			break;
		case R.id.me_edit_info_layout:
			Intent editInfoIntent = new Intent(getActivity(),
					EditInfoActivity.class);
			startActivityForResult(editInfoIntent,
					C.ActivityRequest.REQUEST_UPDATE_INFO);
			break;
		case R.id.me_notification_layout:
			SharedPreferencesUtil.getInstance().setInt(C.SPKey.SPK_MESSAEG_COUNT, 0);//设置为0
			//移除红点
			if(msgNotifyBadge!=null && msgNotifyBadge.getVisibility() == View.VISIBLE){
				msgNotifyBadge.setVisibility(View.GONE);
			}
			//移除菜单栏的小红点
			((HomeActivity)getActivity()).hideRedDot(C.menu.FRAGMENT_ME_MENU_ID);//隐藏菜单的小红点
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

	private void addBadge(int count) {
		if(msgNotifyBadge == null){
			msgNotifyBadge = new BadgeView(getActivity());
			msgNotifyBadge.setTargetView(meNotificationLayout);
			msgNotifyBadge.setBadgeMargin(0, 0, 20, 0);
			msgNotifyBadge.setBadgeGravity(Gravity.CENTER | Gravity.RIGHT);
		}
		msgNotifyBadge.setBadgeCount(count);
	}

	private void getDataFromServer() {
		RequestParams params = new RequestParams();
		params.put("userId", Account.getUserInfo().getUserId());
		ApiManager.getInstance().post(getActivity(), C.API.GET_MY_INFO, params,
				new HttpCallBack() {

					@Override
					public void onSuccess(Object res) {
						// TODO Auto-generated method stub
						@SuppressWarnings("unchecked")
						List<User> list = (List<User>) res;
						try {
							user = list.get(0);
						} catch (NullPointerException e) {
							// TODO: handle exception
						}
						initData();
					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub

					}
				}, "User");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == C.ActivityRequest.REQUEST_UPDATE_INFO) {
				getDataFromServer();
			}
		}
	}
	/**
	 * 显示消息提醒的条数
	 */
	private void showMessageRedDot(){
		int count = SharedPreferencesUtil.getInstance().getInt(C.SPKey.SPK_MESSAEG_COUNT, 0);
		if(count > 0){
			addBadge(count);
		}
	}
	@Override
	public void OnReceive(XgMessage msg) {
		// TODO Auto-generated method stub
		showMessageRedDot();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		showMessageRedDot();
		XGPushManager.onActivityStarted(getActivity());
		XgMessageReceiver.addCustomMessageListener(this);
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		XGPushManager.onActivityStoped(getActivity());
		XgMessageReceiver.removeCustomMessageListener(this);
	}
}
