package com.ktl.moment.android.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktl.moment.R;
import com.ktl.moment.android.activity.HomeActivity;
import com.ktl.moment.android.base.AccountBaseFragment;
import com.ktl.moment.android.component.LoadingDialog;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.User;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.utils.EditTextUtil;
import com.ktl.moment.utils.SharedPreferencesUtil;
import com.ktl.moment.utils.net.ApiManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnFocusChange;
import com.loopj.android.http.RequestParams;

public class LoginFragment extends AccountBaseFragment {

	/******** login ********/

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

	@ViewInject(R.id.login_forget_pass_tv)
	private TextView loginForgetPassTv;

	@ViewInject(R.id.login_register_tv)
	private TextView loginRegistertv;

	@ViewInject(R.id.login_close)
	private ImageView loginClose;

	public OnLoginToRegisterListener onLoginToRegisterListener;
	public OnForgetListener onForgetListener;
	public OnCloseLoginListener onCloseLoginListener;

	public interface OnLoginToRegisterListener {
		void loginToRegisterClick();
	}

	public interface OnForgetListener {
		void forgetClick();
	}

	public interface OnCloseLoginListener {
		void closeLoginClick();
	}

	/**
	 * 设置回调接口
	 * 
	 * @param toRegisterListener
	 */
	public void setOnToRegisterListener(
			OnLoginToRegisterListener onLoginToRegisterListener) {
		this.onLoginToRegisterListener = onLoginToRegisterListener;
	}

	public void setOnToForgetListener(OnForgetListener onForgetListener) {
		this.onForgetListener = onForgetListener;
	}

	public void OnCloseLoginListener(OnCloseLoginListener onCloseLoginListener) {
		this.onCloseLoginListener = onCloseLoginListener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_account_login,
				container, false);
		ViewUtils.inject(this, view);

		init();
		return view;
	}

	public void init() {
		registerEditTextListener();
	}

	public void registerEditTextListener() {
		addTextChange(loginAccountEt, loginDeleteAccountImg);
		addTextChange(loginPassEt, loginDeletePassImg);
	}

	/**
	 * 监听edit text焦点变化事件
	 * 
	 * @param v
	 * @param hasFocus
	 */
	@OnFocusChange({ R.id.login_account_et, R.id.login_pass_et,
			R.id.login_delete_account_text_img,
			R.id.login_delete_pass_text_img, })
	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId()) {
		case R.id.login_account_et:
			focusChange(loginAccountEt, loginDeleteAccountImg, hasFocus);
			break;
		case R.id.login_pass_et:
			focusChange(loginPassEt, loginDeletePassImg, hasFocus);
			break;
		default:
			break;
		}
	}

	/**
	 * 监听点击事件
	 * 
	 * @param v
	 */
	@OnClick({ R.id.login_btn, R.id.login_delete_account_text_img,
			R.id.login_delete_pass_text_img, R.id.login_register_tv,
			R.id.login_forget_pass_tv, R.id.login_close })
	public void onClick(View v) {
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
			SharedPreferencesUtil.getInstance().putString(C.SPKey.SPK_IS_LOGIN,
					"true");
			break;
		case R.id.login_forget_pass_tv:
			if (getActivity() instanceof OnCloseLoginListener) {
				((OnCloseLoginListener) getActivity()).closeLoginClick();
			}
			break;
		case R.id.login_register_tv:
			if (getActivity() instanceof OnLoginToRegisterListener) {
				((OnLoginToRegisterListener) getActivity())
						.loginToRegisterClick();
			}
			break;
		case R.id.login_close:
			if (getActivity() instanceof OnCloseLoginListener) {
				((OnCloseLoginListener) getActivity()).closeLoginClick();
			}
			break;
		default:
			break;
		}
	}

	/***************************************************************************
	 * 手机号登陆开始
	 ***************************************************************************/
	private void mobileLogin() {
		String phone = loginAccountEt.getText().toString().trim();
		String pass = loginPassEt.getText().toString().trim();
		if (C.Account.IS_CHECK_INPUT) {
			if (phone.isEmpty()) {
				toast("请输入手机号");
				return;
			} else if (phone.length() != 11) {
				toast("请输入合法的手机号");
				return;
			}
			if (pass.isEmpty()) {
				toast("请输入密码");
				return;
			}
		}
		final LoadingDialog dialog = new LoadingDialog(getActivity());
		dialog.show();
		RequestParams params = new RequestParams();
		params.put("mobilePhone", phone);
		params.put("password", pass);
		params.put("packname", "");
		params.put("deviceId", "");
		ApiManager.getInstance().post(getActivity(), C.API.USER_LOGIN, params,
				new HttpCallBack() {

					@Override
					public void onSuccess(Object res) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						@SuppressWarnings("unchecked")
						List<User> user = (List<User>) res;
						SharedPreferencesUtil.getInstance().putObject(
								C.SPKey.SPK_LOGIN_INFO, user.get(0));
						
						Intent intent = new Intent(getActivity(),
								HomeActivity.class);
						intent.putExtra("data", user.get(0).getUserId());
						startActivity(intent);
						getActivity().finish();
					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						toast((String) res);
					}
				}, "User");
	}

	/***************************************************************************
	 * 手机号登陆逻辑结束
	 ***************************************************************************/

}
