package com.ktl.moment.android.activity;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.ktl.moment.R;
import com.ktl.moment.android.base.AccountBaseFragment;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.android.fragment.VerifyFragment.BackToRegisterListener;
import com.ktl.moment.android.fragment.LoginFragment.ToForgetListener;
import com.ktl.moment.android.fragment.LoginFragment.ToRegisterListener;
import com.ktl.moment.android.fragment.RegisterFragment.BackToLoginListener;
import com.ktl.moment.android.fragment.RegisterFragment.ToVerifyListener;
import com.ktl.moment.common.constant.C;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnTouch;

public class AccountActivity extends BaseActivity implements ToRegisterListener,ToForgetListener,BackToLoginListener,ToVerifyListener,
	BackToRegisterListener{
	
	private FragmentManager fragmentManager;// 管理器
	private FragmentTransaction fragmentTransaction;// fragment事务
	private String currentFgTag = "";//一定要和需要默认显示的fragment 不一样
	private Map<String, String> registerData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
		
		//初始为登陆界面
		fragmentManager = getSupportFragmentManager();//获取fragment的管理器
		switchFragmentByTag(C.Account.FRAGMENT_LOGIN,C.Account.ANIMATION_FIRST);//设置默认的界面
		
		ViewUtils.inject(this);
	}
	
	
	/**
	 * 监听触摸事件
	 * @param v
	 * @param e
	 * @return
	 */
	@OnTouch({R.id.account_base_layout})
	public boolean onTouch(View v, MotionEvent e){
		/**
		 * 隐藏软键盘
		 */
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);  
		return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	}
	
	public boolean hideSoftKeyboard(){
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);  
		return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	}

	@Override
	public void onToRegisterClick() {
		// TODO Auto-generated method stub
		switchFragmentByTag(C.Account.FRAGMENT_REGISTER,C.Account.ANIMATION_SCALE);//login to register
	}

	@Override
	public void onToForgetClick() {
		// TODO Auto-generated method stub
		switchFragmentByTag(C.Account.FRAGMENT_FORGET_PASS,C.Account.ANIMATION_MOVE_UP);//login to forget
	}
	
	@Override
	public void onBackToLoginClick() {
		// TODO Auto-generated method stub
		switchFragmentByTag(C.Account.FRAGMENT_LOGIN,C.Account.ANIMATION_SCALE);//register back to login
	}

	@Override
	public void onToVerifyClick() {
		// TODO Auto-generated method stub
//		hideSoftKeyboard();
		switchFragmentByTag(C.Account.FRAGMENT_VERIFY,C.Account.ANIMATION_MOVE_UP);//register to verify
	}

	@Override
	public void onBackToRegisterClick() {
		// TODO Auto-generated method stub
		switchFragmentByTag(C.Account.FRAGMENT_REGISTER,C.Account.ANIMATION_MOVE_DOWN);// back to register
	}
	

	private void setCurrentTag (String tag){
		this.currentFgTag = tag;
	}

	/**
	 * 通过tag切换标签
	 * @param selectedTag
	 */
	private void switchFragmentByTag(String selectedTag, int moveFlag){
		if(selectedTag == currentFgTag || selectedTag.equals(currentFgTag)){
			return ;
		}
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentSwitchAnim(moveFlag);
		if(selectedTag!=null  && !selectedTag.equals("")){
			detachFragment(getFragmentByTag(currentFgTag));//detach当前的fragment
		}
		attachFragment(R.id.account_content_cantainer,getFragmentByTag(selectedTag),selectedTag);
		commitTransactions();//提交事务
		setCurrentTag(selectedTag);//设置当前的tag
	}
	
	/**
	 * 提交fragment transaction
	 */
	private void commitTransactions(){
		if (fragmentTransaction != null && !fragmentTransaction.isEmpty()) {
		    fragmentTransaction.commitAllowingStateLoss();
		    fragmentTransaction = null;
		}
	}
	/**
	 * attach fragment
	 * @param layout
	 * @param f
	 * @param tag
	 */
	private void attachFragment(int layout,Fragment f,String tag){
		if(f!=null){
			if (fragmentTransaction == null) {
	            fragmentTransaction = fragmentManager.beginTransaction();
	        }
			if(f.isDetached()){
				fragmentTransaction.attach(f);
			}else if(!f.isAdded()){
				fragmentTransaction.replace(layout, f,tag);
			}else{
				//Nothing to do
			}
		}
	}
	/**
	 * detach fragment
	 * @param f
	 */
	private void detachFragment(Fragment f){
		if(f!=null && !f.isDetached()){
			if (fragmentTransaction == null) {
	            fragmentTransaction = fragmentManager.beginTransaction();
	            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	        }
			fragmentTransaction.detach(f);
		}
	}
	/**
	 * 通过标签获取fragment
	 * @param tag
	 * @return
	 */
	private Fragment getFragmentByTag(String tag){
		Fragment f = fragmentManager.findFragmentByTag(tag);
		if(f==null){
			f = AccountBaseFragment.getInstance(tag);
		}
		return f;
	}
	
	private void fragmentSwitchAnim(int moveFlag){
		switch (moveFlag) {
		case C.Account.ANIMATION_FIRST:
			break;
		case C.Account.ANIMATION_SCALE:
			fragmentTransaction.setCustomAnimations(R.anim.fragment_account_scale_in, R.anim.fragment_account_scale_out);
			break;
		case C.Account.ANIMATION_MOVE_UP:
			fragmentTransaction.setCustomAnimations(R.anim.fragment_account_up_in, R.anim.fragment_account_up_out);
			break;
		case C.Account.ANIMATION_MOVE_DOWN:
			fragmentTransaction.setCustomAnimations(R.anim.fragment_account_down_in, R.anim.fragment_account_down_out);
		default:
			break;
		}
	}
	
	public void setRegisterData(String phone, String pass){
		registerData = new HashMap<String, String>();
		registerData.put("phone", phone);
		registerData.put("pass", pass);
	}
	
	public Map<String, String> getRegisterData(){
		return this.registerData;
	}
}
