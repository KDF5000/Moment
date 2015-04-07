package com.ktl.moment.android.fragment;

import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ktl.moment.R;
import com.ktl.moment.android.activity.AccountActivity;
import com.ktl.moment.android.base.AccountBaseFragment;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.utils.CountDownTimerUtil;
import com.ktl.moment.utils.VerificationUtil;
import com.ktl.moment.utils.VerificationUtil.ActionStartListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class VerifyFragment extends AccountBaseFragment implements ActionStartListener{
	
	@ViewInject(R.id.verify_code_et)
	private EditText verifyCodeEt;
	
	@ViewInject(R.id.verify_resend_btn)
	private Button verifyResendBtn;
	
	@ViewInject(R.id.verify_next_btn)
	private Button verifyNextbtn;
	
	@ViewInject(R.id.verify_to_register)
	private LinearLayout verifyToRegister;
	
	public BackToRegisterListener backToRegisterListener;
	
	private VerificationUtil verification;
	private Map<String,String> registerData;
	
	public interface BackToRegisterListener{
		void onBackToRegisterClick();
	}
	
	public void setOnBackToRegisterListener(BackToRegisterListener backToRegisterListener){
		this.backToRegisterListener = backToRegisterListener;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_account_verify, container, false);

		ViewUtils.inject(this, view);
		initDownTimer();
		getRegisterData();
		
		return view;
	}
	
	private void getRegisterData(){
		AccountActivity  accountActivity = (AccountActivity)getActivity();
		if(accountActivity!=null){
			registerData = accountActivity.getRegisterData();
		}
	}
	
	@OnClick({R.id.verify_resend_btn,R.id.verify_next_btn,R.id.verify_to_register})
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.verify_resend_btn:
			resend();
			break;
		case R.id.verify_next_btn:
			next();
			break;
		case R.id.verify_to_register:
			if(getActivity() instanceof BackToRegisterListener){
				((BackToRegisterListener)getActivity()).onBackToRegisterClick();
			}
		default:
			break;
		}
	}
	
	private void initDownTimer(){
		CountDownTimerUtil downTimer = new CountDownTimerUtil(60000*C.YunZhiXun.VERIFY_VALID_TIME, 1000, verifyResendBtn);
		
		/*on tick*/
		downTimer.setLeftSymbol("(");
		downTimer.setRightSymbol(")");
		downTimer.setOnTickText(getText(R.string.verify_resend));
		downTimer.setOnTickBg(R.drawable.verify_resend_btn_on_tick_shape);
		downTimer.setOnTickTextColor(getResources().getColor(R.color.verify_resend_on_tick_text_color));
		
		/*on finish*/
		downTimer.setOnFinishText(getText(R.string.verify_resend));
		downTimer.setOnFinishBg(R.drawable.verify_resend_btn_on_finish_shape);
		downTimer.setOnFinishTextColor(getResources().getColor(R.color.verify_resend_on_finish_text_color));
		
		/*start downTimer*/
		downTimer.start();
	}
	
	private void resend(){
		initDownTimer();
		if(verification == null){
			verification = new VerificationUtil(getActivity());
		}
		if(C.Account.IS_SEND_VERIFY){
			verification.requestVerificationCode(registerData.get("phone"));
		}
	}
	
	private void next(){
		String verifyCode = verifyCodeEt.getText().toString().trim();
		if(verifyCode.isEmpty()){
			toast("请输入您收到的短信验证码");
			return;
		}
		if(C.Account.IS_SEND_VERIFY){
			if(verification == null){
				verification = new VerificationUtil(getActivity());
			}
			verification.startVerificationCode(registerData.get("phone"), verifyCode);
			verification.setActionStartListener(this);
		}
	}


	@Override
	public void action() {
		// TODO Auto-generated method stub
		toast("验证通过");
	}
	
}
