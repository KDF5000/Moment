/**
 * 登录窗口
 * @author KDF5000
 * @date 2015-3-29
 */
package com.ktl.moment.android.activity;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ktl.moment.R;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.common.util.BitmapUtil;
import com.ktl.moment.common.util.QQShareHelper;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.utils.net.ApiManager;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnFocusChange;
import com.lidroid.xutils.view.annotation.event.OnTouch;
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
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

public class LoginActivity extends BaseActivity {
	private static final String TAG = "LoginActivity";
	@ViewInject(R.id.login_base_layout)
	private LinearLayout loginBaseLayout;
	
	@ViewInject(R.id.login_avatar)
	private ImageView headImg;
	
	@ViewInject(R.id.login_account_et)
	private EditText loginAccountEt;
	
	@ViewInject(R.id.login_pass_et)
	private EditText loginPassEt;
	
	@ViewInject(R.id.login_delete_account_text_img)
	private ImageView loginDeleteAccountImg;
	
	@ViewInject(R.id.login_delete_pass_text_img)
	private ImageView loginDeletePassImg;
	
	@ViewInject(R.id.login_btn)
    private Button loginBtn;  //使用第三方库xutils注入
	
	@ViewInject(R.id.login_wechat_img)
	private ImageView wecahtLoginImg;
	
	@ViewInject(R.id.login_qq_img)
	private ImageView qqLoginImg;
	
	@ViewInject(R.id.login_weibo_img)
	private ImageView weiboLogin;
	
	@ViewInject(R.id.login_forget_pass_tv)
	private TextView loginForgetPassTv;
	
	@ViewInject(R.id.login_register_tv)
	private TextView loginRegistertv;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_login);
		ViewUtils.inject(this);
		
		qqShareHelper = new QQShareHelper(this);
		
		registerEditTextListener();
	}
	
	public void registerEditTextListener(){
		loginAccountEt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(loginAccountEt.length() > 0){
					loginDeleteAccountImg.setVisibility(View.VISIBLE);
				}else{
					loginDeleteAccountImg.setVisibility(View.INVISIBLE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});
		
		loginPassEt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(loginPassEt.length() > 0){
					loginDeletePassImg.setVisibility(View.VISIBLE);
				}else{
					loginDeletePassImg.setVisibility(View.INVISIBLE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	/**
	 * 监听edit text焦点变化事件
	 * @param v
	 * @param hasFocus
	 */
	@OnFocusChange({R.id.login_account_et,R.id.login_pass_et,R.id.login_delete_account_text_img,
		R.id.login_delete_pass_text_img})
	public void onFocusChange(View v,boolean hasFocus){
		switch (v.getId()) {
		case R.id.login_account_et:
			if(hasFocus){
				if(loginAccountEt.length() > 0){
					loginDeleteAccountImg.setVisibility(View.VISIBLE);
				}
			}else{
				loginDeleteAccountImg.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.login_pass_et:
			if(hasFocus){
				if(loginPassEt.length() > 0){
					loginDeletePassImg.setVisibility(View.VISIBLE);
				}
			}else{
				loginDeletePassImg.setVisibility(View.INVISIBLE);
			}
		default:
			break;
		}
	}
	
	/**
	 * 监听触摸事件
	 * @param v
	 * @param e
	 * @return
	 */
	@OnTouch({R.id.login_base_layout})
	public boolean onTouch(View v, MotionEvent e){
		/**
		 * 隐藏软键盘
		 */
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);  
		return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);  
	}
	
	/**
	 * 监听点击事件
	 * @param v
	 */
	@OnClick({R.id.login_btn,R.id.login_weibo_img,R.id.login_qq_img,R.id.login_delete_account_text_img,
		R.id.login_delete_pass_text_img,R.id.login_register_tv,R.id.login_forget_pass_tv})
	public void OnClick(View v){
		switch (v.getId()) {
		case R.id.login_delete_account_text_img:
			loginAccountEt.setText("");
			break;
		case R.id.login_delete_pass_text_img:
			loginPassEt.setText("");
			break;
		case R.id.login_btn:
			Toast.makeText(LoginActivity.this, "mobile login", Toast.LENGTH_SHORT).show();
			mobileLogin();
			break;
		case R.id.login_weibo_img:
			Toast.makeText(LoginActivity.this, "weibo login", Toast.LENGTH_SHORT).show();
			weiboLogin();
			break;
		case R.id.login_qq_img:
			Toast.makeText(LoginActivity.this, "qq login", Toast.LENGTH_SHORT).show();
			qqLogin();
			break;
		case R.id.login_forget_pass_tv:
			findMyPass();
			break;
		case R.id.login_register_tv:
			toRegister();
			break;
		default:
			break;
		}
	}
	
	private void findMyPass(){
		Intent forgetIntent = new Intent(LoginActivity.this,ForgetPassActivity.class);
		startActivity(forgetIntent);
	}
	
	private void toRegister(){
		Intent toRegisterIntent = new Intent(LoginActivity.this,RegisterActivity.class);
		startActivity(toRegisterIntent);
	}
		
	/***************************************************************************
	 * 手机号登陆开始
	 ***************************************************************************/
	private void mobileLogin (){
		RequestParams params = new RequestParams();
		String phone = loginAccountEt.getText().toString();
		String pass = loginPassEt.getText().toString();
		Log.i("param", "phone="+phone+",pass="+pass);
		params.put("mobileNumber", phone);
		params.put("password", pass);
		ApiManager.getInstance().post(this, C.api.USER_LOGIN,params,new HttpCallBack() {
			
			@Override
			public void onSuccess(Object res) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
				intent.putExtra("data", (String)res);
				startActivity(intent);
				finish();
			}
			
			@Override
			public void onFailure(Object res) {
				// TODO Auto-generated method stub
				Toast.makeText(LoginActivity.this, (String)res, Toast.LENGTH_SHORT).show();
			}
		},"User");
	}
	/***************************************************************************
	 * 手机号登陆逻辑结束
	 ***************************************************************************/
	
	/***************************************************************************
	 * 微博登陆
	 ***************************************************************************/
	private AuthInfo mAuthInfo;
	/** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
	private Oauth2AccessToken mAccessToken;
	/** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
	private SsoHandler mSsoHandler;
	private UsersAPI mUsersAPI; 

	private QQShareHelper qqShareHelper;
	private static final int FLAG_QQ_LOGIN_SUCCESS = 0;
	
	private void weiboLogin(){
		mAuthInfo = new AuthInfo(this, C.ThirdSdk.WEIBO_APP_KEY,
				C.ThirdSdk.WEIBO_REDIRECT_URL, C.ThirdSdk.WEIBO_SCOPE);
		mSsoHandler = new SsoHandler(LoginActivity.this, mAuthInfo);
		mSsoHandler.authorize(new AuthListener());
		
		qqShareHelper = new QQShareHelper(this);
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
                    //保存登陆成功的用户信息，然后跳转到主页
                    actionStart(HomeActivity.class);
//                    Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
//                    startActivity(intent);
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
	
	/****************************************************************************
	 * QQ Login Start
	 * **************************************************************************/
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
				Toast.makeText(LoginActivity.this, "nickname="+nickName+",gender="+gender, Toast.LENGTH_SHORT).show();
				
//				String uri = jsonObject.optString("figureurl_qq_2");
//				BitmapUtils bitmapUtils = new BitmapUtils(LoginActivity.this);
//				bitmapUtils.display(headImg, uri);
			}else{
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
			Toast.makeText(LoginActivity.this, "login", Toast.LENGTH_SHORT).show();
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
			Toast.makeText(LoginActivity.this, "取消授权", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onError(UiError error) {
			// TODO Auto-generated method stub
			Toast.makeText(LoginActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
		}

		protected void doComplete(JSONObject values) {
			// TODO Auto-generated method stub
		}
	}
	/****************************************************************************
	 * QQ Login End
	 * **************************************************************************/

     
}
