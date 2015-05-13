package com.ktl.moment.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ktl.moment.R;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.manager.AppManager;
import com.ktl.moment.utils.SharedPreferencesUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class SettingActivity extends BaseActivity {

	@ViewInject(R.id.setting_logout)
	private Button logout;

	@ViewInject(R.id.title_back_img)
	private ImageView back;

	@ViewInject(R.id.setting_save)
	private ImageView save;

	@ViewInject(R.id.setting_push)
	private ImageView push;

	@ViewInject(R.id.setting_qq_binding_img)
	private ImageView qqBindingImg;

	@ViewInject(R.id.setting_weibo_binding_img)
	private ImageView weiboBindingImg;

	@ViewInject(R.id.setting_wecaht_binding_img)
	private ImageView wechatBindingImg;

	@ViewInject(R.id.setting_about_us_layout)
	private RelativeLayout aboutUsLayout;

	private boolean isBindingWeibo = false;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		getLayoutInflater().inflate(R.layout.activity_setting, contentLayout,
				true);
		ViewUtils.inject(this);

		initView();
	}

	private void initView() {
		setTitleRightTvVisible(true);
		setTitleBackImgVisible(true);
		setMiddleTitleVisible(true);
		setTitleBackImg(R.drawable.title_return_white);
		setMiddleTitleName(R.string.setting_title);
		setBaseActivityBgColor(getResources()
				.getColor(R.color.main_title_color));

		String pushFlag = SharedPreferencesUtil.getInstance().getString(
				C.SPKey.SPK_IS_PUSH);
		if (pushFlag.equals("")) {
			SharedPreferencesUtil.getInstance().putString(C.SPKey.SPK_IS_PUSH,
					"true");// 默认为开启推送
			push.setImageResource(R.drawable.setting_open);
		} else if (pushFlag.equals("false")) {// 用户设置了关闭推送
			push.setImageResource(R.drawable.setting_close);
		} else {// 用户设置开启推送
			push.setImageResource(R.drawable.setting_open);
		}

		String saveDataFlag = SharedPreferencesUtil.getInstance().getString(
				C.SPKey.SPK_IS_SAVE_DATA);
		if (saveDataFlag.equals("")) {
			SharedPreferencesUtil.getInstance().putString(
					C.SPKey.SPK_IS_SAVE_DATA, "false");// 默认关闭省流量模式
			save.setImageResource(R.drawable.setting_close);
		} else if (saveDataFlag.equals("false")) {// 用户设置关闭省流量模式
			save.setImageResource(R.drawable.setting_close);
		} else {// 用户设置开启省流量模式
			save.setImageResource(R.drawable.setting_open);
		}
	}

	@OnClick({ R.id.setting_logout, R.id.title_back_img, R.id.setting_save,
			R.id.setting_push, R.id.setting_wecaht_binding_img,
			R.id.setting_weibo_binding_img, R.id.setting_qq_binding_img,
			R.id.setting_about_us_layout })
	public void click(View v) {
		switch (v.getId()) {
		case R.id.setting_logout:
			SharedPreferencesUtil.getInstance().delete(C.SPKey.SPK_LOGIN_INFO);
			Intent intent = new Intent(this, AccountActivity.class);
			startActivity(intent);
			AppManager.getInstance().finishAll();
			break;
		case R.id.title_back_img:
			finish();
			break;
		case R.id.setting_save:
			save();
			break;
		case R.id.setting_push:
			push();
			break;
		case R.id.setting_weibo_binding_img:
			weiboBinding();
			break;
		case R.id.setting_about_us_layout:
			Intent aboutUsIntent = new Intent(this, AboutUsActivity.class);
			startActivity(aboutUsIntent);
			break;
		default:
			break;
		}
	}

	private void save() {
		String str = SharedPreferencesUtil.getInstance().getString(
				C.SPKey.SPK_IS_SAVE_DATA);
		if (str.equals("false")) {// 原来设置为关闭省流量模式
			save.setImageResource(R.drawable.setting_open);
			SharedPreferencesUtil.getInstance().putString(
					C.SPKey.SPK_IS_SAVE_DATA, "true");
		} else {
			save.setImageResource(R.drawable.setting_close);
			SharedPreferencesUtil.getInstance().putString(
					C.SPKey.SPK_IS_SAVE_DATA, "false");
		}
	}

	private void push() {
		String str = SharedPreferencesUtil.getInstance().getString(
				C.SPKey.SPK_IS_PUSH);
		if (str.equals("false")) {// 原来设置为关闭推送
			push.setImageResource(R.drawable.setting_open);
			SharedPreferencesUtil.getInstance().putString(C.SPKey.SPK_IS_PUSH,
					"true");
		} else {
			push.setImageResource(R.drawable.setting_close);
			SharedPreferencesUtil.getInstance().putString(C.SPKey.SPK_IS_PUSH,
					"false");
		}
	}

	private void weiboBinding() {
		if (!isBindingWeibo) {
			weiboBindingImg.setImageResource(R.drawable.setting_binding);
			isBindingWeibo = true;
		} else {
			weiboBindingImg.setImageResource(R.drawable.setting_unbindling);
			isBindingWeibo = false;
		}
	}

	@Override
	public void OnDbTaskComplete(Message res) {
		// TODO Auto-generated method stub

	}
}
