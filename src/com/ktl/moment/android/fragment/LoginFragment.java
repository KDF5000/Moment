package com.ktl.moment.android.fragment;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ktl.moment.R;
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
import com.ktl.moment.android.activity.HomeActivity;
import com.ktl.moment.android.base.BaseFragment;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.common.util.BitmapUtil;
import com.ktl.moment.common.util.EditTextUtil;
import com.ktl.moment.common.util.QQShareHelper;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.utils.net.ApiManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnFocusChange;
import com.loopj.android.http.RequestParams;

public class LoginFragment extends BaseFragment{
	
	/********login********/
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
    private Button loginBtn;
	
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
	
	@ViewInject(R.id.login_layout)
	private LinearLayout loginLayout;
	
	public ToRegisterListener toRegisterListener;
	public ToForgetListener toForgetListener;
	
	public interface ToRegisterListener{
		void onToRegisterClick();
	}
	
	public interface ToForgetListener{
		void onToForgetClick();
	}

	/**
	 * 设置回调接口
	 * @param toRegisterListener
	 */
	public void setToRegisterListener(ToRegisterListener toRegisterListener){
		this.toRegisterListener = toRegisterListener;
	}
	
	public void setToForgetListener(ToForgetListener toForgetListener){
		this.toForgetListener = toForgetListener;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_account_login, container, false);
		ViewUtils.inject(this, view);
		
		init();
		return view;
	}
	
	public void init(){
		qqShareHelper = new QQShareHelper(getActivity());
		registerEditTextListener();
	}
	
	public void registerEditTextListener(){
		addTextChange(loginAccountEt,loginDeleteAccountImg);
		addTextChange(loginPassEt,loginDeletePassImg);
	}

	/**
	 * 监听edit text焦点变化事件
	 * @param v
	 * @param hasFocus
	 */
	@OnFocusChange({R.id.login_account_et,R.id.login_pass_et,R.id.login_delete_account_text_img,R.id.login_delete_pass_text_img,})
	public void onFocusChange(View v,boolean hasFocus){
		switch (v.getId()) {
		case R.id.login_account_et:
			focusChange(loginAccountEt,loginDeleteAccountImg,hasFocus);
			break;
		case R.id.login_pass_et:
			focusChange(loginPassEt,loginDeletePassImg,hasFocus);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 监听点击事件
	 * @param v
	 */
	@OnClick({R.id.login_btn,R.id.login_weibo_img,R.id.login_qq_img,R.id.login_delete_account_text_img,R.id.login_delete_pass_text_img,
		R.id.login_register_tv,R.id.login_forget_pass_tv})
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.login_delete_account_text_img:
			EditTextUtil.setEditTextEmpty(loginAccountEt);
			break;
		case R.id.login_delete_pass_text_img:
			EditTextUtil.setEditTextEmpty(loginPassEt);
			break;
		case R.id.login_btn:
			toast("mobile login");
			mobileLogin();
			break;
		case R.id.login_weibo_img:
			toast("weibo login");
			weiboLogin();
			break;
		case R.id.login_qq_img:
			toast("qq login");
			qqLogin();
			break;
		case R.id.login_forget_pass_tv:
//			findMyPass();
			break;
		case R.id.login_register_tv:
//			scaleAccountLayout(true);
			toast("you click me");
			if(getActivity() instanceof ToRegisterListener){
				((ToRegisterListener)getActivity()).onToRegisterClick();
			}
			break;
		default:
			break;
		}
	}
	

	
	/***************************************************************************
	 * 手机号登陆开始
	 ***************************************************************************/
	private void mobileLogin (){
		String phone = loginAccountEt.getText().toString().trim();
		String pass = loginPassEt.getText().toString().trim();
		if(phone.length() == 0){
			toast("请输入手机号");
			return;
		}else if(phone.length() != 11){
			toast("请输入合法的手机号");
			return;
		}
		if(pass.length() == 0){
			toast("请输入密码");
			return;
		}
		RequestParams params = new RequestParams();
		Log.i("param", "phone="+phone+",pass="+pass);
		params.put("mobileNumber", phone);
		params.put("password", pass);
		ApiManager.getInstance().post(getActivity().getApplicationContext(), C.api.USER_LOGIN,params,new HttpCallBack() {
			
			@Override
			public void onSuccess(Object res) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),HomeActivity.class);
				intent.putExtra("data", (String)res);
				startActivity(intent);
				getActivity().finish();
			}
			
			@Override
			public void onFailure(Object res) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), (String)res, Toast.LENGTH_SHORT).show();
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
		mAuthInfo = new AuthInfo(getActivity(), C.ThirdSdk.WEIBO_APP_KEY,
				C.ThirdSdk.WEIBO_REDIRECT_URL, C.ThirdSdk.WEIBO_SCOPE);
		mSsoHandler = new SsoHandler(getActivity(), mAuthInfo);
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
                mUsersAPI  =  new UsersAPI(getActivity(), C.ThirdSdk.WEIBO_APP_KEY, mAccessToken);
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
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancel() {
            Toast.makeText(getActivity(), "取消授权", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(getActivity(), 
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
                    Toast.makeText(getActivity(), 
                            "获取User信息成功，用户ID：" + user.id+"  用户昵称:"+user.name, 
                            Toast.LENGTH_LONG).show();
                    
                    ///此处写微博登陆成功逻辑
                    //保存登陆成功的用户信息，然后跳转到主页
                    actionStart(HomeActivity.class);
                } else {
                    Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            Toast.makeText(getActivity(), info.toString(), Toast.LENGTH_LONG).show();
        }
    };
	/***************************************************************************
	 * 微博登陆结束
	 **************************************************************************/

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

					//完成用户头像绘制后跳转至主界面
					actionStart(HomeActivity.class);
				}
				
				@Override
				public void onCancel() {
					// TODO Auto-generated method stub
				}
			};
			UserInfo userInfo = new UserInfo(getActivity(),qqShareHelper.getTencent().getQQToken());
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
				toast("nickname="+nickName+",gender="+gender);
				
//				String uri = jsonObject.optString("figureurl_qq_2");
//				BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
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
			toast("login");
			initOpenIdAndToken(values);
			updateUserInfo();
		};
	};
	
	private void qqLogin(){
		if(!qqShareHelper.getTencent().isSessionValid()){
			qqShareHelper.getTencent().login(getActivity(), "all", qqLoginListener);
		}else{
			qqShareHelper.getTencent().logout(getActivity());
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
			toast("取消授权");
		}

		@Override
		public void onError(UiError error) {
			// TODO Auto-generated method stub
			toast("授权失败");
		}

		protected void doComplete(JSONObject values) {
			// TODO Auto-generated method stub
		}
	}
	/****************************************************************************
	 * QQ Login End
	 * **************************************************************************/

     
	
}
