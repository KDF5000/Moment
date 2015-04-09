package com.ktl.moment.android.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktl.moment.R;
import com.ktl.moment.android.activity.AccountActivity;
import com.ktl.moment.android.base.AccountBaseFragment;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.utils.EditTextUtil;
import com.ktl.moment.utils.VerificationUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnFocusChange;

public class RegisterFragment extends AccountBaseFragment{
	
	@ViewInject(R.id.register_account_et)
	private EditText registerAccountEt;
	
	@ViewInject(R.id.register_pass_et)
	private EditText registerPassEt;
	
	@ViewInject(R.id.register_delete_account_text_img)
	private ImageView registerDelAccountImg;
	
	@ViewInject(R.id.register_delete_pass_text_img)
	private ImageView registerDelPassImg;
	
	@ViewInject(R.id.register_next_btn)
	private Button registerNextbtn;
	
	@ViewInject(R.id.register_to_login)
	private TextView registerToLogin;
	
	@ViewInject(R.id.register_close)
	private ImageView registerClose;
	
	private VerificationUtil verification;
	
	public OnCloseRegisterListener onCloseRegisterListener;
	public OnBackToLoginListener onBackToLoginListener;
	public OnNextListener onNextListener;
	
	public interface OnCloseRegisterListener{
		void closeRegisterClick();
	}
	
	public void OnCloseRegisterListener(OnCloseRegisterListener onCloseRegisterListener){
		this.onCloseRegisterListener = onCloseRegisterListener;
	}
	
	public interface OnBackToLoginListener{
		void backToLoginClick();
	}
	
	public void setOnBackToLoginListener(OnBackToLoginListener onBackToLoginListener){
		this.onBackToLoginListener = onBackToLoginListener;
	}
	
	public interface OnNextListener{
		void nextToVerifyClick();
	}
	
	public void setOnNextListener(OnNextListener onNextListener){
		this.onNextListener = onNextListener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_account_register, container, false);
		
		ViewUtils.inject(this, view);
		registerEditTextListener();
		
		return view;
	}
	
	public void registerEditTextListener(){
		addTextChange(registerAccountEt,registerDelAccountImg);
		addTextChange(registerPassEt,registerDelPassImg);
	}

	/**
	 * 监听edit text焦点变化事件
	 * @param v
	 * @param hasFocus
	 */
	@OnFocusChange({R.id.register_account_et,R.id.register_pass_et,R.id.register_delete_account_text_img,R.id.register_delete_pass_text_img})
	public void onFocusChange(View v,boolean hasFocus){
		switch (v.getId()) {
		case R.id.register_account_et:
			focusChange(registerAccountEt,registerDelAccountImg,hasFocus);
			break;
		case R.id.register_pass_et:
			focusChange(registerPassEt,registerDelPassImg,hasFocus);
			break;
		default:
			break;
		}
	}
	
	@OnClick({R.id.register_delete_account_text_img,R.id.register_delete_pass_text_img,R.id.register_next_btn,R.id.register_to_login,
		R.id.register_close,R.id.register_next_btn})
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.register_delete_account_text_img:
			EditTextUtil.setEditTextEmpty(registerAccountEt);
			break;
		case R.id.register_delete_pass_text_img:
			EditTextUtil.setEditTextEmpty(registerPassEt);
			break;
		case R.id.register_to_login:
			if(getActivity() instanceof OnBackToLoginListener){
				((OnBackToLoginListener)getActivity()).backToLoginClick();
			}
			break;
		case R.id.register_next_btn:
			next();
			break;
		case R.id.register_close:
			if(getActivity() instanceof OnCloseRegisterListener){
				((OnCloseRegisterListener)getActivity()).closeRegisterClick();
			}
			break;
		default:
			break;
		}
	}
	
	public void next(){
		String phone = registerAccountEt.getText().toString().trim();
		String pass = registerPassEt.getText().toString().trim();
		/*参数校验*/
		if(phone.isEmpty()){
			toast("请输入手机号");
			return;
		}
		if(phone.length() != 11){
			toast("请输入合法的手机号");
			return;
		}
		if(pass.isEmpty()){
			toast("请输入密码");
			return;
		}
		AccountActivity  accountActivity = (AccountActivity)getActivity();
		if(accountActivity!=null){
			accountActivity.setRegisterData(phone, pass);
		}
		if(C.Account.IS_SEND_VERIFY){
			Log.i("tag", "here");
			if(verification == null){
				verification = new VerificationUtil(getActivity());
			}
			Log.i("register_phone", phone);
			verification.requestVerificationCode(phone);
		}
		if(getActivity() instanceof OnNextListener){
			((OnNextListener)getActivity()).nextToVerifyClick();
		}
	}
		
}
