/**
 * 登录窗口
 * @author KDF5000
 * @date 2015-3-29
 */
package com.ktl.moment.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.User;

public class LoginActivity extends BaseActivity {
	private static final String TAG = "LoginActivity";
	
	@ViewInject(R.id.button1)
    private Button button;  //使用第三方库xutils注入
	
	@ViewInject(R.id.button2)
    private Button button2;  //使用第三方库xutils注入
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_login);
		ViewUtils.inject(this);
	}
	
	@OnClick({R.id.button1,R.id.button2})
	public void OnClick(View v){
		switch (v.getId()) {
		case R.id.button1:
			mobileLogin();
			break;
		case R.id.button2:
			weiboLogin();
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
	
	/***************************************************************************
	 * 微博登陆
	 ***************************************************************************/
	private AuthInfo mAuthInfo;
	/** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
	private Oauth2AccessToken mAccessToken;
	/** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
	private SsoHandler mSsoHandler;
	private UsersAPI mUsersAPI; 
	
	private void weiboLogin(){
		mAuthInfo = new AuthInfo(this, C.ThirdSdk.WEIBO_APP_KEY,
				C.ThirdSdk.WEIBO_REDIRECT_URL, C.ThirdSdk.WEIBO_SCOPE);
		mSsoHandler = new SsoHandler(LoginActivity.this, mAuthInfo);
		mSsoHandler.authorize(new AuthListener());
	}
	/**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     *    该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
	class AuthListener implements WeiboAuthListener {
        
        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            if (mAccessToken.isSessionValid()) {
                mUsersAPI  =  new UsersAPI(LoginActivity.this, C.ThirdSdk.WEIBO_APP_KEY, mAccessToken);
                long uid = Long.parseLong(mAccessToken.getUid());
                mUsersAPI.show(uid, mListener);
            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                String message = "";
                if (!TextUtils.isEmpty(code)) {
                    message = "授权失败" + "\nObtained the code: " + code;
                }
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this, "取消授权", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(LoginActivity.this, 
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
	 /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                // 调用 User#parse 将JSON串解析成User对象
                User user = User.parse(response);
                if (user != null) {
                    Toast.makeText(LoginActivity.this, 
                            "获取User信息成功，用户ID：" + user.id+"  用户昵称:"+user.name, 
                            Toast.LENGTH_LONG).show();
                    
                    ///此处写微博登陆成功逻辑
                    Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            Toast.makeText(LoginActivity.this, info.toString(), Toast.LENGTH_LONG).show();
        }
    };
	/***************************************************************************
	 * 微博登陆结束
	 **************************************************************************/

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	    // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
        if (mSsoHandler != null) {
        	 mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
	}
     
}
