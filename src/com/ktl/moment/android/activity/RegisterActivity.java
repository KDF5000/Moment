package com.ktl.moment.android.activity;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ktl.moment.R;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.common.util.BitmapUtil;
import com.ktl.moment.common.util.QQShareHelper;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

public class RegisterActivity extends BaseActivity {
	
	@ViewInject(R.id.register_btn)
	private Button registerBtn;
	@ViewInject(R.id.user_logo_img)
	private ImageView headImg;

	private QQShareHelper qqShareHelper;
	private static final int FLAG_QQ_LOGIN_SUCCESS = 0;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		getLayoutInflater().inflate(R.layout.activity_register, contentLayout);
		
		qqShareHelper = new QQShareHelper(this);
		
		ViewUtils.inject(this);
		initActivity();
	}
	
	private void initActivity(){
		setMiddleTitleVisible(true);
		setMiddleTitleName(R.string.register);
		setBaseActivityBgColor(getResources().getColor(R.color.register_background_color));
	}
	
	@OnClick({R.id.register_btn})
	public void doClick(View v){
		switch (v.getId()) {
		case R.id.qq_img:
			qqLogin();
			break;
		}
	}
	
	/********************************************QQ Login Start*********************************************/
	/**
	 * 获取用户qq信息
	 */
	private void updateUserInfo(){
		if(qqShareHelper.getTencent() != null && qqShareHelper.getTencent().isSessionValid()){
			IUiListener listener = new IUiListener() {
				
				@Override
				public void onError(UiError error) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onComplete(final Object response) {
					// TODO Auto-generated method stub
					Message msg = new Message();
					msg.obj = response;
					msg.what = 0;
					handler.sendMessage(msg);
					/**
					 * 这里为了避免对不安全的UI线程操作引起异常发生,将该线程与handler线程（更新UI需要通知主线程来更新）分开
					 */
					new Thread(){
						public void run() {
							// TODO Auto-generated method stub
							JSONObject jsonObject = (JSONObject) response;
							if(jsonObject.has("figureurl")){
								Bitmap bitmap = null;
								bitmap = BitmapUtil.getBitmapFromNet(jsonObject.optString("figureurl_qq_2"));
								
								Message msg = new Message();
								msg.obj = bitmap;
								msg.what = 1;
								handler.sendMessage(msg);
							}
						}
					}.start();
				}
				
				@Override
				public void onCancel() {
					// TODO Auto-generated method stub
				}
			};
			UserInfo userInfo = new UserInfo(this,qqShareHelper.getTencent().getQQToken());
			userInfo.getUserInfo(listener);
		}
	}
	
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.what == 0){
				JSONObject jsonObject = (JSONObject) msg.obj;
				String nickName = jsonObject.optString("nickname");
				String gender = "2";
				String sex = jsonObject.optString("gender");
				if(sex.equals("男")){
					gender = "0";
				}else if(sex.equals("女")){
					gender = "1";
				}
				Toast.makeText(RegisterActivity.this, "nickname="+nickName+",gender="+gender, Toast.LENGTH_SHORT).show();
				
			}else if(msg.what == 1){
				Bitmap bitmap = (Bitmap) msg.obj;
				headImg.setImageBitmap(bitmap);
			}
		}
	};
	
	/**
	 * 获取用户授权登陆token
	 * @param jsonObject
	 */
	private void initOpenIdAndToken(JSONObject jsonObject){
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                qqShareHelper.getTencent().setAccessToken(token, expires);
                qqShareHelper.getTencent().setOpenId(openId);
            }
        } catch(Exception e) {
        }
	}
	
	IUiListener qqLoginListener = new BaseUiListener(){
		protected void doComplete(JSONObject values) {
			Toast.makeText(RegisterActivity.this, "login", Toast.LENGTH_SHORT).show();
			initOpenIdAndToken(values);
			updateUserInfo();
		};
	};
	
	private void qqLogin(){
		if(!qqShareHelper.getTencent().isSessionValid()){
			qqShareHelper.getTencent().login(this, "all", qqLoginListener);
		}else{
			qqShareHelper.getTencent().logout(this);
			updateUserInfo();
		}
	}
	
	/**
	 * 实现IUiListener接口的基类
	 * @author HUST_LH
	 *
	 */
	private class BaseUiListener implements IUiListener{

		@Override
		public void onComplete(Object response) {
			// TODO Auto-generated method stub
			if( response == null){
				return;
			}
			JSONObject jsonResponse = (JSONObject) response;
			if(null != jsonResponse && jsonResponse.length() == 0){
				return;
			}
			doComplete(jsonResponse);
		}

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub
		}

		@Override
		public void onError(UiError error) {
			// TODO Auto-generated method stub
		}

		protected void doComplete(JSONObject values) {
			// TODO Auto-generated method stub
		}
	}
	/********************************************QQ Login End*********************************************/
}
