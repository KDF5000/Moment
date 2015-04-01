/**
 * 登录窗口
 * @author KDF5000
 * @date 2015-3-29
 */
package com.ktl.moment.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.ktl.moment.R;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.utils.net.ApiManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.loopj.android.http.RequestParams;

public class LoginActivity extends BaseActivity {
	private static final String TAG = "LoginActivity";
	
	@ViewInject(R.id.button1)
    private Button button;  //使用第三方库xutils注入
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_login);
		ViewUtils.inject(this);
	}
	
	@OnClick({R.id.button1})
	public void OnClick(View v){
		switch (v.getId()) {
		case R.id.button1:
			mobileLogin();
			break;
		default:
			break;
		}
	}
	/**
	 * 手机号登录
	 */
	private void mobileLogin (){
		RequestParams params = new RequestParams();
		params.put("mobileNumber", "13397266727");
		params.put("password", "123456");
		ApiManager.getInstance().post(this, C.api.USER_LOGIN,params,new HttpCallBack() {
			
			@Override
			public void onSuccess(Object res) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
				intent.putExtra("data", (String)res);
				startActivity(intent);
				finish();
			}
			
			@Override
			public void onFailure(Object res) {
				// TODO Auto-generated method stub
				Toast.makeText(LoginActivity.this, (String)res, 1).show();
			}
		},"User");
	}
	
}
