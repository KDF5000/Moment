package com.ktl.moment.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.ktl.moment.android.activity.HomeActivity;
import com.ktl.moment.android.component.LoadingDialog;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.Moment;
import com.ktl.moment.entity.User;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.utils.net.ApiManager;
import com.loopj.android.http.RequestParams;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

@SuppressLint("HandlerLeak")
public class TencentQQUtils {

	private Tencent tencent;
	private Activity activity;

	private static final int FLAG_GET_QQ_USER_INFO_COMPLETE = 0;
	private static final int FLAG_QQ_LOGIN = 1;

	public TencentQQUtils(Activity activity) {
		this.activity = activity;
		tencent = Tencent.createInstance(C.ThirdSdk.QQ_OPEN_FLAT_APP_ID,
				activity.getApplicationContext());
	}

	public Tencent getTencent() {
		return this.tencent;
	}

	/****************************************************************************
	 * QQ Login Start
	 * **************************************************************************/
	/**
	 * 获取用户qq信息
	 */
	private void getQQUserInfo() {
		if (tencent != null && tencent.isSessionValid()) {
			IUiListener listener = new IUiListener() {

				@Override
				public void onError(UiError error) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onComplete(final Object response) {
					// TODO Auto-generated method stub
					// UI改版后这里没有任何回调数据，所以直接进行页面跳转，后续如果需要上传需要的数据，直接完善此块注释代码即可
					Message msg = new Message();
					msg.obj = response;
					msg.what = FLAG_GET_QQ_USER_INFO_COMPLETE;
					handler.sendMessage(msg);
					// /**
					// *
					// 这里为了避免对不安全的UI线程操作引起异常发生,将该线程与handler线程（更新UI需要通知主线程来更新）分开
					// */
					// new Thread(){
					// @Override
					// public void run() {
					// // TODO Auto-generated method stub
					// JSONObject jsonObject = (JSONObject) response;
					// // if(jsonObject.has("figureurl")){
					// // Bitmap bitmap = null;
					// // bitmap =
					// BitmapUtil.getBitmapFromNet(jsonObject.optString("figureurl_qq_2"));
					// // Message msg = new Message();
					// // msg.obj = bitmap;
					// // msg.what = 1;
					// // handler.sendMessage(msg);
					// // }
					// }
					// }.start();
				}

				@Override
				public void onCancel() {
					// TODO Auto-generated method stub
				}
			};
			UserInfo userInfo = new UserInfo(activity, tencent.getQQToken());
			userInfo.getUserInfo(listener);
		}
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == FLAG_GET_QQ_USER_INFO_COMPLETE) {
				final JSONObject jsonObject = (JSONObject) msg.obj;
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						String nickName = jsonObject.optString("nickname");
						int gender = 1;
						String sex = jsonObject.optString("gender");
						if (sex.equals("男")) {
							gender = 1;
						} else if (sex.equals("女")) {
							gender = 0;
						}
						String avatarUrl = jsonObject
								.optString("figureurl_qq_2");
						String openId = "qq" + tencent.getOpenId();

						RequestParams params = new RequestParams();
						params.put("identifier", openId);
						params.put("logintype", C.ThirdLoginType.WEIXIN_LOGIN);
						params.put("nickName", nickName);
						params.put("userAvatar", avatarUrl);
						params.put("sex", gender);

						Message msg = new Message();
						msg.obj = params;
						msg.what = FLAG_QQ_LOGIN;
						handler.sendMessage(msg);
					}
				}).start();

			} else if (msg.what == FLAG_QQ_LOGIN) {
				RequestParams params = (RequestParams) msg.obj;
				thirdPartyLogin(params);
			}
		}
	};

	/**
	 * 获取用户授权登陆token
	 * 
	 * @param jsonObject
	 */
	private void initOpenIdAndToken(JSONObject jsonObject) {
		try {
			String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
			String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
			String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
			if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
					&& !TextUtils.isEmpty(openId)) {
				tencent.setAccessToken(token, expires);
				tencent.setOpenId(openId);
			}
		} catch (Exception e) {
		}
	}

	IUiListener tencentListener = new BaseUiListener() {
		@Override
		protected void doComplete(JSONObject values) {
			ToastUtil.show(activity, "login");
			initOpenIdAndToken(values);
			getQQUserInfo();
		};
	};

	public void qqLogin() {
		if (!tencent.isSessionValid()) {
			tencent.login(activity, "all", tencentListener);
		} else {
			tencent.logout(activity);
			getQQUserInfo();
		}
	}

	/**
	 * 实现IUiListener接口的基类
	 * 
	 * @author HUST_LH
	 * 
	 */
	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(Object response) {
			// TODO Auto-generated method stub
			if (response == null) {
				return;
			}
			JSONObject jsonResponse = (JSONObject) response;
			if (null != jsonResponse && jsonResponse.length() == 0) {
				return;
			}
			doComplete(jsonResponse);
		}

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub
			ToastUtil.show(activity, "取消授权");
		}

		@Override
		public void onError(UiError error) {
			// TODO Auto-generated method stub
			ToastUtil.show(activity, "授权失败");
		}

		protected void doComplete(JSONObject values) {
			// TODO Auto-generated method stub
		}
	}

	/****************************************************************************
	 * QQ Login End
	 * **************************************************************************/

	/**
	 * 第三方登陆请求
	 * 
	 * @param params
	 */
	public void thirdPartyLogin(RequestParams params) {
		final LoadingDialog dialog = new LoadingDialog(activity);
		dialog.show();
		ApiManager.getInstance().post(activity, C.API.USER_THIRD_PARTY_LOGIN,
				params, new HttpCallBack() {

					@Override
					public void onSuccess(Object res) {
						// TODO Auto-generated method stub
						/**
						 * 登陆成功跳转
						 */
						// 将用户基本信息写入SP
						@SuppressWarnings("unchecked")
						List<User> user = (List<User>) res;
						SharedPreferencesUtil.getInstance().putObject(
								C.SPKey.SPK_LOGIN_INFO, user.get(0));

						Intent intent = new Intent(activity, HomeActivity.class);
						activity.startActivity(intent);
						activity.finish();
						dialog.dismiss();
					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						ToastUtil.show(activity, (String) res);
					}
				}, "User");
	}

	/**
	 * share moment to QQ friends
	 * 
	 * @param params
	 */
	public void shareToQQFriends(Moment moment) {
		Bundle params = new Bundle();
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
				QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		params.putString(QQShare.SHARE_TO_QQ_TITLE, moment.getTitle());// 分享的标题
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY, moment.getContent());// 分享的内容
		params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, moment.getMomentImgs());// 分享的图片
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,
				"http://www.hao123.com/?tn=90681109_hao_pg");// 被点击时跳转的页面，即用户浏览时看到的页面
		params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "灵动");
		doShareAsync(params, "qqFriends");
	}

	/**
	 * share moment to Qzone
	 * 
	 * @param params
	 */
	public void shareToQzone(Moment moment) {
		Bundle params = new Bundle();
		params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
				QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
		params.putString(QzoneShare.SHARE_TO_QQ_TITLE, moment.getTitle());
		params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, moment.getContent());
		params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,
				"http://www.hao123.com/?tn=90681109_hao_pg");// 必填
		params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL,
				new ArrayList<String>());//暂时这样写，这行不能删除，否则分享至Qzone不能正常工作
		doShareAsync(params, "qzone");
	}

	/**
	 * 执行异步分享
	 * 
	 * @param params
	 */
	private void doShareAsync(final Bundle params, final String shareType) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (shareType.equals("qqFriends")) {
					tencent.shareToQQ(activity, params, tencentListener);
				}
				if (shareType.equals("qzone")) {
					tencent.shareToQzone(activity, params, tencentListener);
				}
			}
		}).start();
	}
}
