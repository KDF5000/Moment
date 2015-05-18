package com.ktl.moment.android.activity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.ktl.moment.R;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.User;
import com.ktl.moment.utils.SharedPreferencesUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class StartPageActivity extends Activity {
	Class<?> activityClass;
	Class[] paramTypes = { Integer.TYPE, Integer.TYPE };
	Method overrideAnimation = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startpage);

		try {
			activityClass = Class.forName("android.app.Activity");
			overrideAnimation = activityClass.getDeclaredMethod(
					"overridePendingTransition", paramTypes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// 是否登陆了
				User userInfo = (User) SharedPreferencesUtil.getInstance().getObject(
						C.SPKey.SPK_LOGIN_INFO);
				if (userInfo != null) {
					actionStart(HomeActivity.class);
				} else if (userInfo == null) {
					actionStart(AccountActivity.class);
				}
//				Intent i = new Intent(StartPageActivity.this,
//						SplashActivity.class);
//				startActivity(i);
//				finish();
				if (overrideAnimation != null) {
					try {
						overrideAnimation
								.invoke(StartPageActivity.this,
//										android.R.anim.fade_in,
										R.anim.home_page_anim,
										android.R.anim.fade_out);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}, 3000);
	}

	/**
	 * 销毁当前界面跳到指定界面
	 * 
	 * @param classObj
	 */
	protected void actionStart(Class<?> classObj) {
		Intent intent = new Intent(this, classObj);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
		this.finish();
	}
}
