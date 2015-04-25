package com.ktl.moment.utils;

import android.os.CountDownTimer;
import android.widget.Button;

/**
 * 倒计时工具类
 * @author HUST_LH
 *
 */
public class CountDownTimerUtil extends CountDownTimer{
	
	private Button button;
	private CharSequence onTickText;
	private CharSequence onFinishText;
	private CharSequence leftSymbol;
	private CharSequence rightSymbol;
	private int onTickBg;
	private int onFinishBg;
	private int onTickTextColor;
	private int onFinishTextColor;
	
	public String test;

	public CountDownTimerUtil(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);
		// TODO Auto-generated constructor stub
	}
	
	public CountDownTimerUtil(long millisInFuture, long countDownInterval, Button button){
		super(millisInFuture, countDownInterval);
		this.button = button;
	}

	@Override
	public void onTick(long millisUntilFinished) {
		// TODO Auto-generated method stub
		if(button != null){
			CharSequence showString = onTickText+""+leftSymbol+""+millisUntilFinished/1000+""+rightSymbol;
			button.setText(showString);
			button.setTextColor(onTickTextColor);
			button.setBackgroundResource(onTickBg);
			button.setClickable(false);
		}
	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		if(button != null){
			button.setText(onFinishText);
			button.setTextColor(onFinishTextColor);
			button.setBackgroundResource(onFinishBg);
			button.setClickable(true);
		}
	}
	
	public void setOnTickText(CharSequence text){
		onTickText = text;
	}
	
	public void setOnFinishText(CharSequence text){
		onFinishText = text;
	}
	
	public void setLeftSymbol(CharSequence leftSymbol){
		this.leftSymbol = leftSymbol;
	}
	
	public void setRightSymbol(CharSequence rightSymbol){
		this.rightSymbol = rightSymbol;
	}
	
	public void setOnTickBg(int resId){
		onTickBg = resId;
	}
	
	public void setOnTickTextColor(int colorResId){
		onTickTextColor = colorResId;
	}
	
	public void setOnFinishBg(int resId){
		onFinishBg = resId;
	}
	
	public void setOnFinishTextColor(int colorResId){
		onFinishTextColor = colorResId;
	}

}
