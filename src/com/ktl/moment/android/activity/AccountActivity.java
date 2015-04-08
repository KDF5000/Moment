package com.ktl.moment.android.activity;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.ktl.moment.R;
import com.ktl.moment.android.base.AccountBaseFragment;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.android.fragment.LoginFragment.OnCloseLoginListener;
import com.ktl.moment.android.fragment.LoginFragment.OnForgetListener;
import com.ktl.moment.android.fragment.LoginFragment.OnLoginToRegisterListener;
import com.ktl.moment.android.fragment.RegisterFragment.OnBackToLoginListener;
import com.ktl.moment.android.fragment.RegisterFragment.OnCloseRegisterListener;
import com.ktl.moment.android.fragment.StartFragment.OnLoginListener;
import com.ktl.moment.android.fragment.StartFragment.OnRegisterListener;
import com.ktl.moment.common.constant.C;
import com.lidroid.xutils.ViewUtils;

public class AccountActivity extends BaseActivity implements OnLoginListener,OnLoginToRegisterListener,OnForgetListener,OnCloseLoginListener,
	OnRegisterListener,OnCloseRegisterListener,OnBackToLoginListener{
	
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
		switchFragmentByTag(C.Account.FRAGMENT_START,C.Account.ANIMATION_NO_ANIM);//设置默认的界面
		
		ViewUtils.inject(this);
	}
	
	
	/**
	 * 监听触摸事件
	 * @param v
	 * @param e
	 * @return
	 */
//	@OnTouch({R.id.account_base_layout})
//	public boolean onTouch(View v, MotionEvent e){
//		/**
//		 * 隐藏软键盘
//		 */
//		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);  
//		return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//	}
	
	public boolean hideSoftKeyboard(){
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);  
		return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
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
		case C.Account.ANIMATION_NO_ANIM:
			break;
		case C.Account.ANIMATION_ALPHA_IN:
			fragmentTransaction.setCustomAnimations(R.anim.login_alpha_in, R.anim.login_alpha_out);
			break;
		case C.Account.ANIMATION_MOVE_RIGHT:
			fragmentTransaction.setCustomAnimations(R.anim.left_in, R.anim.right_out);
			break;
		case C.Account.ANIMATION_MOVE_LEFT:
			fragmentTransaction.setCustomAnimations(R.anim.left_out, R.anim.right_in);
			break;
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
	
	/**
	 * 接口回调
	 */
	@Override
	public void login() {
		// TODO Auto-generated method stub
		switchFragmentByTag(C.Account.FRAGMENT_LOGIN,C.Account.ANIMATION_ALPHA_IN);//login to register
	}


	@Override
	public void forgetClick() {
		// TODO Auto-generated method stub
		Log.i("tag","forget");
	}


	@Override
	public void loginToRegisterClick() {
		// TODO Auto-generated method stub
		switchFragmentByTag(C.Account.FRAGMENT_REGISTER, C.Account.ANIMATION_MOVE_LEFT);
	}


	@Override
	public void closeLoginClick() {
		// TODO Auto-generated method stub
		switchFragmentByTag(C.Account.FRAGMENT_START,C.Account.ANIMATION_ALPHA_IN);//login to register
	}


	@Override
	public void register() {
		// TODO Auto-generated method stub
		switchFragmentByTag(C.Account.FRAGMENT_REGISTER, C.Account.ANIMATION_ALPHA_IN);
	}


	@Override
	public void backToLoginClick() {
		// TODO Auto-generated method stub
		switchFragmentByTag(C.Account.FRAGMENT_LOGIN, C.Account.ANIMATION_MOVE_RIGHT);
	}


	@Override
	public void closeRegisterClick() {
		// TODO Auto-generated method stub
		switchFragmentByTag(C.Account.FRAGMENT_START, C.Account.ANIMATION_ALPHA_IN);
	}

}
