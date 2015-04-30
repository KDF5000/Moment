package com.ktl.moment.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.ktl.moment.R;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.android.fragment.DynamicFragment;

public class WPActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		initView();
		setFragment();
	}

	private void initView() {
		setTitleBackImgVisible(true);
		setTitleBackImg(R.drawable.title_return_white);
		setMiddleTitleVisible(true);
		Intent intent = getIntent();
		String wp = intent.getStringExtra("wp");
		if (wp.equals("watch")) {
			setMiddleTitleName("我的围观");
		} else {
			setMiddleTitleName("我的点赞");
		}
		setBaseActivityBgColor(getResources()
				.getColor(R.color.main_title_color));

		titleBackImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void setFragment() {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		DynamicFragment df = new DynamicFragment();
		ft.replace(R.id.activity_base_content_container, df);
		ft.commit();
	}

	@Override
	public void OnDbTaskComplete(Message res) {
		// TODO Auto-generated method stub
		
	}
}
