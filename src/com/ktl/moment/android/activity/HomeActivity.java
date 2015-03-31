package com.ktl.moment.android.activity;

import android.os.Bundle;

import com.ktl.moment.R;
import com.ktl.moment.android.base.BaseActivity;

public class HomeActivity extends BaseActivity {
	private static final String TAG = "HomeAtivity";
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		getLayoutInflater().inflate(R.layout.activity_home, contentLayout);
		
		initActivity();
	}
	
	private void initActivity(){
		setHomeTitleVisible(true);
		setTitleTvName(R.string.found_text_view);
		setBaseActivityBgColor(getResources().getColor(R.color.found_background_color));
	}
}
