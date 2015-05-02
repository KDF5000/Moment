package com.ktl.moment.android.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ktl.moment.R;
import com.ktl.moment.android.activity.HomeActivity;
import com.ktl.moment.android.base.AccountBaseFragment;
import com.ktl.moment.android.component.LoadingDialog;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.utils.TencentQQUtils;
import com.ktl.moment.utils.SharedPreferencesUtil;
import com.ktl.moment.utils.ToastUtil;
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

public class StartFragment extends AccountBaseFragment {

	@ViewInject(R.id.start_lump)
	private ImageView startLump;

	@ViewInject(R.id.start_word)
	private ImageView startWords;

	@ViewInject(R.id.start_white_content_layout)
	private LinearLayout startWhiteLayout;

	@ViewInject(R.id.start_login)
	private Button startLogin;

	@ViewInject(R.id.start_register)
	private Button startRegister;

	@ViewInject(R.id.start_wechat_img)
	private ImageView wechatImg;

	@ViewInject(R.id.start_weibo_img)
	private ImageView weiboImg;

	@ViewInject(R.id.start_qq_img)
	private ImageView qqImg;

	public OnLoginListener onLoginListener;
	public OnRegisterListener onRegisterListener;

	private TencentQQUtils tencentQQUtils;

	public interface OnLoginListener {
		public void login();
	}

	public void setOnLoginListener(OnLoginListener onLoginListener) {
		this.onLoginListener = onLoginListener;
	}

	public interface OnRegisterListener {
		public void register();
	}

	public void setOnRegisterListener(OnRegisterListener onRegisterListener) {
		this.onRegisterListener = onRegisterListener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_account_start,
				container, false);
		ViewUtils.inject(this, view);
		init();

		return view;
	}

	public void init() {
		tencentQQUtils = new TencentQQUtils(getActivity());
	}

	@OnClick({ R.id.start_qq_img, R.id.start_weibo_img, R.id.start_wechat_img,
			R.id.start_login, R.id.start_register })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start_weibo_img:
			weiboLogin();
			break;
		case R.id.start_qq_img:
			tencentQQUtils.qqLogin();
			break;
		case R.id.start_login:
			startAnim(true);
			break;
		case R.id.start_register:
			startAnim(false);
			break;
		default:
			break;
		}
	}

	public void startAnim(final boolean isLogin) {
		ToastUtil.show(getActivity(), "login");
		Animation lumpAnim = AnimationUtils.loadAnimation(getActivity(),
				R.anim.lump_move_up);
		Animation wordsAnim = AnimationUtils.loadAnimation(getActivity(),
				R.anim.words_move_up);
		Animation whiteContentAnim = AnimationUtils.loadAnimation(
				getActivity(), R.anim.white_content_move_down);

		startLump.startAnimation(lumpAnim);
		startWords.startAnimation(wordsAnim);
		startWhiteLayout.startAnimation(whiteContentAnim);

		whiteContentAnim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				if (isLogin) {
					if (getActivity() instanceof OnLoginListener) {
						((OnLoginListener) getActivity()).login();
					}
				} else {
					if (getActivity() instanceof OnRegisterListener) {
						((OnRegisterListener) getActivity()).register();
					}
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				ToastUtil.show(getActivity(), "good bye");
			}
		});
	}

	/***************************************************************************
	 * 微博登陆
	 ***************************************************************************/
	private AuthInfo mAuthInfo;
	/** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能 */
	private Oauth2AccessToken mAccessToken;
	/** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
	private SsoHandler mSsoHandler;
	private UsersAPI mUsersAPI;

	private void weiboLogin() {
		mAuthInfo = new AuthInfo(getActivity(), C.ThirdSdk.WEIBO_APP_KEY,
				C.ThirdSdk.WEIBO_REDIRECT_URL, C.ThirdSdk.WEIBO_SCOPE);
		mSsoHandler = new SsoHandler(getActivity(), mAuthInfo);
		mSsoHandler.authorize(new AuthListener());

	}

	/**
	 * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用
	 * {@link SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
	 * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
	 * SharedPreferences 中。
	 */
	class AuthListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			// 从 Bundle 中解析 Token
			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			if (mAccessToken.isSessionValid()) {
				mUsersAPI = new UsersAPI(getActivity(),
						C.ThirdSdk.WEIBO_APP_KEY, mAccessToken);
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
				Toast.makeText(getActivity(), message, Toast.LENGTH_LONG)
						.show();
			}
		}

		@Override
		public void onCancel() {
			Toast.makeText(getActivity(), "取消授权", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(getActivity(), "Auth exception : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
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
							"获取User信息成功，用户ID：" + user.id + "  用户昵称:"
									+ user.name, Toast.LENGTH_LONG).show();

					//此处写微博登陆成功逻辑
					RequestParams params = new RequestParams();
					params.put("identifier", "weibo_"+user.id);
					params.put("logintype", C.ThirdLoginType.WEIBO_LOGIN);
					params.put("nickName", user.name);
					params.put("userAvatar", user.avatar_hd);
					int sex = 1;//未知
					if(user.gender == "f"){
						sex = 0;
					} 
					params.put("sex", sex);
					thirdPartyLogin(params);
				} else {
					Toast.makeText(getActivity(), response, Toast.LENGTH_LONG)
							.show();
				}
			}
		}

		@Override
		public void onWeiboException(WeiboException e) {
			ErrorInfo info = ErrorInfo.parse(e.getMessage());
			Toast.makeText(getActivity(), info.toString(), Toast.LENGTH_LONG)
					.show();
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

	/**
	 * 第三方登陆请求
	 * 
	 * @param params
	 */
	public void thirdPartyLogin(RequestParams params) {
		final LoadingDialog dialog = new LoadingDialog(getActivity());
		dialog.show();
		ApiManager.getInstance().post(getActivity(),
				C.API.USER_THIRD_PARTY_LOGIN, params, new HttpCallBack() {

					@Override
					public void onSuccess(Object res) {
						// TODO Auto-generated method stub
						/**
						 * 登陆成功跳转
						 */
						//保存用户信息
						@SuppressWarnings("unchecked")
						List<User> user = (List<User>) res;
						if(user==null || user.isEmpty()){
							ToastUtil.show(getActivity(), "登陆失败");
							return ;
						}
						SharedPreferencesUtil.getInstance().putObject(
								C.SPKey.SPK_LOGIN_INFO, user.get(0));
						actionStart(HomeActivity.class);
						dialog.dismiss();
					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						ToastUtil.show(getActivity(), (String) res);
					}
				}, "User");
	}

}
