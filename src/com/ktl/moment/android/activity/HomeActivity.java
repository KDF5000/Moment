package com.ktl.moment.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.ktl.moment.R;
import com.ktl.moment.android.base.BaseActivity;

public class HomeActivity extends BaseActivity {
	private static final String TAG = "HomeAtivity";
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_home);
		Intent intent = getIntent();
		String userName = intent.getStringExtra("userName");
		Toast.makeText(this, "ÓÃ»§Ãû:"+userName, 1).show();
	}
}
