package com.ktl.moment.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ktl.moment.R;
import com.ktl.moment.android.base.BaseActivity;
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

	// 这几个布尔变量后续需要以配置文件的方式保存，这里只是暂时这样使用
	private boolean isSave = false;
	private boolean isPush = false;
	private boolean isBindingQQ = false;
	private boolean isBindingWeibo = false;
	private boolean isBindingWechat = false;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		getLayoutInflater().inflate(R.layout.activity_setting, contentLayout,
				true);
		initView();
		ViewUtils.inject(this);
	}

	private void initView() {
		setTitleRightTvVisible(true);
		setTitleBackImgVisible(true);
		setMiddleTitleVisible(true);
		setTitleBackImg(R.drawable.title_return_white);
		setMiddleTitleName(R.string.setting_title);
		setBaseActivityBgColor(getResources()
				.getColor(R.color.main_title_color));
	}

	@OnClick({ R.id.setting_logout, R.id.title_back_img, R.id.setting_save,
			R.id.setting_push, R.id.setting_wecaht_binding_img,
			R.id.setting_weibo_binding_img,R.id.setting_qq_binding_img })
	public void click(View v) {
		switch (v.getId()) {
		case R.id.setting_logout:
			Intent intent = new Intent(this, AccountActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.title_back_img:
			finish();
		case R.id.setting_save:
			save();
			break;
		case R.id.setting_push:
			push();
			break;
		case R.id.setting_weibo_binding_img:
			weiboBinding();
			break;
		default:
			break;
		}
	}

	private void save() {
		if (!isSave) {
			save.setImageResource(R.drawable.setting_open);
			isSave = true;
		} else {
			save.setImageResource(R.drawable.setting_close);
			isSave = false;
		}
	}

	private void push() {
		if (!isPush) {
			push.setImageResource(R.drawable.setting_open);
			isPush = true;
		} else {
			push.setImageResource(R.drawable.setting_close);
			isPush = false;
		}
	}
	
	private void weiboBinding(){
		if (!isBindingWeibo) {
			weiboBindingImg.setImageResource(R.drawable.setting_binding);
			isBindingWeibo = true;
		} else {
			weiboBindingImg.setImageResource(R.drawable.setting_unbindling);
			isBindingWeibo = false;
		}
	}
}
