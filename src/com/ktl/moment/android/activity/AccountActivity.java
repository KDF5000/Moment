package com.ktl.moment.android.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.ktl.moment.R;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.android.base.BaseFragment;
import com.ktl.moment.android.fragment.LoginFragment.ToForgetListener;
import com.ktl.moment.android.fragment.LoginFragment.ToRegisterListener;
import com.ktl.moment.android.fragment.RegisterFragment.ToLoginListener;
import com.ktl.moment.common.constant.C;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnTouch;

public class AccountActivity extends BaseActivity implements ToRegisterListener,ToForgetListener,ToLoginListener{
	
	private FragmentManager fragmentManager;// 管理器
	private FragmentTransaction fragmentTransaction;// fragment事务
	private String currentFgTag = "";//一定要和需要默认显示的fragment 不一样
	
	private boolean isLoginFragFirstIn = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
		
		//初始为登陆界面
		fragmentManager = getSupportFragmentManager();//获取fragment的管理器
		switchFragmentByTag(C.Account.FRAGMENT_LOGIN);//设置默认的界面
		isLoginFragFirstIn = false;
		
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

	@Override
	public void onToRegisterClick() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "click listener", Toast.LENGTH_SHORT).show();
		switchFragmentByTag(C.Account.FRAGMENT_REGISTER);
	}

	@Override
	public void onToForgetClick() {
		// TODO Auto-generated method stub
		switchFragmentByTag(C.Account.FRAGMENT_FORGET_PASS);
	}
	
	@Override
	public void onToLoginClick() {
		// TODO Auto-generated method stub
		switchFragmentByTag(C.Account.FRAGMENT_LOGIN);
	}
	

	private void setCurrentTag (String tag){
		this.currentFgTag = tag;
	}

	/**
	 * 通过tag切换标签
	 * @param selectedTag
	 */
	private void switchFragmentByTag(String selectedTag){
		if(selectedTag == currentFgTag || selectedTag.equals(currentFgTag)){
			return ;
		}
		fragmentTransaction = fragmentManager.beginTransaction();
//		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);//设置fragment切换时的动画效果
		fragmentSwitchAnim(selectedTag);
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
//	            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	        }
			if(f.isDetached()){
				Log.i("tag", "attach");
				fragmentTransaction.attach(f);
			}else if(!f.isAdded()){
				Log.i("tag", "replace");
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
			f = BaseFragment.getInstance(tag);
		}
		return f;
	}
	
	private void fragmentSwitchAnim(String selectedTag){
		Fragment f = getFragmentByTag(selectedTag);
		if(isLoginFragFirstIn){
			return;
		}
		if(selectedTag.equals(C.Account.FRAGMENT_LOGIN) || selectedTag.equals(C.Account.FRAGMENT_REGISTER)){
			fragmentTransaction.setCustomAnimations(R.anim.fragment_account_scale_in, R.anim.fragment_account_scale_out);
		}
	}
	
}
