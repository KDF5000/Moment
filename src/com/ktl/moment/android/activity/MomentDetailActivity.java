package com.ktl.moment.android.activity;

import android.os.Bundle;

import com.ktl.moment.R;
import com.ktl.moment.android.base.BaseActivity;

public class MomentDetailActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		initView();
	}
	
	private void initView(){
		getLayoutInflater().inflate(R.layout.activity_moment_detail, contentLayout, true);
		setMiddleTitleVisible(true);
		setTitleBackImgVisible(true);
		setBaseActivityBgColor(getResources().getColor(R.color.main_title_color));
		setMiddleTitleName(R.string.moment_detail_text_view);
	}

}
