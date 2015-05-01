package com.ktl.moment.utils.db;

import android.os.Handler;
import android.os.Message;

import com.ktl.moment.android.base.BaseActivity;

public class DbTaskHandler extends Handler {
	BaseActivity uiActivity = null;
	public DbTaskHandler(BaseActivity uiActivity ) {
		// TODO Auto-generated constructor stub
		this.uiActivity = uiActivity;
	}

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		if(this.uiActivity != null){
			this.uiActivity.OnDbTaskComplete(msg);
		}
	}

}
