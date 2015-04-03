package com.ktl.moment.android.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ktl.moment.R;
import com.ktl.moment.android.base.BaseActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class RegisterActivity extends BaseActivity {
	
	@ViewInject(R.id.register_btn)
	private Button registerBtn;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		getLayoutInflater().inflate(R.layout.activity_register, contentLayout);
		
		
		ViewUtils.inject(this);
//		initActivity();
	}
	
	private void initActivity(){
		setMiddleTitleVisible(true);
		setMiddleTitleName(R.string.register);
		setBaseActivityBgColor(getResources().getColor(R.color.register_background_color));
	}
	
	@OnClick({R.id.register_btn})
	public void doClick(View v){
		switch (v.getId()) {
		case R.id.register_btn:
				
			break;
		}
	}
}
	
