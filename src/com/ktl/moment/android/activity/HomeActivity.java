package com.ktl.moment.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.ktl.moment.R;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.android.base.BaseFragment;
import com.ktl.moment.android.component.BottomMenu;
import com.ktl.moment.android.component.BottomMenu.OnMenuItemClickListener;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.utils.ToastUtil;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

public class HomeActivity extends BaseActivity {
	private static final String TAG = "HomeAtivity";

	private BottomMenu customMenu;// 菜单
	private FragmentManager fragmentManager;// 管理器
	private FragmentTransaction fragmentTransaction;// fragment事务
	private String currentFgTag = "";// 一定要和需要默认显示的fragment 不一样

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		initView();
		initEvent();
		
		// 初始为发现界面
		fragmentManager = getSupportFragmentManager();// 获取fragment的管理器
		switchMenuByTag(C.menu.FRAGMENT_DEFAULT_SHOW_TAG);// 设置默认的界面
		
		//注册信鸽
		registXgPush();
	}

	/**
	 * 初始化view
	 */
	private void initView() {
		getLayoutInflater()
				.inflate(R.layout.activity_home, contentLayout, true);

		setHomeTitleVisible(true);
		setTitleTvName(R.string.attention_text_view);
		setBaseActivityBgColor(getResources()
				.getColor(R.color.main_title_color));//设置title颜色
		setBaseContainerBgColor(getResources().getColor(
				R.color.main_content_container_color));//设置内容区域颜色
		customMenu = (BottomMenu) findViewById(R.id.bottom_menu);
		customMenu.setDefaultCheckedMenu(R.id.menu_foucs);
		
	}

	/**
	 * 初始化一些事件
	 */
	private void initEvent() {

		customMenu.setOnMenuItemClick(new OnMenuItemClickListener() {

			@Override
			public void OnClick(int id) {
				// TODO Auto-generated method stub
				String tag = "";
				switch (id) {
				case C.menu.FRAGMENT_FIND_MENU_ID:
					tag = C.menu.FRAGMENT_FIND_TAG;
					setTitleTvNameEmpty();
					setMiddleFindTabVisible(true);
					break;
				case C.menu.FRAGMENT_DYNAMIC_MENU_ID:
					tag = C.menu.FRAGMENT_DYNAMIC_TAG;
					setTitleTvName(R.string.attention_text_view);
					setMiddleFindTabVisible(false);
					break;
				case C.menu.FRAGMENT_ADD_MOMENT_MENU_ID:
					Intent editorIntent = new Intent(HomeActivity.this, EditorActivity.class);
					startActivity(editorIntent);
					return;
				case C.menu.FRAGMENT_MOMENT_MENU_ID:
					tag = C.menu.FRAGMENT_MOMENT_TAG;
					setTitleTvName(R.string.moment_text_view);
					setMiddleFindTabVisible(false);
					break;
				case C.menu.FRAGMENT_ME_MENU_ID:
					tag = C.menu.FRAGMENT_ME_TAG;
					setTitleTvName(R.string.me_text_view);
					setMiddleFindTabVisible(false);
					break;
				}
				switchMenuByTag(tag);
			}

		});
	}

	private void setCurrentTag(String tag) {
		this.currentFgTag = tag;
	}

	/**
	 * 通过tag切换标签
	 * 
	 * @param selectedTag
	 */
	private void switchMenuByTag(String selectedTag) {
		if (selectedTag == currentFgTag || selectedTag.equals(currentFgTag)) {
			return;
		}
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);//
		if (selectedTag != null && !selectedTag.equals("")) {
			detachFragment(getFragmentByTag(currentFgTag));// detach当前的fragment
		}
		attachFragment(R.id.home_content_container,
				getFragmentByTag(selectedTag), selectedTag);
		commitTransactions();// 提交事务
		setCurrentTag(selectedTag);// 设置当前的tag
	}

	/**
	 * 提交fragmenttransaction
	 */
	private void commitTransactions() {
		if (fragmentTransaction != null && !fragmentTransaction.isEmpty()) {
			fragmentTransaction.commitAllowingStateLoss();
			fragmentTransaction = null;
		}
	}

	/**
	 * attach fragment
	 * 
	 * @param layout
	 * @param f
	 * @param tag
	 */
	private void attachFragment(int layout, Fragment f, String tag) {
		if (f != null) {
			if (fragmentTransaction == null) {
				fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction
						.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			}
			if (f.isDetached()) {
				fragmentTransaction.attach(f);
			} else if (!f.isAdded()) {
				fragmentTransaction.add(layout, f, tag);
			} else {
				// Nothing to do
			}
		}
	}

	/**
	 * detach fragment
	 * 
	 * @param f
	 */
	private void detachFragment(Fragment f) {
		if (f != null && !f.isDetached()) {
			if (fragmentTransaction == null) {
				fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction
						.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			}
			fragmentTransaction.detach(f);
		}
	}

	/**
	 * 通过标签获取fragment
	 * 
	 * @param tag
	 * @return
	 */
	private Fragment getFragmentByTag(String tag) {
		Fragment f = fragmentManager.findFragmentByTag(tag);
		if (f == null) {
			f = BaseFragment.getInstance(tag);
		}
		return f;
	}
	/**
	 * 注册信鸽推送服务
	 */
	public void registXgPush(){
		int userId = 0;
		XGPushManager.registerPush(getApplicationContext(), userId+"12", new XGIOperateCallback() {
			
			@Override
			public void onSuccess(Object data, int flag) {
				// TODO Auto-generated method stub
				ToastUtil.show(HomeActivity.this, "注册成功"+data);
			}
			
			@Override
			public void onFail(Object data, int errCode, String msg) {
				// TODO Auto-generated method stub
				ToastUtil.show(HomeActivity.this, "注册失败"+ msg);
				
			}
		});
	}

}
