package com.ktl.moment.android.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;

import com.ktl.moment.R;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.android.fragment.DynamicFragment;

public class WatchedActivity extends BaseActivity{
	
//	private LayoutInflater inflater;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		initView();
		setFragment();
	}
	
	private void initView(){
//		this.inflater = getLayoutInflater();
//		this.inflater.inflate(R.layout.fragment_dynamic, contentLayout, true);
		
		setTitleBackImgVisible(true);
		setTitleBackImg(R.drawable.title_return_white);
		setMiddleTitleVisible(true);
		setMiddleTitleName("我的围观");
		setBaseActivityBgColor(getResources().getColor(R.color.main_title_color));
	}
	
	private void setFragment(){
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		DynamicFragment df = new DynamicFragment();
//		ft.replace(R.id., arg1)
	}
}
